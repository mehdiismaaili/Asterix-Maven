# File Integrity Manager App In Java
![demo](https://github.com/mehdiismaaili/profile/blob/master/app3.png)

## Introduction
### Asterix is a file integrity manager developed in java it surveiles your files of choice and makes sure to notify you of all changes and audit them.

## Develped By: 
  - Mehdi Ismaaili
  - Leon Meizou

## How To run The App
### Database set up
  * The app uses a mysql database so you need to install Mysql community & Mysql Wrokbenche 
  * localy on your machine then create a database called 'asterix' and for the user
  * Leave it as 'root' just set the password to 'hightech2024'
  * You can find the db class here : ```src/main/java/inc/asterix/fim/Dbconnection.java```

### Note: You must have jdk22 installed in your machine to run the app
  * To run the app you can clone the app with this command ```git clone https://github.com/mehdicode123/Asterix-Maven.git```
  * Then open it in vs code an then navigate to this class ```src/main/java/asterix/main/Application.java``` press the run button and it should work.
  * Note : If you find any issues while trying to run the app please report to us.

## Techonlgies used to develope the app
- [Java](https://www.java.com/en/):
  * The app was developed in java, from the backend to the GUI, we used JFrame framework for the GUI
    and used flatflaf look and feel to design it.
  * With java we were able to develope most of the functionalities for our application, from
    the changes detection, reporting of the changes and also create a visual representations for
    the integretted files stats(modificaions, type of modiffications, graphs...).
     
- [Mysql](https://www.mysql.com/en/):
  * The App uses a Mysql database, this database will store the user info(username, email, pass, secret key)
  * The database will also store the the files with their name, path on your machine and thier content hashed.       
  
### Copright 2024 © [Mehdi Ismaaili](https://github.com/mehdiismaaili)
