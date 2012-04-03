package ajedrez.common.ifaz;

import org.apache.mina.common.IoSession;

import common.ifaz.BasicServerHandler;

public interface AjedrezServerHandler extends BasicServerHandler {
    void createRoom(IoSession session, int puntos, int minutos);
}
