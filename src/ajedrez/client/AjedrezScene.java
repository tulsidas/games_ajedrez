package ajedrez.client;

import static pulpcore.image.Colors.WHITE;
import static pulpcore.image.Colors.rgb;
import pulpcore.CoreSystem;
import pulpcore.Input;
import pulpcore.Stage;
import pulpcore.animation.Easing;
import pulpcore.animation.Timeline;
import pulpcore.animation.event.RemoveSpriteEvent;
import pulpcore.animation.event.TimelineEvent;
import pulpcore.image.Colors;
import pulpcore.image.CoreFont;
import pulpcore.image.CoreImage;
import pulpcore.image.filter.DropShadow;
import pulpcore.scene.Scene;
import pulpcore.sound.Sound;
import pulpcore.sprite.Button;
import pulpcore.sprite.Group;
import pulpcore.sprite.ImageSprite;
import pulpcore.sprite.Label;
import pulpcore.sprite.TextField;
import ajedrez.common.ifaz.GameHandler;
import ajedrez.common.messages.AceptaTablasMessage;
import ajedrez.common.messages.MoveMessage;
import ajedrez.common.messages.TablasMessage;
import ajedrez.common.model.AjedrezRoom;
import ajedrez.common.model.Move;
import client.DisconnectedScene;
import client.InGameChatArea;
import client.PingScene;
import client.PulpcoreUtils;
import client.DisconnectedScene.Reason;

import common.game.AbandonRoomMessage;
import common.game.ProximoJuegoMessage;
import common.messages.chat.RoomChatMessage;
import common.messages.server.RoomJoinedMessage;
import common.model.AbstractRoom;
import common.model.User;

public class AjedrezScene extends PingScene implements GameHandler {

   private static final int[] RANDOM_COLORS = new int[] { rgb(0x00e300),
         rgb(0xf6f300), rgb(0x00bff6), rgb(0x2bc5ad), rgb(0xff0099),
         rgb(0xff9900) };

   private static final int LABEL_X = 80;

   private static final int LABEL_W = 219;

   private GameConnector connection;

   private User currentUser, oponente;

   private AjedrezRoom room;

   private boolean mustDisconnect;

   // me toca jugar
   private boolean miTurno;

   // el momento en que tengo que abandonar
   private long relojMio, relojOtro;

   // lo que falta para que me rajen
   private int tiempoMio, tiempoOtro;

   private InGameChatArea chatBox;

   private TextField chatTF;

   private MovesBox movesBox;

   private ComidasGroup comidas;

   private Button sendChat, abandonGame, disableSounds;

   private Button nuevoJuegoSi, nuevoJuegoNo;

   private int colorYo, colorOtro;

   // private CoreFont fontYo, fontOtro;

   private CoreFont din13, din13white, din24, din30w;

   private Label turno, timerLabelYo, timerLabelOtro;

   private Label finalLabel, finalLabel2;

   private TableroGroup tablero;

   private Button tablas;

   // SFX
   private Sound tic;

   public AjedrezScene(GameConnector connection, User usr, AjedrezRoom room) {
      super(connection);

      this.connection = connection;
      this.currentUser = usr;
      this.room = room;

      // inject
      connection.setGameHandler(this);
   }

   @Override
   public void load() {
      // fonts
      din13 = CoreFont.load("imgs/DIN13.font.png");
      din24 = CoreFont.load("imgs/DIN24.font.png").tint(WHITE);
      CoreFont din30 = CoreFont.load("imgs/DIN30.font.png");
      din30w = din30.tint(WHITE);
      din13white = din13.tint(WHITE);

      // blancas si soy el primero
      boolean blancas = room.getPlayers().size() == 1;

      add(new ImageSprite("imgs/fondo.png", 0, 0));

      // label con datos de la sala
      add(new Label(din13white, "por " + room.getPuntosApostados() + " puntos",
            420, 40));

      // chat box
      chatBox = new InGameChatArea(din13, 466, 254, 244, 141);
      add(chatBox);

      // campo de texto donde se chatea
      chatTF = new TextField(din13, din13white, "", 467, 407, 222, -1);
      add(chatTF);

      // donde aparecen las movidas
      movesBox = new MovesBox(496, 142, 214, 100, blancas);
      add(movesBox);

      // boton para enviar el chat (asociado al ENTER)
      sendChat = new Button(CoreImage.load("imgs/btn-send.png").split(3), 697,
            403);
      sendChat.setKeyBinding(Input.KEY_ENTER);
      add(sendChat);

      comidas = new ComidasGroup();
      add(comidas);

      // mi color
      colorYo = RANDOM_COLORS[(int) (Math.random() * RANDOM_COLORS.length)];
      // fontYo = din13.tint(colorYo);

      // el otro (si hay)
      for (User otro : room.getPlayers()) {
         if (!otro.equals(currentUser)) {
            oponente = otro;
            drawNames();
            break;
         }
      }

      // tablero
      tablero = new TableroGroup(this, blancas);
      tablero.enabled.set(false);
      add(tablero);

      // boton abandonar
      abandonGame = new Button(CoreImage.load("imgs/btn-abandonar.png")
            .split(3), 300, 0);
      add(abandonGame);

      // mute
      disableSounds = new Button(CoreImage.load("imgs/sonidos.png").split(6),
            255, 30, true);
      disableSounds.setSelected(CoreSystem.isMute());
      disableSounds.setPixelLevelChecks(false);
      add(disableSounds);

      nuevoJuegoSi = new Button(CoreImage.load("imgs/btn-si.png").split(3),
            100, 200);
      nuevoJuegoSi.enabled.set(false);
      nuevoJuegoSi.setPixelLevelChecks(false);

      nuevoJuegoNo = new Button(CoreImage.load("imgs/btn-no.png").split(3),
            200, 200);
      nuevoJuegoNo.enabled.set(false);
      nuevoJuegoNo.setPixelLevelChecks(false);

      turno = new Label(din13white, "Esperando oponente", 560, 40);

      finalLabel = new Label(din30w, "", 0, 130);
      finalLabel.visible.set(false);
      finalLabel.setFilter(new DropShadow(3, 3, Colors.BLACK));
      add(finalLabel);
      finalLabel2 = new Label(din30w, "", 0, 160);
      finalLabel2.visible.set(false);
      finalLabel2.setFilter(new DropShadow(3, 3, Colors.BLACK));
      add(finalLabel2);

      // animo el alpha para que titile
      Timeline alphaCycle = new Timeline();
      int dur = 1000;
      alphaCycle.animate(turno.alpha, 255, 0, dur, Easing.NONE, 0);
      alphaCycle.animate(turno.alpha, 0, 255, dur, Easing.NONE, dur);
      alphaCycle.loopForever();
      addTimeline(alphaCycle);
      add(turno);

      // timer (en un nuevo layer para estar encima de todo)
      timerLabelYo = new Label(din13white, "", 0, 0);
      timerLabelOtro = new Label(din13white, "", 355, 35);

      Group g = new Group();
      g.add(timerLabelYo);
      g.add(timerLabelOtro);
      addLayer(g);

      // sfx
      tic = Sound.load("sfx/tic.wav");

      resetRelojGlobal();

      tablas = new Button(CoreImage.load("imgs/btn-tablas.png").split(3), 380,
            0);
      add(tablas);

      // envio mensaje que me uni a la sala correctamente
      connection.send(new RoomJoinedMessage());
   }

   public void unload() {
      if (mustDisconnect) {
         connection.disconnect();
      }
   }

   @Override
   public void update(int elapsedTime) {
      super.update(elapsedTime);

      if (disableSounds.isClicked()) {
         CoreSystem.setMute(disableSounds.isSelected());
      }
      else if (nuevoJuegoSi.enabled.get() && nuevoJuegoSi.isClicked()) {
         nuevoJuegoSi.enabled.set(false);
         remove(nuevoJuegoSi);
         nuevoJuegoNo.enabled.set(false);
         remove(nuevoJuegoNo);

         finalLabel.setText("Esperando respuesta del oponente...");
         finalLabel.visible.set(true);
         finalLabel2.visible.set(false);
         PulpcoreUtils.centerSprite(finalLabel, LABEL_X, LABEL_W);

         // paro reloj
         setMiTurno(false);

         connection.send(new ProximoJuegoMessage(true));
      }
      else if (nuevoJuegoNo.enabled.get() && nuevoJuegoNo.isClicked()) {
         // aviso que no
         connection.send(new ProximoJuegoMessage(false));

         invokeLater(new Runnable() {
            public void run() {
               // y me rajo al lobby
               setScene(new LobbyScene(currentUser, connection));
            }
         });
      }

      if (miTurno) {
         updateRelojMio(elapsedTime);

         if (tablas.isClicked()) {
            // mando tablas
            connection.send(new TablasMessage());

            // paro timer
            setMiTurno(false);
         }
      }
      else {
         if (oponente != null) {
            // actualizo el reloj del otro
            updateRelojOtro(elapsedTime);
         }
      }

      if (sendChat.isClicked() && chatTF.getText().trim().length() > 0) {
         connection.send(new RoomChatMessage(chatTF.getText()));

         chatBox.addLine(currentUser.getName() + ": " + chatTF.getText());
         chatTF.setText("");
      }
      else if (abandonGame.enabled.get() && abandonGame.isClicked()) {
         abandonGame();
      }
   }

   public void kicked() {
      invokeLater(new Runnable() {
         public void run() {
            connection.disconnect();
            Stage.setScene(new DisconnectedScene(Reason.KICKED));
         }
      });
   }

   public void disconnected() {
      invokeLater(new Runnable() {
         public void run() {
            Stage.setScene(new client.DisconnectedScene(Reason.FAILED));
         }
      });
   }

   public void incomingChat(final User from, final String msg) {
      invokeLater(new Runnable() {
         public void run() {
            tic.play();

            chatBox.addLine(from.getName() + ": " + msg);
         }
      });
   }

   public void oponenteAbandono(boolean enJuego, User user) {
      if (enJuego) {
         finalLabel.setText("¡Tu oponente abandono!");
      }
      else {
         finalLabel.setText("No quiso jugar otro");
      }

      PulpcoreUtils.centerSprite(finalLabel, LABEL_X, LABEL_W);
      finalLabel.visible.set(true);
      finalLabel2.visible.set(false);

      nuevoJuegoSi.visible.set(false);
      nuevoJuegoNo.visible.set(false);

      addEvent(new TimelineEvent(2000) {
         @Override
         public void run() {
            setScene(new LobbyScene(currentUser, connection));
         }
      });
   }

   public void roomJoined(AbstractRoom room, User user) {
      if (!user.equals(currentUser)) {
         oponente = user;
         drawNames();
      }
   }

   public void updatePoints(int puntos) {
      // actualizo puntos
      currentUser.setPuntos(puntos);

      invokeLater(new Runnable() {
         public void run() {
            jugarOtro();
         }
      });
   }

   private final void setScene(final Scene s) {
      mustDisconnect = false;
      Stage.setScene(s);
   }

   private void drawNames() {
      invokeLater(new Runnable() {
         public void run() {
            int MAX_SIZE = 10;

            String yo = currentUser.getName().toUpperCase();
            if (yo.length() > MAX_SIZE) {
               yo = yo.substring(0, MAX_SIZE);
            }
            String otro = oponente.getName().toUpperCase();
            if (otro.length() > MAX_SIZE) {
               otro = otro.substring(0, MAX_SIZE);
            }

            do {
               // uno distinto!
               colorOtro = RANDOM_COLORS[(int) (Math.random() * RANDOM_COLORS.length)];
            }
            while (colorOtro == colorYo);

            // fontOtro = CoreFont.load("imgs/DIN13.font.png").tint(colorOtro);

            add(new Label(din24, yo, 520, 65));
            add(new Label(din24, otro, 520, 100));
         }
      });
   }

   private void abandonGame() {
      // envio abandono
      connection.send(new AbandonRoomMessage());

      invokeLater(new Runnable() {
         public void run() {
            // me rajo al lobby
            setScene(new LobbyScene(currentUser, connection));
         }
      });
   }

   private void setMiTurno(boolean miTurno) {
      if (miTurno) {
         turno.setText("Te toca");
      }
      else {
         turno.setText("Esperando jugada");
      }

      this.miTurno = miTurno;

      tablero.enabled.set(miTurno);
   }

   // /////////////////
   // GameHandler
   // /////////////////
   /** recibo movida */
   public void move(Move move) {
      movesBox.addMove(move);

      if (tablero.isComida(move)) {
         comidas.add(tablero.getPiezaComida(move));
      }

      tablero.makeMove(move);
      tablero.highlightOtro(move);

      if (tablero.jaque()) {
         // System.out.println("JAQUE");
      }

      setMiTurno(true);
   }

   /** envio movida */
   public void makeMove(Move move) {
      movesBox.addMove(move);

      if (tablero.isComida(move)) {
         comidas.add(tablero.getPiezaComida(move));
      }

      connection.send(new MoveMessage(move));
      setMiTurno(false);
   }

   /** recibo oferta de tablas */
   public void tablas() {
      setMiTurno(true);

      add(new TablasPopUp(this, 188, 193));
   }

   public void setAceptarTablas(boolean acepto) {
      if (acepto) {
         gameOver("¡Tablas! Empate");
         jugarOtro();
      }
      else {
         // no era mi turno
         setMiTurno(false);
      }

      connection.send(new AceptaTablasMessage(acepto));
   }

   public void aceptaTablas(boolean acepta) {
      if (acepta) {
         gameOver("¡Empate!");
         jugarOtro();
      }
      else {
         // seguimos
         invokeLater(new Runnable() {
            public void run() {
               Label l = new Label(din13.tint(Colors.RED), "No aceptó tablas",
                     150, 150);
               add(l);

               l.moveTo(300, 300, 3000, Easing.REGULAR_OUT);
               l.alpha.animateTo(0, 2000, Easing.NONE, 1000);

               addEvent(new RemoveSpriteEvent(getMainLayer(), l, 3000));

               setMiTurno(true);
            }
         });
      }
   }

   public void newGame() {
      invokeLater(new Runnable() {
         public void run() {
            setMiTurno(false);

            // habilito tablero
            tablero.enabled.set(true);

            // reseteo tablero
            tablero.reset();

            // borro comidas
            comidas.removeAll();

            // borro movidas
            movesBox.removeAll();

            turno.setText("Esperando oponente");

            nuevoJuegoNo.update(0);
            nuevoJuegoSi.update(0);

            finalLabel.visible.set(false);
            finalLabel2.visible.set(false);

            // visibilizo y habilito el boton de abandonar
            abandonGame.visible.set(true);
            abandonGame.enabled.set(true);

            resetRelojGlobal();
         }
      });
   }

   public void startGame(final boolean start) {
      invokeLater(new Runnable() {
         public void run() {
            setMiTurno(start);
         }
      });
   }

   public void finJuego(final boolean victoria) {
      invokeLater(new Runnable() {
         public void run() {
            String txt = victoria ? "¡Ganaste! Sos groso"
                  : "¡Perdiste, zoquete!";
            if (!victoria) {
               // TODO haha.play();
            }

            gameOver(txt);

            // cambio texto del cartel
            turno.setText("Actualizando puntos");

            // espero a que lleguen los puntos
            setMiTurno(false);
         }
      });
   }

   /**
    * muestra textos de game over
    * 
    * @param txt
    */
   private void gameOver(String txt) {
      finalLabel.setText(txt);
      finalLabel.visible.set(true);
      PulpcoreUtils.centerSprite(finalLabel, LABEL_X, LABEL_W);

      // invisibilizo y deshabilito el boton de abandonar
      abandonGame.visible.set(false);
      abandonGame.enabled.set(false);

      // deshabilito tablero
      tablero.enabled.set(false);
   }

   private void jugarOtro() {
      // obligo a contestar o que vuelva al lobby
      setMiTurno(true);

      if (currentUser.getPuntos() >= room.getPuntosApostados()) {
         add(nuevoJuegoSi);
         nuevoJuegoSi.enabled.set(true);
         add(nuevoJuegoNo);
         nuevoJuegoNo.enabled.set(true);

         finalLabel2.setText("¿Otro partido?");
         finalLabel2.visible.set(true);
         PulpcoreUtils.centerSprite(finalLabel2, LABEL_X, LABEL_W);
      }
      else {
         // no me alcanza para jugar otro

         // aviso que no
         connection.send(new ProximoJuegoMessage(false));

         // y me rajo al lobby
         setScene(new LobbyScene(currentUser, connection));
      }
   }

   private void resetRelojGlobal() {
      relojMio = room.getMinutos() * 60 * 1000;
      relojOtro = room.getMinutos() * 60 * 1000;

      updateRelojMio(0);
      updateRelojOtro(0);
   }

   private void updateRelojMio(int elapsedTime) {
      relojMio -= elapsedTime;

      // actualizacion del timer
      int tg = Math.round(relojMio / 1000);

      if (tg < 0) {
         abandonGame();
      }

      if (tg != tiempoMio) {
         tiempoMio = tg;

         timerLabelYo.setText(tg / 60 + ":" + ((tg % 60 < 10) ? "0" : "") + tg
               % 60);
         timerLabelYo.alpha.set(0xff);

         if (tg >= 10) {
            timerLabelYo.x.set(285);
            timerLabelYo.y.set(35);
         }
         else if (tg < 10) {
            timerLabelYo.x.set(170);
            timerLabelYo.y.set(210);

            timerLabelYo.alpha.animateTo(0, 500);
            timerLabelYo.width.animateTo(100, 500);
            timerLabelYo.height.animateTo(100, 500);
            timerLabelYo.x.animateTo(timerLabelYo.x.get() - 50, 500);
            timerLabelYo.y.animateTo(timerLabelYo.y.get() - 50, 500);
         }
      }
   }

   private void updateRelojOtro(int elapsedTime) {
      relojOtro -= elapsedTime;

      // actualizacion del timer
      int tg = Math.round(relojOtro / 1000);

      if (tg != tiempoOtro) {
         tiempoOtro = tg;

         timerLabelOtro.setText(tg / 60 + ":" + ((tg % 60 < 10) ? "0" : "")
               + tg % 60);
      }
   }
}