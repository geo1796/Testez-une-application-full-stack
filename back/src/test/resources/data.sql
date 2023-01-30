INSERT INTO TEACHERS (first_name, last_name)
VALUES ('Margot', 'DELAHAYE'),
       ('Hélène', 'THIERCELIN');


INSERT INTO USERS (first_name, last_name, admin, email, password)
VALUES ('Admin', 'Admin', true, 'yoga@studio.com', '$2a$10$.Hsa/ZjUVaHqi0tp9xieMeewrnZxrZ5pQRzddUXE/WjDu2ZThe6Iq'),
       ('User', 'User', false, 'user@test.com', '$2a$12$zsLVIzD3I3mqZ1n1sQ.4xOkvnxr/OUttcDYePmDc2vbWIDg8lRIH6');

INSERT INTO SESSIONS (name, description, teacher_id, date)
VALUES ('stretching', 'A stretching session', 1, '2023-01-01');

INSERT INTO PARTICIPATE (user_id, session_id)
VALUES (2, 1);