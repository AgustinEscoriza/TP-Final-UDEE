DROP DATABASE IF EXISTS tp_udee_bd2;
CREATE DATABASE IF NOT EXISTS tp_udee_bd2;
USE tp_udee_bd2;

DROP TABLE IF EXISTS users;
CREATE TABLE IF NOT EXISTS users(
  id int auto_increment,
  email varchar(50),
  is_employee boolean,
  last_name varchar(50),
  name varchar(50),
  password varchar(50),
  constraint pk_user primary key (id)
)ENGINE=InnoDB DEFAULT CHARACTER SET = utf8;

DROP TABLE IF EXISTS fee_types;
CREATE TABLE IF NOT EXISTS fee_types(
	id int auto_increment,
    detail varchar(50),
    priceperkwh float,
    constraint pk_fee_types primary key (id)
)ENGINE=InnoDB DEFAULT CHARACTER SET = utf8;


DROP TABLE IF EXISTS clients;
CREATE TABLE IF NOT EXISTS clients(
	id int auto_increment,
    dni varchar(25) default null,
    id_user int,
    constraint pk_client primary key (id),
    constraint fk_clients_user foreign key (id_user) REFERENCES users(id) ON UPDATE CASCADE ON DELETE CASCADE
)ENGINE=InnoDB DEFAULT CHARACTER SET = utf8;



DROP TABLE IF EXISTS meter_brands;
CREATE TABLE IF NOT EXISTS meter_brands(
	id int auto_increment,
    name varchar(50),
    constraint pk_meter_brands primary key (id)
)ENGINE=InnoDB DEFAULT CHARACTER SET = utf8;

DROP TABLE IF EXISTS meter_models;
CREATE TABLE IF NOT EXISTS meter_models(
	id int auto_increment,
    name varchar(50),
    id_brand int,
    constraint pk_meter_models primary key (id),
    constraint fk_meter_models_brands foreign key (id_brand) references meter_brands(id)
)ENGINE=InnoDB DEFAULT CHARACTER SET = utf8;


DROP TABLE IF EXISTS energy_meters;
CREATE TABLE IF NOT EXISTS energy_meters(
	serial_number int auto_increment,
    password varchar(50),
    id_brand int,
    id_model int,
    constraint pk_energy_meter primary key (serial_number),
    constraint fk_energy_meters_brand foreign key (id_brand) REFERENCES meter_brands(id),
    constraint fk_energy_meters_model foreign key (id_model) REFERENCES meter_models(id)
)ENGINE=InnoDB DEFAULT CHARACTER SET = utf8;

DROP TABLE IF EXISTS measurements;
CREATE TABLE IF NOT EXISTS measurements(
	id int auto_increment,
    billed boolean,
    date_time date,
    kwh float,
    serial_number_energy_meter int,
    constraint pk_measurements primary key (id),
    constraint fk_measurements_energy_meter foreign key (serial_number_energy_meter) references energy_meters(serial_number)
)ENGINE=InnoDB DEFAULT CHARACTER SET = utf8;

DROP TABLE IF EXISTS residences;
CREATE TABLE IF NOT EXISTS residences(
	id int auto_increment,
    fee_value int,
    number varchar(4),
    postal_number int,
    street varchar(50),
    id_client int,
    serial_number_energy_meter int,
    constraint pk_residence primary key(id),
    constraint fk_fee_value_types foreign key (fee_value) references fee_types(id),
    constraint fk_residences_clients foreign key (id_client) references clients(id),
    constraint fk_residences_energy_meter foreign key (serial_number_energy_meter) references energy_meters(serial_number)
)ENGINE=InnoDB DEFAULT CHARACTER SET = utf8;

DROP TABLE IF EXISTS invoices;
CREATE TABLE IF NOT EXISTS invoices(
	id int auto_increment,
	emission_date datetime,
    final_consuption int,
    final_reading_date int,
    final_consumption int,
    initial_reading_date int,
	paid boolean,
    total_consumption float,
    id_client int,
    energy_meter_serial_number int,
    id_residence int,
    constraint pk_invoice primary key (id),
    constraint fk_invoices_energy_meter foreign key (energy_meter_serial_number) references energy_meters(serial_number),
    constraint fk_invoices_residence foreign key (id_residence) references residences(id),
    constraint fk_invoices_client foreign key (id_client) references clients(id)
)ENGINE=InnoDB DEFAULT CHARACTER SET = utf8;




CALL getClientBillsByDates(1,"2020-01-01" , "2022-06-06");
