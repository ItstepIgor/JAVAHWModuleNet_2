package server.sevice;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import server.entity.Game;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class CalcGame {
    private List<Game> gameList = new ArrayList<>();

    public void calcRound(int player1, int player2) {
        if (player1 == 1 && player2 == 2) {
            gameList.add(new Game(player1, player2, 1, 0));
        } else if (player1 == 1 && player2 == 3) {
            gameList.add(new Game(player1, player2, 0, 1));
        } else if (player1 == 2 && player2 == 3) {
            gameList.add(new Game(player1, player2, 1, 0));
        } else if (player1 == 3 && player2 == 2) {
            gameList.add(new Game(player1, player2, 0, 1));
        } else if (player1 == 3 && player2 == 1) {
            gameList.add(new Game(player1, player2, 1, 0));
        } else if (player1 == 2 && player2 == 1) {
            gameList.add(new Game(player1, player2, 0, 1));
        } else {
            gameList.add(new Game(player1, player2, 0, 0));
        }
    }
}
