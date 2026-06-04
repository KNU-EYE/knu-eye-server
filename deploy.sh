#!/bin/bash

# Update system
sudo apt-get update
sudo apt-get install -y ca-certificates curl gnupg lsb-release

# Install Docker
sudo mkdir -p /etc/apt/keyrings
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg
echo "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
sudo apt-get update
sudo apt-get install -y docker-ce docker-ce-cli containerd.io docker-compose-plugin

# Start Docker and enable it on boot
sudo systemctl start docker
sudo systemctl enable docker

# Allow current user to run docker commands without sudo
sudo usermod -aG docker $USER

echo "Docker installation complete. Please log out and log back in to apply group changes."
echo "After logging back in, run: docker compose up -d"
