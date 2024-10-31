package org.iaulitin._02_plain_blocking_socket_thread_per_request;

import org.iaulitin.Constants;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ThreadPerRequestServer {

    public static void main(String[] args) {

        try (ServerSocket serverSocket = new ServerSocket(Constants.SERVER_PORT)) {
            int count = 0;
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Accepted connection #" + count++);
                Thread thread = new Thread(() -> {
                    try {
                        org.iaulitin._01_plain_blocking_socket.Server.doServerWork(clientSocket);
                    } catch (IOException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }, "thread-" + count);
                thread.start();
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

    }
}
