# Biznaga Load
This is a companion repository for the talk "Diseño y evaluación de sistemas guiados por pruebas de rendimiento".
In this repo, you can navigate the different 6 steps followed in the design of the system.

## Step 1
It is marked in the repo with the tag v1.0. This is the initial example.

## Step 2
It is marked in the repo with the tag v2.0. This adds pagination to the getMessages endpoint.

## Step 3
It is marked in the repo with the tag v3.0. This adds caching to the getMessages endpoint.

## Step 4
It is marked in the repo with the tag v4.0. This moves the application to Kubernetes.

## Step 5
It is marked in the repo with the tag v5.0. This denormalizes the DB.

## Step 6
It is marked in the repo with the tag v6.0. This adds Horizontal Pod Autoscaler.

Every step contains its own README.md file. All of them have a setUp.sh file to automate the creation of the containers and start the application. They also have a tearDown.sh file to remove all when you are done. Most of them have runLoadTest.sh and runHighLoadTest.sh. They will run different load test scenarios depending on the step you're in.

When you move between steps 1-4 to steps 5-6 or vice-versa, you should make sure you're removing your docker volume of the DB because DB schema is changing and they are incompatible; setUp.sh will regenerate it for you.