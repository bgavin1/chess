package chess;

import java.util.*;

/**
 * A class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    TeamColor ThisTurn = TeamColor.WHITE;
    ChessBoard myBoard = new ChessBoard();

    public ChessGame() {
        myBoard.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return ThisTurn;
    }

    /**
     * Sets which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        ThisTurn = team;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return ThisTurn == chessGame.ThisTurn && Objects.equals(myBoard, chessGame.myBoard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ThisTurn, myBoard);
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets all valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece myPiece = myBoard.getPiece(startPosition);


        if (myPiece == null) {
            return null;
        }
        Collection<ChessMove> moveList = myPiece.pieceMoves(myBoard, startPosition);
        Collection<ChessMove> finalMoveList = new ArrayList<>();


        for (ChessMove move : moveList) {
            ChessBoard subBoard = new ChessBoard();
            for (int rowIndex = 0; rowIndex < 8; rowIndex ++) {
                for (int colIndex = 0; colIndex < 8; colIndex++) {
                    ChessPiece thisPiece = myBoard.getPiece(new ChessPosition(rowIndex + 1, colIndex + 1));
                    if (thisPiece != null) {
                        subBoard.addPiece(new ChessPosition(rowIndex + 1, colIndex + 1), thisPiece);
                    }
                }
            }
            subBoard.addPiece(move.getEndPosition(), myPiece);
            subBoard.addPiece(move.getStartPosition(), null);
            if (!helperIsInCheck(myPiece.getTeamColor(), subBoard)) {
                finalMoveList.add(move);
            }
        }
        return finalMoveList;
    }

    public boolean helperIsInCheck(TeamColor teamColor, ChessBoard board) {
        //find my king
        ChessPosition myKingPosition = null;
        for (int rowIndex = 0; rowIndex < 8; rowIndex ++) {
            for (int colIndex = 0; colIndex < 8; colIndex ++) {
                ChessPiece thisPiece = board.getPiece(new ChessPosition(rowIndex+1, colIndex+1));
                if (thisPiece != null) {
                    if (thisPiece.getTeamColor() == teamColor) {
                        if (thisPiece.getPieceType() == ChessPiece.PieceType.KING) {
                            myKingPosition = new ChessPosition(rowIndex+1, colIndex+1);
                        }
                    }
                }
            }
        }
        //for every spot containing a piece on the other team
        for (int rowIndex = 0; rowIndex < 8; rowIndex ++) {
            for (int colIndex = 0; colIndex < 8; colIndex ++) {
                ChessPiece myPiece = board.getPiece(new ChessPosition(rowIndex+1, colIndex+1));
                if (myPiece != null) {
                    if (myPiece.getTeamColor() != teamColor) {
                        //check movelist
                        Collection<ChessMove> moveList = myPiece.pieceMoves(board, new ChessPosition(rowIndex+1, colIndex+1));
                        for (ChessMove move : moveList) {
                            if (move.getEndPosition().equals(myKingPosition)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece myPiece = myBoard.getPiece(move.getStartPosition());
        if (myPiece == null) {
            throw new InvalidMoveException("Invalid move.");
        }
        if (myPiece.getTeamColor() != ThisTurn) {
            throw new InvalidMoveException("Invalid move.");
        }
        Collection<ChessMove> moveList = validMoves(move.getStartPosition());
        if (!moveList.contains(move)) {
            throw new InvalidMoveException("Invalid move.");
        } else {
            if (move.getPromotionPiece() == null) {
                myBoard.addPiece(move.getEndPosition(), myPiece);
                myBoard.addPiece(move.getStartPosition(), null);
            } else {
                ChessPiece newPiece = new ChessPiece(ThisTurn, move.getPromotionPiece());
                myBoard.addPiece(move.getEndPosition(), newPiece);
                myBoard.addPiece(move.getStartPosition(), null);
            }
            if (ThisTurn == TeamColor.WHITE) {
                ThisTurn = TeamColor.BLACK;
            } else {
                ThisTurn = TeamColor.WHITE;
            }
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        //find my king
        ChessPosition myKingPosition = null;
        for (int rowIndex = 0; rowIndex < 8; rowIndex ++) {
            for (int colIndex = 0; colIndex < 8; colIndex ++) {
                ChessPiece thisPiece = myBoard.getPiece(new ChessPosition(rowIndex+1, colIndex+1));
                if (thisPiece != null) {
                    if (thisPiece.getTeamColor() == teamColor) {
                        if (thisPiece.getPieceType() == ChessPiece.PieceType.KING) {
                            myKingPosition = new ChessPosition(rowIndex+1, colIndex+1);
                        }
                    }
                }
            }
        }
        //for every spot containing a piece on the other team
        for (int rowIndex = 0; rowIndex < 8; rowIndex ++) {
            for (int colIndex = 0; colIndex < 8; colIndex ++) {
                ChessPiece myPiece = myBoard.getPiece(new ChessPosition(rowIndex+1, colIndex+1));
                if (myPiece != null) {
                    if (myPiece.getTeamColor() != teamColor) {
                        //check movelist
                        Collection<ChessMove> moveList = myPiece.pieceMoves(myBoard, new ChessPosition(rowIndex+1, colIndex+1));
                        for (ChessMove move : moveList) {
                            if (move.getEndPosition().equals(myKingPosition)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        boolean checkmate = true;
        if (isInCheck(teamColor)) {
            for (int rowIndex = 0; rowIndex < 8; rowIndex ++) {
                for (int colIndex = 0; colIndex < 8; colIndex ++) {
                    ChessPiece myPiece = myBoard.getPiece(new ChessPosition(rowIndex + 1, colIndex + 1));
                    if (myPiece != null) {
                        if (myPiece.getTeamColor() == teamColor) {
                            if (validMoves(new ChessPosition(rowIndex + 1, colIndex + 1)).isEmpty()) {
                                return true;
                            } else {
                                return false;

                            }
                        }
                    }
                }
            }
        } else {
            checkmate = false;
        }
        return checkmate;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        boolean stalemate = true;
        if (!isInCheck(teamColor)) {
            for (int rowIndex = 0; rowIndex < 8; rowIndex ++) {
                for (int colIndex = 0; colIndex < 8; colIndex ++) {
                    ChessPiece myPiece = myBoard.getPiece(new ChessPosition(rowIndex + 1, colIndex + 1));
                    if (myPiece != null) {
                        if (myPiece.getTeamColor() == teamColor) {
                            if (validMoves(new ChessPosition(rowIndex + 1, colIndex + 1)).isEmpty()) {
                                stalemate = true;
                            } else {
                                stalemate = false;
                                return stalemate;
                            }
                        }
                    }
                }
            }
        } else {
            stalemate = false;
        }
        return stalemate;
    }

    /**
     * Sets this game's chessboard to a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        myBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return myBoard;
    }
}
