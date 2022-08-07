package me.spike;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/hello")
public class GreetingResource {

    @Inject
    @RestClient
    GreetingRestClient greetingService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Greeting hello(){
        return greetingService.getGreeting();
    }
}
