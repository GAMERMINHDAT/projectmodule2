--
CREATE SCHEMA course_management;

SET search_path TO course_management;
-- Bảng Admin
CREATE TABLE Admin (
                       id INT PRIMARY KEY ,
                       username VARCHAR(50) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL
);

-- Bảng Student
CREATE TABLE Student (
                         id SERIAL PRIMARY KEY,
                         name VARCHAR(100) NOT NULL,
                         dob DATE NOT NULL,
                         email VARCHAR(100) UNIQUE NOT NULL,
                         sex SMALLINT CHECK (sex IN (0,1)),
                         phone VARCHAR(20),
                         password VARCHAR(255) NOT NULL,
                         create_at DATE DEFAULT CURRENT_DATE
);
DROP TABLE Student cascade ;
SELECT * FROM Student;
-- Bảng Course
CREATE TABLE Course (
                        id INT PRIMARY KEY ,
                        name VARCHAR(100) NOT NULL,
                        duration INT NOT NULL,
                        instructor VARCHAR(100) NOT NULL,
                        create_at DATE DEFAULT (CURRENT_DATE)
);

-- Bảng Enrollment
CREATE TABLE Enrollment (
                            id INT PRIMARY KEY,
                            student_id INT NOT NULL,
                            course_id INT NOT NULL,
                            registered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            status VARCHAR(20) DEFAULT 'WAITING'
                                CHECK (status IN ('WAITING','DENIED','CANCEL','CONFIRMED')),
                            FOREIGN KEY (student_id) REFERENCES Student(id),
                            FOREIGN KEY (course_id) REFERENCES Course(id)
);
select*from Student ;
-- Insert dữ liệu mẫu
INSERT INTO Admin (id,username, password) VALUES
('02','admin', 'admin123'),
('03','admin1','admin456');

INSERT INTO Student (id,name, dob, email, sex, phone, password, create_at) VALUES
(11,'Nguyễn Văn A', '2000-01-15', 'nguyenvana@gmail.com', 1, '0912345678', '123456', CURRENT_DATE),
(22,'Trần Thị B', '2001-03-20', 'tranthib@gmail.com', 0, '0923456789', '123456', CURRENT_DATE),
(33,'Lê Văn C', '1999-07-10', 'levanc@gmail.com', 1, '0934567890', '123456', CURRENT_DATE),
(44,'Phạm Thị D', '2002-11-25', 'phamthid@gmail.com', 0, '0945678901', '123456', CURRENT_DATE),
(55,'Hoàng Văn E', '2000-09-30', 'hoangvane@gmail.com', 1, '0956789012', '123456', CURRENT_DATE);
INSERT INTO Course (id,name, duration, instructor, create_at) VALUES
('10','Java Core', 40, 'Thầy Nguyễn Văn X', CURRENT_DATE),
('20','Spring Boot', 50, 'Cô Trần Thị Y', CURRENT_DATE),
('30','ReactJS', 35, 'Thầy Lê Văn Z', CURRENT_DATE),
('40','MySQL', 30, 'Cô Phạm Thị W', CURRENT_DATE),
('50','HTML/CSS', 25, 'Thầy Hoàng Văn T', CURRENT_DATE);
select*from Course;
INSERT INTO Enrollment (id,student_id, course_id, registered_at, status) VALUES
(5,11, 10, '2024-01-15 09:30:00', 'CONFIRMED'),
(6,11, 20, '2024-02-20 14:45:00', 'WAITING'),
(7,22, 40, '2024-01-16 10:15:00', 'CONFIRMED'),
(8,22, 30, '2024-03-10 11:20:00', 'DENIED'),
(9,33, 50, '2024-02-25 15:30:00', 'CONFIRMED'),
(10,33, 20, '2024-03-05 08:45:00', 'CONFIRMED');
select*from Enrollment;