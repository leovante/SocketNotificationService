package com.nlmk.adp.config.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Дополнительная конфигурация OpenAPI.
 */
@Configuration
@OpenAPIDefinition(security = {
        @SecurityRequirement(name = "security_auth")
})
@SecurityScheme(name = "security_auth", type = SecuritySchemeType.OAUTH2,
                flows = @OAuthFlows(
                        authorizationCode = @OAuthFlow(
                                authorizationUrl = "${spring.security.oauth2.resource-server.jwt.issuer-uri}"
                                        + "/protocol/openid-connect/auth",
                                tokenUrl = "${spring.security.oauth2.resource-server.jwt.issuer-uri}"
                                        + "/protocol/openid-connect/token")
                )
)
public class OpenApiConfig {

    private static final String NOT_FOUND = """
            {
               "type": "about:blank",
               "title": "Not Found",
               "status": 404,
               "detail": "description",
               "instance": "/api/v1/url"
             }""";

    /**
     * Служебные ответы.
     */
    @Bean
    OpenApiCustomizer addDefaultResponses() {
        Content content = new Content()
                .addMediaType(
                        org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
                        new MediaType().example(NOT_FOUND));

        ApiResponse notFoundResponse = new ApiResponse()
                .description("Not Found")
                .content(content);

        ApiResponse notAuthorized = new ApiResponse()
                .description("Not Authorized");

        return openApi ->
                openApi.getPaths()
                       .values()
                       .forEach(pathItem -> pathItem
                               .readOperations()
                               .forEach(operation -> operation
                                       .getResponses()
                                       .addApiResponse("404", notFoundResponse)
                                       .addApiResponse("401", notAuthorized)));
    }

}
