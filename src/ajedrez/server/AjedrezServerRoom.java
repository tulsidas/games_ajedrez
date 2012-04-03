package ajedrez.server;

import org.apache.mina.common.IoSession;

import server.TwoPlayersServerRoom;
import ajedrez.common.messages.AceptaTablasMessage;
import ajedrez.common.messages.MoveMessage;
import ajedrez.common.messages.TablasMessage;
import ajedrez.common.messages.server.FinJuegoMessage;
import ajedrez.common.messages.server.NewGameMessage;
import ajedrez.common.messages.server.StartGameMessage;
import ajedrez.common.model.AjedrezRoom;
import ajedrez.common.model.Move;
import ajedrez.common.model.Tablero;
import ajedrez.common.model.Tablero.ImpossibleMoveException;

import common.messages.server.UpdatedPointsMessage;
import common.model.AbstractRoom;

public class AjedrezServerRoom extends TwoPlayersServerRoom {

    private Tablero tablero;

    private int movidas;

    private int minutos;

    public AjedrezServerRoom(AjedrezSaloon saloon, IoSession session,
            int puntos, int minutos) {
        super(saloon, session, puntos);

        this.minutos = minutos;

        movidas = 0;
        tablero = new Tablero();
        tablero.init();

        setEnJuego(true);
    }

    @Override
    public AbstractRoom createRoom() {
        return new AjedrezRoom(getId(), puntosApostados, minutos, getUsers());
    }

    @Override
    public void startNuevoJuego() {
        movidas = 0;
        tablero.init();

        player1.write(new NewGameMessage());
        player2.write(new NewGameMessage());

        setEnJuego(true);

        startGame();
    }

    @Override
    public void startGame() {
        player1.write(new StartGameMessage(true));
        player2.write(new StartGameMessage(false));
    }

    @Override
    public boolean isGameOn() {
        return movidas >= 2;
    }

    public void move(IoSession session, Move move) {
        movidas++;

        try {
            tablero.makeMove(move);

            if (tablero.noAvailableMoves()) {
                if (tablero.isKingChecked()) {
                    // jaque mate!
                    // el que hizo la movida gano
                    session.write(new FinJuegoMessage(true));

                    MoveMessage mm = new MoveMessage(move);
                    mm.setGameOver(false);

                    IoSession otro = getOtherPlayer(session);

                    otro.write(mm);

                    // transfiero puntos
                    int newPoints[] = saloon.transferPoints(session, otro,
                            puntosApostados);

                    // mando puntos (si siguen conectados)
                    if (session != null) {
                        session.write(new UpdatedPointsMessage(newPoints[0]));
                    }
                    if (otro != null) {
                        otro.write(new UpdatedPointsMessage(newPoints[1]));
                    }
                }
                else {
                    // empate, no transfiero puntos
                    setEnJuego(false);

                    // rey ahogado
                    player1.write(new AceptaTablasMessage(true));
                    player2.write(new AceptaTablasMessage(true));
                }
            }
            else {
                getOtherPlayer(session).write(new MoveMessage(move));
            }
        }
        catch (ImpossibleMoveException e) {
            // no deberia pasar nunca
            e.printStackTrace();
        }
    }

    public void tablas(IoSession session) {
        getOtherPlayer(session).write(new TablasMessage());
    }

    public void aceptaTablas(IoSession session, boolean acepta) {
        if (acepta) {
            // game over, sin transferencia
            setEnJuego(false);
        }

        // reenvio al otro jugador
        getOtherPlayer(session).write(new AceptaTablasMessage(acepta));
    }
}

// TODO elegir coronacion de peon
// TODO sincronizar relojes