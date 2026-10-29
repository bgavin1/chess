import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import sql.MySqlDataAccess;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ChessSerializationTest {

    @Test
            void testSerialization() throws InvalidMoveException {
        ChessGame myGame = new ChessGame();
        ChessPosition startPosition = new ChessPosition(2,1);
        ChessPosition endPosition = new ChessPosition(3,1);
        ChessMove myMove = new ChessMove(startPosition, endPosition, null);
        myGame.makeMove(myMove);
        Gson gson = new Gson();
        String json = gson.toJson(myGame);
        ChessGame restoredGame = gson.fromJson(json, ChessGame.class);
        assertEquals(myGame, restoredGame);
    }

    @Test
    void testCreateTables() throws Exception {
        new MySqlDataAccess();

    }
}
