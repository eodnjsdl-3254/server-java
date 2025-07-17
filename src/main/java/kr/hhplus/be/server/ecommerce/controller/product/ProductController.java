package kr.hhplus.be.server.ecommerce.controller.product;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.ecommerce.domain.product.Product;
import kr.hhplus.be.server.ecommerce.service.product.IProductService;
import lombok.RequiredArgsConstructor;

@Tag(name = "상품 관리 API", description = "상품 정보 조회 및 인기 상품 조회")
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final IProductService productService;

    @Operation(summary = "모든 상품 목록 조회", description = "페이지네이션을 사용하여 모든 상품 정보를 조회합니다.")
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts(
            @Parameter(description = "페이지 번호 (0부터 시작)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 당 상품 수", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        List<Product> products = productService.getAllProducts(page, size);
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "단일 상품 조회", description = "특정 상품 ID로 상품 상세 정보를 조회합니다.")
    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable Long productId) {
        Optional<Product> product = productService.getProduct(productId);
        return product.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "카테고리별 상품 목록 조회", description = "특정 카테고리에 해당하는 상품 목록을 조회합니다.")
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Product>> getProductsByCategory(
            @Parameter(description = "조회할 상품 카테고리", example = "전자제품")
            @PathVariable String category,
            @Parameter(description = "페이지 번호 (0부터 시작)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 당 상품 수", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        List<Product> products = productService.getProductsByCategory(category, page, size);
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "인기 판매 상품 조회", description = "가장 많이 팔린 상위 N개 상품 정보를 조회합니다. (Mock 데이터 기반)")
    @GetMapping("/popular")
    public ResponseEntity<List<Product>> getPopularProducts(
            @Parameter(description = "조회할 인기 상품의 수", example = "5")
            @RequestParam(defaultValue = "5") int limit) {
        List<Product> popularProducts = productService.getPopularProducts(limit);
        return ResponseEntity.ok(popularProducts);
    }
}
