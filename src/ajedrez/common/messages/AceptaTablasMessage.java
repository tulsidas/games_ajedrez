package ajedrez.common.messages;

import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.IoSession;

import ajedrez.common.ifaz.GameHandler;
import ajedrez.common.ifaz.GameMessage;
import ajedrez.common.ifaz.SaloonHandler;

import common.messages.TaringaProtocolEncoder;

public class AceptaTablasMessage extends AjedrezClientGameMessage implements
      GameMessage {

   private boolean acepta;

   public AceptaTablasMessage() {
   }

   public AceptaTablasMessage(boolean acepta) {
      this.acepta = acepta;
   }

   @Override
   public void execute(IoSession session, SaloonHandler salon) {
      salon.aceptaTablas(session, acepta);
   }

   @Override
   public String toString() {
      return "AceptaTablas: " + acepta;
   }

   public void execute(GameHandler game) {
      game.aceptaTablas(acepta);
   }

   @Override
   public int getContentLength() {
      // booleano si/no
      return 1;
   }

   @Override
   public void decode(ByteBuffer buff) {
      acepta = buff.get() == TaringaProtocolEncoder.TRUE;
   }

   @Override
   public void encodeContent(ByteBuffer buff) {
      buff.put(acepta ? TaringaProtocolEncoder.TRUE
            : TaringaProtocolEncoder.FALSE);
   }

   // @Override
   public byte getMessageId() {
      return (byte) 0x80;
   }
}
