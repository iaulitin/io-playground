package org.iaulitin._04_nio_with_files;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class FileWriter {

    public static void main(String[] args) throws IOException {
        String message = "Hello, world!";
        int bufferSize = 20;

        try (ByteChannel byteChannel = Files.newByteChannel(
                Path.of("output.txt"),
                StandardOpenOption.CREATE,
                StandardOpenOption.WRITE)) {
            ByteBuffer byteBuffer = ByteBuffer.allocate(bufferSize);
            byteBuffer.put(message.getBytes(StandardCharsets.UTF_8));
            // when we wrote to the buffer, the pointer to the buffer moved. This call resets pointer back to 0
            // write to the file starts from the current position of the pointer
            byteBuffer.flip();

            byteChannel.write(byteBuffer);
        }
    }

}
