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

    String encoding;

    public BufferThreadManager(String encoding, Process process, Consumer<Integer> endCallback, Consumer<String> outputCallback) {
        this.encoding = encoding;
        this.process = process;
        this.bufferThread = new Thread(() -> fetchBuffer());
        this.endCallback = endCallback;
        this.outputCallback = outputCallback;
    }

    public void start() {
        this.bufferThread.start();
        outputCallback.accept("Console output thread started");
    }

    public void stop() {
        this.bufferThread.interrupt();
    }

    private void fetchBuffer() {
        BufferedReader input = null;
        try {
            input = new BufferedReader(new InputStreamReader(process.getInputStream(), encoding));
        } catch (UnsupportedEncodingException e) {
            outputCallback.accept("Unable to fetch console output : " + e.getMessage());
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
