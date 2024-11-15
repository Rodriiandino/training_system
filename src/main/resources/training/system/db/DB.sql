-- Creación de la base de datos
CREATE DATABASE IF NOT EXISTS training_system;
USE training_system;

-- Creación de las tablas
CREATE TABLE IF NOT EXISTS Person (
    id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS Role (
    id INT AUTO_INCREMENT PRIMARY KEY,
    role_name ENUM('ROLE_USER', 'ROLE_ADMINISTRATOR', 'ROLE_TRAINER') NOT NULL
);

-- Se insertan los roles por defecto
INSERT INTO Role (role_name) VALUES ('ROLE_USER'), ('ROLE_ADMINISTRATOR'), ('ROLE_TRAINER');

CREATE TABLE IF NOT EXISTS Gym (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) unique NOT NULL,
    address VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS User (
    id INT PRIMARY KEY,
    gym_train_id INT,
    gym_admin_id INT,
    FOREIGN KEY (id) REFERENCES Person(id),
    FOREIGN KEY (gym_train_id) REFERENCES Gym(id),
    FOREIGN KEY (gym_admin_id) REFERENCES Gym(id)
);

CREATE TABLE IF NOT EXISTS User_Roles (
    user_id INT,
    role_id INT,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES User(id),
    FOREIGN KEY (role_id) REFERENCES Role(id)
);

CREATE TABLE IF NOT EXISTS Trainer_Client (
    trainer_id INT,
    client_id INT,
    PRIMARY KEY (trainer_id, client_id),
    FOREIGN KEY (trainer_id) REFERENCES User(id),
    FOREIGN KEY (client_id) REFERENCES User(id)
);

CREATE TABLE IF NOT EXISTS Gym_worker_user (
    gym_id INT,
    user_id INT,
    PRIMARY KEY (gym_id, user_id),
    FOREIGN KEY (gym_id) REFERENCES Gym(id),
    FOREIGN KEY (user_id) REFERENCES User(id)
);

CREATE TABLE IF NOT EXISTS Exercise (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    explanation TEXT NOT NULL,
    demo_video_url VARCHAR(255),
    is_predefined BOOLEAN DEFAULT FALSE NOT NULL,
    user_id INT,
    trainer_id INT,
    FOREIGN KEY (user_id) REFERENCES User(id),
    FOREIGN KEY (trainer_id) REFERENCES User(id)
);

CREATE TABLE IF NOT EXISTS Category (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS Exercise_Category (
    exercise_id INT,
    category_id INT,
    PRIMARY KEY (exercise_id, category_id),
    FOREIGN KEY (exercise_id) REFERENCES Exercise(id),
    FOREIGN KEY (category_id) REFERENCES Category(id)
);

CREATE TABLE IF NOT EXISTS Routine (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    user_id INT NOT NULL,
    trainer_id INT,
    FOREIGN KEY (user_id) REFERENCES User(id),
    FOREIGN KEY (trainer_id) REFERENCES User(id)
);

CREATE TABLE IF NOT EXISTS Routine_Exercise (
    routine_id INT,
    exercise_id INT,
    PRIMARY KEY (routine_id, exercise_id),
    FOREIGN KEY (routine_id) REFERENCES Routine(id),
    FOREIGN KEY (exercise_id) REFERENCES Exercise(id)
);

CREATE TABLE IF NOT EXISTS Progress (
    id INT AUTO_INCREMENT PRIMARY KEY,
    progress_date DATE NOT NULL,
    repetitions INT,
    weight DECIMAL(5, 2),
    time INT,
    user_id INT NOT NULL,
    trainer_id INT,
    exercise_id INT,
    FOREIGN KEY (user_id) REFERENCES User(id),
    FOREIGN KEY (trainer_id) REFERENCES User(id),
    FOREIGN KEY (exercise_id) REFERENCES Exercise(id)
);

CREATE TABLE IF NOT EXISTS Note (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    purpose TEXT NOT NULL,
    note_date DATE NOT NULL,
    user_id INT NOT NULL,
    trainer_id INT,
    FOREIGN KEY (user_id) REFERENCES User(id),
    FOREIGN KEY (trainer_id) REFERENCES User(id)
);

-- Inserción de datos de prueba

-- Insertar personas
INSERT INTO Person (first_name, last_name, email, password)
VALUES ('Rodrigo', 'Andino', 'rodrigo@gmail.com', '12345678');
INSERT INTO Person (first_name, last_name, email, password)
VALUES ('Gonzalo', 'Sanchez','gonzalo@gmail.com', '12345678');

-- Insertar User
INSERT INTO User (id) VALUES (1); -- Rodrigo
INSERT INTO User (id) VALUES (2); -- Gonzalo

-- Insertar roles al usuario
-- ROLE_USER = 1 ROLE_ADMINISTRATOR = 2 ROLE_TRAINER = 3
INSERT INTO User_Roles (user_id, role_id) VALUES (1, 1); -- Rodrigo es un usuario
INSERT INTO User_Roles (user_id, role_id) VALUES (1, 2); -- Rodrigo es un administrador

INSERT INTO User_Roles (user_id, role_id) VALUES (2, 1); -- Gonzalo es un usuario
INSERT INTO User_Roles (user_id, role_id) VALUES (2, 3); -- Gonzalo es un entrenador

-- Insertar Gym
INSERT INTO Gym (name, address) VALUES ('Gym 1', 'Calle 1');

-- Insertar Gym_worker_user
INSERT INTO Gym_worker_user (gym_id, user_id) VALUES (1, 2); -- Gonzalo trabaja en Gym 1

-- Update User
UPDATE User SET gym_train_id = 1 WHERE id = 1; -- Rodrigo entrena en Gym 1
UPDATE User SET gym_admin_id = 1 WHERE id = 1; -- Rodrigo administra Gym 1

-- Insertar Exercise Predefinidos
INSERT INTO Exercise (name, description, explanation, is_predefined)
-- Ejercicios predefinidos, los puede ver cualquier usuario
VALUES ('Flexiones', 'Flexiones de brazos', 'Flexiones de brazos', TRUE),
    ('Sentadillas', 'Sentadillas', 'Sentadillas', TRUE),
    ('Abdominales', 'Abdominales', 'Abdominales', TRUE);

-- Insertar Exercise Personalizados por el usuario
INSERT INTO Exercise (name, description, explanation, user_id)
-- Ejercicios personalizados, solo los puede ver el usuario que los creó
VALUES ('Flexiones una mano', 'Flexiones de brazos con una mano', 'Flexiones de brazos con una mano', 1);

-- Insertar Category
INSERT INTO Category (name, description) VALUES ('Pecho', 'Ejercicios para el pecho');
INSERT INTO Category (name, description) VALUES ('Piernas', 'Ejercicios para las piernas');
INSERT INTO Category (name, description) VALUES ('Abdomen', 'Ejercicios para el abdomen');

-- Insertar Exercise_Category
INSERT INTO Exercise_Category (exercise_id, category_id) VALUES (1, 1); -- Flexiones
INSERT INTO Exercise_Category (exercise_id, category_id) VALUES (2, 2); -- Sentadillas
INSERT INTO Exercise_Category (exercise_id, category_id) VALUES (3, 3); -- Abdominales
INSERT INTO Exercise_Category (exercise_id, category_id) VALUES (4, 1); -- Flexiones una mano

-- Insertar Routine creada por el usuario
INSERT INTO Routine (name, description, user_id)
VALUES ('Rutina por el usuario', 'Rutina por el usuario', 1);

-- Insertar Routine_Exercise
INSERT INTO Routine_Exercise (routine_id, exercise_id) VALUES (1, 1); -- Flexiones
INSERT INTO Routine_Exercise (routine_id, exercise_id) VALUES (1, 2); -- Sentadillas
INSERT INTO Routine_Exercise (routine_id, exercise_id) VALUES (1, 3); -- Abdominales
INSERT INTO Routine_Exercise (routine_id, exercise_id) VALUES (1, 4); -- Flexiones una mano

-- Insertar Progress del usuario
INSERT INTO Progress (progress_date, repetitions, weight, time, user_id, exercise_id)
VALUES ('2024-10-01', 10, 0, 0, 1, 1);
INSERT INTO Progress (progress_date, repetitions, weight, time, user_id, exercise_id)
VALUES ('2024-10-01', 10, 0, 0, 1, 2);
INSERT INTO Progress (progress_date, repetitions, weight, time, user_id, exercise_id)
VALUES ('2024-10-01', 10, 0, 0, 1, 3);
INSERT INTO Progress (progress_date, repetitions, weight, time, user_id, exercise_id)
VALUES ('2024-10-01', 10, 0, 0, 1, 4);

-- Insertar Note
INSERT INTO Note (title, content, purpose, note_date, user_id)
VALUES ('Nota del usuario', 'Contenido del usuario', 'Propósito de la nota del usuario', '2024-10-01', 1);

-- Insertar Trainer_Client
INSERT INTO Trainer_Client (trainer_id, client_id) VALUES (2, 1);
-- Gonzalo es el entrenador de Rodrigo

-- Insertar Exercise creado por el entrenador para el usuario
INSERT INTO Exercise (name, description, explanation, user_id, trainer_id)
-- Ejercicios creados por el entrenador para el usuario
VALUES ('Caminata de granjero', 'Caminata de granjero', 'Caminata de granjero', 1, 2);

-- Insertar Routine creada por el entrenador para el usuario
INSERT INTO Routine (name, description, user_id, trainer_id)
VALUES ('Rutina por el entrenador', 'Rutina por el entrenador', 1, 2);

-- Insertar Routine_Exercise creada por el entrenador para el usuario
INSERT INTO Routine_Exercise (routine_id, exercise_id) VALUES (2, 5); -- Caminata de granjero

-- Insertar Progress del usuario por el entrenador
INSERT INTO Progress (progress_date, repetitions, weight, time, user_id, trainer_id, exercise_id)
VALUES ('2024-10-01', 3, 20, 0, 1, 2, 5);

-- Insertar Note creada por el entrenador para el usuario
INSERT INTO Note (title, content, purpose, note_date, user_id, trainer_id)
VALUES ('Nota del entrenador', 'Contenido del entrenador', 'Propósito de la nota del entrenador', '2024-10-01', 1, 2);

-- Consultas
-- Consulta para obtener los roles de un usuario
SELECT p.first_name, p.last_name, r.role_name
FROM Person p
    JOIN User u ON p.id = u.id
    JOIN User_Roles ur ON u.id = ur.user_id
    JOIN Role r ON ur.role_id = r.id;

-- Consulta para obtener el gimnasio de un usuario
SELECT p.first_name, g.name AS gym_train, g2.name AS gym_admin, g3.name AS gym_worker
FROM Person p
    JOIN User u ON p.id = u.id
    LEFT JOIN Gym g ON u.gym_train_id = g.id
    LEFT JOIN Gym g2 ON u.gym_admin_id = g2.id
    LEFT JOIN Gym_worker_user gw ON u.id = gw.user_id
    LEFT JOIN Gym g3 ON gw.gym_id = g3.id;

-- Consulta para obtener el entrenador y cliente
SELECT t.first_name AS trainer_first_name, t.last_name AS trainer_last_name,
    c.first_name AS client_first_name, c.last_name AS client_last_name
FROM Trainer_Client tc
    JOIN Person t ON tc.trainer_id = t.id
    JOIN Person c ON tc.client_id = c.id;

-- Consulta para obtener los ejercicios de una rutina
SELECT r.name AS routine_name, e.name AS exercise_name
FROM Routine r
    JOIN Routine_Exercise re ON r.id = re.routine_id
    JOIN Exercise e ON re.exercise_id = e.id;

-- Consulta para obtener el progreso de un usuario
SELECT p.first_name, e.name AS exercise_name, pr.repetitions,
    pr.weight, pr.time, pr.progress_date, t.first_name AS trainer_name
FROM Progress pr
    JOIN User u ON pr.user_id = u.id
    JOIN Person p ON u.id = p.id
    JOIN Exercise e ON pr.exercise_id = e.id
    LEFT JOIN Person t ON pr.trainer_id = t.id
WHERE u.id = 1;

-- Consulta para obtener las notas de un usuario
SELECT n.title, n.content, n.purpose, n.note_date,
    p.first_name AS note_owner, t.first_name AS trainer_name
FROM Note n
    JOIN Person p ON n.user_id = p.id
    LEFT JOIN Person t ON n.trainer_id = t.id
WHERE n.user_id = 1;

-- Consulta para obtener los ejercicios predefinidos
SELECT e.name, e.description
    FROM Exercise e
WHERE e.is_predefined = TRUE;

SELECT e.id, e.name, e.description, e.explanation, e.demo_video_url, c.id, c.name, c.description
FROM exercise e
    left JOIN exercise_category ec ON e.id = ec.exercise_id
    left JOIN category c ON ec.category_id = c.id;

SELECT r.id, r.name, r.description, r.user_id, per.email
FROM training_system.routine r
         LEFT JOIN training_system.user u ON r.user_id = u.id
         LEFT JOIN training_system.person per ON u.id = per.id;

SELECT
    e.id AS exercise_id,
    e.name AS exercise_name,
    e.description AS exercise_description,
    e.explanation,
    e.demo_video_url,
    c.id AS category_id,
    c.name AS category_name,
    c.description AS category_description
FROM training_system.exercise e
         JOIN training_system.exercise_category ec ON e.id = ec.exercise_id
         JOIN training_system.category c ON ec.category_id = c.id
WHERE e.id = 5;

SELECT r.id, r.name, r.description, per.email, ex.id, ex.name, ex.description
FROM training_system.routine r
         LEFT JOIN training_system.user u ON r.user_id = u.id
         LEFT JOIN training_system.person per ON u.id = per.id
         LEFT JOIN training_system.routine_exercise re ON r.id = re.routine_id
         LEFT JOIN training_system.exercise ex ON re.exercise_id = ex.id;

SELECT
    n.id AS note_id,
    n.title AS note_title,
    n.content AS note_content,
    n.purpose AS note_purpose,
    n.note_date AS note_date,
    u.id AS user_id,
    p.first_name AS user_first_name,
    p.last_name AS user_last_name,
    u2.id AS trainer_id,
    p2.first_name AS trainer_first_name,
    p2.last_name AS trainer_last_name
FROM training_system.note n
         LEFT JOIN training_system.user u ON n.user_id = u.id
         LEFT JOIN training_system.person p ON u.id = p.id
         LEFT JOIN training_system.user u2 ON n.trainer_id = u2.id
         LEFT JOIN training_system.person p2 ON u2.id = p2.id;

SELECT p.id, p.progress_date, p.repetitions, p.weight, p.time, per.email, e.name
FROM training_system.progress p
         LEFT JOIN training_system.user u ON p.user_id = u.id
         LEFT JOIN training_system.person per ON u.id = per.id
         LEFT JOIN training_system.exercise e ON p.exercise_id = e.id;

SELECT r.id, r.name, r.description, t.id, t.first_name, t.email, e.id, e.name, e.description
FROM training_system.routine r
         LEFT JOIN training_system.user u ON r.trainer_id = u.id
         LEFT JOIN training_system.person t ON u.id = t.id
         LEFT JOIN training_system.routine_exercise re ON r.id = re.routine_id
         LEFT JOIN training_system.exercise e ON re.exercise_id = e.id
WHERE r.user_id = ?;

SELECT
    e.id AS exercise_id,
    e.name AS exercise_name,
    e.description AS exercise_description,
    e.explanation,
    e.demo_video_url,
    e.is_predefined,
    c.id AS category_id,
    c.name AS category_name,
    c.description AS category_description,
    e.trainer_id,
    pt.email AS trainer_email
FROM training_system.exercise e
         LEFT JOIN training_system.exercise_category ec ON e.id = ec.exercise_id
         LEFT JOIN training_system.category c ON ec.category_id = c.id
         LEFT JOIN training_system.user t ON e.trainer_id = t.id
         LEFT JOIN training_system.person pt ON t.id = pt.id
WHERE e.user_id = ? OR e.is_predefined = TRUE;

SELECT
    p.id,
    p.progress_date,
    p.repetitions,
    p.weight,
    p.time,
    t.id,
    t.first_name,
    t.email,
    e.id,
    e.name,
    e.description
FROM training_system.progress p
         LEFT JOIN training_system.user u ON p.trainer_id = u.id
         LEFT JOIN training_system.person t ON u.id = t.id
         LEFT JOIN training_system.exercise e ON p.exercise_id = e.id
WHERE p.user_id = ?;

SELECT
    e.id AS exercise_id,
    e.name AS exercise_name,
    e.description AS exercise_description,
    e.explanation,
    e.demo_video_url,
    p.id,
    p.first_name,
    p.email,
    c.id AS category_id,
    c.name AS category_name,
    c.description AS category_description
FROM training_system.exercise e
         LEFT JOIN training_system.user u ON e.user_id = u.id
         LEFT JOIN training_system.person p ON u.id = p.id
         LEFT JOIN training_system.exercise_category ec ON e.id = ec.exercise_id
         LEFT JOIN training_system.category c ON ec.category_id = c.id;

SELECT p.id, first_name, last_name, email
FROM training_system.user u
         LEFT JOIN training_system.person p ON u.id = p.id
WHERE u.gym_train_id = ?;

SELECT p.id, first_name, last_name, email
FROM training_system.gym_worker_user g
         LEFT JOIN training_system.person p ON g.user_id = p.id
WHERE g.gym_id = ?;

SELECT p.id, first_name, last_name, email
FROM training_system.user u
         LEFT JOIN training_system.person p ON u.id = p.id
WHERE u.gym_admin_id = ?;

-- Eliminar datos de prueba
-- Eliminar notas
DELETE FROM Note WHERE user_id = 1;
-- Eliminar progreso
DELETE FROM Progress WHERE user_id = 1;
-- Eliminar Routine_Exercise
DELETE FROM Routine_Exercise WHERE routine_id = 1;
DELETE FROM Routine_Exercise WHERE routine_id = 2;
-- Eliminar Routine
DELETE FROM Routine WHERE user_id = 1;
-- Eliminar Exercise_Category
DELETE FROM Exercise_Category WHERE exercise_id = 1;
DELETE FROM Exercise_Category WHERE exercise_id = 2;
DELETE FROM Exercise_Category WHERE exercise_id = 3;
DELETE FROM Exercise_Category WHERE exercise_id = 4;
-- Eliminar Exercise
DELETE FROM Exercise WHERE user_id = 1;
DELETE FROM Exercise WHERE is_predefined = TRUE;
-- Eliminar Trainer_Client
DELETE FROM Trainer_Client WHERE trainer_id = 2;
-- Eliminar Gym_worker_user
DELETE FROM Gym_worker_user WHERE user_id = 2;
-- Eliminar User_Roles
DELETE FROM User_Roles WHERE user_id = 1;
DELETE FROM User_Roles WHERE user_id = 2;
-- Eliminar User
DELETE FROM User WHERE id = 1;
DELETE FROM User WHERE id = 2;
-- Eliminar Person
DELETE FROM Person WHERE id = 1;
DELETE FROM Person WHERE id = 2;
-- Eliminar Gym
DELETE FROM Gym WHERE id = 1;
-- Eliminar Category
DELETE FROM Category WHERE id = 1 OR id = 2 OR id = 3;

