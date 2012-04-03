package ajedrez.common.messages;

import ajedrez.common.messages.client.CreateAjedrezRoomMessage;
import ajedrez.common.messages.server.FinJuegoMessage;
import ajedrez.common.messages.server.NewGameMessage;
import ajedrez.common.messages.server.StartGameMessage;

import common.messages.TaringaProtocolDecoder;

public class AjedrezProtocolDecoder extends TaringaProtocolDecoder {

    public AjedrezProtocolDecoder() {
        // empiezo en 0x80 para darle lugar a los base
        classes.put(new AceptaTablasMessage().getMessageId(),
                AceptaTablasMessage.class);
        classes
                .put(new FinJuegoMessage().getMessageId(),
                        FinJuegoMessage.class);
        classes.put(new NewGameMessage().getMessageId(), NewGameMessage.class);
        classes.put(new StartGameMessage().getMessageId(),
                StartGameMessage.class);
        classes.put(new MoveMessage().getMessageId(), MoveMessage.class);
        classes.put(new TablasMessage().getMessageId(), TablasMessage.class);
        classes.put(new CreateAjedrezRoomMessage().getMessageId(),
                CreateAjedrezRoomMessage.class);
    }
}
