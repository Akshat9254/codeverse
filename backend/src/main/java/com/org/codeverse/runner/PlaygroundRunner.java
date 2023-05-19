package com.org.codeverse.runner;

import com.org.codeverse.exception.CompilationError;
import com.org.codeverse.exception.TimeLimitExceededException;
import com.org.codeverse.enums.SubmissionResult;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class PlaygroundRunner extends QuestionRunner{
    private static final int TIMEOUT_SECONDS = 5;

    public String generateInputFile(UUID id, String input) throws IOException {
        String fileName = id.toString() + "-input.txt";
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(input);
        }

        return fileName;
    }
    
    public String runFile(String scriptFilename, String executableFilename, String inputFilename) throws IOException,
            InterruptedException {
        String[] command = {"bash", scriptFilename, executableFilename};
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.redirectErrorStream(true);

        Process process = pb.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        boolean timedOut = false;
        long startTime = System.currentTimeMillis();
        String line = null;
        StringBuilder output = new StringBuilder();
        SubmissionResult result = null;

        try {
            boolean processExited = process.waitFor(TIMEOUT_SECONDS, TimeUnit.SECONDS);
            if (!processExited) {
                process.destroy();
                deleteFile(inputFilename);
                deleteFile(executableFilename);
                throw new TimeLimitExceededException();
            } else {
                int exitCode = process.exitValue();

                switch (exitCode) {
                    case 0: result = SubmissionResult.SUCCESSFUL;
                        break;
                    case 1: result = SubmissionResult.COMPILATION_ERROR;
                        break;
                    case 2: result = SubmissionResult.RUNTIME_ERROR;
                        break;
                    default: result = SubmissionResult.SOMETHING_WENT_WRONG;
                }
            }

            line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        } catch (InterruptedException e) {
            process.destroy();
        } catch (IOException e) {

        }

        if(result == SubmissionResult.COMPILATION_ERROR) {
            deleteFile(inputFilename);
            deleteFile(executableFilename);
            throw new CompilationError(output.toString());
        }

        long endTime = System.currentTimeMillis();

        output.append("#");
        output.append(result).append("\n");
        output.append((endTime - startTime));

        return output.toString();
    }
}
