if ! docker info > /dev/null 2>&1; then
    echo "Error: Docker is not running or not installed."
    exit 1  # Exit the script with an error code
fi

docker compose down

cd -

echo "Navigating to 'socialbiznaga' directory and running 'docker compose down' in detached mode..."
cd ./socialbiznaga || { echo "Error: Failed to change to 'socialbiznaga' directory."; exit 1; }
docker compose down

cd -

echo "Application is down"