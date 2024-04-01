CREATE DATABASE IF NOT EXISTS medical_conditions;

USE medical_conditions;

CREATE TABLE IF NOT EXISTS symptoms (
    id INT AUTO_INCREMENT PRIMARY KEY,
    symptom VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS conditions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    med_condition VARCHAR(255) NOT NULL,
    symptom VARCHAR(255) NOT NULL
);

INSERT INTO symptoms (symptom) VALUES
('Fever'),
('Cough'),
('Headache'),
('Fatigue'),
('Nausea'),
('Shortness of breath');

INSERT INTO conditions (med_condition, symptom) VALUES
('Common Cold', 'Fever'),
('Common Cold', 'Cough'),
('Common Cold', 'Fatigue'),
('Influenza', 'Fever'),
('Influenza', 'Cough'),
('Influenza', 'Fatigue'),
('Influenza', 'Headache'),
('COVID-19', 'Fever'),
('COVID-19', 'Cough'),
('COVID-19', 'Fatigue'),
('COVID-19', 'Shortness of breath');
