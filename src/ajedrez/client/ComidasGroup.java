package ajedrez.client;

import java.util.HashMap;
import java.util.Map;

import pulpcore.image.CoreImage;
import pulpcore.sprite.Group;
import pulpcore.sprite.ImageSprite;
import ajedrez.common.model.Tablero;

public class ComidasGroup extends Group {

    private Map<Integer, CoreImage> piezas;

    private Map<Integer, Integer> agregadas;

    public ComidasGroup() {
        super(400, 60, 45, 378);

        // peon, caballo, alfil, torre, reina, rey
        CoreImage[] imgs = CoreImage.load("imgs/comidas.png").split(12);

        piezas = new HashMap<Integer, CoreImage>();
        piezas.put(Tablero.BP, imgs[0]);
        piezas.put(Tablero.BN, imgs[1]);
        piezas.put(Tablero.BB, imgs[2]);
        piezas.put(Tablero.BR, imgs[3]);
        piezas.put(Tablero.BQ, imgs[4]);
        piezas.put(Tablero.BK, imgs[5]);

        piezas.put(Tablero.WP, imgs[6]);
        piezas.put(Tablero.WN, imgs[7]);
        piezas.put(Tablero.WB, imgs[8]);
        piezas.put(Tablero.WR, imgs[9]);
        piezas.put(Tablero.WQ, imgs[10]);
        piezas.put(Tablero.WK, imgs[11]);

        agregadas = new HashMap<Integer, Integer>();
    }

    public void add(int pieza) {
        if (!agregadas.containsKey(pieza)) {
            agregadas.put(pieza, 0);
        }

        int mulx = (pieza == Tablero.BP || pieza == Tablero.WP) ? 4 : 20;

        add(new ImageSprite(piezas.get(pieza), agregadas.get(pieza) * mulx,
                pieza * 28));

        // sumo
        agregadas.put(pieza, agregadas.get(pieza) + 1);
    }
}