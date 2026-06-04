#!/bin/bash

# 1. 내 컴퓨터(로컬)에서 빌드 수행
./gradlew bootJar

# 2. 빌드된 JAR 파일만 서버로 전송 (SCP 사용)
# USER: ubuntu, IP: EC2 주소, PATH: 서버 내 프로젝트 경로
EC2_USER="ubuntu"
EC2_IP="your-ec2-public-ip"
EC2_PATH="~/knu-eye-server"
KEY_PATH="your-key.pem"

scp -i "$KEY_PATH" build/libs/*.jar "$EC2_USER@$EC2_IP:$EC2_PATH/build/libs/"

# 3. 서버에 접속하여 업데이트 스크립트 실행
ssh -i "$KEY_PATH" "$EC2_USER@$EC2_IP" "cd $EC2_PATH && ./update.sh"
