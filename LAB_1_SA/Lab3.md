```mermaid
classDiagram
    class Customer {
        -String name
        -String phone
        -String email
        -Address address
        -CustomerStatus status
        +upgradeStatus(): void
    }

    class Address {
        -String street
        -String city
        -String zip
    }

    class CustomerStatus {
        <<enumeration>>
        BRONZE
        SILVER
        GOLD
        PLATINUM
    }

    class Tool {
        -String name
        -int quantityAvailable
        -ToolCategory category
        -String locationCode
        -List<Price> prices
        -Supplier supplier
        +isAvailable(): boolean
    }

    class ToolCategory {
        -String name
    }

    class Supplier {
        -String name
        -String phone
        -String email
    }

    class Price {
        -PricingType type
        -Money amount
    }

    class PricingType {
        <<enumeration>>
        PER_HOUR
        PER_HALF_DAY
        PER_DAY
        PER_WEEK
    }

    class Money {
        -BigDecimal amount
        -Currency currency
        +convertTo(currency): Money
    }

    class Currency {
        -String code
        -String symbol
    }

    class Rental {
        -Date startDateTime
        -Date endDateTime
        -Tool tool
        -Customer customer
        -Money computedPrice
        +computeTotalPrice(): Money
        +computeDuration(): Duration
    }

    class Payment {
        -Money amount
        -Date paymentDate
        -PaymentMethod method
    }

    class PaymentMethod {
        <<enumeration>>
        CASH
        CREDIT_CARD
        DEBIT_CARD
        PAYPAL
    }

    class PricingService {
        <<domain service>>
        +calculateRentalPrice(Rental, Customer, ToolCategory): Money
    }

    class RentalCreatedEvent {
        <<domain event>>
        -Rental rental
        -Date occurredOn
    }

    Customer "1" --> "*" Rental : rents
    Rental "*" --> "1" Tool : includes
    Tool "*" --> "1" ToolCategory : belongs to
    Tool "1" --> "1" Supplier : supplied by
    Rental "1" --> "*" Payment : paid by
    Customer "1" --> "1" Address : has
    Tool "1" --> "*" Price : offers

```

```mermaid
sequenceDiagram
    participant Customer
    participant WebUI
    participant OrderController
    participant OrderApplicationService
    participant PaymentService
    participant EmailService
    participant OrderRepository

    Customer->>WebUI: Click "Place Order"
    WebUI->>OrderController: POST /orders (cart, shipping info, credit card)
    OrderController->>OrderApplicationService: placeOrder(request)
    OrderApplicationService->>PaymentService: processPayment(order, creditCard)
    PaymentService-->>OrderApplicationService: paymentSuccess = true
    OrderApplicationService->>OrderRepository: save(order)
    OrderApplicationService->>EmailService: sendOrderConfirmation(customer, order)
    EmailService-->>Customer: send confirmation email
    OrderApplicationService-->>OrderController: return OrderPlacedEvent
    OrderController-->>WebUI: show “Order confirmed”

```

# Lab4

```flow
graph TD
  C[Customer: Frank Brown<br/>email: fbrown@gmail.com] -->|PLACED| O[Order #122435<br/>total: 5160.00<br/>date: 2021-11-09]
  O -->|CONTAINS<br/>qty:2| P1[Product: iPhone 12<br/>A546]
  O -->|CONTAINS<br/>qty:4| P2[Product: Samsung Galaxy 12S<br/>S333]

```

```mermaid
graph TD
  C[Customer: Frank Brown<br/>email: fbrown@gmail.com] -->|PLACED| O[Order #122435<br/>total: 5160.00<br/>date: 2021-11-09]
  O -->|CONTAINS<br/>qty:2| P1[Product: iPhone 12<br/>A546]
  O -->|CONTAINS<br/>qty:4| P2[Product: Samsung Galaxy 12S<br/>S333]

```

