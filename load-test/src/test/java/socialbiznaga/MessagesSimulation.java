package socialbiznaga;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.FeederBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class MessagesSimulation extends Simulation {
    FeederBuilder<String> feeder = csv("messages.csv").circular();

    Iterator<Map<String, Object>> feederUUID =
            Stream.generate((Supplier<Map<String, Object>>) () -> {
                        String uuidString = UUID.randomUUID().toString();
                        return Collections.singletonMap("uuidString", uuidString);
                    }).iterator();


    ChainBuilder register =
            tryMax(2)
                    .on(
                            feed(feederUUID),
                            http("postMessage")
                                    .post("/api/users/register")
                                    .body(StringBody("{\n" +
                                            "    \"username\": \"#{uuidString}\",\n" +
                                            "    \"password\": \"test\",\n" +
                                            "    \"email\": \"#{uuidString}@test.com\"\n" +
                                            "}")).asJson()
                                    .check(status().is(200))
                    )
                    .exitHereIfFailed()
                    .pause(1)
                    .exec(http("postMessage")
                            .post("/api/users/login")
                            .body(StringBody("{\n" +
                                    "    \"username\": \"#{uuidString}\",\n" +
                                    "    \"password\": \"test\"\n" +
                                    "}")).asJson()
                            .check(status().is(200))
                            .check(jsonPath("$.tokenId").saveAs("token")))
                    .exitHereIfFailed();

    ChainBuilder messages =
            tryMax(2)
                    .on(
                            feed(feeder),
                            http("postMessage")
                                    .post("/api/messages")
                                    .body(StringBody("{\"content\": \"#{content}\"}")).asJson()
                                    .header("Authorization", "Bearer #{token}")
                                    .check(status().is(202))
                    )
                    .exitHereIfFailed()
                    .pause(1)
                    .foreach(List.of("1","2","3","4","5"),"page")
                    .on(exec(http("getMessage")
                            .get("/api/messages/page/#{page}")
                            .header("Authorization", "Bearer #{token}")
                            .check(status().is(200))))
                    .exitHereIfFailed();

    HttpProtocolBuilder httpProtocol =
            http.baseUrl("http://localhost:30000")
                    .acceptHeader("text/html,application/json")
                    .acceptLanguageHeader("en-US,en;q=0.5")
                    .acceptEncodingHeader("gzip, deflate")
                    .userAgentHeader(
                            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:109.0) Gecko/20100101 Firefox/119.0"
                    );

    ScenarioBuilder messagesScenario = scenario("Users").exec(register, messages);

    {
        setUp(
                messagesScenario.injectOpen(
                        atOnceUsers(1),
                        rampUsersPerSec(1).to(25).during(45),
                        constantUsersPerSec(25).during(120),
                        rampUsersPerSec(25).to(1).during(45)
                ).protocols(httpProtocol)
        );
    }
}
