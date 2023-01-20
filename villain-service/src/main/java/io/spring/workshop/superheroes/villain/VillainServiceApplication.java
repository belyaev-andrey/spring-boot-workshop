package io.spring.workshop.superheroes.villain;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(title = "Villain API",
                description = "This API allows CRUD operations on a villain",
                version = "1.0",
                contact = @Contact(name = "Spring Buddy", url = "https://spring-buddy.com")),
        servers = {
                @Server(url = "http://localhost:8084")
        },
        externalDocs = @ExternalDocumentation(url = "https://quarkus.io/quarkus-workshops/super-heroes", description = "Based on this workshop")
)
public class VillainServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(VillainServiceApplication.class, args);
    }

}
