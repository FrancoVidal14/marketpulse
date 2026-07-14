CREATE TABLE price_alert (
                             id BIGSERIAL PRIMARY KEY,
                             user_id BIGINT NOT NULL REFERENCES app_user(id),
                             instrument_id BIGINT NOT NULL REFERENCES instrument(id),
                             condition VARCHAR(10) NOT NULL,
                             target_price NUMERIC(18,8) NOT NULL,
                             status VARCHAR(15) NOT NULL DEFAULT 'ACTIVE',
                             created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE alert_event (
                             id BIGSERIAL PRIMARY KEY,
                             alert_id BIGINT NOT NULL REFERENCES price_alert(id) ON DELETE CASCADE,
                             triggered_price NUMERIC(18,8) NOT NULL,
                             triggered_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);