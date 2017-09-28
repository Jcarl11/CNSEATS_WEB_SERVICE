CREATE DATABASE SOFTENG;
USE SOFTENG;

CREATE TABLE ACCOUNTS
(
       USERNO INT AUTO_INCREMENT PRIMARY KEY,
       USERNAME varchar(255),
       UPASSWORD varchar(255),
       FNAME varchar(255),
       LNAME varchar(255),
       MINITIALS varchar(255),
       EMAIL varchar(255),
       ADDRESS varchar(255)
);

CREATE TABLE MOVIES
(
       MOVIENO INT AUTO_INCREMENT PRIMARY KEY,
	   TITLE VARCHAR(255),
       RATING VARCHAR(20),
       PRICE DOUBLE,
       SHOWSTATUS VARCHAR(10),
       DESCRIPTION VARCHAR(255),
       PLAYDATE DATETIME
);

CREATE TABLE SEAT
(
       SEATNUMBER VARCHAR(255),
       USERNAME VARCHAR(255),
       MOVIENAME VARCHAR(255),
       PLAYTIME DATETIME
);


