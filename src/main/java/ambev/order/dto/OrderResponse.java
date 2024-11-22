package ambev.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderResponse {
    private Long id;
    private String customerName;
    private String productName;
    private int quantity;
    private String createdAt;
}
