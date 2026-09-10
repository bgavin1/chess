package chess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    ChessPiece[][] squares = new ChessPiece[8][8];

    public ChessBoard() {
        
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares[position.getRow()-1][position.getColumn()-1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {

        return squares[position.getRow()-1][position.getColumn()-1];
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(squares, that.squares);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(squares);
    }

    @Override
    public String toString() {
        return "ChessBoard{" +
                "squares=" + Arrays.toString(squares) +
                '}';
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        Collection<ChessPiece> WhiteBackRow = new ArrayList<>();
        Collection<ChessPiece> BlackBackRow = new ArrayList<>();

        WhiteBackRow.add(new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
        WhiteBackRow.add(new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
        WhiteBackRow.add(new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        WhiteBackRow.add(new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN));
        WhiteBackRow.add(new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING));
        WhiteBackRow.add(new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        WhiteBackRow.add(new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
        WhiteBackRow.add(new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));

        BlackBackRow.add(new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
        BlackBackRow.add(new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
        BlackBackRow.add(new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        BlackBackRow.add(new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN));
        BlackBackRow.add(new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING));
        BlackBackRow.add(new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        BlackBackRow.add(new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
        BlackBackRow.add(new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));


        int col = 1;
        for (ChessPiece piece : WhiteBackRow) {
            addPiece(new ChessPosition(1,col), piece);
            col += 1;
        }
        col = 1;
        for (ChessPiece piece : BlackBackRow) {
            int i = 1;
            addPiece(new ChessPosition(8,col), piece);
            col += 1;
        }
        col = 1;
        for (int i = 0; i < 8; i++) {
            addPiece(new ChessPosition(2, col), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
            col += 1;

        }
        col = 1;
        for (int i = 0; i < 8; i++) {
            addPiece(new ChessPosition(7, col), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
            col += 1;

        }
    }
}
