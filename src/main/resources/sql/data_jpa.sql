TRUNCATE TABLE CAR_RACES CASCADE;
TRUNCATE TABLE CAR_SPONSORS CASCADE;
TRUNCATE TABLE SPONSORS RESTART IDENTITY CASCADE;
TRUNCATE TABLE RACES RESTART IDENTITY CASCADE;
TRUNCATE TABLE CARS RESTART IDENTITY CASCADE;
TRUNCATE TABLE application_user RESTART IDENTITY CASCADE;
TRUNCATE TABLE role CASCADE;
TRUNCATE TABLE user_roles CASCADE;


-- Insert sample data into CARS table
INSERT INTO CARS (brand, model, engine, horsepower, production_year, category, image)
VALUES ('Ferrari', '488 GTB', 3.9, 670, 2021, 'SPORTS', 'ferrari.jpg'),
       ('Subaru', 'Impreza WRX', 2.5, 300, 2019, 'RALLY', 'subaru.jpg'),
       ('Alpine', 'A524', 1.6, 950, 2024, 'F1', 'alpine.jpg'),
       ('Dodge', 'Challenger', 6.2, 797, 2018, 'DRAG', 'dodge.jpg'),
       ('Porsche', '911 Turbo S', 3.8, 640, 2022, 'SPORTS', 'porsche.jpg'),
       ('Ford', 'Focus RS', 2.3, 350, 2020, 'RALLY', 'ford.jpg'),
       ('Red Bull', 'RB19', 1.6, 1050, 2023, 'F1', 'red_bull.jpg'),
       ('Chevrolet', 'Camaro ZL1', 6.2, 650, 2017, 'DRAG', 'chevrolet_camaro.jpg'),
       ('Lamborghini', 'Huracan EVO', 5.2, 630, 2021, 'SPORTS', 'lamborghini.jpg'),
       ('Mitsubishi', 'Lancer Evolution IX', 2.0, 276, 2006, 'RALLY', 'mitsubishi.jpg'),
       ('Mercedes', 'W14', 1.6, 1045, 2023, 'F1', 'mercedes.jpg'),
       ('Nissan', 'GT-R R35', 3.8, 565, 2021, 'DRAG', 'nissan_gtr.jpg'),
       ('Ford', 'Mustang Boss 429', 7.0, 375, 1969, 'DRAG', 'mustang_boss_429.jpg'),
       ('Ferrari', '250 GTO', 3.0, 300, 1962, 'SPORTS', 'ferrari_250_gto.jpg'),
       ('Porsche', '356 Speedster', 1.6, 60, 1956, 'SPORTS', 'porsche_356_speedster.jpg'),
       ('Jaguar', 'E-Type Series 1', 4.2, 265, 1962, 'SPORTS', 'jaguar_e_type.jpg');


-- Insert sample data into RACES table
INSERT INTO RACES (name, date, track, location, distance, image)
VALUES ('Monaco Grand Prix', '2023-05-28', 'Monaco Grand Prix', 'Monaco', 3.37, 'monaco.jpg'),
       ('Rallysprint de Atogo', '2024-10-05', 'Rallysprint de Atogo', 'Spain', 61.30, 'atogo.jpg'),
       ('Nurburgring Endurance', '2023-08-10', 'Nurburgring', 'Germany', 24.43, 'nurburgring.jpg'),
       ('Centerville Dragway', '2024-07-15', 'Centerville Dragway', 'USA', 10, 'drag.jpg'),
       ('Silverstone Grand Prix', '2023-07-09', 'Silverstone Circuit', 'United Kingdom', 5.89, 'silverstone.jpg'),
       ('Safari Rally Kenya', '2024-06-24', 'Nairobi', 'Kenya', 35.10, 'safari_rally.jpg'),
       ('Spa 24 Hours', '2024-08-01', 'Circuit de Spa-Francorchamps', 'Belgium', 7.00, 'spa.jpg'),
       ('Hockenheim Drag Fest', '2024-09-18', 'Hockenheimring', 'Germany', 8.00, 'hockenheim.jpg'),
       ('Le Mans Classic', '2024-07-05', 'Circuit de la Sarthe', 'France', 13.63, 'lemans.jpg'),
       ('Monte Carlo Rally', '2024-01-23', 'Monte Carlo', 'Monaco', 40.00, 'monte_carlo.jpg'),
       ('Austrian Grand Prix', '2023-06-25', 'Red Bull Ring', 'Austria', 4.32, 'austria.jpg'),
       ('NHRA Nationals', '2024-03-15', 'Las Vegas Motor Speedway', 'USA', 6.40, 'nhra.jpg');

-- Insert sample data into SPONSORS table
INSERT INTO SPONSORS (id, name, industry, founding_year, image)
VALUES (1, 'Red Bull', 'Energy Drink', 1987, 'redbull.jpg'),
       (2, 'Monster Energy', 'Energy Drink', 2002, 'monster.jpg'),
       (3, 'Goodyear', 'Tires', 1898, 'goodyear.jpg'),
       (4, 'Shell', 'Oil and Gas', 1907, 'shell.jpg'),
       (5, 'Castrol', 'Oil and Lubricants', 1899, 'castrol.jpg'),
       (6, 'Pirelli', 'Tires', 1872, 'pirelli.jpg'),
       (7, 'BMW Motorsport', 'Automotive', 1972, 'bmw.jpg'),
       (8, 'Toyota Gazoo Racing', 'Automotive', 1937, 'toyota.jpg'),
       (9, 'Mercedes-Benz', 'Automotive', 1926, 'amg.jpg'),
       (10, 'Audi Sport', 'Automotive', 1983, 'audi.jpg');

-- Insert data into CAR_RACES (Many-to-Many relationships)
INSERT INTO CAR_RACES (car_id, race_id)
VALUES (1, 1),   -- Ferrari -> Monaco Grand Prix
       (2, 2),   -- Subaru -> Rallysprint de Atogo
       (3, 3),   -- Alpine -> Nurburgring Endurance
       (4, 4),   -- Dodge -> Centerville Dragway
       (1, 3),   -- Ferrari -> Nurburgring Endurance
       (5, 5),   -- Porsche -> Silverstone Grand Prix
       (6, 6),   -- Ford -> Safari Rally Kenya
       (7, 5),   -- Red Bull -> Hockenheim Drag Fest
       (8, 4),   -- Chevrolet -> Centerville Dragway
       (9, 5),   -- Lamborghini -> Silverstone Grand Prix
       (10, 6),  -- Mitsubishi -> Safari Rally Kenya
       (11, 1),  -- Mercedes -> Monte Carlo Rally
       (12, 12), -- Nissan -> NHRA Nationals-- Ferrari -> Nurburgring Endurance
       (13, 7),
       (14, 9),
       (15, 9),
       (16, 9);

-- Insert data into CARS_SPONSORS (Many-to-Many relationships)
INSERT INTO CAR_SPONSORS (car_id, sponsor_id)
VALUES (1, 1),   -- Ferrari -> Red Bull
       (2, 2),   -- Subaru -> Monster Energy
       (3, 6),   -- Alpine -> Pirelli
       (4, 3),   -- Dodge -> Goodyear
       (1, 4),   -- Ferrari -> Shell
       (5, 4),   -- Porsche -> Shell
       (6, 5),   -- Ford -> Castrol
       (7, 1),   -- Red Bull -> Red Bull
       (8, 3),   -- Chevrolet -> Goodyear
       (9, 7),   -- Lamborghini -> BMW Motorsport
       (10, 2),  -- Mitsubishi -> Monster Energy
       (11, 9),  -- Mercedes -> Mercedes-Benz
       (12, 10), -- Nissan -> Audi Sport
       (13, 3),
       (14, 6),
       (15, 5),
       (16, 1);


INSERT INTO APPLICATION_USER (first_name, last_name, email, password)
VALUES ('Alexandru', 'Primac', 'alexandru@gmail.com', '$2a$12$ZGlUWyWUHfcoVeRQ240qu.WNWHSghtgDFMK5mrsZWmaNgxlJX.726'),
       ('Sponge', 'Bob', 'sponge@gmail.com', '$2a$12$BMTk3LgleQzPFwWouCjTdObCcnDNOjvVro436feaDVfO1Ee.BXxgC');


INSERT INTO role (id, name)
VALUES (1, 'ROLE_USER'),
       (2, 'ROLE_ADMIN');


INSERT INTO user_roles (user_id, role_id)
VALUES ((SELECT id FROM application_user WHERE email = 'alexandru@gmail.com'),
        (SELECT id FROM role WHERE name = 'ROLE_USER'));

INSERT INTO user_roles (user_id, role_id)
VALUES ((SELECT id FROM application_user WHERE email = 'sponge@gmail.com'),
        (SELECT id FROM role WHERE name = 'ROLE_ADMIN'));




