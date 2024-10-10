package com.psjoon.codingtest.controller;

import com.psjoon.codingtest.dto.CodeRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api")
public class CodeExecutionController {
    @PostMapping("/run-code")
    public ResponseEntity<Map<String, String>> runCode(@RequestBody CodeRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        System.out.println(userId);
        String language = request.getLanguage();
        String code = request.getCode();

        // 정규식을 사용하여 클래스명 추출 (public class 뒤의 단어)
        Pattern pattern = Pattern.compile("public class\\s+(\\w+)");
        Matcher matcher = pattern.matcher(code);
        String className = null;
        if (matcher.find()) {
            className = matcher.group(1);  // 클래스명 추출
        }

        if (className == null) {
            // 클래스명이 없는 경우 에러 반환
            return ResponseEntity.badRequest().body(Map.of("error", "클래스명을 찾을 수 없습니다."));
        }

        String testLv = "easy_1";  // 난이도 수준
        String id = "temp";        // 나중에 로그인한 사용자 ID로 변경 예정

        // executeInDocker로 코드, 클래스명, testLv, id 전달
        try {
            String result = executeInDocker("java-container", code, className, testLv, id);
            return ResponseEntity.ok(Map.of("result", result));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/submit-code")
    public ResponseEntity<Map<String, Boolean>> submitCode(@RequestBody CodeRequest request) {
        // 정답 여부를 판별하는 로직 추가
        boolean isCorrect = checkAnswer(request.getCode(), request.getLanguage());

        Map<String, Boolean> response = new HashMap<>();
        response.put("correct", isCorrect);
        return ResponseEntity.ok(response);
    }

    private String executeInDocker(String containerName, String code, String className, String testLv, String id) throws IOException, InterruptedException {
        // 파일 경로를 /app/{id}/{testLv}/ 으로 설정
        String basePath = "/app/" + id + "/" + testLv;
        String javaFileName = basePath + "/" + className + ".java";  // 클래스명으로 파일 이름 설정
        String resultFileName = basePath + "/result_" + className + ".txt";  // 결과 파일 이름 설정

        System.out.println("Attempting to create and copy the Java file to Docker container...");

        // Docker 컨테이너 내 경로가 없으면 생성
        ProcessBuilder mkdirBuilder = new ProcessBuilder("docker", "exec", containerName, "mkdir", "-p", basePath);
        Process mkdirProcess = mkdirBuilder.start();
        int mkdirResult = mkdirProcess.waitFor();

        if (mkdirResult != 0) {
            System.err.println("Error creating directory in Docker container. Process exit code: " + mkdirResult);
            return "Directory creation failed.";
        }

        // 임시 파일로 자바 코드 작성
        Path tempFile = Files.createTempFile("code", ".java");
        Files.write(tempFile, code.getBytes(StandardCharsets.UTF_8));

        // 임시 파일을 Docker 컨테이너로 복사
        ProcessBuilder copyBuilder = new ProcessBuilder("docker", "cp", tempFile.toString(), containerName + ":" + javaFileName);
        Process copyProcess = copyBuilder.start();
        int copyResult = copyProcess.waitFor();

        // 복사 성공 여부 확인
        if (copyResult != 0) {
            System.err.println("Error copying file to Docker container. Process exit code: " + copyResult);
            Files.delete(tempFile);  // 임시 파일 삭제
            return "File copy failed.";
        } else {
            System.out.println("File copied successfully to Docker container.");
        }

        // start.sh 스크립트에서 파일명 인자 추가 (비동기 실행)
        ProcessBuilder execBuilder = new ProcessBuilder("docker", "exec", containerName, "bash", "/app/start.sh", javaFileName, resultFileName);
        execBuilder.start();  // 백그라운드에서 실행되므로 waitFor() 사용하지 않음

        // result.txt 파일이 생성될 때까지 주기적으로 확인
        boolean resultReady = false;
        while (!resultReady) {
            ProcessBuilder checkResultBuilder = new ProcessBuilder("docker", "exec", containerName, "bash", "-c", "[ -f " + resultFileName + " ] && echo 'ready'");
            Process checkProcess = checkResultBuilder.start();
            BufferedReader checkReader = new BufferedReader(new InputStreamReader(checkProcess.getInputStream()));
            String checkOutput = checkReader.readLine();

            if ("ready".equals(checkOutput)) {
                resultReady = true;
            } else {
                Thread.sleep(1000);  // 1초 대기 후 다시 확인
            }
        }

        // result.txt 파일에서 결과 읽기
        ProcessBuilder resultBuilder = new ProcessBuilder("docker", "exec", containerName, "cat", resultFileName);
        Process resultProcess = resultBuilder.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(resultProcess.getInputStream()));

        StringBuilder output = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }

        // cat 명령이 완료될 때까지 기다림
        int resultProcessExitCode = resultProcess.waitFor();
        if (resultProcessExitCode != 0) {
            System.err.println("Error reading result file. Process exit code: " + resultProcessExitCode);
            return "Error reading result file.";
        }

        // result.txt 파일 삭제
        ProcessBuilder deleteResultFileBuilder = new ProcessBuilder("docker", "exec", containerName, "rm", resultFileName);
        Process deleteResultFileProcess = deleteResultFileBuilder.start();
        int deleteResult = deleteResultFileProcess.waitFor();

        if (deleteResult != 0) {
            System.err.println("Error deleting result file. Process exit code: " + deleteResult);
        } else {
            System.out.println("Result file deleted successfully.");
        }

        // Java 소스 파일 삭제
        ProcessBuilder deleteJavaFileBuilder = new ProcessBuilder("docker", "exec", containerName, "rm", javaFileName);
        Process deleteJavaFileProcess = deleteJavaFileBuilder.start();
        int deleteJavaResult = deleteJavaFileProcess.waitFor();

        if (deleteJavaResult != 0) {
            System.err.println("Error deleting Java file. Process exit code: " + deleteJavaResult);
        } else {
            System.out.println("Java file deleted successfully.");
        }

        // 클래스 파일(.class) 삭제
        String classFileName = basePath + "/" + className + ".class";
        ProcessBuilder deleteClassFileBuilder = new ProcessBuilder("docker", "exec", containerName, "rm", classFileName);
        Process deleteClassFileProcess = deleteClassFileBuilder.start();
        int deleteClassResult = deleteClassFileProcess.waitFor();

        if (deleteClassResult != 0) {
            System.err.println("Error deleting class file. Process exit code: " + deleteClassResult);
        } else {
            System.out.println("Class file deleted successfully.");
        }

        // 임시 파일 삭제
        Files.delete(tempFile);

        // 결과 반환
        return output.toString();
    }






    private boolean checkAnswer(String code, String language) {
        // 제출된 코드를 평가하는 로직
        return true; // 예시로 항상 정답으로 처리
    }
}
