INSERT INTO products(title, article, description, price, image)
SELECT 'Медведь', 10001, 'Плюшевый медведь коричневого цвета', 13.99, '/bear10001.png'
WHERE NOT EXISTS(SELECT * FROM products WHERE article = 10001);