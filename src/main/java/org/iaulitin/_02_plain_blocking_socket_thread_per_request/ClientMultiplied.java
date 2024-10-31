package org.iaulitin._02_plain_blocking_socket_thread_per_request;

import org.iaulitin.Constants;

import java.net.Socket;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientMultiplied {

    private static final ExecutorService es = Executors.newFixedThreadPool(100);
    private static final int REQUEST_AMOUNT = 10000;

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture<?>[] futures = new CompletableFuture[REQUEST_AMOUNT];
        for (int i = 0; i < REQUEST_AMOUNT; i++) {
            CompletableFuture<?> future = CompletableFuture.supplyAsync(() -> {
                try (Socket clientSocket = new Socket("localhost", Constants.SERVER_PORT)) {
                    org.iaulitin._01_plain_blocking_socket.Client.doClientWork(clientSocket);
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
                return null;
            }, es);
            System.out.println("Submitted a client #" + i);
            futures[i] = future;
        }
        CompletableFuture.allOf(futures).get();
        System.out.println("All done");
        es.shutdown();
    }

}
