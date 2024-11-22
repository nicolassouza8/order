package ambev.order.controller;

import ambev.order.dto.OrderRequest;
import ambev.order.entity.Order;
import ambev.order.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderRepository orderRepository;

    @BeforeEach
    void setup() {
        orderRepository.deleteAll();
    }

    @Test
    void shouldCreateOrderSuccessfully() throws Exception {
        OrderRequest request = new OrderRequest();
        request.setCustomerName("Juliana Torres");
        request.setProductName("iPad");
        request.setQuantity(1);

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                        "customerName": "Juliana Torres",
                        "productName": "iPad",
                        "quantity": 1
                    }
                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerName").value("Juliana Torres"))
                .andExpect(jsonPath("$.productName").value("iPad"))
                .andExpect(jsonPath("$.quantity").value(1));
    }

    @Test
    void shouldReturnConflictForDuplicateOrder() throws Exception {
        Order order = new Order();
        order.setCustomerName("Nicolas Souza");
        order.setProductName("Notebook");
        order.setQuantity(1);
        orderRepository.save(order);

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                        "customerName": "Nicolas Souza",
                        "productName": "Notebook",
                        "quantity": 1
                    }
                """))
                .andExpect(status().isConflict())
                .andExpect(content().string("Já existe um pedido para este cliente e produto."));
    }

    @Test
    void shouldReturnPagedOrders() throws Exception {
        for (int i = 1; i <= 5; i++) {
            Order order = new Order();
            order.setCustomerName("Cliente " + i);
            order.setProductName("Produto " + i);
            order.setQuantity(i);
            orderRepository.save(order);
        }

        mockMvc.perform(get("/api/orders")
                        .param("page", "0")
                        .param("size", "2")
                        .param("sort", "customerName,asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].customerName").value("Cliente 1"))
                .andExpect(jsonPath("$.content[1].customerName").value("Cliente 2"));
    }
}
