if ! docker info > /dev/null 2>&1; then
    echo "Error: Docker is not running or not installed."
    exit 1  # Exit the script with an error code
fi

docker compose down

cd -

echo "Cleaning up Kubernetes"
kubectl delete namespace socialbiznaga
kubectl delete namespace monitoring

echo "Application is down"