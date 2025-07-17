package kr.hhplus.be.server.ecommerce.service.product.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import kr.hhplus.be.server.ecommerce.domain.product.Product;
import kr.hhplus.be.server.ecommerce.service.product.IProductService;

@Service
public class ProductServiceImpl implements IProductService {

    private final Map<Long, Product> products = new ConcurrentHashMap<>();
    private final AtomicLong productIdCounter = new AtomicLong(0L);

    // Mocking을 위한 판매량 데이터 (인기 판매 상품 조회를 위해 사용)
    // 실제로는 주문/결제 시 업데이트되어야 함
    private final Map<Long, Long> productSalesCount = new ConcurrentHashMap<>();

    // 초기 상품 데이터 및 판매량 설정
    public ProductServiceImpl() {
        addProduct(new Product(null, "무선 키보드", new BigDecimal("50000.00"), 100, "전자제품", "편안한 타이핑 경험", LocalDateTime.now(), LocalDateTime.now()));
        addProduct(new Product(null, "게이밍 마우스", new BigDecimal("35000.00"), 150, "전자제품", "정확한 클릭 반응", LocalDateTime.now(), LocalDateTime.now()));
        addProduct(new Product(null, "USB-C 허브", new BigDecimal("20000.00"), 200, "액세서리", "다양한 포트 지원", LocalDateTime.now(), LocalDateTime.now()));
        addProduct(new Product(null, "노트북 스탠드", new BigDecimal("25000.00"), 80, "액세서리", "자세 교정 및 발열 관리", LocalDateTime.now(), LocalDateTime.now()));
        addProduct(new Product(null, "가죽 지갑", new BigDecimal("40000.00"), 50, "패션", "고급스러운 디자인", LocalDateTime.now(), LocalDateTime.now()));
        addProduct(new Product(null, "면 티셔츠", new BigDecimal("15000.00"), 300, "의류", "부드러운 면 100%", LocalDateTime.now(), LocalDateTime.now()));

        // 초기 판매량 설정 (예시)
        productSalesCount.put(1L, 100L); // 무선 키보드
        productSalesCount.put(2L, 120L); // 게이밍 마우스
        productSalesCount.put(3L, 80L);  // USB-C 허브
        productSalesCount.put(4L, 90L);  // 노트북 스탠드
        productSalesCount.put(5L, 70L);  // 가죽 지갑
        productSalesCount.put(6L, 150L); // 면 티셔츠
    }

    private void addProduct(Product product) {
        Long newId = productIdCounter.incrementAndGet();
        product.setProductId(newId);
        products.put(newId, product);
    }

    @Override
    public Optional<Product> getProduct(Long productId) {
        return Optional.ofNullable(products.get(productId));
    }

    @Override
    public List<Product> getProductsByCategory(String category, int page, int size) {
        return products.values().stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .skip((long) page * size)
                .limit(size)
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> getAllProducts(int page, int size) {
        return products.values().stream()
                .skip((long) page * size)
                .limit(size)
                .collect(Collectors.toList());
    }

    @Override
    public synchronized boolean decreaseStock(Long productId, Integer quantity) {
        Product product = products.get(productId);
        if (product == null || product.getStock() < quantity) {
            return false; // 상품이 없거나 재고 부족
        }
        product.setStock(product.getStock() - quantity);
        product.setUpdatedAt(LocalDateTime.now());
        System.out.println("Mock Product stock decreased for ID " + productId + ". New stock: " + product.getStock());
        return true;
    }

    @Override
    public synchronized void increaseStock(Long productId, Integer quantity) {
        Product product = products.get(productId);
        if (product != null) {
            product.setStock(product.getStock() + quantity);
            product.setUpdatedAt(LocalDateTime.now());
            System.out.println("Mock Product stock increased for ID " + productId + ". New stock: " + product.getStock());
        }
    }

    @Override
    public List<Product> getPopularProducts(int limit) {
        // Mock 데이터 기반으로 판매량이 가장 높은 상품 반환
        // 실제로는 최근 3일간의 주문 데이터를 분석해야 함
        return productSalesCount.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())) // 판매량 내림차순 정렬
                .limit(limit)
                .map(entry -> products.get(entry.getKey()))
                .filter(java.util.Objects::nonNull) // null 체크 (혹시 없는 상품 ID일 경우)
                .collect(Collectors.toList());
    }

    // Mocking을 위한 판매량 업데이트 (주문 서비스에서 호출)
    public void addSalesCount(Long productId, long count) {
        productSalesCount.merge(productId, count, Long::sum);
        System.out.println("Mock Product sales count updated for ID " + productId + ". New sales: " + productSalesCount.get(productId));
    }
}

