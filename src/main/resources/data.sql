INSERT INTO employees
(first_name, last_name, username, encrypted_password, role)
VALUES
("Mr.Rabbit", "Mechanic", "admin", "$2a$12$H70KfbIyV2kYSWt8hu8Cu.cYb8yTIyRBBOdTW8mFpnZ/xGF0Bmgn2", "ADMIN");

INSERT INTO employees
(first_name, last_name, username, encrypted_password, role)
VALUES
("João", "Faustino", "joao.faustino", "$2a$12$H70KfbIyV2kYSWt8hu8Cu.cYb8yTIyRBBOdTW8mFpnZ/xGF0Bmgn2", "MECHANIC");

INSERT INTO employees
(first_name, last_name, username, encrypted_password, role)
VALUES
("Rafael", "Miranda", "rafa.miranda", "$2a$12$H70KfbIyV2kYSWt8hu8Cu.cYb8yTIyRBBOdTW8mFpnZ/xGF0Bmgn2", "RECEPTIONIST");

INSERT INTO customers
(first_name, last_name, address, email, cell_Number)
VALUES
("David", "Peter", "Gang Street", "david.peter@gmail.com", "925816696");

INSERT INTO customers
(first_name, last_name, address, email, cell_Number)
VALUES
("Peter", "Parker", "Spidy Street", "peter.parker@gmail.com", "911452362");

INSERT INTO customers
(first_name, last_name, address, email, cell_Number)
VALUES
("Sara", "Cardoso", "Mindera Street", "sara.cardoso@gmail.com", "958256325");

INSERT INTO customers
(first_name, last_name, address, email, cell_Number)
VALUES
("Roger", "Jane", "Yder Street", "roger.Jane@gmail.com", "958256325");

INSERT INTO cars
(customer_id, brand, engine_type, plate)
VALUES
("1", "ABARTH", "Petrol", "El-Rei");

INSERT INTO cars
(customer_id, brand, engine_type, plate)
VALUES
("2", "SEAT", "Diesel", "Fumarento");

INSERT INTO cars
(customer_id, brand, engine_type, plate)
VALUES
("3", "LAMBORGHINI", "Petrol", "Queen");

INSERT INTO cars
(customer_id, brand, engine_type, plate)
VALUES
("4", "MITSUBISHI", "Petrol", "Gangsta");

INSERT INTO repairs
(car_id, repair_name, repair_description, start_date, end_date, price)
VALUES
("1", "Change Tires", "Continental tyres x4", "2021-01-01", "2021-01-01", "100");

INSERT INTO repairs
(car_id, repair_name, repair_description, start_date, end_date, price)
VALUES
("2", "Oil Leak", "Oil 15W-40", "2021-01-15", "2021-01-30", "80");

INSERT INTO repairs
(car_id, repair_name, repair_description, start_date)
VALUES
("3", "New Glass", "front glass changed", "2021-05-07");

INSERT INTO repairs
(car_id, repair_name, repair_description, start_date)
VALUES
("4", "Electric", "some problems with control unit", "2021-04-10");

INSERT INTO repairs
(car_id, repair_name, repair_description, start_date)
VALUES
("2", "New Motor", "motor breaked, new motor coming", "2021-03-01");

INSERT INTO repairs
(car_id, repair_name, repair_description, start_date, end_date, price)
VALUES
("3", "New Color", "full car pink", "2021-04-10", "2021-04-20", "200");

INSERT INTO repairs
(car_id, repair_name, repair_description, start_date, end_date, price)
VALUES
("3", "New Color", "full car pink", "2021-04-10", "2021-04-20", "200");