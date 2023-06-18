package me.spike;

import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit.MockServerConfig;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.PactSpecVersion;
import au.com.dius.pact.core.model.V4Pact;
import au.com.dius.pact.core.model.annotations.Pact;
import io.quarkus.test.junit.QuarkusTest;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.inject.Inject;

@QuarkusTest
@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(pactVersion = PactSpecVersion.V4)
@MockServerConfig(port = "8080")
public class ConsumerPactTest {

    @Inject
    @RestClient
    GreetingRestClient greetingService;

    @Pact(provider = "springboot-provider-pact", consumer = "Consumer")
    public V4Pact greetingAKnownPerson(PactDslWithProvider builder) {
        return builder
                .given("greetingAKnownPerson")
                .uponReceiving("a greeting from a known person")
                .path("/hello")
                .matchQuery("firstName", "John")
                .method("GET")
                .willRespondWith()
                .status(200)
                .matchHeader("Content-Type", "application/json")
                .body(new PactDslJsonBody()
                        .stringType("firstName", "John")
                        .stringType("lastName", "Doe")
                        .stringType("greeting", "Hello John Doe!")
                )
                .toPact(V4Pact.class); //Reference - https://github.com/pact-foundation/pact-jvm/issues/1488
    }

    @Test
    @PactTestFor(pactMethod = "greetingAKnownPerson")
    void testGreetingAKnownPerson() {
        Greeting actualGreeting = greetingService.getGreeting("John");
        Assertions.assertNotNull(actualGreeting.greeting());
        Assertions.assertNotNull(actualGreeting.firstName());
        Assertions.assertNotNull(actualGreeting.lastName());
    }

    @Pact(provider = "springboot-provider-pact", consumer = "Consumer")
    public V4Pact greetingAnUnknownPerson(PactDslWithProvider builder) {
        return builder
                .given("greetingAnUnknownPerson")
                .uponReceiving("a greeting from an unknown person")
                .path("/hello")
                .matchQuery("firstName", "Unknown")
                .method("GET")
                .willRespondWith()
                .status(404)
                .toPact(V4Pact.class); //Reference - https://github.com/pact-foundation/pact-jvm/issues/1488
    }

    @Test
    @PactTestFor(pactMethod = "greetingAnUnknownPerson")
    void testGreetingAnUnknownPerson() {
        Assertions.assertThrows(UnknownPersonException.class,
                () -> greetingService.getGreeting("Unknown"));
    }

}
