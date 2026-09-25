CREATE TABLE IF NOT EXISTS tb_user (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100) NOT NULL,
    email VARCHAR(30) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(11) NOT NULL,
    role VARCHAR(30) NOT NULL,
    created_at TIMESTAMP(3) DEFAULT now(),
    updated_at TIMESTAMP(3) DEFAULT now(),

    CONSTRAINT chk_role CHECK (role IN ('PROVIDER',  'ADMIN'))
);