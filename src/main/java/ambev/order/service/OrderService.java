package ambev.order.service;

import ambev.order.dto.OrderRequest;
import ambev.order.dto.OrderResponse;
import ambev.order.entity.Order;
import ambev.order.exception.DuplicateOrderException;
import ambev.order.repository.OrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public OrderResponse createOrder(OrderRequest orderRequest) {
        orderRepository.findByCustomerNameAndProductName(orderRequest.getCustomerName(), orderRequest.getProductName())
                .ifPresent(o -> {
                    throw new DuplicateOrderException("Já existe um pedido para este cliente e produto.");
                });

        Order order = new Order();
        order.setCustomerName(orderRequest.getCustomerName());
        order.setProductName(orderRequest.getProductName());
        order.setQuantity(orderRequest.getQuantity());

        Order savedOrder = orderRepository.save(order);
        return new OrderResponse(savedOrder.getId(), savedOrder.getCustomerName(),
                savedOrder.getProductName(), savedOrder.getQuantity(), savedOrder.getCreatedAt().toString());
    }

    public Page<OrderResponse> getOrders(Pageable pageable) {
        return orderRepository.findAll(pageable)
                .map(order -> new OrderResponse(
                        order.getId(),
                        order.getCustomerName(),
                        order.getProductName(),
                        order.getQuantity(),
                        order.getCreatedAt().toString()));
    }
}
