package ajedrez.common.messages.client;

import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.IoSession;

import ajedrez.common.ifaz.AjedrezServerHandler;

import common.ifaz.BasicClientGameMessage;
import common.ifaz.BasicServerHandler;
import common.messages.client.CreateRoomMessage;

/**
 * Pedido de un usuario de crear una sala
 */
public class CreateAjedrezRoomMessage extends CreateRoomMessage implements
      BasicClientGameMessage {

   private int tiempo;

   public CreateAjedrezRoomMessage() {
   }

   public CreateAjedrezRoomMessage(int puntos, int tiempo) {
      super(puntos);
      this.tiempo = tiempo;
   }

   @Override
   public void execute(IoSession session, BasicServerHandler server) {
      ((AjedrezServerHandler) server).createRoom(session, puntos, tiempo);
   }

   @Override
   public String toString() {
      return "Create Room (" + puntos + " pts), " + tiempo + "'";
   }

   @Override
   public int getContentLength() {
      return super.getContentLength() + 4;
   }

   @Override
   public void decode(ByteBuffer buff) {
      super.decode(buff);
      tiempo = buff.getInt();
   }

   protected void encodeContent(ByteBuffer buff) {
      super.encodeContent(buff);
      buff.putInt(tiempo);
   }

   public byte getMessageId() {
      return (byte) 0x86;
   }
}
