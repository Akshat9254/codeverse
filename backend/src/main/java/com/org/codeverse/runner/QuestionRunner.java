package com.org.codeverse.runner;
import com.org.codeverse.enums.SubmissionResult;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

public class QuestionRunner {
    private final int TIMEOUT_SECONDS = 5;
    public String generateInputFile(UUID id, List<String> inputs) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append(inputs.size()).append(System.lineSeparator());
        for (String input : inputs) {
            sb.append(input).append(System.lineSeparator());
        }

        String fileName = id.toString() + "-input.txt";
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(sb.toString());
        }

        return fileName;
    }

    public String generateOutputFile(UUID id) throws IOException {
        String fileName = id.toString() + "-output.txt";
        Files.createFile(Paths.get(fileName));
        return fileName;
    }

    public String generateStdoutFile(UUID id) throws IOException {
        String fileName = id.toString() + "-stdout.txt";
        Files.createFile(Paths.get(fileName));
        return fileName;
    }

    public String generateExecutableFile(UUID id, String code) throws IOException {
        String filename = id.toString() + ".java";
        FileWriter writer = new FileWriter(filename);
        writer.write(code);
        writer.close();
        return filename;
    }

    public SubmissionResult runExecutableFile(String scriptFilename, String executableFilename) throws IOException,
            InterruptedException {
        String[] command = {"bash", scriptFilename, executableFilename};
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.redirectErrorStream(true);

        Process process = pb.start();
//        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        boolean timedOut = false;
        long startTime = System.currentTimeMillis();
        SubmissionResult result = null;
        while (true) {
            try {
                int exitCode = process.exitValue();
                if (exitCode == 0) {
                    result = SubmissionResult.SUCCESSFUL;
                } else if (exitCode == 1) {
                    result =  SubmissionResult.COMPILATION_ERROR;
                } else if (exitCode == 2) {
                    result = SubmissionResult.RUNTIME_ERROR;
                } else {
                    result =  SubmissionResult.SOMETHING_WENT_WRONG;
                }

                break;
            } catch (IllegalThreadStateException e) {
                if ((System.currentTimeMillis() - startTime) > TIMEOUT_SECONDS * 1000) {
                    process.destroy();
                    timedOut = true;
                    break;
                }
                Thread.sleep(100); // Sleep for 100ms before checking again
            }
        }

        return timedOut ? SubmissionResult.TIME_LIMIT_EXCEEDED : result;
    }

    public boolean deleteFile(String filename) {
        File file = new File(filename);

        if (file.exists()) {
            file.delete();
        } else {
            System.out.println(filename + " not found.");
        }

        return false;
    }
}
