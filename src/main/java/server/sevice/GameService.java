package server.sevice;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.ToString;
import server.entity.RoundGame;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@ToString
public class GameService {
    private final List<RoundGame> roundGameList = new ArrayList<>();
    private final List<String> resultGamePlayer1 = new ArrayList<>();

    List<String> list = List.of("Камень", "Ножницы", "Бумага");
    private String messagePlayer1;
    private String messagePlayer2;

    @SneakyThrows
    public int inputOutputServer(DataInputStream inputStream, DataOutputStream outputStream, int i, int selectPlayer1, int j) {
        outputStream.writeInt(selectPlayer1);
        int selectPlayer2 = inputStream.readInt();
        if (selectPlayer1 == selectPlayer2) {
            System.out.println("Ничья");
            i--;
        } else {
            int victoryFirstPlayer = calcRound(selectPlayer1, selectPlayer2, j);

            if (victoryFirstPlayer == 1) {
                messagePlayer1 = "У вас " + list.get(selectPlayer1 - 1) + " у противника " + list.get(selectPlayer2 - 1)
                        + " вы победили ";
                messagePlayer2 = "У вас " + list.get(selectPlayer2 - 1) + " у противника " + list.get(selectPlayer1 - 1)
                        + " вы проиграли ";
            } else {
                messagePlayer1 = "У вас " + list.get(selectPlayer1 - 1) + " у противника " + list.get(selectPlayer2 - 1)
                        + " вы проиграли ";
                messagePlayer2 = "У вас " + list.get(selectPlayer2 - 1) + " у противника " + list.get(selectPlayer1 - 1)
                        + " вы победили ";
            }
            outputStream.writeUTF(messagePlayer2);
            System.out.println(messagePlayer1);
        }
        return i;
    }

    public int calcRound(int player1, int player2, int j) {
        int victoryFirstPlayer = 0;
        if (player1 == 1 && player2 == 2) {
            roundGameList.add(new RoundGame(player1, player2, 1, 0, j));
            victoryFirstPlayer = 1;
        } else if (player1 == 1 && player2 == 3) {
            roundGameList.add(new RoundGame(player1, player2, 0, 1, j));
        } else if (player1 == 2 && player2 == 3) {
            roundGameList.add(new RoundGame(player1, player2, 1, 0, j));
            victoryFirstPlayer = 1;
        } else if (player1 == 3 && player2 == 2) {
            roundGameList.add(new RoundGame(player1, player2, 0, 1, j));
        } else if (player1 == 3 && player2 == 1) {
            roundGameList.add(new RoundGame(player1, player2, 1, 0, j));
            victoryFirstPlayer = 1;
        } else if (player1 == 2 && player2 == 1) {
            roundGameList.add(new RoundGame(player1, player2, 0, 1, j));
        }
        return victoryFirstPlayer;
    }

    public void resultGame(DataOutputStream outputStream, int j) {

        int sumPointsPlayer1 = getRoundGameList().stream().filter(roundGame -> roundGame.getNumberGame() == j)
                .mapToInt(RoundGame::getPointsPlayer1).sum();
        int sumPointsPlayer2 = getRoundGameList().stream().filter(roundGame -> roundGame.getNumberGame() == j)
                .mapToInt(RoundGame::getPointsPlayer2).sum();
        String message = buildMessage(outputStream, sumPointsPlayer1, sumPointsPlayer2);

        resultGamePlayer1.add("Игра №" + (j + 1) + " " + message);

        System.out.println(message);
    }

    @SneakyThrows
    public void resultMatch(DataOutputStream outputStream) {
        int sumPointsPlayer1 = getRoundGameList().stream().mapToInt(RoundGame::getPointsPlayer1).sum();
        int sumPointsPlayer2 = getRoundGameList().stream().mapToInt(RoundGame::getPointsPlayer2).sum();

        System.out.println("Результат матча");
        resultGamePlayer1.forEach(System.out::println);

        System.out.println(buildMessage(outputStream, sumPointsPlayer1, sumPointsPlayer2));

        List<String> result = new ArrayList<>();

        getRoundGameList().forEach(roundGame -> {
            result.add(list.get(roundGame.getSelectionPlayer1() - 1));
            result.add(list.get(roundGame.getSelectionPlayer2() - 1));
        });

        String maxElement = "Самая популярная фигура: " + result
                .stream()
                .collect(Collectors.groupingBy(name -> name, Collectors.counting()))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .get()
                .getKey();

        System.out.println(maxElement);
        outputStream.writeUTF(maxElement);

        String minElement = "Самая непопулярная фигура: " + result
                .stream()
                .collect(Collectors.groupingBy(name -> name, Collectors.counting()))
                .entrySet()
                .stream()
                .min(Map.Entry.comparingByValue())
                .get()
                .getKey();

        System.out.println(minElement);
        outputStream.writeUTF(minElement);
    }

    @SneakyThrows
    private String buildMessage(DataOutputStream outputStream, int sumPointsPlayer1, int sumPointsPlayer2) {
        if (sumPointsPlayer1 > sumPointsPlayer2) {
            messagePlayer1 = "Вы победили " + " счет " + sumPointsPlayer1 + " : " + sumPointsPlayer2 + " в вашу пользу ";
            messagePlayer2 = "Вы проиграли " + " счет " + sumPointsPlayer2 + " : " + sumPointsPlayer1 + " в пользу противника ";
        } else {
            messagePlayer1 = "Вы проиграли " + " счет " + sumPointsPlayer1 + " : " + sumPointsPlayer2 + " в пользу противника ";
            messagePlayer2 = "Вы победили " + " счет " + sumPointsPlayer2 + " : " + sumPointsPlayer1 + " в вашу пользу ";
        }
        outputStream.writeUTF(messagePlayer2);
        return messagePlayer1;
    }
}
