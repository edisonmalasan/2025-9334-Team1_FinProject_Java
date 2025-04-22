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
