package server;

import lombok.Getter;
import lombok.SneakyThrows;
import server.sevice.GameService;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

@Getter
public class ServerRunner {

    private static final int PORT = 8080;

    @SneakyThrows

    public static void main(String[] args) {
        String offer;
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
                                outputStream.writeUTF("game");

                                offer = inputStream.readUTF();
                                if (offer.equals("capitulate")) {
                                    System.out.println("Противник сдался");
                                    cycleStop = true;
                                    socket.close();
                                    serverSocket.close();
                                } else if (offer.equals("offerDraw")) {
                                    System.out.println("""
                                            Противник предлагает ничью:
                                            1 - согласиться
                                            2 - продолжить игру
                                            """);
                                    int a = scanner.nextInt();
                                    if (a == 1) {
                                        outputStream.writeBoolean(true);
                                        System.out.println("Игроки заключили ничью");
                                        cycleStop = true;
                                        socket.close();
                                        serverSocket.close();
                                    } else {
                                        outputStream.writeBoolean(false);
                                        i--;
                                    }
                                } else {
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
                            case 2 -> {
                                //Обработка с ничьей на сервере
                                inputStream.readUTF();
                                outputStream.writeUTF("offerDraw");
                                boolean a = inputStream.readBoolean();
                                if (a) {
                                    cycleStop = true;
                                    System.out.println("Игроки заключили ничью");
                                    socket.close();
                                } else {
                                    System.out.println("Игра продолжается");
                                    i--;
                                }
                            }
                            case 3 -> {
                                outputStream.writeUTF("capitulate");
                                System.out.println("Вы сдались");
                                inputStream.readUTF();
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
                        outputStream.writeUTF("game");
                        offer = inputStream.readUTF();
                        int selectPlayer1 = new Random().nextInt(3) + 1;
                        if (offer.equals("capitulate")) {
                            System.out.println("Противник сдался");
                            cycleStop = true;
                            socket.close();
                            serverSocket.close();
                        } else if (offer.equals("offerDraw")) {
                            //Сервер принимает ничью сразу. Можно написать логику на отказ
                            outputStream.writeBoolean(true);
                            System.out.println("Игроки заключили ничью");
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
