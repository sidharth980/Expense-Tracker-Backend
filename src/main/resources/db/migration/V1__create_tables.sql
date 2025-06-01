CREATE TABLE users (
    user_id SERIAL PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TYPE account_type AS ENUM ('SAVINGS', 'CREDIT_CARD');

CREATE TABLE accounts (
    account_id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL REFERENCES users(user_id),
    account_name VARCHAR(255) NOT NULL,
    account_type account_type NOT NULL,
    balance DECIMAL DEFAULT 0.00,
    credit_limit DECIMAL,
    statement_date INTEGER,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE expenses (
    expense_id SERIAL PRIMARY KEY,
    description VARCHAR(255) NOT NULL,
    amount DECIMAL NOT NULL,
    expense_date DATE NOT NULL,
    paid_by_user_id INTEGER NOT NULL REFERENCES users(user_id),
    account_id INTEGER REFERENCES accounts(account_id),
    category VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE expense_splits (
    split_id SERIAL PRIMARY KEY,
    expense_id INTEGER NOT NULL REFERENCES expenses(expense_id),
    user_id INTEGER NOT NULL REFERENCES users(user_id),
    owes_amount DECIMAL NOT NULL,
    is_settled BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
