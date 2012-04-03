package ajedrez.common.model;


/**
 * This class represents a chess move.
 */
public class Move {

    private int from;

    private int to;

    private int pawnPromotion = Tablero.NONE;

    /**
     * Returns a string representation of this Move.
     */
    public String toString() {
        String moveString = "" + (char) ((int) 'a' + (from & 7)) + ""
                + ((from >> 3) + 1) + (char) ((int) 'a' + (to & 7)) + ""
                + ((to >> 3) + 1);

        if (pawnPromotion != Tablero.NONE) {
            if (pawnPromotion == Tablero.QUEEN) {
                moveString += "Q";
            }
            else if (pawnPromotion == Tablero.ROOK) {
                moveString += "R";
            }
            else if (pawnPromotion == Tablero.BISHOP) {
                moveString += "B";
            }
            else if (pawnPromotion == Tablero.KNIGHT) {
                moveString += "N";
            }
        }
        return moveString;
    }

    /**
     * Returns a string representation of this Move.
     */
    public String toInvString() {
        String moveString = "" + (char) ((int) 'a' + 7 - (from & 7))
                + (7 - (from >> 3) + 1) + (char) ((int) 'a' + 7 - (to & 7))
                + (7 - (to >> 3) + 1);

        if (pawnPromotion != Tablero.NONE) {
            if (pawnPromotion == Tablero.QUEEN) {
                moveString += "Q";
            }
            else if (pawnPromotion == Tablero.ROOK) {
                moveString += "R";
            }
            else if (pawnPromotion == Tablero.BISHOP) {
                moveString += "B";
            }
            else if (pawnPromotion == Tablero.KNIGHT) {
                moveString += "N";
            }
        }
        return moveString;
    }

    /**
     * Returns true if this move is valid. The from an to fields will be checked
     * to be inside of the board and the selected promotion has to be a valid
     * choice. If this move was construction from an malformed string, it will
     * return false.
     */
    public boolean isValid() {
        return from > 0
                && from < 64
                && to > 0
                && to < 64
                && (pawnPromotion == Tablero.NONE
                        || pawnPromotion == Tablero.QUEEN
                        || pawnPromotion == Tablero.ROOK
                        || pawnPromotion == Tablero.BISHOP || pawnPromotion == Tablero.KNIGHT);
    }

    /**
     * Returns the number of the field this move starts from.
     */
    public int fromField() {
        return from;
    }

    /**
     * Returns the number of the field this move starts from.
     */
    public int toField() {
        return to;
    }

    /**
     * Returns the file of the field this move starts from.
     */
    public int fromFile() {
        return (int) (from & 7);
    }

    /**
     * Returns the rank of the field this move starts from.
     */
    public int fromRank() {
        return (int) (from >> 3);
    }

    /**
     * Returns the file of the field this move goes to.
     */
    public int toFile() {
        return (int) (to & 7);
    }

    /**
     * Returns the rank of the field this move goes to.
     */
    public int toRank() {
        return (int) (to >> 3);
    }

    /**
     * Returns the figure that a pawn would promote to.
     */
    public int pawnPromotion() {
        return pawnPromotion;
    }

    /**
     * Sets the figure a pawn would promote to.
     */
    public void setPawnPromotion(int prom) {
        pawnPromotion = prom;
    }

    /**
     * Creates an integer hash of this move.
     */
    public int hashCode() {
        // Note: pawnPromotion is not considered here.
        return from | (to << 6);
    }

    /**
     * Compares this move to the given move.
     */
    public boolean equals(Move m) {
        return m.fromField() == from && m.toField() == to
                && m.pawnPromotion() == pawnPromotion
                && m.isValid() == isValid();
    }

    /**
     * Creates a move, from field number to field number
     * 
     * @param f
     *            from-field number
     * @param t
     *            to-field number
     */
    public Move(int f, int t) {
        from = f;
        to = t;
    }

    /**
     * Creates a move, from field number to field number
     * 
     * @param f
     *            from-field number
     * @param t
     *            to-field number
     * @param _pawnPromotion
     *            what a promoted pawn shall become
     */
    public Move(int f, int t, int _pawnPromotion) {
        from = f;
        to = t;
        pawnPromotion = _pawnPromotion;
    }

    /**
     * Creates a move rank/file to rank/file.
     */
    public Move(int fromRank, int fromFile, int toRank, int toFile) {
        from = (int) ((fromRank << 3) + fromFile);
        to = (int) ((toRank << 3) + toFile);
    }
}
