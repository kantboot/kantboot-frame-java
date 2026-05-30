package com.kantboot.official.plugin.functional.agent.executor;

import com.kantboot.ai.agent.executor.AiAgentExecutor;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

public class LocalAgentExecutor implements AiAgentExecutor {

    private static final int TIMEOUT_SECONDS = 60;
    private static final int MAX_OUTPUT_LENGTH = 8000;

    private final String workDir;

    public LocalAgentExecutor(String workDir) {
        this.workDir = workDir;
    }

    @Override
    public String execute(String command) throws Exception {
        ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);
        pb.redirectErrorStream(true);
        if (workDir != null && !workDir.isBlank()) {
            pb.directory(new java.io.File(workDir));
        }

        Process process = pb.start();
        StringBuilder output = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (output.length() < MAX_OUTPUT_LENGTH) {
                    output.append(line).append("\n");
                }
            }
        }

        boolean finished = process.waitFor(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        if (!finished) {
            process.destroyForcibly();
            output.append("\n[TIMEOUT: command exceeded ").append(TIMEOUT_SECONDS).append("s]");
        }

        if (output.length() >= MAX_OUTPUT_LENGTH) {
            output.append("\n[OUTPUT TRUNCATED]");
        }

        int exitCode = finished ? process.exitValue() : -1;
        if (exitCode != 0) {
            output.insert(0, "[exit code: " + exitCode + "]\n");
        }

        return output.toString();
    }

    @Override
    public void close() {
        // 无需释放资源
    }
}
