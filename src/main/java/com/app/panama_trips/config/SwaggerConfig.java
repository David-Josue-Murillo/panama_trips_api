package com.app.panama_trips.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.http.HttpHeaders;

@OpenAPIDefinition(
    info = @Info(
        title = "Panama Trips API",
        description = "API for managing trips in Panama",
        version = "1.0.0",
        contact = @Contact(
                name = "David Murillo",
                url = "https://davidmurilloup.netlify.app/",
                email = "david.josue.murillo23@gmail.com"
        ),
        license = @License(
                name = "MIT License",
                url = "https://opensource.org/licenses/MIT"
        )
    ),
    servers = {
            @Server(
                    description = "Development Server",
                    url = "http://localhost:8080"
            ),
            @Server(
                    description = "Production Server",
                    url = "https://panama-trips-api.com"
            )
    },
    security = @SecurityRequirement(
            name = "Security Token"
    )
)
@SecurityScheme(
    name = "Security Token",
    description = "Access token for the API",
    type = SecuritySchemeType.HTTP,
    paramName = HttpHeaders.AUTHORIZATION,
    in = SecuritySchemeIn.HEADER,
    scheme = "bearer",
    bearerFormat = "JWT"
)
public class SwaggerConfig {
}
