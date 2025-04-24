
-- DATABASE SCHEMA
CREATE DATABASE IF NOT EXISTS blood_donation;

USE blood_donation;

-- USERS TABLE: login & role system
CREATE TABLE IF NOT EXISTS Users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('DOCTOR', 'PATIENT') NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- BLOOD BANK
CREATE TABLE IF NOT EXISTS BloodBank (
    Bid INT PRIMARY KEY AUTO_INCREMENT,
    Name VARCHAR(255) NOT NULL,
    ContactEmail VARCHAR(255),
    ContactPhone VARCHAR(20),
    Address TEXT NOT NULL,
    City VARCHAR(100),
    Country VARCHAR(100),
    PostalCode VARCHAR(20),
    LastInventoryDate DATE
) ENGINE=InnoDB;

-- DOCTOR
CREATE TABLE IF NOT EXISTS Doctor (
    dssn VARCHAR(20) PRIMARY KEY,
    user_id INT UNIQUE,
    dname VARCHAR(255),
    specialization VARCHAR(100),
    email VARCHAR(255),
    blood_bank_id INT,
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (blood_bank_id) REFERENCES BloodBank(Bid) ON DELETE SET NULL
) ENGINE=InnoDB;

-- PATIENT
CREATE TABLE IF NOT EXISTS Patient (
    pssn VARCHAR(20) PRIMARY KEY,
    user_id INT UNIQUE,
    name VARCHAR(255),
    blood_type ENUM('A+', 'A-', 'B+', 'B-', 'AB+', 'AB-', 'O+', 'O-'),
    age INT,
    gender ENUM('Male', 'Female', 'Other'),
    phone VARCHAR(20),
    email VARCHAR(255),
    assigned_doctor_id VARCHAR(20),
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (assigned_doctor_id) REFERENCES Doctor(dssn) ON DELETE SET NULL
) ENGINE=InnoDB;

-- BLOOD STOCK
CREATE TABLE IF NOT EXISTS BloodStock (
    StockId INT PRIMARY KEY AUTO_INCREMENT,
    BloodType ENUM('A+', 'A-', 'B+', 'B-', 'AB+', 'AB-', 'O+', 'O-') NOT NULL,
    Quantity INT DEFAULT 0 CHECK (Quantity >= 0),
    Status ENUM('available', 'unavailable', 'used', 'expired') DEFAULT 'available',
    ExpirationDate DATE NOT NULL,
    bid INT,
    StorageLocation VARCHAR(100),
    FOREIGN KEY (bid) REFERENCES BloodBank(Bid)
) ENGINE=InnoDB;

-- DONOR
CREATE TABLE IF NOT EXISTS Donor (
    donor_ssn VARCHAR(20) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    gender ENUM('Male', 'Female', 'Other'),
    age INT CHECK (age BETWEEN 18 AND 65),
    blood_type ENUM('A+', 'A-', 'B+', 'B-', 'AB+', 'AB-', 'O+', 'O-'),
    weight DECIMAL(5,2) CHECK (weight >= 50),
    last_donation_date DATE,
    health_status TEXT,
    is_eligible BOOLEAN DEFAULT TRUE,
    registration_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    email VARCHAR(255),
    phone VARCHAR(20)
) ENGINE=InnoDB;

-- DONATION HISTORY
CREATE TABLE IF NOT EXISTS DonationHistory (
    donation_id INT PRIMARY KEY AUTO_INCREMENT,
    donor_ssn VARCHAR(20),
    bid INT,
    date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    quantity INT CHECK (quantity BETWEEN 100 AND 500),
    FOREIGN KEY (donor_ssn) REFERENCES Donor(donor_ssn),
    FOREIGN KEY (bid) REFERENCES BloodBank(Bid)
) ENGINE=InnoDB;

-- BLOOD REQUEST
CREATE TABLE IF NOT EXISTS BloodRequest (
    request_id INT PRIMARY KEY AUTO_INCREMENT,
    dssn VARCHAR(20),
    pssn VARCHAR(20),
    blood_type ENUM('A+', 'A-', 'B+', 'B-', 'AB+', 'AB-', 'O+', 'O-'),
    quantity INT NOT NULL CHECK (quantity >= 100),
    status ENUM('PENDING', 'APPROVED', 'REJECTED') DEFAULT 'PENDING',
    request_date DATE,
    response_date DATE,
    FOREIGN KEY (dssn) REFERENCES Doctor(dssn),
    FOREIGN KEY (pssn) REFERENCES Patient(pssn)
) ENGINE=InnoDB;
