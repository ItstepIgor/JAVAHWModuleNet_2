package server;

import lombok.SneakyThrows;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ServerRunner {

    private static final int PORT = 8080;

    @SneakyThrows
    public static void main(String[] args) {

        try (ServerSocket serverSocket = new ServerSocket(PORT);
             Socket socket = serverSocket.accept();
             DataInputStream inputStream = new DataInputStream(socket.getInputStream());
             DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
             Scanner scanner = new Scanner(System.in)) {
            System.out.println(inputStream.readUTF());
            outputStream.writeUTF("Привет от сервера");
        }

    }
}
