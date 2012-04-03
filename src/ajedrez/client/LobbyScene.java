package ajedrez.client;

import pulpcore.image.CoreFont;
import pulpcore.image.CoreImage;
import pulpcore.scene.Scene;
import pulpcore.sprite.ImageSprite;
import pulpcore.sprite.Sprite;
import ajedrez.common.messages.client.CreateAjedrezRoomMessage;
import ajedrez.common.model.AjedrezRoom;
import client.AbstractGameConnector;
import client.AbstractLobbyScene;
import client.Spinner;

import common.messages.Message;
import common.model.AbstractRoom;
import common.model.User;

public class LobbyScene extends AbstractLobbyScene {

    private Spinner tiempo;

    public LobbyScene(User user, AbstractGameConnector connection) {
        super(user, connection);
    }

    @Override
    public void load() {
        super.load();

        // spinner background
        add(new ImageSprite(CoreImage.load("imgs/jugar-por-minutos.png"), 80,
                385));

        tiempo = new Spinner(80, 400, 54, 25, CoreFont
                .load("imgs/DIN18_numeric.font.png"), CoreImage.load(
                "imgs/btn-puntos.png").split(3));
        tiempo.setMinValue(1);
        tiempo.setMaxValue(180);

        // 15' default
        tiempo.setValue(15);
        add(tiempo);
    }

    @Override
    protected Scene getGameScene(AbstractGameConnector connection, User usr,
            AbstractRoom room) {
        return new AjedrezScene((GameConnector) connection, usr,
                (AjedrezRoom) room);
    }

    @Override
    protected Sprite getGameImage() {
        return new ImageSprite(CoreImage.load("imgs/logo-chess.png"), 495, 10);
    }

    @Override
    protected Message createRoomMessage(int value) {
        return new CreateAjedrezRoomMessage(value, tiempo.getValue());
    }
}
