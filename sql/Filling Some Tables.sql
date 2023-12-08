INSERT INTO forum_user
(username, email, creation_date, password, forum_role_id)
VALUES
('Byb', 'example0@yandex.ru', CURRENT_TIMESTAMP,
'$2a$10$ANUFP29dspC2vKjLj/AF1eSvNNsI.KNZIErAOGWAfQC80ZVH7Wo5C', 1);

INSERT INTO forum_user_details
(name, gender, date_of_birth, country, city, forum_user_id)
VALUES
('Панько Иван', 'MALE', make_date(2002, 5, 19), 'Россия', 'Санкт-Петербург', 1);

INSERT INTO forum_role
(name)
VALUES
('ROLE_OWNER');

INSERT INTO forum_authority
(name)
VALUES
('WORKING_WITH_ROLES_AND_AUTHORITIES');

INSERT INTO forum_role_forum_authority
(forum_role_id, forum_authority_id)
VALUES
(1, 1);
