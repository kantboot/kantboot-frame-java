package com.kantboot.official.plugin.functional.agent.executor;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.kantboot.ai.agent.executor.AiAgentExecutor;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class SshAgentExecutor implements AiAgentExecutor {

    private static final int CONNECT_TIMEOUT_MS = 10_000;
    private static final int COMMAND_TIMEOUT_MS = 60_000;
    private static final int MAX_OUTPUT_LENGTH = 8000;

    private final Session session;
    private final String workDir;

    public SshAgentExecutor(String host, int port, String user, String password, String workDir) throws Exception {
        JSch jsch = new JSch();
        this.session = jsch.getSession(user, host, port);
        this.session.setPassword(password);
        this.session.setConfig("StrictHostKeyChecking", "no");
        this.session.connect(CONNECT_TIMEOUT_MS);
        this.workDir = workDir;
    }

    @Override
    public String execute(String command) throws Exception {
        String fullCommand = (workDir != null && !workDir.isBlank())
                ? "cd " + workDir + " && " + command
                : command;

        ChannelExec channel = (ChannelExec) session.openChannel("exec");
        channel.setCommand(fullCommand);
        channel.setErrStream(System.err);

        StringBuilder output = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(channel.getInputStream(), StandardCharsets.UTF_8))) {
            channel.connect(CONNECT_TIMEOUT_MS);

            long deadline = System.currentTimeMillis() + COMMAND_TIMEOUT_MS;
            String line;
            while ((line = reader.readLine()) != null) {
                if (System.currentTimeMillis() > deadline) {
                    output.append("\n[TIMEOUT]");
                    break;
                }
                if (output.length() < MAX_OUTPUT_LENGTH) {
                    output.append(line).append("\n");
                }
            }
        } finally {
            int exitCode = channel.getExitStatus();
            channel.disconnect();
            if (exitCode != 0 && exitCode != -1) {
                output.insert(0, "[exit code: " + exitCode + "]\n");
            }
        }

        if (output.length() >= MAX_OUTPUT_LENGTH) {
            output.append("\n[OUTPUT TRUNCATED]");
        }

        return output.toString();
    }

    @Override
    public void close() {
        if (session != null && session.isConnected()) {
            session.disconnect();
        }
    }
}
