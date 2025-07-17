package kr.hhplus.be.server.ecommerce.domain.product;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    /**
     * 특정 카테고리의 상품 목록을 조회합니다.
     * @param category 조회할 카테고리
     * @return 해당 카테고리의 상품 리스트
     */
    List<Product> findByCategory(String category);

    /**
     * 특정 카테고리의 상품 목록을 페이지네이션하여 조회합니다.
     * @param category 조회할 카테고리
     * @param pageable 페이지네이션 정보
     * @return 해당 카테고리의 상품 페이지
     */
    Page<Product> findByCategory(String category, Pageable pageable);

    /**
     * 상품 이름에 특정 키워드가 포함된 상품 목록을 조회합니다 (Like 검색).
     * @param name 검색할 상품 이름 키워드
     * @return 키워드를 포함하는 상품 리스트
     */
    List<Product> findByNameContaining(String name);

    /**
     * 재고가 특정 값보다 큰 상품 목록을 조회합니다.
     * @param stock 최소 재고 수량
     * @return 해당 재고 수량보다 큰 상품 리스트
     */
    List<Product> findByStockGreaterThan(Integer stock);
}
