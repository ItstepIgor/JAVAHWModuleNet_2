package client;

import lombok.SneakyThrows;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
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
            outputStream.writeUTF("Привет от клиента");
            System.out.println(inputStream.readUTF());
        }
    }
}
