You're reading the step 4 version of the README file. If you want to go to other step, use the corresponding git tag.

Prerequisites: You need to have mvn, docker and kubectl installed in your machine.

# Step 4
In this step we are moving the application inside a Kubernetes cluster. The starting point is with 2 pods.

```mermaid
graph TD
    subgraph External
        UserRequest["User Requests"]
        DB[(MySQL Database)]
    end

    subgraph Kubernetes Cluster
        LB["Load Balancer (Service)"]
        Pod1["Pod 1"]
        Pod2["Pod 2"]
    end

    UserRequest --> LB
    LB --> Pod1
    LB --> Pod2
    Pod1 --> DB
    Pod2 --> DB
```

The relationship between Users and Messages is defined here:
```mermaid
erDiagram
    User {
        Long id
        String username
        String password
        String email
    }
    Message {
        Long id
        String content
        LocalDateTime createdAt
    }
    User ||--o{ Message : "has many"
```

We have a simple application deployed on Kubernetes and a DB deployed on a container. The application is exposing endpoints to register, login, create a message, retrieve all messages (paginated in groups of 20) and retrieve all messages from a given user. Retrieving paginated messages will refresh its value every 10 seconds.

We use [JWT](https://jwt.io/) for authorisation tokens.

To run this step, just run inside this folder (check it has execute permissions, otherwise run chmod 755 on these script files)
```
./setUp.sh
```
This will create all the containers and apply all Kubernetes manifests and start the application. When it is up and running you can run
```
./runLoadTest.sh
```

This scenario is adding users every second to the system (after a ramp-up). The user is doing registration, login, posting 1 message and retrieving 5 pages of messages from other users. When all that is done, the user stops the activity.

You can check the load test results in the console after running the test (a link is provided) and you can also check your product and DB metrics in Grafana and some trace examples in APM by opening http://localhost:5601/app/apm/services/social-biznaga.

To run a breaking load test, just run
```
./runHighLoadTest.sh
```

When you are done, run 
```
./tearDown.sh
```
to destroy all containers

## Check Grafana
Go to http://localhost:3000/login and use admin/admin as credentials (it will as you to change if you wish). The first thing to do in Grafana, if you haven't done, is to setup a Data Source. Go to http://localhost:3000/connections/datasources/new and select Prometheus. Then in the Prometheus url add http://host.docker.internal:30090 and you can click on save and test. 
Now you should create your dashboard, so go to http://localhost:3000/dashboards and click on new > import or go to http://localhost:3000/dashboard/import and then you can upload the spring_board.json file (select your Prometheus source) and save. You can do the same with mysql_board.json to have a MySQL board.
Both dashboards will now appear in http://localhost:3000/dashboards so you can check them.