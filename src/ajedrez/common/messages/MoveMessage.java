package ajedrez.common.messages;

import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.IoSession;

import common.messages.TaringaProtocolEncoder;

import ajedrez.common.ifaz.GameHandler;
import ajedrez.common.ifaz.GameMessage;
import ajedrez.common.ifaz.SaloonHandler;
import ajedrez.common.messages.AjedrezClientGameMessage;
import ajedrez.common.model.Move;

public class MoveMessage extends AjedrezClientGameMessage implements
      GameMessage {

   private Move move;

   private Boolean gameOver;

   public MoveMessage() {
   }

   public MoveMessage(Move move) {
      this.move = move;
   }

   @Override
   public void execute(IoSession session, SaloonHandler salon) {
      salon.move(session, move);
   }

   @Override
   public String toString() {
      return "MoveMessage -> " + move;
   }

   public void execute(GameHandler game) {
      game.move(move);

      if (gameOver != null) {
         game.finJuego(gameOver.booleanValue());
      }
   }

   public void setGameOver(Boolean gameOver) {
      this.gameOver = gameOver;
   }

   @Override
   public int getContentLength() {
      // 3 bytes de la movida + 1 byte del gameover
      return 4;
   }

   @Override
   public void decode(ByteBuffer buff) {
      int from = buff.get();
      int to = buff.get();
      int pawn = buff.get();
      move = new Move(from, to, pawn);

      byte gOver = buff.get();
      if (gOver != TaringaProtocolEncoder.NULL) {
         gameOver = gOver == TaringaProtocolEncoder.TRUE;
      }
   }

   @Override
   public void encodeContent(ByteBuffer buff) {
      // movida
      buff.put((byte) move.fromField());
      buff.put((byte) move.toField());
      buff.put((byte) move.pawnPromotion());

      if (gameOver != null) {
         buff.put(gameOver ? TaringaProtocolEncoder.TRUE
               : TaringaProtocolEncoder.FALSE);
      }
      else {
         buff.put(TaringaProtocolEncoder.NULL);
      }
   }

   @Override
   public byte getMessageId() {
      return (byte) 0x84;
   }
}
