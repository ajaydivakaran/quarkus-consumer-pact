package me.spike;

import org.eclipse.microprofile.rest.client.ext.ResponseExceptionMapper;

import javax.ws.rs.core.Response;

public class InvalidGreetingHandler implements ResponseExceptionMapper<RuntimeException> {
    @Override
    public RuntimeException toThrowable(Response response) {
        int status = response.getStatus();
        if (status == 404) {
            throw new UnknownPersonException();
        } else {
            throw new RuntimeException();
        }
    }
}
