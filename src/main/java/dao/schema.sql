/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  dugwi731
 * Created: 5/08/2020
 */

create table Product (
	Product_ID varchar(50) not null,
	Name varchar(50) not null,	
	Description varchar(3000) not null,
	Category varchar(50) not null,
	List_Price decimal (5,2) not null,
	Quantity_In_Stock integer not null,
	constraint Product_ID primary key (Product_ID)
);

create table Customer (
	Customer_ID bigint auto_increment (1000),
	Username varchar(50) not null unique,
	FirstName varchar(50) not null,
	SurName varchar(50) not null,	
	Email_Address varchar(50) not null,
	Shipping_Address varchar(100) not null,
	Password varchar (50) not null,
	constraint Customer_ID primary key (Customer_ID)
);

create table Sale (
	Sale_ID bigint auto_increment (1000),
	Date timestamp not null,
	Status varchar(50) not null,
	Customer_ID bigint not null,
	constraint Sale_ID primary key (Sale_ID),
	constraint Sale_Customer foreign key (Customer_ID) references Customer
);

create table SaleItem (
	Quantity_Purchased Integer not null,
	Sale_Price decimal(5,2) not null,
	Sale_ID bigint not null,
	Product_ID varchar(50) not null,
	constraint SaleItem_PK primary key (Sale_ID, Product_ID),
	constraint SaleItem_Product foreign key (Product_ID) references Product,
	constraint SaleItem_Sale_ID foreign key (Sale_ID) references Sale
);