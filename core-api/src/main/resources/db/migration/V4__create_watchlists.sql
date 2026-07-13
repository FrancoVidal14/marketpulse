CREATE TABLE watchlist (
                           id BIGSERIAL PRIMARY KEY,
                           user_id BIGINT NOT NULL REFERENCES app_user(id),
                           name VARCHAR(100) NOT NULL,
                           created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE watchlist_item (
                                id BIGSERIAL PRIMARY KEY,
                                watchlist_id BIGINT NOT NULL REFERENCES watchlist(id) ON DELETE CASCADE,
                                instrument_id BIGINT NOT NULL REFERENCES instrument(id),
                                CONSTRAINT uq_watchlist_instrument UNIQUE (watchlist_id, instrument_id)
);