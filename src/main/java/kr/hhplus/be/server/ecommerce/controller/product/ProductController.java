package kr.hhplus.be.server.ecommerce.controller.product;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import kr.hhplus.be.server.ecommerce.controller.product.dto.ProductCreateRequest;
import kr.hhplus.be.server.ecommerce.controller.product.dto.ProductResponse;
import kr.hhplus.be.server.ecommerce.controller.product.dto.ProductUpdateRequest;
import kr.hhplus.be.server.ecommerce.domain.product.Product;
import kr.hhplus.be.server.ecommerce.service.product.IProductService;
import lombok.RequiredArgsConstructor;

@Tag(name = "상품 관리 API", description = "상품 정보 조회 및 인기 상품 조회")
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

	private final IProductService productService; // 인터페이스 사용

    @Operation(summary = "모든 상품 목록 조회", description = "시스템에 등록된 모든 상품 목록을 페이지네이션하여 조회합니다.")
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<Product> products = productService.getAllProducts(page, size);
        List<ProductResponse> responses = products.stream()
                                                .map(ProductResponse::from)
                                                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "카테고리별 상품 목록 조회", description = "특정 카테고리에 속한 상품 목록을 페이지네이션하여 조회합니다.")
    @GetMapping("/category/{category}")
    public ResponseEntity<List<ProductResponse>> getProductsByCategory(
            @PathVariable String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<Product> products = productService.getProductsByCategory(category, page, size);
        List<ProductResponse> responses = products.stream()
                                                .map(ProductResponse::from)
                                                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "단일 상품 조회", description = "특정 상품 ID로 상품 상세 정보를 조회합니다.")
    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long productId) {
        return productService.getProduct(productId) // getProduct로 변경
                .map(product -> ResponseEntity.ok(ProductResponse.from(product)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "상품 재고 감소", description = "특정 상품의 재고를 지정된 수량만큼 감소시킵니다. 주로 주문 시 내부적으로 호출됩니다.")
    @PostMapping("/{productId}/decrease-stock")
    public ResponseEntity<String> decreaseProductStock(
            @PathVariable Long productId,
            @RequestParam @Min(1) Integer quantity) {
        boolean success = productService.decreaseStock(productId, quantity);
        if (success) {
            return ResponseEntity.ok("재고가 성공적으로 감소되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("재고 감소 실패: 상품이 없거나 재고가 부족합니다.");
        }
    }

    @Operation(summary = "상품 재고 증가", description = "특정 상품의 재고를 지정된 수량만큼 증가시킵니다. 주로 주문 취소/환불 시 내부적으로 호출됩니다.")
    @PostMapping("/{productId}/increase-stock")
    public ResponseEntity<String> increaseProductStock(
            @PathVariable Long productId,
            @RequestParam @Min(1) Integer quantity) {
        productService.increaseStock(productId, quantity);
        return ResponseEntity.ok("재고가 성공적으로 증가되었습니다.");
    }

    @Operation(summary = "인기 판매 상품 조회", description = "가장 많이 판매된 상품 목록을 조회합니다.")
    @GetMapping("/popular")
    public ResponseEntity<List<ProductResponse>> getPopularProducts(
            @RequestParam(defaultValue = "5") @Min(1) int limit) {
        List<Product> popularProducts = productService.getPopularProducts(limit);
        List<ProductResponse> responses = popularProducts.stream()
                                                    .map(ProductResponse::from)
                                                    .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "상품 정보 업데이트 (재고 포함)", description = "특정 상품의 재고를 포함한 정보를 업데이트합니다. (관리자용)")
    @PatchMapping("/{productId}/stock-update") // 기존 updateProductStock과 겹치지 않도록 경로 변경
    public ResponseEntity<ProductResponse> updateProductInfo(
            @PathVariable Long productId,
            @Valid @RequestBody ProductUpdateRequest request) { // ProductUpdateRequest는 재고만 포함
        try {
            return productService.updateProductStock(productId, request.getNewStock())
                    .map(product -> ResponseEntity.ok(ProductResponse.from(product)))
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null); // 에러 메시지를 포함하는 DTO 반환 권장
        }
    }


    @Operation(summary = "새로운 상품 등록", description = "관리자가 새로운 상품을 시스템에 등록합니다.")
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductCreateRequest request) {
        try {
            // DTO를 Product 엔티티로 변환 (Mock 서비스가 Product 엔티티를 받으므로)
            Product productToCreate = Product.builder()
                                        .name(request.getName())
                                        .description(request.getDescription())
                                        .price(request.getPrice())
                                        .stock(request.getStock())
                                        .category(request.getCategory())
                                        .build();

            return productService.createProduct(productToCreate)
                    .map(product -> ResponseEntity.status(HttpStatus.CREATED).body(ProductResponse.from(product)))
                    .orElseGet(() -> ResponseEntity.internalServerError().build()); // Mock 서비스에서 이 경우는 잘 발생하지 않지만, 방어 코드
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @Operation(summary = "상품 삭제", description = "관리자가 특정 상품을 시스템에서 삭제합니다.")
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        try {
            productService.deleteProduct(productId);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build(); // 삭제할 상품을 찾지 못했을 경우 404
        }
    }
    
}
