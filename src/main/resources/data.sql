INSERT INTO products(title, article, description, price, image, in_stock, sale)
SELECT 'Медведь', 10001, 'Плюшевый медведь коричневого цвета', 13.99, '/bear10001.png', true, 5
WHERE NOT EXISTS(SELECT * FROM products WHERE article = 10001);

--INSERT INTO orders(cost, payMethod, products, comment, timeCreation, status)
--SELECT 23.15, 'CARD', ..., 'Упаковать получше', TIMESTAMP '2025-01-13 15:25:30.123456789', 'PROCESSING');