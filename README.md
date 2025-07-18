# [E-Commerce 시스템]

## 프로젝트 소개

이 프로젝트는 서버구축을 위한 설계 작업을 위한 프로젝트 입니다.

## 주요 기능

* 사용자 및 잔액 관리
* 상품 관리
* 쿠폰 관리
* 주문 및 결제

## 시스템 아키텍처 및 설계


### ERD (Entity-Relationship Diagram)
![ERD 다이어그램](docs/erd/e-commerce-erd.png)

### 클래스 다이어그램

**사용자 잔액 관리:**
![사용자 잔액 관리](https://github.com/eodnjsdl-3254/server-java/blob/main/docs/class-diagram/01.%EC%82%AC%EC%9A%A9%EC%9E%90_%EC%9E%94%EC%95%A1%20%EA%B4%80%EB%A6%AC_%ED%81%B4%EB%9E%98%EC%8A%A4%EB%8B%A4%EC%9D%B4%EC%96%B4%EA%B7%B8%EB%9E%A8.png)

**상품 관리:**
![상품 관리](https://github.com/eodnjsdl-3254/server-java/blob/main/docs/class-diagram/02.%EC%83%81%ED%92%88%EA%B4%80%EB%A6%AC_%ED%81%B4%EB%9E%98%EC%8A%A4%EB%8B%A4%EC%9D%B4%EC%96%B4%EA%B7%B8%EB%9E%A8.png)

**쿠폰 관리:**
![쿠폰 관리](https://github.com/eodnjsdl-3254/server-java/blob/main/docs/class-diagram/03.%EC%BF%A0%ED%8F%B0%EA%B4%80%EB%A6%AC_%ED%81%B4%EB%9E%98%EC%8A%A4%EB%8B%A4%EC%9D%B4%EC%96%B4%EA%B7%B8%EB%9E%A8.png)

**주문/결제:**
![주문/결제](https://github.com/eodnjsdl-3254/server-java/blob/main/docs/class-diagram/04.%EC%A3%BC%EB%AC%B8%EA%B2%B0%EC%A0%9C_%ED%81%B4%EB%9E%98%EC%8A%A4%EB%8B%A4%EC%9D%B4%EC%96%B4%EA%B7%B8%EB%9E%A8.png)

### 시퀀스 다이어그램

핵심 비즈니스 흐름에 대한 시퀀스 다이어그램입니다.

**주문/결제 시퀀스:**
![주문/결제 시퀀스 다이어그램 이미지](https://github.com/eodnjsdl-3254/server-java/blob/main/docs/sequence-diagram/%EC%A3%BC%EB%AC%B8_%EA%B2%B0%EC%A0%9C_%EC%8B%9C%ED%80%80%EC%8A%A4%EB%8B%A4%EC%9D%B4%EC%96%B4%EA%B7%B8%EB%9E%A8.png)
**선착순 쿠폰 발급 시퀀스:**
![선착순 쿠폰 발급 시퀀스](https://github.com/eodnjsdl-3254/server-java/blob/main/docs/sequence-diagram/%EC%84%A0%EC%B0%A9%EC%88%9C%EC%BF%A0%ED%8F%B0_%EB%B0%9C%EA%B8%89_%EC%8B%9C%ED%80%80%EC%8A%A4%EB%8B%A4%EC%9D%B4%EC%96%B4%EA%B7%B8%EB%9E%A8.png)
**잔액 충전 시퀀스:** 
![잔액 충전 시퀀스](https://github.com/eodnjsdl-3254/server-java/blob/main/docs/sequence-diagram/%EC%9E%94%EC%95%A1_%EC%B6%A9%EC%A0%84_%EC%8B%9C%ED%80%80%EC%8A%A4%EB%8B%A4%EC%9D%B4%EC%96%B4%EA%B7%B8%EB%9E%A8.png)
**상품 조회 시퀀스:**
![상품 조회 시퀀스](https://github.com/eodnjsdl-3254/server-java/blob/main/docs/sequence-diagram/%EC%83%81%ED%92%88_%EC%A1%B0%ED%9A%8C_%EC%8B%9C%ED%80%80%EC%8A%A4%EB%8B%A4%EC%9D%B4%EC%96%B4%EA%B7%B8%EB%9E%A8.png)
