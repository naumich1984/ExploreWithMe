CREATE TABLE IF NOT EXISTS  categories (
    category_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(50) NOT NULL,
    CONSTRAINT pk_category PRIMARY KEY (category_id),
    CONSTRAINT uq_category_name UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS  users (
    user_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    email VARCHAR(254) NOT NULL,
    name VARCHAR(250) NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (user_id),
    CONSTRAINT uq_user_email UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS  events (
     event_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
     annotation VARCHAR(2000) ,
     category_id BIGINT NOT NULL,
     description VARCHAR(7000) NOT NULL,
     created_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     event_date TIMESTAMP NOT NULL,
     user_id BIGINT NOT NULL,
     paid BOOLEAN NOT NULL,
     participant_limit INTEGER DEFAULT 10,
     published_on TIMESTAMP,
     request_moderation BOOLEAN NOT NULL,
     state VARCHAR(16) NOT NULL,
     title VARCHAR(120) NOT NULL,
     lat FLOAT NOT NULL,
     lon FLOAT NOT NULL,
     CONSTRAINT pk_event PRIMARY KEY (event_id),
     CONSTRAINT fk_events_to_categories FOREIGN KEY(category_id) REFERENCES categories(category_id) on delete restrict,
     CONSTRAINT fk_events_to_users FOREIGN KEY(user_id) REFERENCES users(user_id) on delete cascade
);

CREATE TABLE IF NOT EXISTS  compilations (
    compilation_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    title VARCHAR(50) NOT NULL,
    pinned BOOLEAN NOT NULL,
    CONSTRAINT pk_compilation PRIMARY KEY (compilation_id)
);

CREATE TABLE IF NOT EXISTS  compilations_events (
     c_e_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
     compilation_id BIGINT NOT NULL,
     event_id BIGINT NOT NULL,
     CONSTRAINT pk_compilation_event PRIMARY KEY (c_e_id),
     CONSTRAINT fk_c_e_to_compilations FOREIGN KEY(compilation_id) REFERENCES compilations(compilation_id) on delete cascade,
     CONSTRAINT fk_c_e_to_events FOREIGN KEY(event_id) REFERENCES events(event_id) on delete cascade
);

CREATE TABLE IF NOT EXISTS requests (
    request_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    event_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    status VARCHAR(16),
    CONSTRAINT pk_request PRIMARY KEY (request_id),
    CONSTRAINT fk_requests_to_events FOREIGN KEY(event_id) REFERENCES events(event_id) on delete cascade,
    CONSTRAINT fk_requests_to_users FOREIGN KEY(user_id) REFERENCES users(user_id) on delete cascade
);

CREATE TABLE IF NOT EXISTS  comments (
    comment_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    changed TIMESTAMP,
    user_id BIGINT NOT NULL,
    event_id BIGINT NOT NULL,
    text VARCHAR(512) NOT NULL,
    CONSTRAINT pk_comment PRIMARY KEY (comment_id),
    CONSTRAINT fk_comments_to_events FOREIGN KEY(event_id) REFERENCES events(event_id) on delete cascade,
    CONSTRAINT fk_comments_to_users FOREIGN KEY(user_id) REFERENCES users(user_id) on delete cascade
);