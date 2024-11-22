package ambev.order.service;

import ambev.order.dto.OrderRequest;
import ambev.order.dto.OrderResponse;
import ambev.order.entity.Order;
import ambev.order.exception.DuplicateOrderException;
import ambev.order.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    private final OrderRepository orderRepository = Mockito.mock(OrderRepository.class);
    private final OrderService orderService = new OrderService(orderRepository);

    @Test
    void shouldCreateOrderSuccessfully() {
        OrderRequest request = new OrderRequest();
        request.setCustomerName("John Doe");
        request.setProductName("Laptop");
        request.setQuantity(1);

        when(orderRepository.findByCustomerNameAndProductName("John Doe", "Laptop"))
                .thenReturn(Optional.empty());

        Order savedOrder = new Order();
        savedOrder.setId(1L);
        savedOrder.setCustomerName("John Doe");
        savedOrder.setProductName("Laptop");
        savedOrder.setQuantity(1);

        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

        OrderResponse response = orderService.createOrder(request);

        assertEquals("John Doe", response.getCustomerName());
        assertEquals("Laptop", response.getProductName());
        assertEquals(1, response.getQuantity());
    }

    @Test
    void shouldThrowExceptionForDuplicateOrder() {
        Order existingOrder = new Order();
        existingOrder.setCustomerName("John Doe");
        existingOrder.setProductName("Laptop");

        when(orderRepository.findByCustomerNameAndProductName("John Doe", "Laptop"))
                .thenReturn(Optional.of(existingOrder));

        OrderRequest request = new OrderRequest();
        request.setCustomerName("John Doe");
        request.setProductName("Laptop");
        request.setQuantity(1);

        assertThrows(DuplicateOrderException.class, () -> orderService.createOrder(request));
    }

    @Test
    void shouldReturnPagedOrders() {
        Order order1 = new Order();
        order1.setCustomerName("John Doe");
        order1.setProductName("Laptop");
        order1.setQuantity(1);

        Order order2 = new Order();
        order2.setCustomerName("Jane Smith");
        order2.setProductName("Smartphone");
        order2.setQuantity(2);

        PageRequest pageable = PageRequest.of(0, 2);
        Page<Order> page = new PageImpl<>(Arrays.asList(order1, order2), pageable, 2);

        when(orderRepository.findAll(pageable)).thenReturn(page);

        Page<OrderResponse> responses = orderService.getOrders(pageable);

        assertEquals(2, responses.getTotalElements());
        assertEquals("John Doe", responses.getContent().get(0).getCustomerName());
        assertEquals("Laptop", responses.getContent().get(0).getProductName());
    }
}
