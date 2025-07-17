package kr.hhplus.be.server.ecommerce.domain.product;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
@Builder
public class Product {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;
	@Column(nullable = false, length = 255)
    private String name;
	@Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal price; 
	@Column(nullable = false)
    private Integer stock; 
	@Column(nullable = false, length = 50)
    private String category; 
	@Column(columnDefinition = "TEXT")
    private String description;
	@Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
	@Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
