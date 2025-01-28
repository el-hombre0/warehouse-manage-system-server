INSERT INTO products(id, title, article, description, price, image)
SELECT 1, 'Медведь', 10001, 'Плюшевый медведь коричневого цвета', 13.99, '/bear10001'
WHERE NOT EXISTS(SELECT * FROM products WHERE article = 10001);