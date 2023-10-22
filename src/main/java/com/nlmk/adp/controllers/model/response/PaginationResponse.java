package com.nlmk.adp.controllers.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Информация о странице.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Информация о странице")
public class PaginationResponse {

    @Schema(description = "Номер страницы результатов, с 1")
    private Integer pageNum;

    @Schema(description = "Размер страницы результатов")
    private Integer pageSize;

    @Schema(description = "Общее количество страниц результатов")
    private Integer totalPages;

    @Schema(description = "Общее количество результатов")
    private Integer totalCount;

}
