package ajedrez.common.messages;

import org.apache.mina.common.IoSession;

import ajedrez.common.ifaz.GameHandler;
import ajedrez.common.ifaz.GameMessage;
import ajedrez.common.ifaz.SaloonHandler;

public class TablasMessage extends AjedrezClientGameMessage implements
      GameMessage {

   @Override
   public void execute(IoSession session, SaloonHandler salon) {
      salon.tablas(session);
   }

   @Override
   public String toString() {
      return "Tablas";
   }

   public void execute(GameHandler game) {
      game.tablas();
   }

   @Override
   public byte getMessageId() {
      return (byte) 0x85;
   }

   @Override
   public int getContentLength() {
      return 0;
   }
}
