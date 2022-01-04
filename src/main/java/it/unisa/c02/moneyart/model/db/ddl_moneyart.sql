drop schema if exists moneyart;
create schema moneyart;
use moneyart;

create table utente (
	id bigint auto_increment primary key,
    id_seguito bigint, /*FK*/
	email varchar (100) not null unique,
    pwd binary (32) not null,   # ...o binary (32)
    username varchar (100) not null unique,
    nome varchar (100) not null,
    cognome varchar (100) not null,
    foto blob,
    saldo double precision not null,
	
    foreign key (id_seguito) references utente(id) on update no action on delete no action
);

insert into utente(id_seguito, email, pwd, username, nome, cognome, foto, saldo)
values (NULL, "moneyart@gmail.com", 0x8C6976E5B5410415BDE908BD4DEE15DFB167A9C873FC4BB8A81F6F2AB448A918, "admin", "Money", "Art", null, 99999999),  # Admin, pwd = admin
       (NULL, "alfonso.cannavale@gmail.com", 0x7ED18A1212BF70F759A64C41CD26A0D83E7BC2889AE994748E7EAD6CF00F10DA, "alfcan", "Alfonso", "Cannavale", null, 0),  # pwd = pippo123
       (NULL, "nicolò.delogu@gmail.com", 0x7ED18A1212BF70F759A64C41CD26A0D83E7BC2889AE994748E7EAD6CF00F10DA, "XJustUnluckyX", "Nicolò", "Delogu", null, 0),
       (NULL, "michael.desantis@gmail.com", 0x7ED18A1212BF70F759A64C41CD26A0D83E7BC2889AE994748E7EAD6CF00F10DA, "shoyll", "Michael", "De Santis", null, 0),
       (NULL, "daniele.galloppo@gmail.com", 0x7ED18A1212BF70F759A64C41CD26A0D83E7BC2889AE994748E7EAD6CF00F10DA, "DG266", "Daniele", "Galloppo", null, 0),
       (NULL, "dario.mazza@gmail.com", 0x7ED18A1212BF70F759A64C41CD26A0D83E7BC2889AE994748E7EAD6CF00F10DA, "xDaryamo", "Dario", "Mazza", null, 0),
       (NULL, "mario.peluso@gmail.com", 0x7ED18A1212BF70F759A64C41CD26A0D83E7BC2889AE994748E7EAD6CF00F10DA, "MarioPeluso", "Mario", "Peluso", null, 0),
       (NULL, "aurelio.sepe@gmail.com", 0x7ED18A1212BF70F759A64C41CD26A0D83E7BC2889AE994748E7EAD6CF00F10DA, "AurySepe", "Aurelio", "Sepe", null, 0),
       (NULL, "stefano.zarro@gmail.com", 0x7ED18A1212BF70F759A64C41CD26A0D83E7BC2889AE994748E7EAD6CF00F10DA, "stepzar", "Stefano", "Zarro", null, 0);

create table opera (  
	id bigint auto_increment primary key,
    id_utente bigint not null, /*FK*/
    id_artista bigint not null, /*FK*/
    nome varchar (255) not null, 
    prezzo double precision not null default 0,   # DA RIMUOVERE
    descrizione text, 
    immagine blob not null,
    certificato text,
    stato enum("ALL_ASTA", "IN_VENDITA", "IN_POSSESSO") default "IN_POSSESSO" not null,
    
	unique (id_artista, nome), /* Uno stesso artista non può avere 2 opere con lo stesso nome */
    foreign key (id_utente) references utente(id) on update no action on delete no action, 
    foreign key (id_artista) references utente(id) on update no action on delete no action
);

create table asta (
	id bigint auto_increment primary key,
    id_opera bigint not null, /*FK*/
    data_inizio date not null,
    data_fine date not null,
    stato enum("IN_CORSO", "TERMINATA", "ELIMINATA") default "IN_CORSO" not null,
     
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
 

