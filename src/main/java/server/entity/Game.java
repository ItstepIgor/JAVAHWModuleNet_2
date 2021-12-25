package server.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Game {
    int selectionPlayer1;
    int selectionPlayer2;
    int pointsPlayer1;
    int pointsPlayer2;
}
