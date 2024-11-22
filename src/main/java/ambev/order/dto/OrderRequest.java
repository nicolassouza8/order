package ambev.order.dto;

import lombok.Data;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Data
public class OrderRequest {

    @NotBlank(message = "O nome do cliente é obrigatório")
    private String customerName;

    @NotBlank(message = "O nome do produto é obrigatório")
    private String productName;

    @Min(value = 1, message = "A quantidade deve ser pelo menos 1")
    private int quantity;
}
