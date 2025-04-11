CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       name VARCHAR(100) NOT NULL,
                       email VARCHAR(100) UNIQUE NOT NULL,
                       password_hash VARCHAR(255),
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT current_timestamp,
                       avatar_url VARCHAR(255),
                       bio VARCHAR(255)
);

CREATE TABLE tags (
                      id SERIAL PRIMARY KEY,
                      name VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE posts (
                       id SERIAL PRIMARY KEY,
                       title VARCHAR(255) NOT NULL,
                       content TEXT NOT NULL,
                       user_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                       published boolean,
                       slug VARCHAR(255),
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT current_timestamp
);

CREATE TABLE post_tags (
                           post_id INTEGER NOT NULL REFERENCES posts(id) ON DELETE CASCADE,
                           tag_id INTEGER NOT NULL REFERENCES tags(id) ON DELETE CASCADE,
                           PRIMARY KEY (post_id, tag_id)
);
