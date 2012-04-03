package ajedrez.common.messages.server;

import org.apache.mina.common.ByteBuffer;

import ajedrez.common.ifaz.GameHandler;
import ajedrez.common.ifaz.GameMessage;

import common.messages.FixedLengthMessageAdapter;
import common.messages.TaringaProtocolEncoder;

public class StartGameMessage extends FixedLengthMessageAdapter implements
      GameMessage {

   private boolean start;

   public StartGameMessage() {
   }

   public StartGameMessage(boolean start) {
      this.start = start;
   }

   public void execute(GameHandler game) {
      game.startGame(start);
   }

   @Override
   public int getContentLength() {
      // booleano si/no
      return 1;
   }

   @Override
   public void decode(ByteBuffer buff) {
      start = buff.get() == TaringaProtocolEncoder.TRUE;
   }

   @Override
   public void encodeContent(ByteBuffer buff) {
      buff.put(start ? TaringaProtocolEncoder.TRUE
            : TaringaProtocolEncoder.FALSE);
   }

   // @Override
   public byte getMessageId() {
      return (byte) 0x83;
   }
}
