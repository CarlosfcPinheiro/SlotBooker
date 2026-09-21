CREATE TABLE IF NOT EXISTS tb_service_item (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    price NUMERIC(10, 2) NOT NULL,
    duration_minutes INT NOT NULL,
    provider_id UUID NOT NULL REFERENCES tb_usuario(id) ON DELETE CASCADE,
    active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP(3) DEFAULT now(),
    updated_at TIMESTAMP(3) DEFAULT now(),

    CONSTRAINT chk_price CHECK (price > 0),
    CONSTRAINT chk_duration_minutes CHECK (duration_minutes > 0)
)