CREATE TABLE customer (
    uuid UUID PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    date_of_birth DATE NOT NULL,
);

CREATE TABLE account (
    uuid UUID PRIMARY KEY,
    customer_uuid UUID,
    account_number VARCHAR(20) UNIQUE NOT NULL,
    balance DECIMAL(15, 2) NOT NULL,
    FOREIGN KEY (customer_uuid) REFERENCES Customer(uuid)
);

CREATE TABLE transaction (
    uuid UUID PRIMARY KEY,
    from_account_uuid UUID,
    to_account_uuid UUID,
    amount DECIMAL(15, 2) NOT NULL,
    transaction_date TIMESTAMP,
    FOREIGN KEY (from_account_uuid) REFERENCES Account(uuid),
    FOREIGN KEY (to_account_uuid) REFERENCES Account(uuid)
);
