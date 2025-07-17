package kr.hhplus.be.server.ecommerce.controller.product.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductCreateRequest {
    @NotBlank(message = "상품 이름은 필수입니다.")
    private String name;
    private String description;
    @NotNull(message = "가격은 필수입니다.")
    @DecimalMin(value = "0.00", message = "가격은 0 이상이어야 합니다.")
    private BigDecimal price;
    @NotNull(message = "재고는 필수입니다.")
    @Min(value = 0, message = "재고는 0 이상이어야 합니다.")
    private Integer stock;
    @NotBlank(message = "카테고리는 필수입니다.")
    private String category;
}
