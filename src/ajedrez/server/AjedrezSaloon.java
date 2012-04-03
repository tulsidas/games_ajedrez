package ajedrez.server;

import org.apache.mina.common.IoSession;

import server.AbstractSaloon;
import ajedrez.common.ifaz.AjedrezServerHandler;
import ajedrez.common.ifaz.SaloonHandler;
import ajedrez.common.model.Move;

import common.ifaz.POSTHandler;

public class AjedrezSaloon extends AbstractSaloon implements SaloonHandler,
        AjedrezServerHandler {

    public AjedrezSaloon(int id, POSTHandler poster) {
        super(id, poster);
    }

    @Override
    protected AjedrezServerRoom getRoom(IoSession session) {
        return (AjedrezServerRoom) super.getRoom(session);
    }

    public void createRoom(IoSession session, int puntos) {
        // no se usa
        createRoom(session, puntos, 15);
    }

    public void createRoom(IoSession session, int puntos, int minutos) {
        AjedrezServerRoom asr = new AjedrezServerRoom(this, session, puntos,
                minutos);

        createRoom(session, puntos, asr);
    }

    // Ajedrez methods
    public void move(IoSession session, Move move) {
        getRoom(session).move(session, move);
    }

    public void tablas(IoSession session) {
        getRoom(session).tablas(session);
    }

    public void aceptaTablas(IoSession session, boolean acepta) {
        getRoom(session).aceptaTablas(session, acepta);
    }
}
