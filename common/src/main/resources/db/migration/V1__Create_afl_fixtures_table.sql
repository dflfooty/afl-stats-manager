create table afl_fixtures (
    id SERIAL PRIMARY KEY NOT NULL,
    round INTEGER,
    game INTEGER,
    home_team VARCHAR(4),
    away_team VARCHAR(4),
    ground VARCHAR(4),
    start_time TIMESTAMP,
    timezone VARCHAR,
    end_time TIMESTAMP,
    stats_last_download TIMESTAMP,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);