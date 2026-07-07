CREATE TABLE instrument(
    id BIGSERIAL PRIMARY KEY,
    symbol VARCHAR(20) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    coingecko_id VARCHAR(100) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE
    );

CREATE TABLE price_point (
    id BIGSERIAL PRIMARY KEY,
    instrument_id BIGINT NOT NULL REFERENCES instrument(id),
    price NUMERIC(18,8) NOT NULL,
    volume_24h NUMERIC(24,8),
    change_24h_pct NUMERIC(10,4),
    ts TIMESTAMPTZ NOT NULL
    );

CREATE INDEX idx_price_point_instrument_ts ON price_point (instrument_id, ts);
