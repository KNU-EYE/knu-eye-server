#!/bin/bash

# 1. 내 컴퓨터(로컬)에서 빌드 수행
./gradlew bootJar
# 2. 필수 파일들을 서버로 전송 (SCP 사용)
EC2_USER="ubuntu"
EC2_IP="3.37.123.248"
EC2_PATH="~/knu-eye-server"
KEY_PATH="knu-eye-key.pem"

# SSH 옵션: 새로운 서버 접속 시 yes/no 묻지 않음
SSH_OPTS="-i $KEY_PATH -o StrictHostKeyChecking=no"

echo "Connecting to server to prepare directory..."
ssh $SSH_OPTS "$EC2_USER@$EC2_IP" "mkdir -p $EC2_PATH/build/libs"

echo "Syncing configuration files and JAR to server..."
# scp 옵션: -p (진행 상황 표시 시도), StrictHostKeyChecking=no 추가
# 참고: 스크립트 내에서 scp의 진행률을 완벽히 보려면 터미널 환경에 따라 다를 수 있습니다.
scp -o StrictHostKeyChecking=no -i "$KEY_PATH" \
    docker-compose.yml \
    Dockerfile \
    .dockerignore \
    update.sh \
    build/libs/*.jar \
    "$EC2_USER@$EC2_IP:$EC2_PATH/"

echo "Finalizing file locations on server..."
# JAR 파일은 따로 위치 이동
ssh $SSH_OPTS "$EC2_USER@$EC2_IP" "mv $EC2_PATH/*.jar $EC2_PATH/build/libs/ 2>/dev/null || true"

# 3. 서버에 접속하여 업데이트 스크립트 실행
echo "Restarting application on server..."
ssh $SSH_OPTS "$EC2_USER@$EC2_IP" "chmod +x $EC2_PATH/update.sh && cd $EC2_PATH && ./update.sh"

