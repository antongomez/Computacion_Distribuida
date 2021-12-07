create extension pgcrypto;
create table usuarios
(
	id varchar(30) not null primary key,
	contrasinal varchar(60) not null

);

create table pedirAmizade
(
	solicitante varchar(30) not null,
	receptor varchar(30) not null,

	primary key (solicitante,receptor),
	foreign key (solicitante) references usuarios on delete cascade,
	foreign key (receptor) references usuarios on delete cascade

);

create table serAmigo
(
	usuario1 varchar(30) not null,
	usuario2 varchar(30) not null,

	primary key (usuario1,usuario2),
	foreign key (usuario1) references usuarios on delete cascade,
	foreign key (usuario2) references usuarios on delete cascade

);
