CREATE SCHEMA IF NOT EXISTS lab302;
USE lab302 ;

CREATE TABLE IF NOT EXISTS lab302.users (
  id INT UNSIGNED NOT NULL AUTO_INCREMENT,
  full_name VARCHAR(90) NOT NULL,
  password VARCHAR(45) NOT NULL,
  email VARCHAR(128) NULL,
  phone_number VARCHAR(10) NULL,
  active tinyint(1) NOT NULL DEFAULT 1,
  twitter_handle VARCHAR(45) NULL,
  facebook_url VARCHAR(200) NULL,
  secret_question VARCHAR(200) NOT NULL,
  secret_answer VARCHAR(45) NOT NULL,
  PRIMARY KEY (id));

CREATE TABLE IF NOT EXISTS lab302.user_images (
  id INT UNSIGNED NOT NULL AUTO_INCREMENT,
  user_id INT UNSIGNED NOT NULL,
  content_type VARCHAR(45) NOT NULL,
  image BLOB NOT NULL,
  PRIMARY KEY (id));

CREATE TABLE IF NOT EXISTS lab302.user_roles (
  id INT UNSIGNED NOT NULL AUTO_INCREMENT,
  user_id INT UNSIGNED NOT NULL,
  role VARCHAR(45) NOT NULL,
  PRIMARY KEY (id));


CREATE TABLE IF NOT EXISTS lab302.user_properties (
  id INT UNSIGNED NOT NULL AUTO_INCREMENT,
  user_id INT UNSIGNED NOT NULL,
  prop_name VARCHAR(45) NOT NULL,
  prop_value VARCHAR(255) NOT NULL,
  PRIMARY KEY (id));

CREATE TABLE IF NOT EXISTS lab302.contacts (
  id INT UNSIGNED NOT NULL AUTO_INCREMENT,
  first_name VARCHAR(45) NOT NULL,
  last_name VARCHAR(45) NOT NULL,
  note VARCHAR(200) NOT NULL,
  email VARCHAR(128) NULL,
  phone_number VARCHAR(10) NULL,
  active tinyint(1) NOT NULL DEFAULT 1,
  twitter_handle VARCHAR(45) NULL,
  facebook_url VARCHAR(200) NULL,
  user_id INT UNSIGNED NOT NULL,
  PRIMARY KEY (id));

CREATE TABLE IF NOT EXISTS lab302.contact_images (
  id INT UNSIGNED NOT NULL AUTO_INCREMENT,
  contact_id INT UNSIGNED NOT NULL,
  content_type VARCHAR(45) NOT NULL,
  image BLOB NOT NULL,
  PRIMARY KEY (id));