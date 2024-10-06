#!/bin/bash

JAVA_FILE=$1
RESULT_FILE=$2

# 디렉토리 생성
mkdir -p "$(dirname "$JAVA_FILE")"

while true; do
    if [ ! -f "$JAVA_FILE" ]; then
        echo "Waiting for Java file..."
        sleep 1
        continue

    fi

    echo "Java file found. Processing..."

    # 클래스 이름 추출
    CLASS_NAME=$(grep 'public class ' $JAVA_FILE | awk '{print $3}')

    if [ -z "$CLASS_NAME" ]; then
        echo "Class name could not be determined"
        exit 1
    fi

    # 파일 이름과 클래스 이름을 맞추기
    JAVA_FILE_NAME=$(basename "$JAVA_FILE")
    EXPECTED_FILE_NAME="${CLASS_NAME}.java"

    if [ "$JAVA_FILE_NAME" != "$EXPECTED_FILE_NAME" ]; then
        echo "Class name and file name mismatch: Expected $EXPECTED_FILE_NAME but got $JAVA_FILE_NAME"
        exit 1
    fi

    # Java 파일을 컴파일 후 실행
    if javac "$JAVA_FILE"; then
        echo "Compilation successful. Running $CLASS_NAME..."
        # Java 실행 결과를 result.txt에 저장
        java -cp "$(dirname "$JAVA_FILE")" "$CLASS_NAME" > "$RESULT_FILE" 2>&1
    else
        echo "Compilation failed."
        exit 1
    fi

    # 파일을 처리한 후 삭제
    rm -f "$JAVA_FILE"
    sleep 1
done
