package server;

import lombok.SneakyThrows;
import server.sevice.GameService;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

public class ServerRunner {

    private static final int PORT = 8080;

    @SneakyThrows
    public static void main(String[] args) {

        GameService gameService = new GameService();
        try (ServerSocket serverSocket = new ServerSocket(PORT);
             Socket socket = serverSocket.accept();
             DataInputStream inputStream = new DataInputStream(socket.getInputStream());
             DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
             Scanner scanner = new Scanner(System.in)) {

            int selectGame = inputStream.readInt();
            switch (selectGame) {
                case 1 -> {
                    for (int i = 0; i < 5; i++) {
                        System.out.println("""
                                Выберите:
                                1 - Камень
                                2 - Ножницы
                                3 - Бумага
                                """);
                        int selectPlayer1 = scanner.nextInt();
                        if (selectPlayer1 > 0 && selectPlayer1 < 4) {
                            i = gameService.inputOutputServer(inputStream, outputStream, i, selectPlayer1);
                        } else {
                            System.out.println("Сделайте правильный выбор");
                            i--;
                        }
                    }
                }
                case 2, 3 -> {
                    for (int i = 0; i < 5; i++) {
                        int selectPlayer1 = new Random().nextInt(3) + 1;
                        i = gameService.inputOutputServer(inputStream, outputStream, i, selectPlayer1);
                    }
                }
            }
            gameService.inputOutputResult(outputStream);
        }
    }
}
