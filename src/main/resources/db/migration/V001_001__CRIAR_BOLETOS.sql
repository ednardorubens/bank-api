CREATE SCHEMA bank;

CREATE TABLE bank.tb_boletos (
    payment_date DATE,
    id UUID PRIMARY KEY,
    due_date DATE NOT NULL,
    customer VARCHAR(200) NOT NULL,
    fine DECIMAL(20, 2) DEFAULT 0.0,
    total_in_cents DECIMAL(20, 2) NOT NULL CHECK (total_in_cents >= 0),
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'PAID', 'CANCELED'))
);