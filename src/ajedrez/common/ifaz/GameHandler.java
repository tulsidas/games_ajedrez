package ajedrez.common.ifaz;

import ajedrez.common.model.Move;
import common.ifaz.BasicGameHandler;

/**
 * Mensajes que llegan al cliente
 */
public interface GameHandler extends BasicGameHandler {

    void move(Move move);

    void tablas();

    void aceptaTablas(boolean acepta);
}
