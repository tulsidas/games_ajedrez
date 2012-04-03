package ajedrez.common.messages;

import org.apache.mina.common.IoSession;

import ajedrez.common.ifaz.ClientGameMessage;
import ajedrez.common.ifaz.SaloonHandler;

import common.ifaz.BasicServerHandler;
import common.messages.FixedLengthMessageAdapter;

public abstract class AjedrezClientGameMessage extends FixedLengthMessageAdapter
        implements ClientGameMessage {

    public abstract void execute(IoSession session, SaloonHandler salon);

    public void execute(IoSession session, BasicServerHandler serverHandler) {
        execute(session, (SaloonHandler) serverHandler);
    }
}
