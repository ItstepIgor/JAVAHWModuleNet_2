package client;

import lombok.SneakyThrows;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

public class ClientRunner {
    private static final int PORT = 8080;
    private static final String SERVERIP = "127.0.0.1";

    @SneakyThrows
    public static void main(String[] args) {
        List<String> list = List.of("Камень", "Ножницы", "Бумага");
        try (Socket socket = new Socket(SERVERIP, PORT);
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
                int selectPlayer2 = scanner.nextInt();
                outputStream.writeInt(selectPlayer2);
                int selectPlayer1 = inputStream.readInt();
                if (selectPlayer1 == selectPlayer2) {
                    i--;
                }
            }
        }
    }
}
