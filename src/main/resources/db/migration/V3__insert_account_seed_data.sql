-- Insert accounts for user1 (id: 1)
INSERT INTO accounts (user_id, account_name, account_type, balance) VALUES (1, 'Savings Account', 'SAVINGS', 1000.00);
INSERT INTO accounts (user_id, account_name, account_type, balance, credit_limit, statement_date) VALUES (1, 'Credit Card', 'CREDIT_CARD', 0.00, 5000.00, 15);

-- Insert accounts for user2 (id: 2)
INSERT INTO accounts (user_id, account_name, account_type, balance) VALUES (2, 'Checking Account', 'SAVINGS', 2500.00);
INSERT INTO accounts (user_id, account_name, account_type, balance, credit_limit, statement_date) VALUES (2, 'Another Credit Card', 'CREDIT_CARD', 0.00, 10000.00, 02);
