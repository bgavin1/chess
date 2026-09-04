package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        if (type == PieceType.KING) {
            return KingMoves(board, myPosition);
        } else if (type == PieceType.QUEEN) {
            return null;
        } else if (type == PieceType.BISHOP) {
            return null;
        } else if (type == PieceType.KNIGHT) {
            return null;
        } else if (type == PieceType.ROOK) {
            return null;
        } else {
            return null;
        }

    }
    //will need to add logic for if there is a piece in the way
    private Collection<ChessMove> KingMoves(ChessBoard board, ChessPosition myPosition) {
        //what will be returned at the end
        Collection<ChessMove> MovesForKing = new ArrayList<>();

        //list of the coordinates of every direction that the king can move
        int[][] directions = {{1,0}, {-1,0}, {0,-1}, {0,1}, {1,-1}, {1,1}, {-1,-1}, {-1,1}};

        //iterate through the coordinates and find out if they are on the board or not
        for (int[] direction : directions) {
            int newRow = myPosition.getRow() + direction[0];
            int newCol = myPosition.getColumn() + direction[1];
            ChessPosition endPosition = new ChessPosition(newRow, newCol);
            if (isOnBoard(endPosition)) {
                MovesForKing.add(new ChessMove(myPosition, endPosition, null));
            }
        }

        return MovesForKing;

    }

    public boolean isOnBoard(ChessPosition endPosition) {
        return endPosition.getRow() >= 0 &&
                endPosition.getColumn() <= 7 &&
                endPosition.getRow() <= 7 &&
                endPosition.getColumn() >= 0;


    }

}
