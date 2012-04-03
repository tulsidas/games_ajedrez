package ajedrez.common.model;

import java.util.ArrayList;
import java.util.List;

public class Tablero {

    public static final int EM = 0;

    public static final int WK = 1;

    public static final int WQ = 2;

    public static final int WR = 3;

    public static final int WB = 4;

    public static final int WN = 5;

    public static final int WP = 6;

    public static final int BK = 7;

    public static final int BQ = 8;

    public static final int BR = 9;

    public static final int BB = 10;

    public static final int BN = 11;

    public static final int BP = 12;

    public static final int MAX_FIGURE_VALUE = 12;

    public static final int NONE = 0;

    public static final int KING = 1;

    public static final int QUEEN = 2;

    public static final int ROOK = 3;

    public static final int BISHOP = 4;

    public static final int KNIGHT = 5;

    public static final int PAWN = 6;

    private static final int[] startPosition = { WR, WN, WB, WQ, WK, WB, WN,
            WR, WP, WP, WP, WP, WP, WP, WP, WP, EM, EM, EM, EM, EM, EM, EM, EM,
            EM, EM, EM, EM, EM, EM, EM, EM, EM, EM, EM, EM, EM, EM, EM, EM, EM,
            EM, EM, EM, EM, EM, EM, EM, BP, BP, BP, BP, BP, BP, BP, BP, BR, BN,
            BB, BQ, BK, BB, BN, BR };

    String[] name = { "-", "K", "Q", "R", "B", "N", "P", "k", "q", "r", "b",
            "n", "p" };

    protected int[] field = new int[64];

    protected boolean whiteKingHasMoved;

    protected boolean blackKingHasMoved;

    protected boolean leftBlackRookHasMoved;

    protected boolean rightBlackRookHasMoved;

    protected boolean leftWhiteRookHasMoved;

    protected boolean rightWhiteRookHasMoved;

    protected boolean isWhiteTurn;

    protected int blackKingPos, whiteKingPos;

    protected int lastFrom = 64; // 3 bits per rank/file

    protected int lastTo = 64; // (x>>3) gives rank, (x&7) gives file

    protected int pawnPromotion = NONE;

    protected boolean pawnHasPromoted = false;

    protected int enPassantTarget = 64;

    protected int halfmoveClock = 0;

    protected int fullmoveNumber = 0;

    // This is a cache to speed up makeMove(String algebraicMove);
    private boolean doNotConsiderCheckInTryMove = false;

    private static long[] knightMovesBitboards;

    private static long[] kingMovesBitboards;

    private static long[] rookMovesBitboards;

    private static long[] queenMovesBitboards;

    private static long[] bishopMovesBitboards;

    private static int[][] knightMoves;

    private static int[][] rookMoves;

    private static int[][] queenMoves;

    private static List<Move>[] kingMoves;

    private static List<Move>[] bishopMoves;

    private static long[][] horMoves;

    private static long[][] vertMoves; // Vertical oves for the given

    private static long[][] diag1Moves; // Diagonal moves

    private static long[][] diag2Moves; // Diagonal moves

    private long whitePieces;

    private long blackPieces;

    private long whitePiecesRot; // Rotated

    private long blackPiecesRot;

    private long whitePiecesDiag1; // Rotated

    private long blackPiecesDiag1;

    private long whitePiecesDiag2; // Rotated

    private long blackPiecesDiag2;

    private long whiteAttackBoard = 0l;

    private long blackAttackBoard = 0l;

    private static int[] diag1length = { 1, 2, 3, 4, 5, 6, 7, 8, 2, 3, 4, 5, 6,
            7, 8, 7, 3, 4, 5, 6, 7, 8, 7, 6, 4, 5, 6, 7, 8, 7, 6, 5, 5, 6, 7,
            8, 7, 6, 5, 4, 6, 7, 8, 7, 6, 5, 4, 3, 7, 8, 7, 6, 5, 4, 3, 2, 8,
            7, 6, 5, 4, 3, 2, 1, };

    private static int[] shift = { 0, 1, 3, 6, 10, 15, 21, 28, 1, 3, 6, 10, 15,
            21, 28, 36, 3, 6, 10, 15, 21, 28, 36, 43, 6, 10, 15, 21, 28, 36,
            43, 49, 10, 15, 21, 28, 36, 43, 49, 54, 15, 21, 28, 36, 43, 49, 54,
            58, 21, 28, 36, 43, 49, 54, 58, 61, 28, 36, 43, 49, 54, 58, 61, 63 };

    private static int diag2length[] = { 8, 7, 6, 5, 4, 3, 2, 1, 7, 8, 7, 6, 5,
            4, 3, 2, 6, 7, 8, 7, 6, 5, 4, 3, 5, 6, 7, 8, 7, 6, 5, 4, 4, 5, 6,
            7, 8, 7, 6, 5, 3, 4, 5, 6, 7, 8, 7, 6, 2, 3, 4, 5, 6, 7, 8, 7, 1,
            2, 3, 4, 5, 6, 7, 8 };

    private static int diag2pos[] = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1,
            1, 1, 0, 1, 2, 2, 2, 2, 2, 2, 0, 1, 2, 3, 3, 3, 3, 3, 0, 1, 2, 3,
            4, 4, 4, 4, 0, 1, 2, 3, 4, 5, 5, 5, 0, 1, 2, 3, 4, 5, 6, 6, 0, 1,
            2, 3, 4, 5, 6, 7, };

    private static int[] diag1Mapping = { 0, 1, 8, 2, 9, 16, 3, 10, 17, 24, 4,
            11, 18, 25, 32, 5, 12, 19, 26, 33, 40, 6, 13, 20, 27, 34, 41, 48,
            7, 14, 21, 28, 35, 42, 49, 56, 15, 22, 29, 36, 43, 50, 57, 23, 30,
            37, 44, 51, 58, 31, 38, 45, 52, 59, 39, 46, 53, 60, 47, 54, 61, 55,
            62, 63 };

    private static int[] diag1ReverseMapping;

    private static int[] diag2Mapping = { 56, 48, 57, 40, 49, 58, 32, 41, 50,
            59, 24, 33, 42, 51, 60, 16, 25, 34, 43, 52, 61, 8, 17, 26, 35, 44,
            53, 62, 0, 9, 18, 27, 36, 45, 54, 63, 1, 10, 19, 28, 37, 46, 55, 2,
            11, 20, 29, 38, 47, 3, 12, 21, 30, 39, 4, 13, 22, 31, 5, 14, 23, 6,
            15, 7 };

    private static int[] diag2ReverseMapping;

    private static int diag2shift[] = { 28, 36, 43, 49, 54, 58, 61, 63, 21, 28,
            36, 43, 49, 54, 58, 61, 15, 21, 28, 36, 43, 49, 54, 58, 10, 15, 21,
            28, 36, 43, 49, 54, 6, 10, 15, 21, 28, 36, 43, 49, 3, 6, 10, 15,
            21, 28, 36, 43, 1, 3, 6, 10, 15, 21, 28, 36, 0, 1, 3, 6, 10, 15,
            21, 28 };

    public int score;

    public static class ImpossibleMoveException extends Exception {
        private static final long serialVersionUID = 8230907334323191459L;

        public ImpossibleMoveException(String move) {
            super(move);
        }
    };

    public Move getLastMove() {
        if (lastFrom == 64 || lastTo == 64)
            return null;
        Move move = new Move(lastFrom, lastTo);
        if (pawnHasPromoted)
            move.setPawnPromotion(pawnPromotion);
        return move;
    }

    public boolean isWhiteTurn() {
        return isWhiteTurn;
    }

    public int getWhiteKingPos() {
        return whiteKingPos;
    }

    public int getBlackKingPos() {
        return blackKingPos;
    }

    public int getFullMoveNumber() {
        return fullmoveNumber;
    }

    public int getField(int rank, int file) {
        return field[(rank << 3) + (file & 7)];
    }

    public String toString() {
        String s = "\n";
        for (int r = 7; r >= 0; r--) {
            for (int f = 0; f < 8; f++)
                s += " " + name[field[(r << 3) + f]];
            s += "\n";
        }
        s += "Last move: " + getLastMove() + "\n";
        return s;
    }

    public static boolean isWhiteFigure(int f) {
        return f > 0 && f < 7;
    }

    public static boolean isBlackFigure(int f) {
        return f > 6 && f < 13;
    }

    public void inithorMoves() {
        horMoves = new long[64][256];
        int moves[][] = new int[8][256];
        for (int file = 0; file < 8; file++) {
            for (int occ = 0; occ < 256; occ++) {
                int n = file + 1;
                while (n < 8) {
                    moves[file][occ] |= (1 << n);
                    if ((occ & (1 << n)) != 0)
                        break;
                    n++;
                }
                n = file - 1;
                while (n >= 0) {
                    moves[file][occ] |= (1 << n);
                    if ((occ & (1 << n)) != 0)
                        break;
                    n--;
                }
            }
        }
        for (int rank = 0; rank < 8; rank++) {
            for (int file = 0; file < 8; file++) {
                for (int occ = 0; occ < 256; occ++) {
                    horMoves[(rank << 3) + file][occ] = ((long) moves[file][occ]) << (rank * 8);
                }
            }
        }
    }

    public void initVertMoves() {
        vertMoves = new long[64][256];
        long moves[][] = new long[8][256];
        for (int rank = 0; rank < 8; rank++) {
            for (int occ = 0; occ < 256; occ++) {
                int n = rank + 1;
                while (n < 8) {
                    moves[rank][occ] |= (1l << (n * 8));
                    if ((occ & (1 << n)) != 0)
                        break;
                    n++;
                }
                n = rank - 1;
                while (n >= 0) {
                    moves[rank][occ] |= (1l << (n * 8));
                    if ((occ & (1 << n)) != 0)
                        break;
                    n--;
                }
            }
        }
        for (int rank = 0; rank < 8; rank++) {
            for (int file = 0; file < 8; file++) {
                for (int occ = 0; occ < 256; occ++) {
                    vertMoves[(rank << 3) + file][occ] = moves[rank][occ] << file;
                }
            }
        }
    }

    public void initDiag1Moves() {
        diag1Moves = new long[64][256];
        for (int rank = 0; rank < 8; rank++) {
            for (int file = 0; file < 8; file++) {
                int diagLength = (rank + file <= 7) ? rank + file + 1
                        : (7 - rank) + (7 - file) + 1;
                int sh = shift[(rank << 3) + file];
                for (int occ = 0; occ < (1 << diagLength); occ++) {
                    int n = ((rank + file <= 7) ? rank : 7 - file) + 1; // Position
                    // on
                    // diagonal
                    while (n < diagLength) {
                        diag1Moves[(rank << 3) + file][occ] |= 1l << diag1Mapping[sh
                                + n];
                        if ((occ & (1 << n)) != 0)
                            break;
                        n++;
                    }
                    n = ((rank + file <= 7) ? rank : 7 - file) - 1; // Position
                    // on
                    // diagonal
                    while (n >= 0) {
                        diag1Moves[(rank << 3) + file][occ] |= 1l << diag1Mapping[sh
                                + n];
                        if ((occ & (1 << n)) != 0)
                            break;
                        n--;
                    }
                }
            }
        }
    }

    public void initDiag2Moves() {
        diag2Moves = new long[64][256];
        for (int rank = 0; rank < 8; rank++) {
            for (int file = 0; file < 8; file++) {
                int diagLength = diag2length[(rank << 3) + file];
                int sh = diag2shift[(rank << 3) + file];
                for (int occ = 0; occ < (1 << diagLength); occ++) {
                    int n = diag2pos[(rank << 3) + file] + 1;
                    while (n < diagLength) {
                        diag2Moves[(rank << 3) + file][occ] |= 1l << diag2Mapping[sh
                                + n];
                        if ((occ & (1 << n)) != 0)
                            break;
                        n++;
                    }
                    n = diag2pos[(rank << 3) + file] - 1;
                    while (n >= 0) {
                        diag2Moves[(rank << 3) + file][occ] |= 1l << diag2Mapping[sh
                                + n];
                        if ((occ & (1 << n)) != 0)
                            break;
                        n--;
                    }
                }
            }
        }
    }

    public Tablero() {
        if (knightMoves == null) {
            initKnightMoves();
            initKingMoves();
            initRookMoves();
            initBishopMoves();
            initQueenMoves();
            inithorMoves();
            initVertMoves();
            initDiag1Moves();
            diag1ReverseMapping = new int[64];
            for (int x = 0; x < 64; x++) {
                diag1ReverseMapping[diag1Mapping[x]] = x;
            }
            initDiag2Moves();
            diag2ReverseMapping = new int[64];
            for (int x = 0; x < 64; x++) {
                diag2ReverseMapping[diag2Mapping[x]] = x;
            }
        }
    }

    public List<Move> getAllMoves() {
        List<Move> list = new ArrayList<Move>();

        for (int n = 0; n < 64; n++) {
            if ((isWhiteTurn && isWhiteFigure(field[n]) || (!isWhiteTurn && isBlackFigure(field[n])))) {
                if (field[n] == WP) {
                    if ((n >> 3) == 6) { // A white pawn is going to promote
                        if (isPossibleMove(n, n + 8)) {
                            list.add(new Move(n, n + 8, QUEEN));
                            list.add(new Move(n, n + 8, ROOK));
                            list.add(new Move(n, n + 8, BISHOP));
                            list.add(new Move(n, n + 8, KNIGHT));
                        }
                        if (isPossibleMove(n, n + 7)) { // It might capture
                            // something doing so
                            list.add(new Move(n, n + 7, QUEEN));
                            list.add(new Move(n, n + 7, ROOK));
                            list.add(new Move(n, n + 7, BISHOP));
                            list.add(new Move(n, n + 7, KNIGHT));
                        }
                        if (isPossibleMove(n, n + 9)) { // To left and to right
                            list.add(new Move(n, n + 9, QUEEN));
                            list.add(new Move(n, n + 9, ROOK));
                            list.add(new Move(n, n + 9, BISHOP));
                            list.add(new Move(n, n + 9, KNIGHT));
                        }
                    }
                    else {
                        if (((n >> 3) == 1) && (isPossibleMove(n, n + 16))) {
                            list.add(new Move(n, n + 16));
                        }
                        if (isPossibleMove(n, n + 8)) {
                            list.add(new Move(n, n + 8));
                        }
                        if (isPossibleMove(n, n + 7)) {
                            list.add(new Move(n, n + 7));
                        }
                        if (isPossibleMove(n, n + 9)) {
                            list.add(new Move(n, n + 9));
                        }
                    }
                }
                else if (field[n] == BP) {
                    if ((n >> 3) == 1) { // A black pawn is going to promote
                        if (isPossibleMove(n, n - 8)) {
                            list.add(new Move(n, n - 8, QUEEN));
                            list.add(new Move(n, n - 8, ROOK));
                            list.add(new Move(n, n - 8, BISHOP));
                            list.add(new Move(n, n - 8, KNIGHT));
                        }
                        if (isPossibleMove(n, n - 7)) { // It might capture
                            // something doing so
                            list.add(new Move(n, n - 7, QUEEN));
                            list.add(new Move(n, n - 7, ROOK));
                            list.add(new Move(n, n - 7, BISHOP));
                            list.add(new Move(n, n - 7, KNIGHT));
                        }
                        if (isPossibleMove(n, n - 9)) { // To left and to right
                            list.add(new Move(n, n - 9, QUEEN));
                            list.add(new Move(n, n - 9, ROOK));
                            list.add(new Move(n, n - 9, BISHOP));
                            list.add(new Move(n, n - 9, KNIGHT));
                        }
                    }
                    else {
                        if ((n >> 3 == 6) && (isPossibleMove(n, n - 16))) {
                            list.add(new Move(n, n - 16));
                        }
                        if (isPossibleMove(n, n - 8)) {
                            list.add(new Move(n, n - 8));
                        }
                        if (isPossibleMove(n, n - 7)) {
                            list.add(new Move(n, n - 7));
                        }
                        if (isPossibleMove(n, n - 9)) {
                            list.add(new Move(n, n - 9));
                        }
                    }
                }
                else if (field[n] == WN || field[n] == BN) {
                    list.addAll(getPossibleKnightMoves(n));
                }
                else if (field[n] == WK || field[n] == BK) {
                    list.addAll(getPossibleKingMoves(n));
                }
                else if (field[n] == WB || field[n] == BB) {
                    list.addAll(getPossibleBishopMoves(n));
                }
                else if (field[n] == WR || field[n] == BR) {
                    // tmp = getPossibleRookMoves(n);
                    // if (tmp != null)
                    list.addAll(getPossibleRookMoves(n));
                }
                else if (field[n] == WQ || field[n] == BQ) {
                    list.addAll(getPossibleQueenMoves(n));
                }
            }
        }
        return list;
    }

    public List<Move> simpleGetAllMoves() {
        List<Move> result = new ArrayList<Move>();
        for (int n = 0; n < 64; n++) {
            for (int m = 0; m < 64; m++) {
                if (isPossibleMove(n, m)) {
                    result.add(new Move(n, m));
                }
            }
        }
        return result;
    }

    public List<Move> getPossibleKnightMoves(int from) {
        List<Move> result = new ArrayList<Move>();

        for (int x = 0; x < knightMoves[from].length; x++) {
            Move move = new Move(from, knightMoves[from][x]);
            if (isPossibleMove(move)) {
                result.add(move);
            }
        }
        return result;
    }

    public List<Move> getPossibleKingMoves(int from) {
        List<Move> result = new ArrayList<Move>();

        for (int x = 0; x < kingMoves[from].size(); x++) {
            Move move = (Move) kingMoves[from].get(x);
            if (isPossibleMove(move)) {
                result.add(move);
            }
        }
        return result;
    }

    public List<Move> getPossibleRookMoves(int from) {
        List<Move> result = new ArrayList<Move>();

        for (int x = 0; x < rookMoves[from].length; x++) {
            int to = rookMoves[from][x];
            if (isPossibleMove(from, to)) {
                result.add(new Move(from, to));
            }
        }
        return result;
    }

    public List<Move> getPossibleQueenMoves(int from) {
        List<Move> result = new ArrayList<Move>();

        for (int x = 0; x < queenMoves[from].length; x++) {
            Move move = new Move(from, queenMoves[from][x]);
            if (isPossibleMove(move)) {
                result.add(move);
            }
        }
        return result;
    }

    public List<Move> getPossibleBishopMoves(int from) {
        List<Move> result = new ArrayList<Move>();

        for (int x = 0; x < bishopMoves[from].size(); x++) {
            Move move = (Move) bishopMoves[from].get(x);
            if (isPossibleMove(move)) {
                result.add(move);
            }
        }
        return result;
    }

    public int abs(int a) {
        return (int) ((a < 0) ? -a : a);
    }

    public void clear() {
        for (int n = 0; n < 64; n++) {
            field[n] = EM;
        }
        isWhiteTurn = true;
        whiteKingHasMoved = false;
        blackKingHasMoved = false;
        leftBlackRookHasMoved = false;
        rightBlackRookHasMoved = false;
        leftWhiteRookHasMoved = false;
        rightWhiteRookHasMoved = false;
        lastFrom = 64; // not a valid Position!
        lastTo = 64;
        enPassantTarget = 64;
        halfmoveClock = 0;
        fullmoveNumber = 0;
        whitePieces = 0;
        whitePiecesRot = 0;
        whitePiecesDiag1 = 0;
        whitePiecesDiag2 = 0;
        blackPieces = 0;
        blackPiecesRot = 0;
        blackPiecesDiag1 = 0;
        blackPiecesDiag2 = 0;
    }

    public void init() {
        // Initializes the board ("new game")
        clear();
        for (int n = 0; n < 64; n++) {
            field[n] = startPosition[n];
        }
        blackKingPos = (7 << 3) + 4;
        whiteKingPos = (0 << 3) + 4;
        for (int x = 0; x < 64; x++) {
            if (isWhiteFigure(field[x])) {
                whitePieces |= 1l << x;
                whitePiecesRot |= 1l << (((x & 7) << 3) + (x >> 3));
                whitePiecesDiag1 |= 1l << (diag1ReverseMapping[x]);
                whitePiecesDiag2 |= 1l << (diag2ReverseMapping[x]);
            }
            if (isBlackFigure(field[x])) {
                blackPieces |= 1l << x;
                blackPiecesRot |= 1l << (((x & 7) << 3) + (x >> 3));
                blackPiecesDiag1 |= 1l << (diag1ReverseMapping[x]);
                blackPiecesDiag2 |= 1l << (diag2ReverseMapping[x]);
            }
        }
    }

    public Tablero clonedBoard() {
        Tablero newBoard = new Tablero();
        System.arraycopy(field, 0, newBoard.field, 0, 64);
        newBoard.isWhiteTurn = isWhiteTurn;
        newBoard.whiteKingHasMoved = whiteKingHasMoved;
        newBoard.blackKingHasMoved = blackKingHasMoved;
        newBoard.leftBlackRookHasMoved = leftBlackRookHasMoved;
        newBoard.rightBlackRookHasMoved = rightBlackRookHasMoved;
        newBoard.leftWhiteRookHasMoved = leftWhiteRookHasMoved;
        newBoard.rightWhiteRookHasMoved = rightWhiteRookHasMoved;
        newBoard.lastFrom = lastFrom;
        newBoard.lastTo = lastTo;
        newBoard.enPassantTarget = enPassantTarget;
        newBoard.blackKingPos = blackKingPos;
        newBoard.whiteKingPos = whiteKingPos;
        newBoard.pawnPromotion = pawnPromotion;
        newBoard.pawnHasPromoted = pawnHasPromoted;
        newBoard.fullmoveNumber = fullmoveNumber;
        newBoard.halfmoveClock = halfmoveClock;
        newBoard.whitePieces = whitePieces;
        newBoard.blackPieces = blackPieces;
        newBoard.whitePiecesRot = whitePiecesRot;
        newBoard.blackPiecesRot = blackPiecesRot;
        newBoard.whitePiecesDiag1 = whitePiecesDiag1;
        newBoard.blackPiecesDiag1 = blackPiecesDiag1;
        newBoard.whitePiecesDiag2 = whitePiecesDiag2;
        newBoard.blackPiecesDiag2 = blackPiecesDiag2;
        newBoard.whiteAttackBoard = whiteAttackBoard;
        newBoard.blackAttackBoard = blackAttackBoard;
        return newBoard;
    }

    public boolean equals(Tablero vb) {
        for (int n = 0; n < 64; n++) {
            if (field[n] != vb.field[n]) {
                return false;
            }
        }
        return isWhiteTurn == vb.isWhiteTurn
                && whiteKingHasMoved == vb.whiteKingHasMoved
                && blackKingHasMoved == vb.blackKingHasMoved
                && leftBlackRookHasMoved == vb.leftBlackRookHasMoved
                && rightBlackRookHasMoved == vb.rightBlackRookHasMoved
                && leftWhiteRookHasMoved == vb.leftWhiteRookHasMoved
                && rightWhiteRookHasMoved == vb.rightWhiteRookHasMoved
                && lastFrom == vb.lastFrom && lastTo == vb.lastTo
                && enPassantTarget == vb.enPassantTarget
                && blackKingPos == vb.blackKingPos
                && whiteKingPos == vb.whiteKingPos
                && pawnPromotion == vb.pawnPromotion
                && pawnHasPromoted == vb.pawnHasPromoted
                && fullmoveNumber == vb.fullmoveNumber
                && halfmoveClock == vb.halfmoveClock;
    }

    // public boolean isEqualPosition(Tablero vb) {
    // // Whether rooks or kings have moved is not considered here.
    // if (whitePieces != vb.whitePieces || blackPieces != vb.blackPieces) {
    // return false;
    // }
    //
    // for (int n = 0; n < 64; n++) {
    // if (field[n] != vb.field[n]) {
    // return false;
    // }
    // }
    //
    // return true;
    // }

    public void makeMove(Move move) throws ImpossibleMoveException {
        pawnPromotion = move.pawnPromotion();
        if (isPossibleMove(move)) {
            move(move.fromField(), move.toField());
        }
        else {
            throw new ImpossibleMoveException(move.toString());
        }
    }

    public void makeAnyMove(Move move) {
        pawnPromotion = move.pawnPromotion();
        move(move.fromField(), move.toField());
    }

    public boolean isAttackedByWhite(int f) {
        // A field is attacked, even if the attacker would leave his king in
        // check.
        if (whiteAttackBoard != 0) {
            // Haha, this is faster. But only if it is present, already.
            return (whiteAttackBoard & (1l << f)) != 0;
        }
        // Otherwise, it is faster to check the how the existing black pieces
        // could move.
        long toBoard = 1l << f;
        for (int x = 0; x < 64; x++) {
            if (field[x] != EM) {
                switch (field[x]) {
                case WP:
                    if (WPmightAttack(x >> 3, x & 7, f >> 3, f & 7))
                        return true;
                    break;
                case WK:
                    if ((kingMovesBitboards[x] & toBoard) != 0)
                        return true;
                    break;
                case WN:
                    if ((knightMovesBitboards[x] & toBoard) != 0)
                        return true;
                    break;
                case WQ:
                    if (WQmightMove(x >> 3, x & 7, f >> 3, f & 7))
                        return true;
                    break;
                case WR:
                    if (WRmightMove(x >> 3, x & 7, f >> 3, f & 7))
                        return true;
                    break;
                case WB:
                    if (WBmightMove(x >> 3, x & 7, f >> 3, f & 7))
                        return true;
                }
            }
        }
        return false;
    }

    public boolean isAttackedByBlack(int f) {
        if (whiteAttackBoard != 0) {
            // Haha, this is faster. But only if it is present, already.
            return (whiteAttackBoard & (1l << f)) != 0;
        }
        long toBoard = 1l << f;
        for (int x = 0; x < 64; x++) {
            if (x != f && field[x] != EM) {
                switch (field[x]) {
                case BP:
                    if (BPmightAttack(x >> 3, x & 7, f >> 3, f & 7))
                        return true;
                    break;
                case BK:
                    if ((kingMovesBitboards[x] & toBoard) != 0)
                        return true;
                    break;
                case BN:
                    if ((knightMovesBitboards[x] & toBoard) != 0)
                        return true;
                    break;
                case BQ:
                    if (BQmightMove(x >> 3, x & 7, f >> 3, f & 7))
                        return true;
                    break;
                case BR:
                    if (BRmightMove(x >> 3, x & 7, f >> 3, f & 7))
                        return true;
                    break;
                case BB:
                    if (BBmightMove(x >> 3, x & 7, f >> 3, f & 7))
                        return true;
                }
            }
        }
        return false;
    }

    public long getWhiteAttackBoard() {
        if (whiteAttackBoard != 0) {
            return whiteAttackBoard; // Shortcut, it's already calculated.
        }

        long attackBoard = 0;
        int vert_occupancy, diag1occupancy, diag2occupancy, occupancy;
        for (int x = 0; x < 64; x++) {
            if (field[x] != EM) {
                switch (field[x]) {
                case WP:
                    int rank = x >> 3;
                    int file = x & 7;
                    if ((file < 7) && (rank < 7))
                        attackBoard |= 1l << ((rank + 1) * 8 + file + 1);
                    if ((file > 0) && (rank < 7))
                        attackBoard |= 1l << ((rank + 1) * 8 + file - 1);
                    break;
                case WK:
                    attackBoard |= kingMovesBitboards[x];
                    break;
                case WN:
                    attackBoard |= knightMovesBitboards[x];
                    break;
                case WQ:
                    occupancy = (int) (((whitePieces | blackPieces) >> (x & 56))) & 255;
                    attackBoard |= horMoves[x][occupancy];
                    vert_occupancy = (int) (((whitePiecesRot | blackPiecesRot) >> ((x & 7) * 8))) & 255;
                    attackBoard |= vertMoves[x][vert_occupancy];
                    diag1occupancy = (int) ((whitePiecesDiag1 | blackPiecesDiag1) >> shift[x])
                            & ((1 << diag1length[x]) - 1);
                    attackBoard |= diag1Moves[x][diag1occupancy];
                    diag2occupancy = (int) ((whitePiecesDiag2 | blackPiecesDiag2) >> diag2shift[x])
                            & ((1 << diag2length[x]) - 1);
                    attackBoard |= diag2Moves[x][diag2occupancy];
                    break;
                case WR:
                    occupancy = (int) (((whitePieces | blackPieces) >> (x & 56))) & 255;
                    attackBoard |= horMoves[x][occupancy];
                    vert_occupancy = (int) (((whitePiecesRot | blackPiecesRot) >> ((x & 7) * 8))) & 255;
                    attackBoard |= vertMoves[x][vert_occupancy];
                    break;
                case WB:
                    diag1occupancy = (int) ((whitePiecesDiag1 | blackPiecesDiag1) >> shift[x])
                            & ((1 << diag1length[x]) - 1);
                    attackBoard |= diag1Moves[x][diag1occupancy];
                    diag2occupancy = (int) ((whitePiecesDiag2 | blackPiecesDiag2) >> diag2shift[x])
                            & ((1 << diag2length[x]) - 1);
                    attackBoard |= diag2Moves[x][diag2occupancy];
                }
            }
        }
        whiteAttackBoard = attackBoard & ~whitePieces;
        return whiteAttackBoard;
    }

    public long getBlackAttackBoard() {
        if (blackAttackBoard != 0)
            return blackAttackBoard; // Shortcut, it's already calculated.
        long attackBoard = 0;
        int vert_occupancy, diag1occupancy, diag2occupancy, occupancy;
        for (int x = 0; x < 64; x++) {
            if (field[x] != EM) {
                switch (field[x]) {
                case BP:
                    int rank = x >> 3;
                    int file = x & 7;
                    if ((file < 7) && (rank > 0)) {
                        attackBoard |= 1l << ((rank - 1) * 8 + file + 1);
                    }
                    if (file > 0 && (rank > 0)) {
                        attackBoard |= 1l << ((rank - 1) * 8 + file - 1);
                    }
                    break;
                case BK:
                    attackBoard |= kingMovesBitboards[x];
                    break;
                case BN:
                    attackBoard |= knightMovesBitboards[x];
                    break;
                case BQ:
                    occupancy = (int) (((whitePieces | blackPieces) >> (x & 56))) & 255;
                    attackBoard |= horMoves[x][occupancy];
                    vert_occupancy = (int) (((whitePiecesRot | blackPiecesRot) >> ((x & 7) * 8))) & 255;
                    attackBoard |= vertMoves[x][vert_occupancy];
                    diag1occupancy = (int) ((whitePiecesDiag1 | blackPiecesDiag1) >> shift[x])
                            & ((1 << diag1length[x]) - 1);
                    attackBoard |= diag1Moves[x][diag1occupancy];
                    diag2occupancy = (int) ((whitePiecesDiag2 | blackPiecesDiag2) >> diag2shift[x])
                            & ((1 << diag2length[x]) - 1);
                    attackBoard |= diag2Moves[x][diag2occupancy];
                    break;
                case BR:
                    occupancy = (int) (((whitePieces | blackPieces) >> (x & 56))) & 255;
                    attackBoard |= horMoves[x][occupancy];
                    vert_occupancy = (int) (((whitePiecesRot | blackPiecesRot) >> ((x & 7) * 8))) & 255;
                    attackBoard |= vertMoves[x][vert_occupancy];
                    break;
                case BB:
                    diag1occupancy = (int) ((whitePiecesDiag1 | blackPiecesDiag1) >> shift[x])
                            & ((1 << diag1length[x]) - 1);
                    attackBoard |= diag1Moves[x][diag1occupancy];
                    diag2occupancy = (int) ((whitePiecesDiag2 | blackPiecesDiag2) >> diag2shift[x])
                            & ((1 << diag2length[x]) - 1);
                    attackBoard |= diag2Moves[x][diag2occupancy];
                }
            }
        }
        blackAttackBoard = attackBoard & ~blackPieces;
        return blackAttackBoard;
    }

    public boolean isPossibleMove(Move move) {
        return isPossibleMove(move.fromField(), move.toField());
    }

    public boolean isPossibleMove(int from, int to) {
        if (field[from] != EM && (isWhiteTurn == isWhiteFigure(field[from]))
                && mightBePossibleMove(from, to)) {
            if (doNotConsiderCheckInTryMove) {
                return true;
            }
            Tablero nb = Tablero.this.clonedBoard();
            // nb= new board
            nb.move(from, to);
            if (isWhiteTurn) {
                if (nb.whiteKingPos < 64 && nb.whiteKingPos >= 0
                        && !nb.isAttackedByBlack(nb.whiteKingPos))
                    return true;
            }
            else if (nb.blackKingPos < 64 && nb.blackKingPos >= 0
                    && !nb.isAttackedByWhite(nb.blackKingPos))
                return true;
        }
        return false;
    }

    public Tablero tryMove(int from, int to) {
        if (field[from] != EM && (isWhiteTurn == isWhiteFigure(field[from]))
                && mightBePossibleMove(from, to)) {
            Tablero nb = Tablero.this.clonedBoard();
            // nb= new board
            nb.move(from, to);
            if (doNotConsiderCheckInTryMove)
                return nb;
            if (isWhiteTurn) {
                if (nb.whiteKingPos < 64 && nb.whiteKingPos >= 0
                        && !nb.isAttackedByBlack(nb.whiteKingPos))
                    return nb;
            }
            else if (nb.blackKingPos < 64 && nb.blackKingPos >= 0
                    && !nb.isAttackedByWhite(nb.blackKingPos))
                return nb;
        }
        return null;
    }

    public boolean onlyKingsLeft() {
        for (int n = 0; n < 64; n++)
            if (field[n] != EM && field[n] != WK && field[n] != BK)
                return false;
        return true;
    }

    public boolean noAvailableMoves() {
        // If there are only the kings left, the game is inevitable
        // drawn.
        // if (onlyKingsLeft()) {
        // return true;
        // }

        // Looks for possible moves for current player. Returns immediatly
        // if a move was found, and thus the game cannot be finished.

        for (int from = 0; from < 64; from++) {
            if (field[from] > 0 && (isWhiteTurn == (field[from] < 7))) {
                for (int to = 0; to < 64; to++) {
                    if (isPossibleMove(new Move(from, to))) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean isKingChecked() {
        if (isWhiteTurn) {
            return isAttackedByBlack(getWhiteKingPos());
        }
        else {
            return isAttackedByWhite(getBlackKingPos());
        }
    }

    /*
     * move
     * 
     * This in the core method, where the moves finally are processed.
     * 
     * @author cd
     */
    private void move(int from, int to) {
        // Moves that arrive this point have been checked
        // to be valid.
        int file = from & 7;
        int rank = from >> 3;
        int newFile = to & 7;
        int newRank = to >> 3;

        pawnHasPromoted = false;
        boolean capturing = false;

        if (isWhiteTurn) {
            fullmoveNumber++;
        }

        if (file == 4) {
            if ((field[from] == WK) && file == 4 && newFile == 2) {
                // White castles queen side:
                move(0, 3);
                isWhiteTurn = !isWhiteTurn;
                if (isWhiteTurn) {
                    fullmoveNumber--;
                }
            }
            else if ((field[from] == WK) && file == 4 && newFile == 6) {
                // White castles king side:
                move(7, 5);
                isWhiteTurn = !isWhiteTurn;
                if (isWhiteTurn) {
                    fullmoveNumber--;
                }
            }
            else if ((field[from] == BK) && file == 4 && newFile == 2) {
                // Black castles queen side:
                move(((7 << 3) + 0), ((7 << 3) + 3));
                isWhiteTurn = !isWhiteTurn;
                if (isWhiteTurn) {
                    fullmoveNumber--;
                }
            }
            else if ((field[from] == BK) && file == 4 && newFile == 6) {
                // Black castles king side:
                move(((7 << 3) + 7), ((7 << 3) + 5));
                isWhiteTurn = !isWhiteTurn;
                if (isWhiteTurn) {
                    fullmoveNumber--;
                }
            }
        }
        if ((field[to] == EM && field[from] == WP) && file != newFile) {
            // Capturing en passant (white):
            field[(4 << 3) + newFile] = EM;
            capturing = true;
        }
        else if ((field[to] == EM && field[from] == BP) && file != newFile) {
            // Capturing en passant (black):
            field[(3 << 3) + newFile] = EM;
            capturing = true;
        }
        if (field[to] == WK) { // Oops!
            whiteKingPos = 64; // Illegal field!
        }
        else if (field[to] == BK) {
            blackKingPos = 64;
        }
        // No, thats not a joke. Its required for move checking.
        if (field[from] == WR && from == 0) {
            leftWhiteRookHasMoved = true;
        }
        else if (field[from] == WR && from == 7) {
            rightWhiteRookHasMoved = true;
        }
        else if (field[from] == BR && from == (7 << 3)) {
            leftBlackRookHasMoved = true;
        }
        else if (field[from] == BR && from == (7 << 3) + 7) {
            rightBlackRookHasMoved = true;
        }
        else if (field[from] == WK) {
            whiteKingHasMoved = true;
            whiteKingPos = to;
        }
        else if (field[from] == BK) {
            blackKingHasMoved = true;
            blackKingPos = to;
        }

        if (to == 0) {
            leftWhiteRookHasMoved = true;
        }
        else if (to == 7) {
            rightWhiteRookHasMoved = true;
        }
        else if (to == 56) {
            leftBlackRookHasMoved = true;
        }
        else if (to == 63) {
            rightBlackRookHasMoved = true;
        }

        if (field[to] != EM) {
            capturing = true;
        }

        // Halfmove clock
        if (field[from] == WP || field[from] == BP || capturing) {
            halfmoveClock = 0;
        }
        else {
            halfmoveClock++;
        }

        // Set up enPassantTarget:
        if (field[from] == WP && rank == 1 && newRank == 3)
            enPassantTarget = ((2 << 3) + file);
        else if (field[from] == BP && rank == 6 && newRank == 4)
            enPassantTarget = ((5 << 3) + file);
        else
            enPassantTarget = 64;

        // Update the bitboards
        whitePieces &= ~(1l << from);
        blackPieces &= ~(1l << from);
        blackPiecesRot &= ~(1l << ((file << 3) + rank));
        whitePiecesRot &= ~(1l << ((file << 3) + rank));
        whitePiecesDiag1 &= ~(1l << diag1ReverseMapping[from]);
        blackPiecesDiag1 &= ~(1l << diag1ReverseMapping[from]);
        whitePiecesDiag2 &= ~(1l << diag2ReverseMapping[from]);
        blackPiecesDiag2 &= ~(1l << diag2ReverseMapping[from]);
        if (isWhiteFigure(field[from])) {
            whitePieces |= (1l << to);
            whitePiecesRot |= 1l << ((newFile << 3) + newRank);
            whitePiecesDiag1 |= 1l << diag1ReverseMapping[to];
            whitePiecesDiag2 |= 1l << diag2ReverseMapping[to];
        }
        if (isBlackFigure(field[from])) {
            blackPieces |= (1l << to);
            blackPiecesRot |= 1l << ((newFile << 3) + newRank);
            blackPiecesDiag1 |= 1l << diag1ReverseMapping[to];
            blackPiecesDiag2 |= 1l << diag2ReverseMapping[to];
        }

        // Clear the attack boards, they have to be recalculated when needed.
        whiteAttackBoard = 0;
        blackAttackBoard = 0;

        // Update the array board
        field[to] = field[from];
        field[from] = EM;
        lastFrom = from;
        lastTo = to;
        isWhiteTurn = !isWhiteTurn;

        // Pawn promotion:
        if (field[to] == WP && newRank == 7) {
            if (pawnPromotion == NONE) {
                pawnPromotion = QUEEN;
                field[to] = WQ;
            }
            else if (pawnPromotion == QUEEN) {
                field[to] = WQ;
            }
            else if (pawnPromotion == ROOK) {
                field[to] = WR;
            }
            else if (pawnPromotion == BISHOP) {
                field[to] = WB;
            }
            else if (pawnPromotion == KNIGHT) {
                field[to] = WN;
            }
            pawnHasPromoted = true;
        }
        else if (field[to] == BP && newRank == 0) {
            if (pawnPromotion == NONE) {
                pawnPromotion = QUEEN;
                field[to] = BQ;
            }
            else if (pawnPromotion == QUEEN) {
                field[to] = BQ;
            }
            else if (pawnPromotion == ROOK) {
                field[to] = BR;
            }
            else if (pawnPromotion == BISHOP) {
                field[to] = BB;
            }
            else if (pawnPromotion == KNIGHT) {
                field[to] = BN;
            }
            pawnHasPromoted = true;
        }
    }

    //
    // Move possibilities:
    //
    public boolean mightBePossibleMove(int from, int to) {
        return mightBePossibleMove(from >> 3, from & 7, to >> 3, to & 7);
    }

    private void initKnightMoves() {
        knightMoves = new int[64][];
        knightMovesBitboards = new long[64];
        for (int from = 0; from < 64; from++) {
            int index = 0;
            int[] toFields = new int[64];
            for (int to = 0; to < 64; to++) {
                if (from != to
                        && WNmightMove(from >> 3, from & 7, to >> 3, to & 7)) {
                    toFields[index++] = to;
                    knightMovesBitboards[from] |= 1l << to;
                }
            }
            knightMoves[from] = new int[index];
            for (int n = 0; n < index; n++) {
                knightMoves[from][n] = toFields[n];
            }
        }
    }

    private void initKingMoves() {
        kingMoves = new List[64];
        kingMovesBitboards = new long[64];
        for (int from = 0; from < 64; from++) {
            kingMoves[from] = new ArrayList<Move>();
            for (int to = 0; to < 64; to++) {
                if (from != to
                        && WKmightMove(from >> 3, from & 7, to >> 3, to & 7)) {
                    kingMoves[from].add(new Move(from, to));
                    kingMovesBitboards[from] |= 1l << to;
                }
            }
        }
    }

    private void initRookMoves() {
        rookMoves = new int[64][];
        rookMovesBitboards = new long[64];
        for (int from = 0; from < 64; from++) {
            int index = 0;
            int[] toFields = new int[64];
            for (int to = 0; to < 64; to++) {
                if (from != to
                        && WRmightMove(from >> 3, from & 7, to >> 3, to & 7)) {
                    toFields[index++] = to;
                    rookMovesBitboards[from] |= 1l << to;
                }
            }
            rookMoves[from] = new int[index];
            for (int n = 0; n < index; n++) {
                rookMoves[from][n] = toFields[n];
            }
        }
    }

    private void initQueenMoves() {
        queenMoves = new int[64][];
        queenMovesBitboards = new long[64];
        for (int from = 0; from < 64; from++) {
            int index = 0;
            int[] toFields = new int[64];
            for (int to = 0; to < 64; to++) {
                if (from != to
                        && WQmightMove(from >> 3, from & 7, to >> 3, to & 7)) {
                    toFields[index++] = to;
                    queenMovesBitboards[from] |= 1l << to;
                }
            }
            queenMoves[from] = new int[index - 1];
            for (int n = 0; n < index - 1; n++) {
                queenMoves[from][n] = toFields[n];
            }
        }
    }

    private void initBishopMoves() {
        bishopMoves = new List[64];
        bishopMovesBitboards = new long[64];
        for (int from = 0; from < 64; from++) {
            bishopMoves[from] = new ArrayList<Move>();
            for (int to = 0; to < 64; to++) {
                if (from != to
                        && WBmightMove(from >> 3, from & 7, to >> 3, to & 7)) {
                    bishopMoves[from].add(new Move(from, to));
                }
                bishopMovesBitboards[from] |= 1l << to;
            }
        }
    }

    public boolean mightBePossibleMove(int fromRank, int fromFile, int toRank,
            int toFile) {
        // Might return true even if the King is left in check.
        if (fromRank < 0 || fromFile < 0 || fromRank > 7 || fromFile > 7
                || toRank < 0 || toFile < 0 || toRank > 7 || toFile > 7)
            return false;

        int fromField = getField(fromRank, fromFile);
        if (fromField == EM || (fromRank == toRank && fromFile == toFile))
            return false;

        int toField = getField(toRank, toFile);
        // No capturing of own figures:
        if (isWhiteFigure(fromField) && isWhiteFigure(toField))
            return false;
        if (isBlackFigure(fromField) && isBlackFigure(toField))
            return false;
        switch (fromField) {
        case WK:
            return WKmightMove(fromRank, fromFile, toRank, toFile);
        case WQ:
            return WQmightMove(fromRank, fromFile, toRank, toFile);
        case WR:
            return WRmightMove(fromRank, fromFile, toRank, toFile);
        case WB:
            return WBmightMove(fromRank, fromFile, toRank, toFile);
        case WN:
            return WNmightMove(fromRank, fromFile, toRank, toFile);
        case WP:
            return WPmightMove(fromRank, fromFile, toRank, toFile);
        case BK:
            return BKmightMove(fromRank, fromFile, toRank, toFile);
        case BQ:
            return BQmightMove(fromRank, fromFile, toRank, toFile);
        case BR:
            return BRmightMove(fromRank, fromFile, toRank, toFile);
        case BB:
            return BBmightMove(fromRank, fromFile, toRank, toFile);
        case BN:
            return BNmightMove(fromRank, fromFile, toRank, toFile);
        case BP:
            return BPmightMove(fromRank, fromFile, toRank, toFile);
        }
        return false; // Should never be reached.
    }

    // Black knight
    private boolean BNmightMove(int fromRank, int fromFile, int toRank,
            int toFile) {
        return ((fromFile)) != (toFile)
                && (fromRank) != (toRank)
                && abs(((fromFile)) - (toFile)) + abs((fromRank) - (toRank)) == 3;
    }

    // black queen
    private boolean BQmightMove(int fromRank, int fromFile, int toRank,
            int toFile) {
        return checkLineOfSight(fromRank, fromFile, toRank, toFile);
    }

    // black bishop
    private boolean BBmightMove(int fromRank, int fromFile, int toRank,
            int toFile) {
        return (abs(toRank - fromRank) == abs(toFile - (fromFile)))
                && checkLineOfSight(fromRank, fromFile, toRank, toFile);
    }

    // black rook
    private boolean BRmightMove(int fromRank, int fromFile, int toRank,
            int toFile) {
        return (toRank == fromRank || toFile == (fromFile))
                && checkLineOfSight(fromRank, fromFile, toRank, toFile);
    }

    // black pawn
    private boolean BPmightMove(int fromRank, int fromFile, int toRank,
            int toFile) {
        return ((toRank == fromRank - 1 && toFile == fromFile && getField(
                toRank, toFile) == EM)
                || (toRank == fromRank - 1 && abs(toFile - fromFile) == 1 && (isWhiteFigure(getField(
                        toRank, toFile)) || ((toRank << 3) + toFile) == enPassantTarget)) || ((fromRank) == 6
                && (toRank) == 4
                && toFile == fromFile
                && getField(4, toFile) == EM && getField(5, toFile) == EM));
    }

    private boolean BPmightAttack(int fromRank, int fromFile, int toRank,
            int toFile) {
        return toRank == fromRank - 1 && abs(toFile - (fromFile)) == 1;
    }

    // black king
    // private boolean BKmightAttack(int fromRank, int fromFile, int toRank,
    // int toFile) {
    // return (abs(toRank - fromRank) <= 1 && abs(toFile - fromFile) <= 1);
    // }

    private boolean BKmightMove(int fromRank, int fromFile, int toRank,
            int toFile) {
        return ((abs(toRank - fromRank) <= 1 && abs(toFile - fromFile) <= 1)
                || (fromRank == 7 && fromFile == 4 && toRank == 7
                        && toFile == 2 && BKcouldCastleQueenSide()) || (fromRank == 7
                && fromFile == 4 && toRank == 7 && toFile == 6 && BKcouldCastleKingSide()));
    }

    private boolean BKcouldCastleQueenSide() {
        return !blackKingHasMoved && !leftBlackRookHasMoved
                && checkLineOfSight(7, 0, 7, 4)
                && !isAttackedByWhite((7 << 3) + 4)
                && !isAttackedByWhite((7 << 3) + 3);
    }

    private boolean BKcouldCastleKingSide() {
        return !blackKingHasMoved && !rightBlackRookHasMoved
                && checkLineOfSight(7, 7, 7, 4)
                && !isAttackedByWhite((7 << 3) + 4)
                && !isAttackedByWhite((7 << 3) + 5);
    }

    // white knight
    private boolean WNmightMove(int fromRank, int fromFile, int toRank,
            int toFile) {
        return ((fromFile)) != (toFile)
                && (fromRank) != (toRank)
                && abs(((fromFile)) - (toFile)) + abs((fromRank) - (toRank)) == 3;
    }

    // white queen
    public boolean WQmightMove(int fromRank, int fromFile, int toRank,
            int toFile) {
        return checkLineOfSight(fromRank, fromFile, toRank, toFile);
    }

    // white bishop
    private boolean WBmightMove(int fromRank, int fromFile, int toRank,
            int toFile) {
        return (abs(toRank - fromRank) == abs(toFile - (fromFile)))
                && checkLineOfSight(fromRank, fromFile, toRank, toFile);
    }

    // white rook
    private boolean WRmightMove(int fromRank, int fromFile, int toRank,
            int toFile) {
        return (toRank == fromRank || toFile == fromFile)
                && checkLineOfSight(fromRank, fromFile, toRank, toFile);
    }

    // white king
    public boolean WKmightAttack(int fromRank, int fromFile, int toRank,
            int toFile) {
        return (abs(toRank - fromRank) <= 1 && abs(toFile - fromFile) <= 1);
    }

    private boolean WKmightMove(int fromRank, int fromFile, int toRank,
            int toFile) {
        return ((abs(toRank - fromRank) <= 1 && abs(toFile - fromFile) <= 1)
                || (fromRank == 0 && fromFile == 4 && toRank == 0
                        && toFile == 2 && WKcouldCastleQueenSide()) || (fromRank == 0
                && fromFile == 4 && toRank == 0 && toFile == 6 && WKcouldCastleKingSide()));
    }

    private boolean WKcouldCastleQueenSide() {
        return !whiteKingHasMoved && !leftWhiteRookHasMoved
                && checkLineOfSight(0, 0, 0, 4) && !isAttackedByBlack(4)
                && !isAttackedByBlack(3);
    }

    private boolean WKcouldCastleKingSide() {
        return !whiteKingHasMoved && !rightWhiteRookHasMoved
                && checkLineOfSight(0, 7, 0, 4) && !isAttackedByBlack(4)
                && !isAttackedByBlack(5);
    }

    // white pawn

    public boolean WPmightMove(int fromRank, int fromFile, int toRank,
            int toFile) {
        return ((toRank == fromRank + 1 && toFile == fromFile && getField(
                toRank, toFile) == EM)
                || (toRank == fromRank + 1 && abs(toFile - fromFile) == 1 && (isBlackFigure(getField(
                        toRank, toFile)) || ((toRank << 3) + toFile) == enPassantTarget)) || ((fromRank) == 1
                && (toRank) == 3
                && toFile == fromFile
                && getField(3, toFile) == EM && getField(2, toFile) == EM));
    }

    public boolean WPmightAttack(int fromRank, int fromFile, int toRank,
            int toFile) {
        return toRank == fromRank + 1 && abs(toFile - (fromFile)) == 1;
    }

    // 
    // private methods
    //

    private boolean checkLineOfSight(int fromRank, int fromFile, int toRank,
            int toFile) {
        // Checks wether there is any piece on the line between the two given
        // fields.
        if (fromRank != toRank && fromFile != toFile
                && abs(toRank - fromRank) != abs(toFile - fromFile))
            return false; // Only horizontal, vertical, or diagonal lines
        // are allowed.
        int rdiff = (toRank >= fromRank) ? ((toRank > fromRank) ? 1 : 0) : -1;
        int fdiff = (toFile >= fromFile) ? ((toFile > fromFile) ? 1 : 0) : -1;
        int rank = fromRank + rdiff;
        int file = fromFile + fdiff;
        while (rank != toRank || file != toFile) {
            if (field[(rank << 3) + file] != EM)
                return false;
            rank += rdiff;
            file += fdiff;
        }
        return true;
    }
}
