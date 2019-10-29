package com.fburzigo.thorntail.example.openapidemo.rest;

import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.swarm.jaxrs.JAXRSArchive;

@RunWith(Arquillian.class)
public class HelloWorldEndpointTest {

	@Deployment(testable = false)
	public static Archive createDeployment() throws Exception {
		return ShrinkWrap.create(JAXRSArchive.class, "demo-test.war")
				.addClasses(RestApplication.class, HelloWorldEndpoint.class)
				.setContextRoot("/")
				.addAllDependencies();
	}

	@Test
	@RunAsClient
	public void hitHelloWorldEndpoint() {
		RestAssured.when().get("http://localhost:8080/hello").then()
			.statusCode(200)
			.body(Matchers.is("Hello from Thorntail!"));
	}

	@Test
	@RunAsClient
	public void checkForOpenApiEndpointExistence() {
		RestAssured.when().get("http://localhost:8080/openapi").then()
				.statusCode(200)
				.body(Matchers.not(Matchers.empty()));
	}

	@Test
	@RunAsClient
	public void checkForOpenApiEndpointJsonDefinition() {
		RestAssured.when().get("http://localhost:8080/openapi?format=JSON").then()
				.statusCode(200)
				.body("openapi", Matchers.is("3.0.1"));
	}
}
