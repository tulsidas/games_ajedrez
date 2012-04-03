package ajedrez.common.messages.server;

import ajedrez.common.ifaz.GameHandler;
import ajedrez.common.ifaz.GameMessage;

import common.messages.FixedLengthMessageAdapter;

public class NewGameMessage extends FixedLengthMessageAdapter implements
      GameMessage {

   public void execute(GameHandler game) {
      game.newGame();
   }

   @Override
   public byte getMessageId() {
      return (byte) 0x82;
   }

   @Override
   public int getContentLength() {
      return 0;
   }
}
