package com.example.helloworld.resources;

import com.codahale.metrics.annotation.Timed;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Meter;
import com.codahale.metrics.Gauge;
import com.example.helloworld.api.Saying;
import com.example.helloworld.core.Template;
import io.dropwizard.jersey.caching.CacheControl;
import io.dropwizard.jersey.params.DateTimeParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.ThreadLocalRandom;

@Path("/hello-world")
@Produces(MediaType.APPLICATION_JSON)
public class HelloWorldResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(HelloWorldResource.class);

    private final Template template;
    private final AtomicLong counter;
    private final MetricRegistry metrics;
    private final Meter requests;
    private final Gauge gauge;

    public HelloWorldResource(Template template, MetricRegistry registry) {
        this.template = template;
        this.counter = new AtomicLong();
        this.metrics = registry;
        this.requests = metrics.meter(MetricRegistry.name(HelloWorldResource.class, "myrate"));
        this.gauge = metrics.register(MetricRegistry.name(HelloWorldResource.class, "mygauge"), (Gauge<Integer>) () -> ThreadLocalRandom.current().nextInt(1, 101));
    }

    @GET
    @Timed(name = "get-requests")
    @CacheControl(maxAge = 1, maxAgeUnit = TimeUnit.DAYS)
    public Saying sayHello(@QueryParam("name") Optional<String> name) {
        this.requests.mark();
        try {
            TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(0, 4));
        }
        catch(InterruptedException ex) 
        {
            Thread.currentThread().interrupt();
        }
        return new Saying(counter.incrementAndGet(), template.render(name));
    }

    @POST
    public void receiveHello(@Valid Saying saying) {
        LOGGER.info("Received a saying: {}", saying);
    }

    @GET
    @Path("/date")
    @Produces(MediaType.TEXT_PLAIN)
    public String receiveDate(@QueryParam("date") Optional<DateTimeParam> dateTimeParam) {
        if (dateTimeParam.isPresent()) {
            final DateTimeParam actualDateTimeParam = dateTimeParam.get();
            LOGGER.info("Received a date: {}", actualDateTimeParam);
            return actualDateTimeParam.get().toString();
        } else {
            LOGGER.warn("No received date");
            return null;
        }
    }
}
