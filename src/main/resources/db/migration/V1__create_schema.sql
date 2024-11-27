-- Criação da tabela principal
CREATE TABLE orders (
                        id SERIAL PRIMARY KEY, -- Chave primária com auto incremento
                        customer_name VARCHAR(255) NOT NULL, -- Nome do cliente
                        product_name VARCHAR(255) NOT NULL, -- Nome do produto
                        quantity INT NOT NULL CHECK (quantity > 0), -- Quantidade (deve ser maior que 0)
                        order_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP -- Data do pedido
);

-- Índice único para evitar duplicação de pedidos com base no cliente e no produto
CREATE UNIQUE INDEX unique_customer_product_order
    ON orders (customer_name, product_name);