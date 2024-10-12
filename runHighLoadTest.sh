#!/bin/bash

echo "Preparing load test"
cd ./load-test || { echo "Error: Failed to change to 'load-test' directory."; exit 1; }
mvn clean install || { echo "Error: Failed building the application."; exit 1; }

mvn gatling:test -Dgatling.simulationClass=socialbiznaga.MessagesBrokenSimulation || { echo "Error: Failed Running the load test."; exit 1; }