package kr.hhplus.be.server.ecommerce.service.product;

import java.util.List;
import java.util.Optional;

import kr.hhplus.be.server.ecommerce.domain.product.Product;

public interface IProductService {
	Optional<Product> getProduct(Long productId); // 단일 상품 조회
    List<Product> getProductsByCategory(String category, int page, int size); // 카테고리별 상품 목록 조회
    List<Product> getAllProducts(int page, int size); // 모든 상품 목록 조회
    boolean decreaseStock(Long productId, Integer quantity); // 재고 감소 (주문 시 호출)
    void increaseStock(Long productId, Integer quantity); // 재고 증가 (주문 취소/환불 시 호출)
    List<Product> getPopularProducts(int limit); // 인기 판매 상품 조회 (이 부분은 Mock 데이터를 활용)
    Optional<Product> updateProductStock(Long productId, Integer newStock);
    Optional<Product> createProduct(Product product);
    void deleteProduct(Long productId);
}
