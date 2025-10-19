// java
package G1swp.proj.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${server.port:8080}")
    private String serverPort;

    @Bean
    public OpenAPI customOpenAPI() {
        Server localServer = new Server();
        localServer.setUrl("http://localhost:" + serverPort);
        localServer.setDescription("Local development server");

        Contact contact = new Contact()
                .name("EV-warranty Team")
                .email("support@ev-warranty.com");

        Info info = new Info()
                .title("EV-warranty API")
                .version("1.0.0")
                .description("API documentation for the EV-warranty backend application. " +
                        "Use the /api/auth/login or /api/auth/register endpoint to get a JWT token, " +
                        "then click 'Authorize' button and enter: Bearer {your-token}")
                .contact(contact);

        return new OpenAPI()
                .info(info)
                .servers(List.of(localServer))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Enter JWT token obtained from login/register endpoint")));
    }

    @Bean
    public GroupedOpenApi publicApi() {
        // include all paths so the UI has an API definition available
        return GroupedOpenApi.builder()
                .group("public")
                .pathsToMatch("/**")
                .build();
    }
}
