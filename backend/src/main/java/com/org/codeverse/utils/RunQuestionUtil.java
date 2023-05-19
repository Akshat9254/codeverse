package com.org.codeverse.utils;

import com.org.codeverse.enums.Language;
import com.org.codeverse.exception.CompilationError;
import com.org.codeverse.exception.TimeLimitExceededException;
import com.org.codeverse.enums.SubmissionResult;
import com.org.codeverse.model.TestCase;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class RunQuestionUtil {
    private static final int TIMEOUT_SECONDS = 5;
    public static String generateScriptFile(String script, Language language) throws IOException {
        String fileName = language + "-script.sh";
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(script);
        }
        return fileName;
    }
    public static String generateInputFile(String id, String input) throws IOException {
        String fileName = id + "-input.txt";
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(input);
        }

        return fileName;
    }
    public static String generateInputFile(String id, List<String> inputs) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append(inputs.size() + "\n");
        for(String input: inputs) {
            sb.append(input + "\n");
        }

        return generateInputFile(id, sb.toString());
    }

    public static String generateInputFileWithTestCases(String id, List<TestCase> testCases) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append(testCases.size() + "\n");
        for(TestCase testCase: testCases) {
            sb.append(testCase.getInput() + "\n");
            sb.append(testCase.getOutput() + "\n");
        }

        return generateInputFile(id, sb.toString());
    }
    public static String generateOutputFile(String id) throws IOException {
        String fileName = id + "-output.properties";
        Files.createFile(Paths.get(fileName));
        return fileName;
    }
    public static String generateStdoutFile(String id) throws IOException {
        String fileName = id + "-stdout.txt";
        Files.createFile(Paths.get(fileName));
        return fileName;
    }
    public static String generateExecutableFile(String id, String code, String extension) throws IOException {
        String filename = id + extension;
        FileWriter writer = new FileWriter(filename);
        writer.write(code);
        writer.close();
        return filename;
    }
//    public boolean deleteFile(String filename) {
//        File file = new File(filename);
//
//        if (file.exists()) {
//            file.delete();
//        } else {
//            System.out.println(filename + " not found.");
//        }
//
//        return false;
//    }
public static String runFile(String scriptFilename, String executableFilename, String inputFilename) throws IOException,
        InterruptedException {
    String[] command = {"bash", scriptFilename, executableFilename};
    ProcessBuilder pb = new ProcessBuilder(command);
    pb.redirectErrorStream(true);

    Process process = pb.start();
    StringBuilder output = new StringBuilder();
    SubmissionResult result = null;

    try {
        boolean processExited = process.waitFor(5, TimeUnit.SECONDS);
        if (!processExited) {
            process.destroy();
//            deleteFile(inputFilename);
//            deleteFile(executableFilename);
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

//            line = reader.readLine();
//            while (line != null) {
//                output.append(line);
//                line = reader.readLine();
//            }
    } catch (InterruptedException e) {
        process.destroy();
    }

    if(result == SubmissionResult.COMPILATION_ERROR) {
        throw new CompilationError(output.toString());
    }

    output.append(result);
    return output.toString();
}
    public static String generateJavaClassFile(String driverCode, String snippet, String correctSnippet,
                                               String inputFileName) {
        driverCode = driverCode.replace("{inputFileName}", inputFileName);
        correctSnippet = correctSnippet.replace("class Solution", "class CorrectSolution");
        String code = driverCode + "\n\n" + correctSnippet + "\n\n" + snippet;

        try {
            // The name of the file to write to
            String filename = inputFileName.split("[.]", 0)[0] + ".java";
            FileWriter writer = new FileWriter(filename);

            // Write the code to the file
            writer.write(code);

            // Close the FileWriter object
            writer.close();

            // Print a confirmation message
            System.out.println("Successfully wrote code to " + filename);

            return filename;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    public static String runJavaClassFile(String fileName, int numTestCases) {
        // Get the name of the bash script
        String bashScriptName = "java_run.sh";

        // Get the name of the Java file to compile and run

        // Build the command to run the bash script with the Java file name as an argument
        String[] command = {"bash", bashScriptName, fileName};

        // Create a new ProcessBuilder object with the command
        ProcessBuilder pb = new ProcessBuilder(command);

        // Redirect error output to standard output
        pb.redirectErrorStream(true);

        StringBuilder sb = new StringBuilder();

        try {
            // Start the process and wait for it to finish
            Process process = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                sb.append(line + "\n");
            }

            int exitCode = process.waitFor();

            // Print the exit code of the process
            System.out.println("Process exited with code " + exitCode);
            String response = null;
            if (exitCode == 0) {
                // Success
                response = "Successful\n" + sb.toString();
            } else if (exitCode == 1) {
                // Compilation error
                response = "Compilation Error\n" + sb.toString();
            } else if (exitCode == 2) {
                // Runtime error
                response = "Runtime Error\n" + sb.toString();
            } else {
                // Other error
                response = "Something went wrong\n" + sb.toString();
            }

            return response;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return "";
    }

    public static boolean deleteFile(String filename) {
        File file = new File(filename);

        if (file.exists()) {
            boolean deleted = file.delete();
            if (deleted) {
                System.out.println(filename + " deleted successfully.");
                return true;
            } else {
                System.out.println("Failed to delete " + filename);
            }
        } else {
            System.out.println(filename + " not found.");
        }

        return false;
    }
}
