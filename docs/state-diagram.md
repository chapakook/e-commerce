# 🛒 E-Commerce State Diagram

## 📚 상태 다이어그램 (State Diagram)
- [🛍️ 상품 (Products)](#-상품-products)
- [🧾 주문 / 결재 (Orders & Payments)](#-주문--결재-orders--payments)
- [💵 결재 (Payments)](#-결재-payments)
- [🎟️ 쿠폰 (Coupons)](#-쿠폰-coupons)

## 🛍️ 상품 (Products)
```mermaid
stateDiagram-v2
    direction LR
    state ON_SALE {
        direction LR
        [*] --> AVAILABLE
        AVAILABLE --> OUT_OF_STOCK : 재고 없음
        OUT_OF_STOCK --> AVAILABLE : 재고 보충
        OUT_OF_STOCK --> [*]
    }
    
    [*] --> CREATED : 상품 등록
    
    CREATED --> ON_SALE : 총 재고 ≥ 1
    CREATED --> SOLD_OUT : 총 재고 = 0
    SOLD_OUT --> ON_SALE : 총 재고 ≥ 1
    ON_SALE --> SOLD_OUT : 총 재고 = 0

    CREATED --> DISCONTINUED : 판매 중지
    SOLD_OUT --> DISCONTINUED : 판매 중지
    ON_SALE --> DISCONTINUED : 판매 중지
    
    DISCONTINUED --> [*]
```
### 📝 설명 (Description)
- 상품은 `등록(CREATED)`, `판매중(ON_SALE)`, `품절(SOLD_OUT)`, `판매중지(DISCONTINUED)` 상태를 가집니다.
- `판매중(ON_SALE)` 상태는 다시 `재고있음(AVAILABLE)`과 `재고없음(OUT_OF_STOCK)` 상태로 세분화됩니다.
- 상품은 `총 재고`에 따라 상태가 전이됩니다.
- `판매중지(DISCONTINUED)` 상태는 상품이 더 이상 판매되지 않음을 나타내며, 이 상태에서는 다른 상태로 전이되지 않습니다.
- `판매중지(DISCONTINUED)` 상태의 상품을 재판매 하려면 새로 등록해야 합니다.

## 🧾 주문 / 결재 (Orders & Payments)
```mermaid
stateDiagram-v2
    direction LR
    [*] --> CREATED : 주문 생성
    CREATED --> PENDING : 결재 대기

    state PENDING {
        direction LR
        [*] --> REQUESTED : 결재 요청
        REQUESTED --> SUCCEEDED : 결재 성공
        REQUESTED --> FAILED : 결재 실패
        SUCCEEDED --> [*]
        FAILED -->[*]
    }

    CREATED --> CANCELED : 주문 취소
    PENDING --> CANCELED : 주문 취소
    PENDING --> CANCELED : 결재 실패

    PENDING --> COMPLETED : 결재 완료
    COMPLETED --> [*]
    CANCELED --> [*]
```
### 📝 설명 (Description)
- 주문은 `생성(CREATED)`, `결재 대기(PENDING)`, `결재 완료(COMPLETED)`, `취소(CANCELED)` 상태를 가집니다.
- `결재 대기(PENDING)` 상태는 다시 `결재 요청(REQUESTED)`, `결재 성공(SUCCEEDED)`, `결재 실패(FAILED)` 상태로 세분화됩니다.
- 주문은 `결재 완료(COMPLETED)` 또는 `취소(CANCELED)` 상태로 전이됩니다.
- `취소(CANCELED)` 상태는 주문이 더 이상 진행되지 않음을 나타내며, 이 상태에서는 다른 상태로 전이되지 않습니다.
- `취소(CANCELED)` 상태의 주문을 다시 진행하려면 새로 주문해야 합니다.

## 🎟️ 쿠폰 (Coupons)
```mermaid
stateDiagram-v2
    direction LR
    [*] --> ACTIVE : 쿠폰 발급
    ACTIVE --> USED : 쿠폰 사용
    USED --> ACTIVE : 쿠폰 사용 취소
    ACTIVE --> EXPIRED : 쿠폰 만료
    USED --> [*]
    EXPIRED --> [*]
```
### 📝 설명 (Description)
- 쿠폰은 `활성(ACTIVE)`, `사용됨(USED)`, `만료(EXPIRED)` 상태를 가집니다.
- 쿠폰은 `사용됨(USED)` 또는 `만료(EXPIRED)` 상태로 전이됩니다.
- 쿠폰은 발급 시점부터 유효기간이 지나면 자동으로 `만료(EXPIRED)` 상태로 전이됩니다.
- `만료(EXPIRED)` 상태는 쿠폰이 더 이상 사용되지 않음을 나타내며, 이 상태에서는 다른 상태로 전이되지 않습니다.
- `만료(EXPIRED)` 상태의 쿠폰을 다시 사용하려면 새로 발급받아야 합니다. (⚠️ 재발급 정책은 별도 관리)
- `사용됨(USED)` 상태의 쿠폰은 사용 취소 시 다시 `활성(ACTIVE)` 상태로 돌아갈 수 있습니다.
- 쿠폰 사용 취소는 주문이 취소되거나 환불되는 경우에만 가능합니다.
