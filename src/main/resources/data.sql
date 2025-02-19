INSERT INTO products(product_id, title, article, description, price, in_stock, sale, inventory)
SELECT 1, 'Медведь', 10001, 'Плюшевый медведь коричневого цвета', 13.99, true, 5, 4
WHERE NOT EXISTS(SELECT * FROM products WHERE article = 10001);
--
--INSERT INTO products(title, article, description, price, image, in_stock, sale)
--SELECT 'Котик', 10002, 'Флисовый котик рыжего цвета', 10.8, '/cat10002.png', true, 0
--WHERE NOT EXISTS(SELECT * FROM products WHERE article = 10002);
--
--INSERT INTO orders(cost, pay_method, status, address_id, comment, time_creation, user_id, uuid)
--VALUES( 23.15, 1, 1, null, 'Упаковать получше', TIMESTAMP '2025-01-13 15:25:30.123456789', null, 'f3f2e850-b5d4-11ef-ac7e-96584d5248b2');
--
--INSERT INTO order_product(order_id, product_id)
--SELECT 1, 1 WHERE NOT EXISTS(SELECT * FROM order_product WHERE order_id = 1);
--
--INSERT INTO order_product(order_id, product_id)
--SELECT 1, 2 WHERE NOT EXISTS(SELECT * FROM order_product WHERE order_id = 1);

--INSERT INTO address(country, zipCode, city, street, building, corpus, stroeniye, apartment)



--INSERT INTO products(title, article, description, price, image, in_stock, sale)
--VALUES ('Котик', 10002, 'Флисовый котик рыжего цвета', 10.8, '/cat10002.png', true, 0)
--ON CONFLICT (article) DO NOTHING;


--INSERT INTO orders(cost, payMethod, products, comment, timeCreation, status)
--SELECT 23.15, 'CARD', ..., 'Упаковать получше', TIMESTAMP '2025-01-13 15:25:30.123456789', 'PROCESSING');

--INSERT INTO orders(cost, pay_method, status, address_id, comment, time_creation, user_id, uuid)
--VALUES( 23.15, 1, 1, null, 'Упаковать получше', TIMESTAMP '2025-01-13 15:25:30.123456789', null, 'f3f2e850-b5d4-11ef-ac7e-96584d5248b2');