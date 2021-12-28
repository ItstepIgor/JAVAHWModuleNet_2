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
        boolean cycleStop = false;
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
                                1 - Совершить ход
                                2 - Предложить ничью
                                3 - Признать поражение
                                """);
                        int selectAction = scanner.nextInt();
                        switch (selectAction) {
                            case 1 -> {
                                outputStream.writeBoolean(false);
                                System.out.println("""
                                        Выберите:
                                        1 - Камень
                                        2 - Ножницы
                                        3 - Бумага
                                        """);
                                int selectPlayer1 = scanner.nextInt();
                                if (selectPlayer1 > 0 && selectPlayer1 < 4) {
                                    boolean capitulate = inputStream.readBoolean();
                                    if (capitulate) {
                                        System.out.println("Противник сдался");
                                        cycleStop = true;
                                        socket.close();
                                        serverSocket.close();
                                    } else {
                                        i = gameService.inputOutputServer(inputStream, outputStream, i, selectPlayer1);
                                    }

                                } else {
                                    System.out.println("Сделайте правильный выбор");
                                    i--;
                                }
                            }
//                            case 2 -> {
//                                outputStream.writeUTF("Предлагаю ничью");
//                                String same = inputStream.readUTF();
//                                if (same.equals("Yes")) {
//                                    System.out.println("Игроки заключили ничью");
//                                    socket.close();
//                                }
//                            }
                            case 3 -> {
                                outputStream.writeBoolean(true);
//                                cycleStop = true;
                                System.out.println("Вы сдались");
                                inputStream.readBoolean();
                                cycleStop = true;
                            }
                        }
                        if (cycleStop) {
                            break;
                        }
                    }
                }
                case 2, 3 -> {
                    for (int i = 0; i < 5; i++) {
                        boolean capitulate = inputStream.readBoolean();
                        outputStream.writeBoolean(false);
                        int selectPlayer1 = new Random().nextInt(3) + 1;
                        if (capitulate) {
                            System.out.println("Противник сдался");
                            cycleStop = true;
                            socket.close();
                            serverSocket.close();
                        } else {
                            i = gameService.inputOutputServer(inputStream, outputStream, i, selectPlayer1);
                        }
                        if (cycleStop) {
                            break;
                        }
                    }
                }
            }
            gameService.inputOutputResult(outputStream);
        } catch (Exception exception) {
            System.out.println("Игра закончена");
        }
    }
}
