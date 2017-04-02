-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema simple_company
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema simple_company
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `simple_company` DEFAULT CHARACTER SET utf8 ;
USE `simple_company` ;

-- -----------------------------------------------------
-- Table `simple_company`.`Customer`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `simple_company`.`Customer` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `firstName` VARCHAR(45) NOT NULL,
  `lastName` VARCHAR(45) NOT NULL,
  `gender` CHAR(1) NOT NULL,
  `dob` DATE NOT NULL,
  `email` VARCHAR(45) NOT NULL,
  `address` VARCHAR(45) NOT NULL DEFAULT 'N/A',
  `creditCard` VARCHAR(45) NOT NULL DEFAULT 'N/A',
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `simple_company`.`CreditCard`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `simple_company`.`CreditCard` (
  `name` VARCHAR(45) NOT NULL,
  `ccNumber` VARCHAR(45) NOT NULL,
  `expDate` VARCHAR(45) NOT NULL,
  `securityCode` VARCHAR(45) NOT NULL,
  `Customer_id` BIGINT(20) NOT NULL,
  INDEX `fk_CreditCard_Customer1_idx` (`Customer_id` ASC),
  CONSTRAINT `fk_CreditCard_Customer1`
    FOREIGN KEY (`Customer_id`)
    REFERENCES `simple_company`.`Customer` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `simple_company`.`Address`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `simple_company`.`Address` (
  `address1` VARCHAR(45) NOT NULL,
  `address2` VARCHAR(45) DEFAULT 'N/A',
  `city` VARCHAR(45) NOT NULL,
  `state` VARCHAR(45) NOT NULL,
  `zipcode` VARCHAR(45) NOT NULL,
  `Customer_id` BIGINT(20) NOT NULL,
  INDEX `fk_Address_Customer_idx` (`Customer_id` ASC),
  CONSTRAINT `fk_Address_Customer`
    FOREIGN KEY (`Customer_id`)
    REFERENCES `simple_company`.`Customer` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `simple_company`.`Purchase`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `simple_company`.`Purchase` (
  `id` BIGINT(20) NOT NULL, 
  `productID` BIGINT(20) NOT NULL,
  `customerID` BIGINT(20) NOT NULL,
  `purchaseDate` DATE NOT NULL,
  `purchaseAmount` DECIMAL NOT NULL,
  `Customer_id` BIGINT(20) NOT NULL,
  PRIMARY KEY (`Customer_id`),
  CONSTRAINT `fk_Purchase_Customer1`
    FOREIGN KEY (`Customer_id`)
    REFERENCES `simple_company`.`Customer` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `simple_company`.`Product`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `simple_company`.`Product` (
  `id` BIGINT(20) NOT NULL DEFAULT 0,
  `prodName` VARCHAR(45) NOT NULL,
  `prodDescription` VARCHAR(45) NOT NULL,
  `prodCategory` INT NOT NULL,
  `prodUPC` VARCHAR(45) NOT NULL,
  `Purchase_Customer_id` BIGINT(20) NOT NULL DEFAULT 0,
  PRIMARY KEY (`Purchase_Customer_id`),
  CONSTRAINT `fk_Product_Purchase1`
    FOREIGN KEY (`Purchase_Customer_id`)
    REFERENCES `simple_company`.`Customer` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
