package com.nlmk.adp.config.swagger;

import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

/**
 * Дополнительная конфигурация OpenAPI.
 */
@Configuration
public class OpenApiCustomerConfig {

    private static final String NOT_FOUND = "{"
            + "  \"message\": \"Объект com.nlmk.adp.model.Entity не найден по id=42\"\n"
            + "}";

    /**
     * Описание структуры ошибки 404 Not Found.
     */
    @Bean
    public OpenApiCustomiser notFoundErrorMessage() {
        return openApi -> openApi.getPaths().values().forEach(pathItem -> {
            pathItem.readOperations().forEach(operation -> {
                Content content = new Content().addMediaType(
                        org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
                        new MediaType().example(NOT_FOUND)
                );
                ApiResponse notFoundResponse = new ApiResponse().description("Not Found").content(content);
                operation.getResponses().addApiResponse("404", notFoundResponse);
            });
        });
    }

    /**
     * Описание структуры ошибки 500 Internal Server Error.
     */
    @Bean
    public OpenApiCustomiser internalServerErrorMessage() {
        return openApi -> openApi.getPaths().values().forEach(pathItem -> {
            pathItem.readOperations().forEach(operation -> {
                Content content = new Content().addMediaType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
                        new MediaType().example(HttpStatus.INTERNAL_SERVER_ERROR.value()
                                                        + HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                );
                ApiResponse notFoundResponse = new ApiResponse().description("Internal Server Error").content(content);
                operation.getResponses().addApiResponse("500", notFoundResponse);
            });
        });
    }

}
