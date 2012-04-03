package ajedrez.client;

import pulpcore.image.CoreFont;
import ajedrez.common.model.Move;
import client.LabelScrollable;

public class MovesBox extends LabelScrollable {

   private String ultimaMovida;

   private boolean blancas;

   public MovesBox(int x, int y, int w, int h, boolean blancas) {
      this(x, y, w, h, CoreFont.getSystemFont());

      this.blancas = blancas;
   }

   public MovesBox(int x, int y, int w, int h, CoreFont font) {
      super(x, y, w, h, font);

      // setStartTop(true);
      setSorted(false);
   }

   public void addMove(Move move) {
      String str = blancas ? move.toString() : move.toInvString();

      if (ultimaMovida == null) {
         ultimaMovida = str;
         // super.add(ultimaMovida);
         addItem(ultimaMovida);
      }
      else {
         // agrego 2da columna
         getObjects().set(getObjects().size() - 1, ultimaMovida + "\t\t" + str);

         super.refresh();

         ultimaMovida = null;
      }
   }
}
