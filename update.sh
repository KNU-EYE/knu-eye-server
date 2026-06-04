#!/bin/bash

echo "Starting application update (Pre-built mode)..."

# 1. 기존 컨테이너 중지 및 이미지 재빌드
# 이제 Dockerfile이 COPY만 수행하므로 매우 가볍고 빠릅니다.
docker compose up -d --build

# 2. 사용하지 않는 오래된 이미지 삭제
docker image prune -f

echo "Update complete! Application is running."
docker compose ps
