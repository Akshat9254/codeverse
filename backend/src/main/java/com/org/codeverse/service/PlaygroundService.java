package com.org.codeverse.service;

import com.org.codeverse.dto.PlaygroundRequestDTO;
import com.org.codeverse.dto.PlaygroundResponseDTO;
import com.org.codeverse.enums.SubmissionResult;
import com.org.codeverse.runner.PlaygroundRunner;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.UUID;

@Service
public class PlaygroundService {
    public PlaygroundResponseDTO runCode(PlaygroundRequestDTO requestDTO) throws IOException, InterruptedException {
        String input = requestDTO.getInput() != null ? requestDTO.getInput() : "";
        String code = requestDTO.getCode();
        UUID id = UUID.randomUUID();

            PlaygroundRunner playgroundRunner = new PlaygroundRunner();
            String inputFilename = playgroundRunner.generateInputFile(id, input);
            String executableFilename = playgroundRunner.generateExecutableFile(id, code);
            String response = playgroundRunner.runFile("java_run.sh", executableFilename,
                    inputFilename);

            PlaygroundResponseDTO responseDTO = new PlaygroundResponseDTO();
            String[] parts = response.split("#");

            responseDTO.setOutput(parts[0]);
            String[] metaData = parts[1].split("\n");
            SubmissionResult result = SubmissionResult.valueOf(metaData[0]);
            Double runtime = Double.parseDouble(metaData[1]);

            responseDTO.setResult(result);
            responseDTO.setRuntime(runtime);

            playgroundRunner.deleteFile(inputFilename);
            playgroundRunner.deleteFile(executableFilename);
            return responseDTO;



    }
}
