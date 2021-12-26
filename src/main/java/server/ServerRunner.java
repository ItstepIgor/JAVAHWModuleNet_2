package server;

import lombok.SneakyThrows;
import server.entity.Game;
import server.sevice.CalcGame;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

public class ServerRunner {

    private static final int PORT = 8080;

    @SneakyThrows
    public static void main(String[] args) {
        List<String> list = List.of("Камень", "Ножницы", "Бумага");
        CalcGame calcGame = new CalcGame();
        try (ServerSocket serverSocket = new ServerSocket(PORT);
             Socket socket = serverSocket.accept();
             DataInputStream inputStream = new DataInputStream(socket.getInputStream());
             DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
             Scanner scanner = new Scanner(System.in)) {
            String messagePlayer1;
            String messagePlayer2;
            for (int i = 0; i < 5; i++) {
                System.out.println("""
                        Выберите:
                        1 - Камень
                        2 - Ножницы
                        3 - Бумага
                        """);
                int selectPlayer1 = scanner.nextInt();
                outputStream.writeInt(selectPlayer1);
                int selectPlayer2 = inputStream.readInt();
                if (selectPlayer1 == selectPlayer2) {
                    i--;
                } else {
                    int victoryFirstPlayer = calcGame.calcRound(selectPlayer1, selectPlayer2);
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
            }
            int sumPointsPlayer1 = calcGame.getGameList().stream().mapToInt(Game::getPointsPlayer1).sum();
            int sumPointsPlayer2 = calcGame.getGameList().stream().mapToInt(Game::getPointsPlayer2).sum();
            if (sumPointsPlayer1 > sumPointsPlayer2) {
                messagePlayer1 = " Вы победили " + " счет " + sumPointsPlayer1 + " : " + sumPointsPlayer2 + " в вашу пользу ";
                messagePlayer2 = "Вы проиграли " + " счет " + sumPointsPlayer2 + " : " + sumPointsPlayer1 + " в пользу противника ";
            } else {
                messagePlayer1 = "Вы проиграли " + " счет " + sumPointsPlayer1 + " : " + sumPointsPlayer2 + " в пользу противника ";
                messagePlayer2 = " Вы победили " + " счет " + sumPointsPlayer2 + " : " + sumPointsPlayer1 + " в вашу пользу ";
            }
            System.out.println(messagePlayer1);
            outputStream.writeUTF(messagePlayer2);
        }
    }
}
