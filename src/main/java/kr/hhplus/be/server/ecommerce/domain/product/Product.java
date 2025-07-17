package kr.hhplus.be.server.ecommerce.domain.product;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    private Long productId;
    private String name;
    private BigDecimal price; 
    private Integer stock; 
    private String category; 
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
