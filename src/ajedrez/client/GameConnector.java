package ajedrez.client;

import org.apache.mina.common.IoSession;

import ajedrez.common.ifaz.GameHandler;
import ajedrez.common.ifaz.GameMessage;
import ajedrez.common.messages.AjedrezProtocolDecoder;
import ajedrez.common.model.Move;
import client.AbstractGameConnector;

public class GameConnector extends AbstractGameConnector implements GameHandler {

    public GameConnector(String host, int port, int salon, String user,
            String pass, long version) {
        super(host, port, salon, user, pass, version,
                new AjedrezProtocolDecoder());
    }

    @Override
    public void messageReceived(IoSession sess, Object message) {
        super.messageReceived(sess, message);

        if (message instanceof GameMessage && gameHandler != null) {
            ((GameMessage) message).execute(this);
        }
    }

    // /////////////
    // GameHandler
    // /////////////
    public void move(Move move) {
        if (gameHandler != null) {
            ((GameHandler) gameHandler).move(move);
        }
    }

    public void tablas() {
        if (gameHandler != null) {
            ((GameHandler) gameHandler).tablas();
        }
    }

    public void aceptaTablas(boolean acepta) {
        if (gameHandler != null) {
            ((GameHandler) gameHandler).aceptaTablas(acepta);
        }
    }
}
