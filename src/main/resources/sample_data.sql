-- Sample data for Users
INSERT INTO Users (email, password, role, is_active)
VALUES
('doctor1@example.com', '123456', 'DOCTOR', TRUE),
('patient1@example.com', '123456', 'PATIENT', TRUE),
('admin@example.com', 'admin123', 'DOCTOR', TRUE);

-- Sample data for BloodBank
INSERT INTO BloodBank (Name, ContactEmail, ContactPhone, Address, City, Country, PostalCode)
VALUES
('Central Blood Bank', 'central@bank.com', '0123456789', '123 Main St', 'Hanoi', 'Vietnam', '10000'),
('District Blood Bank', 'district@bank.com', '0987654321', '456 Second St', 'Hanoi', 'Vietnam', '10001');

-- Sample data for Doctor
INSERT INTO Doctor (dssn, user_id, dname, specialization, email, blood_bank_id)
VALUES
('D001', 1, 'Dr. Nguyen Van A', 'Hematology', 'doctor1@example.com', 1);

-- Sample data for Patient
INSERT INTO Patient (pssn, user_id, name, blood_type, age, gender, phone, email, assigned_doctor_id)
VALUES
('P001', 2, 'Le Thi B', 'A+', 25, 'Female', '0909123456', 'patient1@example.com', 'D001');

-- Sample data for Donor
INSERT INTO Donor (donor_ssn, name, gender, age, blood_type, weight, last_donation_date, health_status, is_eligible, registration_date, email, phone)
VALUES
('DN001', 'Tran Van C', 'Male', 30, 'O+', 65.0, '2024-04-01', 'Good', TRUE, NOW(), 'donor1@example.com', '0911122233');

-- Sample data for BloodStock
INSERT INTO BloodStock (BloodType, Quantity, Status, ExpirationDate, bid, StorageLocation)
VALUES
('A+', 10, 'available', '2025-12-31', 1, 'Shelf A'),
('O+', 5, 'available', '2025-11-30', 1, 'Shelf B');

-- Sample data for DonationHistory
INSERT INTO DonationHistory (donor_ssn, bid, date, quantity)
VALUES
('DN001', 1, '2024-04-01 09:00:00', 350);

-- Sample data for BloodRequest
INSERT INTO BloodRequest (dssn, pssn, blood_type, quantity, status, request_date, response_date)
VALUES
('D001', 'P001', 'A+', 300, 'PENDING', '2025-04-23', NULL);
