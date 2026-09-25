CREATE TABLE IF NOT EXISTS tb_booking (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    client_name VARCHAR(100) NOT NULL,
    client_email VARCHAR(30) NOT NULL,
    client_phone VARCHAR(20) NOT NULL,
    start_time TIMESTAMP(3) NOT NULL,
    end_time TIMESTAMP(3) NOT NULL,
    status VARCHAR(30) NOT NULL,
    notes TEXT,
    service_item_id UUID NOT NULL REFERENCES tb_service_item(id) ON DELETE CASCADE,
    provider_id UUID NOT NULL REFERENCES tb_user(id) ON DELETE CASCADE,
    created_at TIMESTAMP(3) DEFAULT now(),
    updated_at TIMESTAMP(3) DEFAULT now(),

    CONSTRAINT chk_status CHECK (status IN ('COMPLETED', 'CONFIRMED', 'CANCELLED'))
)