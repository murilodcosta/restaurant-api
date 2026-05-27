INSERT INTO restaurant_tables (number, description, capacity) VALUES
(1, 'Table next to the entrance', 4),
(2, 'Central table', 4),
(3, 'Table next to the window', 2),
(4, 'Family table', 6),
(5, 'Outdoor table', 4);

INSERT INTO product_categories (name) VALUES
('Starters'),
('Main Courses'),
('Drinks'),
('Desserts');

INSERT INTO products (category_id, name, description, price, prep_time_minutes) VALUES
((SELECT id FROM product_categories WHERE name = 'Starters'), 'French Fries', 'Portion of crispy french fries', 28.90, 15),
((SELECT id FROM product_categories WHERE name = 'Main Courses'), 'Cheeseburguer', 'Juicy cheeseburger with lettuce and tomato', 34.90, 20),
((SELECT id FROM product_categories WHERE name = 'Drinks'), 'Lemonade', 'Freshly squeezed lemonade', 12.00, 5),
((SELECT id FROM product_categories WHERE name = 'Desserts'), 'Chocolate Cake', 'Rich chocolate cake slice', 14.90, 5);

