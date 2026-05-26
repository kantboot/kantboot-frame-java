package com.kantboot.test.starter.main;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.Scanner;

@Slf4j
public class Terminal {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String currentDirectory = "C:\\"; // 默认目录

        while (true) {
            System.out.print("\n" + currentDirectory + "> ");
            String command = scanner.nextLine().trim();

            if (command.equalsIgnoreCase("exit")) {
                log.info("退出终端");
                break;
            }

            if (command.startsWith("cd ")) {
                // 处理cd命令
                String newDir = command.substring(3).trim();
                File dir = new File(newDir);
                if (!dir.isAbsolute()) {
                    dir = new File(currentDirectory, newDir);
                }
                if (dir.exists() && dir.isDirectory()) {
                    currentDirectory = dir.getAbsolutePath();
                    log.info("当前目录: {}", currentDirectory);
                } else {
                    log.error("目录不存在: {}", dir.getAbsolutePath());
                }
                continue;
            }

            executeCommand(command, currentDirectory);
        }
        scanner.close();
    }

    private static void executeCommand(String command, String workingDir) {
        try {
            Process process = Runtime.getRuntime().exec("cmd /c " + command, null, new File(workingDir));

            // 读取标准输出
            readStream(process.getInputStream(), "GBK");
            // 读取错误输出
            readStream(process.getErrorStream(), "GBK");

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                log.info("[命令执行成功]");
            } else {
                log.error("[命令执行失败] 退出码: {}", exitCode);
            }
        } catch (IOException | InterruptedException e) {
            log.error("命令执行出错: {}", e.getMessage());
        }
    }

    private static void readStream(InputStream inputStream, String charset) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, charset));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
        reader.close();
    }
}