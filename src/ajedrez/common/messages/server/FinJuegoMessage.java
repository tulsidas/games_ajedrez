package ajedrez.common.messages.server;

import org.apache.mina.common.ByteBuffer;

import ajedrez.common.ifaz.GameHandler;
import ajedrez.common.ifaz.GameMessage;

import common.messages.FixedLengthMessageAdapter;
import common.messages.TaringaProtocolEncoder;

public class FinJuegoMessage extends FixedLengthMessageAdapter implements
      GameMessage {

   private boolean victoria;

   public FinJuegoMessage() {
   }

   public FinJuegoMessage(boolean victoria) {
      this.victoria = victoria;
   }

   public void execute(GameHandler game) {
      game.finJuego(victoria);
   }

   @Override
   public String toString() {
      return "Fin Juego";
   }

   @Override
   public int getContentLength() {
      // booleano si/no
      return 1;
   }

   @Override
   public void decode(ByteBuffer buff) {
      victoria = buff.get() == TaringaProtocolEncoder.TRUE;
   }

   @Override
   protected void encodeContent(ByteBuffer buff) {
      buff.put(victoria ? TaringaProtocolEncoder.TRUE
            : TaringaProtocolEncoder.FALSE);
   }

   // @Override
   public byte getMessageId() {
      return (byte) 0x81;
   }

}
