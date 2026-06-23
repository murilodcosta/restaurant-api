ALTER TABLE order_items
ADD COLUMN preparation_start_date TIMESTAMP,
ADD COLUMN preparation_end_date TIMESTAMP,
ADD COLUMN delivery_date TIMESTAMP;