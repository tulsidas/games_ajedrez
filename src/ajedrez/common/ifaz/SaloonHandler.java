package ajedrez.common.ifaz;

import org.apache.mina.common.IoSession;

import ajedrez.common.model.Move;

/**
 * Interfaz de los mensajes que recibe el Saloon de los clientes
 * 
 */
public interface SaloonHandler {

    /** una movida */
    void move(IoSession session, Move move);

    /** oferta de tablas */
    void tablas(IoSession session);

    /** respuesta de tablas */
    void aceptaTablas(IoSession session, boolean acepta);
}