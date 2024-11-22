package ambev.order.repository;

import ambev.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByCustomerNameAndProductName(String customerName, String productName);
}
