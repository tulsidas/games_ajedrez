package ajedrez.client;

import pulpcore.Input;
import pulpcore.image.CoreImage;
import pulpcore.sprite.Group;
import pulpcore.sprite.ImageSprite;
import ajedrez.common.model.Move;
import ajedrez.common.model.Tablero;
import ajedrez.common.model.Tablero.ImpossibleMoveException;

public class TableroGroup extends Group {

    private static final int TAM_CELDA = 45;

    private AjedrezScene scene;

    private Tablero tablero;

    private ImageSprite borde;

    private int selected;

    // aca pongo las fichas
    private Group fichas;

    private CoreImage[] fichasImg;

    // para el highlight
    private ImageSprite h1, h2, h3, h4;

    // soy blancas?
    private boolean blancas;

    public TableroGroup(AjedrezScene parent, boolean blancas) {
        super(22, 70, 360, 360);

        this.tablero = new Tablero();
        tablero.init();

        this.scene = parent;
        this.blancas = blancas;

        // nada elegido
        this.selected = -1;

        CoreImage[] f = CoreImage.load("imgs/fichas.png").split(12);
        fichasImg = new CoreImage[] { null, f[5], f[4], f[3], f[2], f[1], f[0],
                f[11], f[10], f[9], f[8], f[7], f[6] };

        borde = new ImageSprite(CoreImage.load("imgs/borde.png"), 0, 0);
        borde.visible.set(false);
        add(borde);

        h1 = new ImageSprite(CoreImage.load("imgs/high.png"), 0, 0);
        h1.visible.set(false);
        h1.alpha.set(64);
        add(h1);

        h2 = new ImageSprite(CoreImage.load("imgs/high.png"), 0, 0);
        h2.visible.set(false);
        h2.alpha.set(64);
        add(h2);

        h3 = new ImageSprite(CoreImage.load("imgs/highYo.png"), 0, 0);
        h3.visible.set(false);
        h3.alpha.set(64);
        add(h3);

        h4 = new ImageSprite(CoreImage.load("imgs/highYo.png"), 0, 0);
        h4.visible.set(false);
        h4.alpha.set(64);
        add(h4);

        // las fichas arriba del tablero
        fichas = new Group();
        moveToTop(fichas);
        add(fichas);

        updateFichas();

        // add(new FilledSprite(0, 0, width.getAsInt(), height.getAsInt(),
        // Colors
        // .rgba(Colors.PURPLE, 128)));
    }

    @Override
    public void update(int elapsedTime) {
        if (enabled.get() && Input.isMouseReleased()) {
            int viewX = Input.getMouseX();
            int viewY = Input.getMouseY();

            int file = (int) getLocalX(viewX, viewY) / TAM_CELDA;
            int rank = (int) getLocalY(viewX, viewY) / TAM_CELDA;

            if (blancas) {
                rank = 7 - rank;
            }

            if (contains(viewX, viewY)) {
                if (selected < 0) {
                    // no hay nada seleccionado
                    if (hayFichaMia(rank, file)) {
                        markSelected(rank, file);
                    }
                }
                else {
                    // habia una pieza seleccionada
                    if (hayFichaMia(rank, file)) {
                        // volvi a cliquear en otra pieza mia, reselecciono
                        markSelected(rank, file);
                    }
                    else {
                        // cliquee en una vacia o en una del otro, muevo/como
                        Move move = new Move(selected, getCasilleroAsInt(rank,
                                blancas ? file : 7 - file));

                        try {
                            if (tablero.isPossibleMove(move)) {
                                // mando la movida
                                scene.makeMove(move);

                                // hago la movida en el tablero
                                tablero.makeMove(move);

                                // actualizo sandeces
                                updateFichas();
                                highlightYo(move);
                            }
                        }
                        catch (Tablero.ImpossibleMoveException e) {
                        }

                        clearSelected();
                    }
                }
            }
            else {
                clearSelected();
            }
        }
    }

    public void makeMove(Move m) {
        try {
            tablero.makeMove(m);
        }
        catch (ImpossibleMoveException e) {
            // no deberia pasar
            e.printStackTrace();
        }
        updateFichas();
    }

    public boolean isComida(Move m) {
        return tablero.getField(m.toRank(), m.toFile()) != Tablero.EM;
    }

    /**
     * PRE: isComida(m)
     * 
     * @param rank
     * @param file
     * @return
     */
    public int getPiezaComida(Move m) {
        return tablero.getField(m.toRank(), m.toFile());
    }

    public void reset() {
        tablero.init();
        updateFichas();
        clearSelected();
        clearHighlights();
    }

    public boolean jaque() {
        if (blancas) {
            return tablero.isAttackedByBlack(tablero.getWhiteKingPos());
        }
        else {
            return tablero.isAttackedByWhite(tablero.getBlackKingPos());
        }
    }

    private void clearHighlights() {
        h1.visible.set(false);
        h2.visible.set(false);
        h3.visible.set(false);
        h4.visible.set(false);
    }

    public void highlightOtro(Move move) {
        highLight(h1, h2, move);
    }

    private void highlightYo(Move move) {
        highLight(h3, h4, move);
    }

    private void highLight(ImageSprite s, ImageSprite t, Move move) {
        int fromRank = blancas ? 7 - move.fromRank() : move.fromRank();
        int toRank = blancas ? 7 - move.toRank() : move.toRank();
        int fromFile = blancas ? move.fromFile() : 7 - move.fromFile();
        int toFile = blancas ? move.toFile() : 7 - move.toFile();

        s.visible.set(true);
        s.setLocation(fromFile * TAM_CELDA, fromRank * TAM_CELDA);

        t.visible.set(true);
        t.setLocation(toFile * TAM_CELDA, toRank * TAM_CELDA);
    }

    private void updateFichas() {
        fichas.removeAll();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                int val = tablero.getField(j, i);
                if (val != Tablero.EM) {
                    if (blancas) {
                        fichas.add(new ImageSprite(fichasImg[val], i
                                * TAM_CELDA, (7 - j) * TAM_CELDA));
                    }
                    else {
                        fichas.add(new ImageSprite(fichasImg[val], (7 - i)
                                * TAM_CELDA, j * TAM_CELDA));
                    }
                }
            }
        }
    }

    private void markSelected(int rank, int file) {
        // seleccione una pieza mia
        borde.visible.set(true);
        borde.setLocation(file * TAM_CELDA, (blancas ? 7 - rank : rank)
                * TAM_CELDA);

        selected = getCasilleroAsInt(rank, (blancas ? file : 7 - file));
    }

    private void clearSelected() {
        selected = -1;
        borde.visible.set(false);
    }

    private boolean hayFichaMia(int rank, int file) {
        return (blancas && Tablero.isWhiteFigure(tablero.getField(rank, file)))
                || (!blancas && Tablero.isBlackFigure(tablero.getField(rank,
                        7 - file)));
    }

    private int getCasilleroAsInt(int rank, int file) {
        int ret = (rank << 3) + (file & 7);
        return ret;
    }
}
