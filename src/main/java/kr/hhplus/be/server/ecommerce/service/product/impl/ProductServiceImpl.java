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

	// ConcurrentHashMap은 기본적으로 스레드 안전하지만,
    // get 후 put과 같은 복합 연산(compound operation)에는 여전히 동기화가 필요합니다.
    // 하지만 대부분의 Mock 시나리오에서는 큰 문제가 되지 않을 수 있습니다.
    // 명시적으로 `synchronized` 키워드를 사용하거나, `compute` 등의 메서드를 활용할 수 있습니다.
    private final Map<Long, Product> products = new ConcurrentHashMap<>();
    private final AtomicLong productIdCounter = new AtomicLong(0L);

    // Mocking을 위한 판매량 데이터 (인기 판매 상품 조회를 위해 사용)
    private final Map<Long, Long> productSalesCount = new ConcurrentHashMap<>();

    // 초기 상품 데이터 및 판매량 설정
    public ProductServiceImpl() {
        // addProduct 메서드를 사용하여 ID가 자동으로 할당되도록 합니다.
        // Product.builder()를 사용하여 객체 생성
        Product p1 = addProduct(Product.builder()
                                        .name("무선 키보드")
                                        .description("편안한 타이핑 경험")
                                        .price(new BigDecimal("50000.00"))
                                        .stock(100)
                                        .category("전자제품")
                                        .createdAt(LocalDateTime.now())
                                        .updatedAt(LocalDateTime.now())
                                        .build());
        Product p2 = addProduct(Product.builder()
                                        .name("게이밍 마우스")
                                        .description("정확한 클릭 반응")
                                        .price(new BigDecimal("35000.00"))
                                        .stock(150)
                                        .category("전자제품")
                                        .createdAt(LocalDateTime.now())
                                        .updatedAt(LocalDateTime.now())
                                        .build());
        Product p3 = addProduct(Product.builder()
                                        .name("USB-C 허브")
                                        .description("다양한 포트 지원")
                                        .price(new BigDecimal("20000.00"))
                                        .stock(200)
                                        .category("액세서리")
                                        .createdAt(LocalDateTime.now())
                                        .updatedAt(LocalDateTime.now())
                                        .build());
        Product p4 = addProduct(Product.builder()
                                        .name("노트북 스탠드")
                                        .description("자세 교정 및 발열 관리")
                                        .price(new BigDecimal("25000.00"))
                                        .stock(80)
                                        .category("액세서리")
                                        .createdAt(LocalDateTime.now())
                                        .updatedAt(LocalDateTime.now())
                                        .build());
        Product p5 = addProduct(Product.builder()
                                        .name("가죽 지갑")
                                        .description("고급스러운 디자인")
                                        .price(new BigDecimal("40000.00"))
                                        .stock(50)
                                        .category("패션")
                                        .createdAt(LocalDateTime.now())
                                        .updatedAt(LocalDateTime.now())
                                        .build());
        Product p6 = addProduct(Product.builder()
                                        .name("면 티셔츠")
                                        .description("부드러운 면 100%")
                                        .price(new BigDecimal("15000.00"))
                                        .stock(300)
                                        .category("의류")
                                        .createdAt(LocalDateTime.now())
                                        .updatedAt(LocalDateTime.now())
                                        .build());

        // 초기 판매량 설정 (예시), 상품 ID를 확인하여 매핑
        productSalesCount.put(p1.getProductId(), 100L);
        productSalesCount.put(p2.getProductId(), 120L);
        productSalesCount.put(p3.getProductId(), 80L);
        productSalesCount.put(p4.getProductId(), 90L);
        productSalesCount.put(p5.getProductId(), 70L);
        productSalesCount.put(p6.getProductId(), 150L);
    }

    // 상품 추가 시 ID 자동 할당 후 Map에 저장
    private Product addProduct(Product product) {
        Long newId = productIdCounter.incrementAndGet();
        product.setProductId(newId);
        products.put(newId, product);
        return product; // 추가된 상품 객체를 반환하여 ID를 알 수 있도록 함
    }

    @Override
    public Optional<Product> getProduct(Long productId) {
        System.out.println("Mock: Fetching product by ID: " + productId);
        return Optional.ofNullable(products.get(productId));
    }

    @Override
    public List<Product> getProductsByCategory(String category, int page, int size) {
        System.out.println("Mock: Fetching products by category '" + category + "', page " + page + ", size " + size);
        return products.values().stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .skip((long) page * size)
                .limit(size)
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> getAllProducts(int page, int size) {
        System.out.println("Mock: Fetching all products, page " + page + ", size " + size);
        return products.values().stream()
                .skip((long) page * size)
                .limit(size)
                .collect(Collectors.toList());
    }

    @Override
    public synchronized boolean decreaseStock(Long productId, Integer quantity) {
        System.out.println("Mock: Attempting to decrease stock for Product ID " + productId + " by " + quantity);
        Product product = products.get(productId);
        if (product == null || product.getStock() < quantity) {
            System.out.println("Mock: Stock decrease failed for Product ID " + productId + " (not found or insufficient stock).");
            return false;
        }
        product.setStock(product.getStock() - quantity);
        product.setUpdatedAt(LocalDateTime.now());
        // Map에 다시 put할 필요는 없습니다. 객체 참조를 통해 직접 변경됩니다.
        System.out.println("Mock: Product stock decreased for ID " + productId + ". New stock: " + product.getStock());
        return true;
    }

    @Override
    public synchronized void increaseStock(Long productId, Integer quantity) {
        System.out.println("Mock: Attempting to increase stock for Product ID " + productId + " by " + quantity);
        Product product = products.get(productId);
        if (product != null) {
            product.setStock(product.getStock() + quantity);
            product.setUpdatedAt(LocalDateTime.now());
            System.out.println("Mock: Product stock increased for ID " + productId + ". New stock: " + product.getStock());
        } else {
            System.out.println("Mock: Product not found for ID " + productId + ", cannot increase stock.");
        }
    }

    @Override
    public List<Product> getPopularProducts(int limit) {
        System.out.println("Mock: Fetching " + limit + " popular products.");
        return productSalesCount.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(limit)
                .map(entry -> products.get(entry.getKey()))
                .filter(java.util.Objects::nonNull)
                .collect(Collectors.toList());
    }

    // Mocking을 위한 판매량 업데이트 (주문 서비스에서 호출될 수 있음)
    // 이 메서드는 `decreaseStock` 등이 호출될 때 내부적으로 호출될 수 있습니다.
    public void addSalesCount(Long productId, long count) {
        productSalesCount.merge(productId, count, Long::sum);
        System.out.println("Mock: Product sales count updated for ID " + productId + ". New total sales: " + productSalesCount.get(productId));
    }

	@Override
    public synchronized Optional<Product> updateProductStock(Long productId, Integer newStock) {
        System.out.println("Mock: Attempting to update product stock for ID " + productId + " to " + newStock);
        Product product = products.get(productId);
        if (product == null) {
            System.out.println("Mock: Product not found for ID " + productId + ", cannot update stock.");
            return Optional.empty();
        }
        if (newStock < 0) {
            System.out.println("Mock: Invalid new stock value " + newStock + " for Product ID " + productId + ". Stock cannot be negative.");
            throw new IllegalArgumentException("재고 수량은 0 이상이어야 합니다.");
        }
        product.setStock(newStock);
        product.setUpdatedAt(LocalDateTime.now());
        System.out.println("Mock: Product " + productId + " stock updated to: " + product.getStock());
        return Optional.of(product);
    }

	@Override
    public synchronized Optional<Product> createProduct(Product product) {
        System.out.println("Mock: Attempting to create new product: " + product.getName());
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("상품 이름은 필수입니다.");
        }
        if (product.getPrice() == null || product.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("상품 가격은 0 이상이어야 합니다.");
        }
        if (product.getStock() == null || product.getStock() < 0) {
            throw new IllegalArgumentException("상품 재고는 0 이상이어야 합니다.");
        }

        // 중복 이름 체크 (간단한 Mock 로직)
        boolean nameExists = products.values().stream()
                                    .anyMatch(p -> p.getName().equalsIgnoreCase(product.getName()));
        if (nameExists) {
            throw new IllegalArgumentException("상품 이름이 이미 존재합니다: " + product.getName());
        }

        Long newId = productIdCounter.incrementAndGet();
        product.setProductId(newId);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        products.put(newId, product);
        System.out.println("Mock: Product created with ID: " + newId + ", Name: " + product.getName());
        return Optional.of(product);
    }

	@Override
    public synchronized void deleteProduct(Long productId) {
        System.out.println("Mock: Attempting to delete product with ID: " + productId);
        Product removedProduct = products.remove(productId);
        if (removedProduct == null) {
            System.out.println("Mock: Product not found for ID " + productId + ", cannot delete.");
            throw new IllegalArgumentException("삭제할 상품을 찾을 수 없습니다. Product ID: " + productId);
        }
        productSalesCount.remove(productId); // 판매량 데이터도 함께 삭제
        System.out.println("Mock: Product " + productId + " deleted successfully.");
    }
}

