drop schema if exists timerservice;
create schema timerservice;
use timerservice;


create table timer(

id bigint auto_increment primary key,
attribute BLOB,
task_type varchar(100),
task_date timestamp

);