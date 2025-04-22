-- SCHEMA (MIGHT CHANGE BASED ON PROJECT DESIGN) --

-- Players table
CREATE TABLE players (
                         id VARCHAR(36) PRIMARY KEY,
                         username VARCHAR(50) UNIQUE NOT NULL,
                         password_hash VARCHAR(255) NOT NULL,
                         wins INT DEFAULT 0,
                         losses INT DEFAULT 0,
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Admins table
CREATE TABLE admins (
                        id VARCHAR(36) PRIMARY KEY,
                        username VARCHAR(50) UNIQUE NOT NULL,
                        password_hash VARCHAR(255) NOT NULL,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Games table
CREATE TABLE games (
                       id VARCHAR(36) PRIMARY KEY,
                       player1_id VARCHAR(36) NOT NULL,
                       player2_id VARCHAR(36),
                       current_round INT DEFAULT 1,
                       player1_wins INT DEFAULT 0,
                       player2_wins INT DEFAULT 0,
                       state VARCHAR(20) NOT NULL,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       FOREIGN KEY (player1_id) REFERENCES players(id),
                       FOREIGN KEY (player2_id) REFERENCES players(id)
);

-- Game rounds table
CREATE TABLE game_rounds (
                             id VARCHAR(36) PRIMARY KEY,
                             game_id VARCHAR(36) NOT NULL,
                             round_number INT NOT NULL,
                             word VARCHAR(50) NOT NULL,
                             player1_guesses VARCHAR(255),
                             player2_guesses VARCHAR(255),
                             winner_id VARCHAR(36),
                             completed_at TIMESTAMP,
                             FOREIGN KEY (game_id) REFERENCES games(id),
                             FOREIGN KEY (winner_id) REFERENCES players(id)
);

-- Game settings table
CREATE TABLE game_settings (
                               id INT PRIMARY KEY DEFAULT 1,
                               wait_time_seconds INT NOT NULL DEFAULT 10,
                               round_duration_seconds INT NOT NULL DEFAULT 30,
                               CONSTRAINT single_row CHECK (id = 1)
);