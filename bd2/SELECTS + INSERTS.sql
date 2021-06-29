
CREATE DATABASE tp_udee;
USE tp_udee;

INSERT INTO users (id,name,last_name,email,password,is_employee) VALUES (1,"Agustin","Escoriza","agusescoriza@outlook.es","1234",false),
																		(2,"Juan","Bianchi","juanbi@outlook.es","1234",false),
                                                                        (3,"Pedro","Peter","PePeter@outlook.es","1234",false),
                                                                        (4,"Francisco","Francis","FranFran@outlook.es","1234",false),
                                                                        (23,"Facu","campazzo","RotundoFracazo@outlook.es","1234",false),
																		(5,"Pablo", "Fino","pabloFino@gmail.com","1234",true),
																		(6,"King", "Kong","KongKing2323@gmail.com","1234",true);
INSERT INTO clients() VALUES (35908302,1),(35909843,2),(37908652,3),(35623302,4);
INSERT INTO fee_types () VALUES (1,"RESIDENTIAL",60),(2,"COMERTIAL",75),(3,"INDUSTRIAL", 40),(4,"OTHER",65);
INSERT INTO meter_brands (id,name) VALUES (1,"EMeter"),(2,"IBM"),(3,"Aclara"),(4,"Tesla"),(5,"Ferrari");

INSERT INTO meter_models (id,name,id_brand) VALUES (1,"EModel2231",1),(2,"EModel2232",1),(3,"EModel2233",1),(4,"EModel2234",1),(5,"EModel2235",1),(6,"EModel2236",1),(7,"EModel2237",1),(8,"EModel2238",1),(9,"EModel2239",1),(10,"EModel2240",1),
													(11,"AModel1242",2),(12,"AModel1241",2),(13,"AModel1243",2),(14,"AModel1243",2),(15,"AModel1244",2),(16,"AModel1245",1),(17,"AModel1246",2),(18,"AModel1247",2),(19,"AModel1248",2),(20,"AModel1249",2),
                                                    (29,"BModel4421",3),(21,"BModel4422",3),(22,"BModel4423",3),(23,"BModel4424",3),(24,"BModel4424",3),(25,"BModel4425",3),(26,"BModel4426",3),(27,"BModel4427",3),(28,"BModel4428",3),(30,"BModel4429",3),
                                                    (31,"CModel4221",4),(32,"CModel4222",4),(33,"CModel4223",4),(34,"CModel4224",4),(35,"CModel4225",4),(36,"CModel4226",1),(37,"CModel4227",1),(38,"CModel4228",4),(39,"CModel4229",4),(40,"CModel4230",4),
                                                    (41,"DModel5821",5),(42,"DModel5822",5),(43,"DModel5823",5),(44,"DModel5824",5),(45,"DModel5825",5),(46,"DModel5826",5),(47,"DModel5827",5),(48,"DModel5828",5),(49,"DModel5829",5),(50,"DModel5830",5);
INSERT INTO energy_meters (serial_number,password,id_brand,id_model) VALUES (1,"1234",1,1),(2,"3234",1,3),(3,"2234",1,4),(4,"4234",1,5),(5,"5234",1,6),(6,"6234",1,7),(7,"7234",1,8),(8,"234",1,9),
									(9,"1234",2,11),(10,"1234",2,12),(11,"1234",2,13),(12,"1234",2,14),(13,"1234",2,12),(14,"1234",2,17),(15,"1234",2,18),(16,"1234",2,19),
                                    (17,"1234",3,21),(18,"1234",3,27),(19,"1234",3,25),(20,"1234",3,28),(21,"1234",3,26),(22,"1234",3,24),(23,"1234",3,23),(24,"1234",3,22),
                                    (25,"1234",4,31),(26,"1234",4,33),(27,"1234",4,32),(28,"1234",4,36),(29,"1234",4,33),(30,"1234",4,37),(31,"1234",4,38),(32,"1234",4,39),
                                    (33,"1234",5,41),(34,"1234",5,49),(35,"1234",5,48),(36,"1234",5,47),(37,"1234",5,46),(38,"1234",5,45),(39,"1234",5,44),(40,"1234",5,42);
INSERT INTO residences (id_fee_type,number,postal_number,street,client_dni,energy_meter_serial_number) VALUES (1,"4982",7600,"Brandsen",35908302,1),(2,"4242",7600,"Luro",35909843,2),
					(3,"1223",7600,"Libertad",37908652,3),(4,"1231",7600,"Neuquen",35623302,4),(1,"4982",7600,"lamadrid",35908302,5),(1,"4982",7600,"Paso",35908302,6),(1,"4982",7600,"Mogotes",35908302,7),
                    (2,"1248",7600,"Calazanz",35909843,8),(2,"3426",7600,"Lasalle",35909843,9),(2,"2621",7600,"Edison",35909843,10),(2,"4242",7600,"Pringles",35909843,11),(2,"4242",7600,"Pueyrredon",35909843,12),
					(3,"5685",7600,"Berutti",37908652,13),(3,"3456",7600,"Alsina",37908652,14),(3,"124",7600,"alem",37908652,15),(3,"8274",7600,"Guemes",37908652,16),
                    (4,"5684",7600,"Marechal",35623302,17),(4,"3456",7600,"Rivadavia",35623302,18),(4,"1289",7600,"Chacabuco",35623302,19),(4,"2468",7600,"San Martin",35623302,20),(4,"3426",7600,"Belgrano",35623302,21),
					(2,"6346",7600,"Jara",35909843,22),(2,"6976",7600,"3 de febrero",35909843,23),(2,"6797",7600,"Marconi",35909843,24),(2,"7457",7600,"Uruguay",35909843,25),(2,"457",7600,"Italia",35909843,26),
                    (1,"3734",7600,"Malvinas",35908302,27),(1,"1241",7600,"Moreno",35908302,28),(1,"9679",7600,"Salta",35908302,29),(1,"4575",7600,"Jujuy",35908302,30),(1,"4574",7600,"Francia",35908302,31),
                    (2,"3747",7600,"Falucho",35909843,32),(2,"4574",7600,"Saavedra",35909843,33),(2,"679",7600,"Buenos Aires",35909843,34),(2,"4587",7600,"Tucuman",35909843,35),(2,"4242",7600,"Almafuerte",35909843,36),
                    (4,"4377",7600,"Gascon",35623302,37),(4,"4577",7600,"Quintana",35623302,38),(4,"796",7600,"Mitre",35623302,39),(4,"4578",7600,"San Luis",35623302,40);
INSERT INTO measurements (billed,date,kwh,id_energy_meter,id_residence) VALUES (true,"2021-01-11 11:12:01",2.4,1,1),(false,"2021-01-11 11:19:01",5.4,1,1),(false,"2021-01-11 11:27:01",7.4,1,1),
(false,"2021-01-11 11:12:01",3.2,1,1),(false,"2021-01-11 11:19:01",6.4,1,1),(false,"2021-01-11 11:24:01",7.4,1,1),(false,"2021-01-11 11:30:01",10.8,1,1),(false,"2021-01-11 11:35:01",19.4,1,1),
(true,"2021-01-11 11:12:01",2.4,2,2),(false,"2021-01-11 11:19:01",5.4,2,2),(false,"2021-01-11 11:24:01",7.4,2,2),(false,"2021-01-11 11:29:01",11.2,2,2),(false,"2021-01-11 11:33:01",24,2,2),
(true,"2021-01-11 11:12:01",2.4,3,3),(false,"2021-01-11 11:19:01",5.4,3,3),(false,"2021-01-11 11:24:01",7.4,3,3),(false,"2021-01-11 11:28:01",13.5,3,3),(false,"2021-01-11 11:32:01",21.4,3,3),
(true,"2021-01-11 11:12:01",2.4,4,4),(false,"2021-01-11 11:19:01",5.4,4,4),(false,"2021-01-11 11:24:01",7.4,4,4),(false,"2021-01-11 11:28:01",16.12,4,4),(false,"2021-01-11 11:31:01",22.4,4,4);
INSERT INTO invoices (final_price,emission_date,final_measurement,final_reading_date,initial_measurement,initial_reading_date,paid,total_consumption,id_client,energy_meter_serial_number,id_residence) VALUES
(555,"2021-03-22 11:12:01",25,"2021-03-21 11:12:01",12,"2021-03-01 11:12:01",true,13,35908302,1,1),
(1424,"2021-03-22 11:12:01",25,"2021-03-21 11:12:01",12,"2021-03-01 11:12:01",false,22,35909843,2,1),
(555,"2021-03-22 11:12:01",25,"2021-03-21 11:12:01",12,"2021-03-01 11:12:01",true,13,35908302,1,1),
(1424,"2021-03-22 11:12:01",25,"2021-03-21 11:12:01",12,"2021-03-01 11:12:01",false,22,35909843,2,1),
(555,"2021-03-22 11:12:01",25,"2021-03-21 11:12:01",12,"2021-03-01 11:12:01",true,13,35908302,1,1),
(1424,"2021-03-22 11:12:01",25,"2021-03-21 11:12:01",12,"2021-03-01 11:12:01",false,22,35909843,2,1)
(555,"2021-03-22 11:12:01",25,"2021-03-21 11:12:01",12,"2021-03-01 11:12:01",true,13,35908302,1,1),
(1424,"2021-03-22 11:12:01",25,"2021-03-21 11:12:01",12,"2021-03-01 11:12:01",false,22,35909843,2,1)
(),
(),(),(),();

                    
SELECT * FROM tp_udee.meter_brands;
SELECT * FROM tp_udee.clients;
SELECT * FROM tp_udee.users;
SELECT * FROM tp_udee.energy_meters;
SELECT * FROM tp_udee.meter_models;
SELECT * FROM tp_udee.residences;
SELECT * FROM tp_udee.invoices;
SELECT * FROM tp_udee.measurements;
SELECT * FROM tp_udee.fee_types;

DROP PROCEDURE IF EXISTS getTop10Clients;
DELIMITER //
CREATE PROCEDURE getTop10Clients(IN first_date DATETIME, IN last_date DATETIME)
BEGIN
	
	SELECT c.dni ,TRUNCATE(SUM(m.kwh),2) As TOTAL
    FROM clients c INNER JOIN residences r ON r.client_dni = c.dni
    INNER JOIN measurements m ON m.id_residence = r.id 
    WHERE m.date BETWEEN first_date AND last_date
    GROUP BY c.dni
    ORDER BY TOTAL DESC
    LIMIT 10;
END;
//

SELECT c.dni ,TRUNCATE(SUM(m.kwh),2) As TOTAL
    FROM clients c INNER JOIN residences r ON r.client_dni = c.dni
    INNER JOIN measurements m ON m.id_residence = r.id 
    GROUP BY c.dni
    ORDER BY TOTAL DESC
    LIMIT 10;
	
call getTop10Clients('2020-01-01','2022-01-01');

DROP PROCEDURE IF EXISTS getClientBillsByDates;
DELIMITER //
CREATE PROCEDURE getClientBillsByDates(IN client_dni INT,IN first_date DATETIME, IN last_date DATETIME)
BEGIN 
    IF( first_date > last_date ) THEN
		SIGNAL SQLSTATE'45000' SET MESSAGE_TEXT='The entered dates are invalid';
	ELSE 
		SELECT i.id, i.paid, i.initial_measurement, i.initial_reading_date, i.final_measurement,i.emission_date,
		       i.final_reading_date, i.final_price, i.total_consumption, i.energy_meter_serial_number, i.id_residence
		FROM invoices AS i
		INNER JOIN residences AS r
		ON i.id_residence = r.id
		INNER JOIN clients AS c
		ON c.dni = r.client_dni
		WHERE c.dni = client_dni AND i.emission_date BETWEEN first_date AND last_date;
	END IF;
END;
//

CALL getClientBillsByDates(35908302,"2020-01-01" , "2022-01-01");

SELECT * FROM tp_udee.invoices;
SELECT * FROM tp_udee.clients;
SELECT * FROM tp_udee.residences;


CREATE UNIQUE INDEX idx_unq_client_dni ON clients (dni) USING HASH;
CREATE  INDEX idx_unq_measurements_date ON measurements (DATE) USING HASH;