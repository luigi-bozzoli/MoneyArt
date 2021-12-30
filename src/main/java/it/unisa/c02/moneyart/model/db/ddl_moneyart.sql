drop schema if exists moneyart;
create schema moneyart; 
use moneyart;

create table utente (
	id bigint auto_increment primary key,
    id_seguito bigint, /*FK*/
	email varchar (100) not null unique,
    pwd tinytext not null,             
    username varchar (100) not null unique,
    nome varchar (100) not null,
    cognome varchar (100) not null,
    foto blob,
    saldo double precision not null,
	
    foreign key (id_seguito) references utente(id) on update no action on delete no action
);
    
create table opera (  
	id bigint auto_increment primary key,
    id_utente bigint not null, /*FK*/
    id_artista bigint not null, /*FK*/
    nome varchar (255) not null, 
    prezzo double precision not null default 0, 
    descrizione text, 
    immagine blob not null,
    certificato text, 
    stato enum("ALL_ASTA", "IN_VENDITA", "IN_POSSESSO") default "IN_POSSESSO" not null,
    
	unique (id_artista, nome), /*Uno stesso artista non può avere 2 opere con lo stesso nome*/
    foreign key (id_utente) references utente(id) on update no action on delete no action, 
    foreign key (id_artista) references utente(id) on update no action on delete no action
);

create table asta (
	id bigint auto_increment primary key,
    id_opera bigint not null, /*FK*/
    data_inizio date not null, 
    data_fine date not null, 
    stato enum("IN_CORSO", "TERMINATA", "ELIMANATA") default "IN_CORSO" not null,
     
    foreign key (id_opera) references opera(id) on update no action on delete cascade 
);


create table rivendita (
	id bigint auto_increment primary key,
    id_opera bigint not null, /*FK*/
    prezzo double precision not null, 
    stato enum ("IN_CORSO", "TERMINATA") default "IN_CORSO" not null,
    
    foreign key (id_opera) references opera(id) on update no action on delete cascade
);

create table partecipazione (
	id bigint auto_increment primary key,
    id_utente bigint not null, /*FK*/
    id_asta bigint not null, /*FK*/
    offerta double precision not null default 0,

    foreign key (id_utente) references utente(id) on update no action on delete no action, 
    foreign key (id_asta) references asta(id) on update no action on delete cascade
);


create table notifica (
	id bigint auto_increment primary key,
    id_utente bigint not null, /*FK*/
    id_rivendita bigint, /*FK*/
    id_asta bigint, /*FK*/
    letta boolean default false not null, 
    tipo enum("VITTORIA", "ANNULLAMENTO", "SUPERATO", "TERMINATA") not null,
    contenuto varchar(255) not null, 

    foreign key (id_utente) references utente(id) on update no action on delete no action, 
    foreign key (id_rivendita) references rivendita(id) on update no action on delete cascade,
    foreign key (id_asta) references asta(id) on update no action on delete cascade
);

create table segnalazione ( 
	id bigint auto_increment primary key,
    id_asta bigint not null, /*FK*/
    commento varchar(255) not null,            
    letta boolean default false not null,

    foreign key (id_asta) references asta(id) on update no action on delete cascade
);
 
/*admin*/    
insert into utente values (0, NULL, "moneyart@gmail.com", "admin", "admin", "Money", "Art", null, 99999999);
