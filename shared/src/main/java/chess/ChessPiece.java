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
    public String toString() {
        return "ChessPiece{" +
                "pieceColor=" + pieceColor +
                ", type=" + type +
                '}';
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
            return QueenMoves(board, myPosition);
        } else if (type == PieceType.BISHOP) {
            return BishopMoves(board, myPosition);
        } else if (type == PieceType.KNIGHT) {
            return KnightMoves(board, myPosition);
        } else if (type == PieceType.ROOK) {
            return RookMoves(board, myPosition);
        } else {
            return PawnMoves(board, myPosition);
        }

    }

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
                //Determine if there is a piece occupying an available spot
                if (PieceOnSpot(board, endPosition) == null) {
                    MovesForKing.add(new ChessMove(myPosition, endPosition, null));
                } else if (!IsMyTeam(board, myPosition, endPosition)) {
                    MovesForKing.add(new ChessMove(myPosition, endPosition, null));
                }
            }
        }

        return MovesForKing;

    }

    private Collection<ChessMove> BishopMoves(ChessBoard board, ChessPosition myPosition) {
        //what will be returned at the end
        Collection<ChessMove> MovesForBishop = new ArrayList<>();

        //list of the coordinates of every direction that the bishop can move
        int[][] directions = {{1, -1}, {1, 1}, {-1, -1}, {-1, 1}};
        for (int[] direction : directions) {
            int newRow = myPosition.getRow() + direction[0];
            int newCol = myPosition.getColumn() + direction[1];
            ChessPosition endPosition = new ChessPosition(newRow, newCol);
            while (isOnBoard(endPosition)) {
                //while the proposed endpoints are on the board
                if (PieceOnSpot(board, endPosition) == null) {
                    //if the spot is empty, add it and keep going that same direction
                    MovesForBishop.add(new ChessMove(myPosition, endPosition, null));
                    newRow += direction[0];
                    newCol += direction[1];
                    endPosition = new ChessPosition(newRow, newCol);
                } else if (!IsMyTeam(board, myPosition, endPosition)) {
                    //if the other team's piece is occupying the space, add the move and be done in that direction
                    MovesForBishop.add(new ChessMove(myPosition, endPosition, null));
                    break;
                } else {
                    break;
                }
            }


        }


        return MovesForBishop;
    }

    private Collection<ChessMove> KnightMoves(ChessBoard board, ChessPosition myPosition) {
        //what will be returned at the end
        Collection<ChessMove> MovesForKnight = new ArrayList<>();

        //list of the coordinates of every direction that the king can move
        int[][] directions = {{2, 1}, {2, -1}, {-2, 1}, {-2, -1}, {1, -2}, {-1, -2}, {1, 2}, {-1, 2}};

        for (int[] direction : directions) {
            int newRow = myPosition.getRow() + direction[0];
            int newCol = myPosition.getColumn() + direction[1];
            ChessPosition endPosition = new ChessPosition(newRow, newCol);
            if (isOnBoard(endPosition)) {
                //Determine if there is a piece occupying an available spot
                if (PieceOnSpot(board, endPosition) == null) {
                    MovesForKnight.add(new ChessMove(myPosition, endPosition, null));
                } else if (!IsMyTeam(board, myPosition, endPosition)) {
                    MovesForKnight.add(new ChessMove(myPosition, endPosition, null));
                }
            }
        }

        return MovesForKnight;
    }

    private Collection<ChessMove> QueenMoves(ChessBoard board, ChessPosition myPosition) {
        //what will be returned at the end
        Collection<ChessMove> MovesForQueen = new ArrayList<>();

        //list of the coordinates of every direction that the queen can move
        int[][] directions = {{1, 0}, {-1, 0}, {0, -1}, {0, 1}, {1, -1}, {1, 1}, {-1, -1}, {-1, 1}};

        for (int[] direction : directions) {
            int newRow = myPosition.getRow() + direction[0];
            int newCol = myPosition.getColumn() + direction[1];
            ChessPosition endPosition = new ChessPosition(newRow, newCol);
            while (isOnBoard(endPosition)) {
                if (PieceOnSpot(board, endPosition) == null) {
                    //if the spot is empty, add it and keep going that same direction
                    MovesForQueen.add(new ChessMove(myPosition, endPosition, null));
                    newRow += direction[0];
                    newCol += direction[1];
                    endPosition = new ChessPosition(newRow, newCol);
                } else if (!IsMyTeam(board, myPosition, endPosition)) {
                    //if the other team's piece is occupying the space, add the move and be done in that direction
                    MovesForQueen.add(new ChessMove(myPosition, endPosition, null));
                    break;
                } else {
                    break;
                }

            }

        }

        return MovesForQueen;
    }

    private Collection<ChessMove> PawnMoves(ChessBoard board, ChessPosition myPosition) {
        //what will be returned at the end
        Collection<ChessMove> MovesForPawn = new ArrayList<>();
        ChessPiece myPiece = board.getPiece(myPosition);

        int[][] captureDirections = {{1,1}, {1,-1}};

        int direction;
        int startingRow;
        if (myPiece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            direction = 1;
            startingRow = 2;
        } else {
            direction = -1;
            startingRow = 7;
        }
        int moveTwo = myPosition.getRow() + (direction * 2);
        int newRow = myPosition.getRow() + direction;
        ChessPosition moveTwoEndPosition = new ChessPosition(moveTwo, myPosition.getColumn());
        ChessPosition endPosition = new ChessPosition(newRow, myPosition.getColumn());

        if (myPosition.getRow() == startingRow) {
            MovesForPawn.add(new ChessMove(myPosition, endPosition, null));
            MovesForPawn.add(new ChessMove(myPosition, moveTwoEndPosition, null));

        }


        return MovesForPawn;
    }

    private Collection<ChessMove> RookMoves(ChessBoard board, ChessPosition myPosition) {
        //what will be returned at the end
        Collection<ChessMove> MovesForRook = new ArrayList<>();

        //list of the coordinates of every direction that the king can move
        int[][] directions = {{1, 0}, {-1, 0}, {0, -1}, {0, 1}};

        for (int[] direction : directions) {
            int newRow = myPosition.getRow() + direction[0];
            int newCol = myPosition.getColumn() + direction[1];
            ChessPosition endPosition = new ChessPosition(newRow, newCol);
            while (isOnBoard(endPosition)) {
                if (PieceOnSpot(board, endPosition) == null) {
                    //if the spot is empty, add it and keep going that same direction
                    MovesForRook.add(new ChessMove(myPosition, endPosition, null));
                    newRow += direction[0];
                    newCol += direction[1];
                    endPosition = new ChessPosition(newRow, newCol);
                } else if (!IsMyTeam(board, myPosition, endPosition)) {
                    //if the other team's piece is occupying the space, add the move and be done in that direction
                    MovesForRook.add(new ChessMove(myPosition, endPosition, null));
                    break;
                } else {
                    break;
                }

            }

        }


        return MovesForRook;
    }

    public boolean isOnBoard(ChessPosition endPosition) {
        return endPosition.getRow() >= 1 &&
                endPosition.getColumn() <= 8 &&
                endPosition.getRow() <= 8 &&
                endPosition.getColumn() >= 1;


    }

    public ChessPiece PieceOnSpot(ChessBoard MyBoard, ChessPosition endPosition) {
        return MyBoard.getPiece(endPosition);

    }

    public boolean IsMyTeam(ChessBoard MyBoard, ChessPosition myPosition, ChessPosition endPosition) {
        ChessPiece MyPiece = MyBoard.getPiece(myPosition);
        ChessPiece OtherPiece = MyBoard.getPiece(endPosition);
        return OtherPiece.getTeamColor() == MyPiece.getTeamColor();

    }

}
