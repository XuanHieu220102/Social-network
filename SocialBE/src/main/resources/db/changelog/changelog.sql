-- liquibase formatted sql

-- changeset hieudx:1
CREATE TABLE users
(
    id              VARCHAR(255) PRIMARY KEY,
    username        VARCHAR(50) UNIQUE  NOT NULL,
    email           VARCHAR(100) UNIQUE NOT NULL,
    password        VARCHAR(255)        NOT NULL,
    age             INTEGER,
    full_name       VARCHAR(100),
    avatar_url      VARCHAR(255),
    education_level VARCHAR(255),
    gender          INTEGER,
    status          INTEGER             NOT NULL,
    address         VARCHAR(255),
    phone_number    VARCHAR(255),
    description     VARCHAR(255),
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- changeset hieudx:2
ALTER TABLE users
    ADD COLUMN access_token_reset_at timestamp;

-- changeset hieudx:3
ALTER TABLE users
    ADD COLUMN failed_login_count integer;

-- changeset hieudx:4
CREATE TABLE posts
(
    post_id    VARCHAR(255),
    user_id    VARCHAR(255),
    content    TEXT NOT NULL,
    image_url  VARCHAR(255),
    status     integer,
    deleted    BOOLEAN,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

-- changeset hieudx:5
ALTER TABLE users
    ADD COLUMN date_of_birth date;

-- changeset hieudx:6
CREATE TABLE friendships
(
    id VARCHAR(255) PRIMARY KEY,
    user_id VARCHAR(255) NOT NULL ,
    friend_id VARCHAR(255) NOT NULL ,
    status integer,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP
);

-- changeset hieudx:7
CREATE INDEX idx_friendships_user_id ON friendships(user_id);
CREATE INDEX idx_friendships_friend_id ON friendships(friend_id);
CREATE INDEX idx_friendships_status ON friendships(status);
CREATE INDEX idx_friendships_user_friend ON friendships(user_id, friend_id);

-- changeset hieudx:8
ALTER TABLE users
    ADD COLUMN list_images jsonb;

-- changeset post:1
ALTER TABLE posts
    ADD COLUMN tag jsonb;
ALTER TABLE posts
    ADD COLUMN hashtag jsonb;
ALTER TABLE posts
    ADD COLUMN likes INTEGER;
ALTER TABLE posts
    ADD COLUMN comments INTEGER;

-- changeset hieudx:100
ALTER TABLE users
    ADD COLUMN countLikes integer;
ALTER TABLE users
    ADD COLUMN countFollows integer;

-- changeset hieudx:101
ALTER TABLE users
    RENAME COLUMN countLikes TO count_likes;
ALTER TABLE users
    RENAME COLUMN countFollows TO count_follows;

-- changeset hieudx:102
CREATE TABLE likes
(
    id VARCHAR(255) PRIMARY KEY,
    user_id VARCHAR(255) NOT NULL ,
    post_id VARCHAR(255) NOT NULL ,
    status integer,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP
);

-- changeset hieudx:103
CREATE INDEX idx_likes_post_status ON likes(post_id, status);

-- changeset hieudx:104
CREATE TABLE comments (
     id VARCHAR(255) PRIMARY KEY,
     user_id VARCHAR(255) NOT NULL ,
     post_id VARCHAR(255) NOT NULL ,
     content VARCHAR(255),
     status INTEGER,
     parent_comment_id VARCHAR(255),
     created_at TIMESTAMP DEFAULT NOW(),
     updated_at TIMESTAMP DEFAULT NOW()
);

-- changeset hieudx:105
ALTER TABLE comments
    ADD COLUMN deleted BOOLEAN;

-- changeset hieudx:106
ALTER TABLE comments
    ADD COLUMN likes integer;

-- changeset hieudx:107
ALTER TABLE posts
    ALTER COLUMN image_url TYPE jsonb
    USING image_url::jsonb;


-- changeset hieudx:108
CREATE TABLE groups (
     id VARCHAR(255) PRIMARY KEY,
     name VARCHAR(255) NOT NULL,
     description VARCHAR(255),
     profile_image VARCHAR(255),
     cover_image VARCHAR(255),
     privacy_status integer,
     created_by VARCHAR(255) NOT NULL,
     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- changeset hieudx:109
CREATE TABLE group_members (
    id VARCHAR(255) PRIMARY KEY ,
    group_id VARCHAR(255),
    user_id VARCHAR(255),
    role VARCHAR(50) CHECK (role IN ('member', 'admin', 'moderator')) DEFAULT 'member',
    joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- changeset hieudx:110
CREATE TABLE pages (
    id VARCHAR(255) PRIMARY KEY ,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    profile_image VARCHAR(255),
    cover_image VARCHAR(255),
    category VARCHAR(255),
    created_by VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- changeset hieudx:111
CREATE TABLE page_followers (
    id VARCHAR(255) PRIMARY KEY ,
    page_id VARCHAR(255) NOT NULL ,
    user_id VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- changeset hieudx:112
ALTER TABLE posts
    ADD COLUMN shares integer;
ALTER TABLE posts
    ADD COLUMN type VARCHAR(50);

-- changeset hieudx:113
CREATE TABLE shares (
    id VARCHAR(255) PRIMARY KEY,
    user_id VARCHAR(255) NOT NULL ,
    post_id VARCHAR(255) NOT NULL ,
    content VARCHAR(255),
    status INTEGER,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

-- changeset hieudx:114
ALTER TABLE page_followers
    ADD COLUMN type VARCHAR(255);

-- changeset hieudx:115
ALTER TABLE pages
    ADD COLUMN deleted BOOLEAN;

-- changeset hieudx:116
ALTER TABLE pages
    ADD COLUMN regime integer;


-- changeset hieudx:117
DROP TABLE shares;

-- changeset hieudx:118
ALTER TABLE posts
    DROP COLUMN shares;

-- changeset hieudx:119
ALTER TABLE posts
    ADD COLUMN share_info jsonb;

-- changeset hieudx:120
ALTER TABLE page_followers
    RENAME COLUMN type TO page_role;

-- changeset hieudx:121
ALTER TABLE page_followers
    ADD COLUMN status integer;

-- changeset hieudx:122
ALTER TABLE groups
    ADD COLUMN deleted boolean;

-- changeset hieudx:123
ALTER TABLE group_members
    ADD COLUMN status integer;

-- changeset hieudx:124
ALTER TABLE posts
    ADD COLUMN privacy integer;

-- changeset hieudx:125
ALTER TABLE posts
    ADD COLUMN type_file VARCHAR(255);

-- changeset hieudx:126
ALTER TABLE comments
    ADD COLUMN reply jsonb;

-- changeset hieudx:127
ALTER TABLE comments
    DROP COLUMN parent_comment_id;

-- changeset hieudx:128
CREATE TABLE notification (
    id VARCHAR(255) PRIMARY KEY,
    receive_id VARCHAR(255) NOT NULL,
    sender_id VARCHAR(255),
    post_id VARCHAR(255),
    type VARCHAR(50) NOT NULL,
    message TEXT NOT NULL,
    data JSONB DEFAULT '{}'::jsonb,
    is_read BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMPTZ DEFAULT NOW()
);

-- changeset hieudx:129
CREATE TABLE Stories (
    id VARCHAR(255) PRIMARY KEY,
    user_id VARCHAR(50),
    content VARCHAR(255),
    video_url VARCHAR(255),
    background_color VARCHAR(20),
    is_active BOOLEAN DEFAULT TRUE,
    type VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP
);

-- changeset hieudx:130
CREATE TABLE conversations (
    id VARCHAR(255) PRIMARY KEY,
    type VARCHAR(50) NOT NULL CHECK (type IN ('PRIVATE', 'GROUP')),
    name VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Tạo index để tối ưu truy vấn
CREATE INDEX idx_conversations_type ON conversations(type);
CREATE INDEX idx_conversations_created_at ON conversations(created_at);

-- changeset hieudx:131
CREATE TABLE conversation_participants (
    id VARCHAR(255) PRIMARY KEY,
    conversation_id VARCHAR(255) NOT NULL,
    user_id VARCHAR(255) NOT NULL,
    joined_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    left_at TIMESTAMP
);

-- Tạo index để tối ưu truy vấn
CREATE INDEX idx_conversation_participants_conversation_id ON conversation_participants(conversation_id);
CREATE INDEX idx_conversation_participants_user_id ON conversation_participants(user_id);
CREATE INDEX idx_conversation_participants_conversation_user ON conversation_participants(conversation_id, user_id);

-- changeset hieudx:132
CREATE TABLE messages (
    id VARCHAR(255) PRIMARY KEY,
    conversation_id VARCHAR(255) NOT NULL,
    sender_id VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    attachments JSONB,
    is_read BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Tạo index để tối ưu truy vấn
CREATE INDEX idx_messages_conversation_id ON messages(conversation_id);
CREATE INDEX idx_messages_sender_id ON messages(sender_id);
CREATE INDEX idx_messages_created_at ON messages(created_at);

