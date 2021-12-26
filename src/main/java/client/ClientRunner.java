package client;

import lombok.SneakyThrows;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

public class ClientRunner {
    private static final int PORT = 8080;
    private static final String SERVERIP = "127.0.0.1";

    @SneakyThrows
    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVERIP, PORT);
             DataInputStream inputStream = new DataInputStream(socket.getInputStream());
             DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
             Scanner scanner = new Scanner(System.in)) {
            System.out.println("""
                    Выберите игру:
                    1 - Человек - человек
                    2 - Человек - компьютер
                    3 - Компьютер - компьютер
                    """);
            int selectGame = scanner.nextInt();
            outputStream.writeInt(selectGame);
            int selectPlayer2;

            switch (selectGame) {

                case 1, 2 -> {
                    for (int i = 0; i < 5; i++) {
                        System.out.println("""
                                Выберите:
                                1 - Камень
                                2 - Ножницы
                                3 - Бумага
                                """);
                        selectPlayer2 = scanner.nextInt();
                        if (selectPlayer2 > 0 && selectPlayer2 < 4) {
                            i = inputOutputClient(inputStream, outputStream, selectPlayer2, i);
                        } else {
                            System.out.println("Сделайте правильный выбор");
                            i--;
                        }
                    }
                }
                case 3 -> {
                    for (int i = 0; i < 5; i++) {
                        selectPlayer2 = new Random().nextInt(3) + 1;
                        i = inputOutputClient(inputStream, outputStream, selectPlayer2, i);
                    }
                }
            }
            //Результат игры
            System.out.println(inputStream.readUTF());
        }
    }

    @SneakyThrows
    private static int inputOutputClient(DataInputStream inputStream, DataOutputStream outputStream, int selectPlayer2, int i) {
        int selectPlayer1;
        outputStream.writeInt(selectPlayer2);
        selectPlayer1 = inputStream.readInt();
        if (selectPlayer1 == selectPlayer2) {
            i--;
        } else {
            //Результат раунда
            System.out.println(inputStream.readUTF());
        }
        return i;
    }
}
