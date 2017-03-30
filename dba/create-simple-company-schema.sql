-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema simply_company
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema simply_company
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `simply_company` DEFAULT CHARACTER SET utf8 ;
USE `simply_company` ;

-- -----------------------------------------------------
-- Table `simply_company`.`Customer`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `simply_company`.`Customer` (
  `id` MEDIUMTEXT NOT NULL,
  `firstName` VARCHAR(45) NOT NULL,
  `lastName` VARCHAR(45) NOT NULL,
  `gender` CHAR(1) NOT NULL,
  `dob` DATE NOT NULL,
  `email` VARCHAR(45) NOT NULL,
  `address` VARCHAR(45) NOT NULL,
  `creditCard` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `simply_company`.`CreditCard`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `simply_company`.`CreditCard` (
  `name` VARCHAR(45) NOT NULL,
  `ccNumber` VARCHAR(45) NOT NULL,
  `expDate` VARCHAR(45) NOT NULL,
  `securityCode` VARCHAR(45) NOT NULL,
  `Customer_id` MEDIUMTEXT NOT NULL,
  INDEX `fk_CreditCard_Customer1_idx` (`Customer_id` ASC),
  CONSTRAINT `fk_CreditCard_Customer1`
    FOREIGN KEY (`Customer_id`)
    REFERENCES `simply_company`.`Customer` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `simply_company`.`Address`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `simply_company`.`Address` (
  `address1` VARCHAR(45) NOT NULL,
  `address2` VARCHAR(45) NOT NULL,
  `city` VARCHAR(45) NOT NULL,
  `state` VARCHAR(45) NOT NULL,
  `zipcode` VARCHAR(45) NOT NULL,
  `Customer_id` MEDIUMTEXT NOT NULL,
  INDEX `fk_Address_Customer_idx` (`Customer_id` ASC),
  CONSTRAINT `fk_Address_Customer`
    FOREIGN KEY (`Customer_id`)
    REFERENCES `simply_company`.`Customer` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `simply_company`.`Purchase`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `simply_company`.`Purchase` (
  `id` MEDIUMTEXT NOT NULL,
  `productID` MEDIUMTEXT NOT NULL,
  `customerID` MEDIUMTEXT NOT NULL,
  `purchaseDate` DATE NOT NULL,
  `purchaseAmount` DECIMAL NOT NULL,
  `Customer_id` MEDIUMTEXT NOT NULL,
  PRIMARY KEY (`Customer_id`),
  CONSTRAINT `fk_Purchase_Customer1`
    FOREIGN KEY (`Customer_id`)
    REFERENCES `simply_company`.`Customer` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `simply_company`.`Product`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `simply_company`.`Product` (
  `id` MEDIUMTEXT NOT NULL,
  `prodName` VARCHAR(45) NOT NULL,
  `prodDescription` VARCHAR(45) NOT NULL,
  `prodCategory` INT NOT NULL,
  `prodUPC` VARCHAR(45) NOT NULL,
  `Purchase_Customer_id` MEDIUMTEXT NOT NULL,
  PRIMARY KEY (`Purchase_Customer_id`),
  CONSTRAINT `fk_Product_Purchase1`
    FOREIGN KEY (`Purchase_Customer_id`)
    REFERENCES `simply_company`.`Purchase` (`Customer_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
