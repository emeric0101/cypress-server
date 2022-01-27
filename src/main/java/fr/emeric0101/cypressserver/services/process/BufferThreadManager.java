package fr.emeric0101.cypressserver.services.process;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.function.Consumer;

@Slf4j
public class BufferThreadManager {
    Process process;
    Thread bufferThread;
    Consumer<Integer> endCallback;
    Consumer<String> outputCallback;

    @Value("${cypress.encoding}")
    private String ENCODING;

    public BufferThreadManager(Process process, Consumer<Integer> endCallback, Consumer<String> outputCallback) {
        this.process = process;
        this.bufferThread = new Thread(() -> fetchBuffer());
        this.endCallback = endCallback;
        this.outputCallback = outputCallback;
    }

    public void start() {
        this.bufferThread.start();
    }

    public void stop() {
        this.bufferThread.interrupt();
    }

    private void fetchBuffer() {
        BufferedReader input = null;
        try {
            input = new BufferedReader(new InputStreamReader(process.getInputStream(), ENCODING));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return;
        }

        while (process.isAlive()) {
            try {
                String line = input.readLine();
                if (line != null) {
                    // flag that logstash is OK
                    outputCallback.accept(line);

                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        try {
            while (input.ready()) {
                String line = input.readLine();
                if (line != null) {
                    // flag that logstash is OK
                    outputCallback.accept(line);
                }
            }
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        // fetch remaining lines
        log.info("Process has end up with code " + process.exitValue());

        if (endCallback != null) {
            endCallback.accept(process.exitValue());
        }
    }

    public boolean isRunning() {
        if (bufferThread == null) {
            return false;
        }
        return bufferThread.isAlive();
    }
}
