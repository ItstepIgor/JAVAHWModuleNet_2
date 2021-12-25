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
                    calcGame.calcRound(selectPlayer1, selectPlayer2);
                }
            }
            int sumPointsPlayer1 = calcGame.getGameList().stream().mapToInt(Game::getPointsPlayer1).sum();
            int sumPointsPlayer2 = calcGame.getGameList().stream().mapToInt(Game::getPointsPlayer2).sum();
//            sumPointsPlayer1 > sumPointsPlayer2 ? outputStream.writeUTF();

        }
    }
}
