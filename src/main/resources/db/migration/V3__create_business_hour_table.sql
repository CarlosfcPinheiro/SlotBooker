CREATE TABLE IF NOT EXISTS tb_business_hour (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    day_of_week INT NOT NULL,
    open_time TIME NOT NULL,
    close_time TIME NOT NULL,
    provider_id UUID NOT NULL REFERENCES tb_user(id) ON DELETE CASCADE
)