#!/bin/bash

# Define the download URL and the file location
FILE_PATH="./socialbiznaga/backend/elastic-apm-agent.jar"
DOWNLOAD_URL="https://oss.sonatype.org/service/local/repositories/releases/content/co/elastic/apm/elastic-apm-agent/1.52.0/elastic-apm-agent-1.52.0.jar"

if [ -f "$FILE_PATH" ]; then
    echo "The file already exists at $FILE_PATH."
else
    echo "The file does not exist. Downloading..."

    curl -o "$FILE_PATH" "$DOWNLOAD_URL"
    
    if [ $? -eq 0 ]; then
        echo "File downloaded successfully to $FILE_PATH."
    else
        echo "Error downloading the file."
    fi
fi

if ! docker info > /dev/null 2>&1; then
    echo "Error: Docker is not running or not installed."
    exit 1  # Exit the script with an error code
fi

echo "Docker is running. Executing 'docker compose up' in detached mode..."
docker compose up -d

echo "Docker Compose is running in the background."

echo "Trying to build the application with Maven"
cd ./socialbiznaga/backend || { echo "Error: Failed to change to 'socialbiznaga' directory."; exit 1; }
mvn clean install || { echo "Error: Failed building the application."; exit 1; }
docker rmi socialbiznaga-backend:latest
docker build . -t socialbiznaga-backend:latest

cd -

echo "Creating elements in Kubernetes"
cd ./manifests || { echo "Error: Failed to change to 'manifests' directory."; exit 1; }
kubectl apply -f 01-namespaces.yml
kubectl apply -f 02-prometheus.yml
kubectl apply -f 03-socialbiznaga.yml
kubectl apply -f 04-configmap.yml
helm install prometheus-adapter prometheus-community/prometheus-adapter --namespace socialbiznaga --values values.yaml
kubectl apply -f 05-autoscaler.yml

cd -