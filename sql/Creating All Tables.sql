CREATE TABLE forum_role (
	id SERIAL PRIMARY KEY,
	name VARCHAR (32) UNIQUE NOT NULL,
	alias VARCHAR (32) UNIQUE NOT NULL
);

CREATE TABLE forum_authority (
	id SERIAL PRIMARY KEY,
	name VARCHAR (128) UNIQUE NOT NULL
);

CREATE TABLE forum_role_forum_authority (
	id SERIAL PRIMARY KEY,
	forum_role_id INT REFERENCES forum_role (id),
	forum_authority_id INT REFERENCES forum_authority (id),
	CONSTRAINT not_duplicate_many_to_many_roles_authorities
	UNIQUE (forum_role_id, forum_authority_id)
);



CREATE TABLE forum_user (
	id SERIAL PRIMARY KEY,
	username VARCHAR (40) UNIQUE NOT NULL,
	email VARCHAR (64) UNIQUE NOT NULL,
	password VARCHAR (256) NOT NULL,
	creation_date TIMESTAMP NOT NULL,
	forum_role_id INT REFERENCES forum_role (id)
);

CREATE TABLE forum_user_details (
	id SERIAL PRIMARY KEY,
	name VARCHAR (80) NULL,
	gender VARCHAR (8) NULL,
	date_of_birth DATE NULL,
	country VARCHAR (64) NULL,
	city VARCHAR (64) NULL,
	image VARCHAR (256) NULL,
	forum_user_id INT REFERENCES forum_user (id) ON DELETE CASCADE UNIQUE NOT NULL
);



CREATE TABLE forum_section (
	id SERIAL PRIMARY KEY,
	name VARCHAR (80) UNIQUE NOT NULL,
	description VARCHAR (200) UNIQUE NOT NULL,
	creation_date TIMESTAMP NOT NULL,
	creator_id INT REFERENCES forum_user (id) ON DELETE SET NULL
);

CREATE TABLE topic (
	id SERIAL PRIMARY KEY,
	name VARCHAR (80) UNIQUE NOT NULL,
	description VARCHAR (200) UNIQUE NOT NULL,
	creation_date TIMESTAMP NOT NULL,
	creator_id INT REFERENCES forum_user (id) ON DELETE SET NULL,
	forum_section_id INT REFERENCES forum_section (id) NOT NULL
);

CREATE TABLE forum_message (
	id BIGSERIAL PRIMARY KEY,
	message_text TEXT NOT NULL,
	creation_date TIMESTAMP NOT NULL,
	editing_date TIMESTAMP NULL,
	creator_id INT REFERENCES forum_user (id) ON DELETE SET NULL,
	topic_id INT REFERENCES topic (id) NOT NULL
);



CREATE TABLE forum_like (
	id BIGSERIAL PRIMARY KEY,
	forum_user_id INT REFERENCES forum_user (id) NOT NULL,
	forum_message_id BIGINT REFERENCES forum_message (id) NOT NULL,
	creation_date TIMESTAMP NOT NULL,
	CONSTRAINT not_duplicate_many_to_many_forum_like
	UNIQUE (forum_user_id, forum_message_id)
);

CREATE TABLE forum_dislike (
	id BIGSERIAL PRIMARY KEY,
	forum_user_id INT REFERENCES forum_user (id) NOT NULL,
	forum_message_id BIGINT REFERENCES forum_message (id) NOT NULL,
	creation_date TIMESTAMP NOT NULL,
	CONSTRAINT not_duplicate_many_to_many_forum_dislike
	UNIQUE (forum_user_id, forum_message_id)
);



CREATE TABLE ban_table (
	id SERIAL PRIMARY KEY,
	forum_user_id INT REFERENCES forum_user (id) NOT NULL,
	creator_id INT REFERENCES forum_user (id) NOT NULL,
	reason TEXT NOT NULL,
	start_date DATE NOT NULL,
	end_date DATE NOT NULL,
	CONSTRAINT id_not_equals_ban
	CHECK (creator_id != forum_user_id)
);
