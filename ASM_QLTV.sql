create database QLTV
go

use QLTV
go


create table Role(
	id bigint identity(1,1) primary key,
	name nvarchar(255),
	permissionLevel int,
	maxReservationLimit int
);

create table Membership(
	id bigint identity(1,1) primary key,
	startDate date,
	endDate date,
	status bit,
	maxLoanBooks int,
	maxReservationBooks int
);

create table [User](
	id bigint identity(1,1) primary key,
	name nvarchar(255),
	email varchar(255),
	status varchar(255),
	membershipLevel bit,
	maxLoanLimit int,
	currentLoanCount int,
	overdueCount int,
	reservationLimit int,
	penaltyBalance decimal(10,2),

	role_id bigint,
	membership_id bigint,
	-- Xoa Role hoac Membership thi User lien quan se bi xoa theo (Cascade)
    CONSTRAINT FK_User_Role FOREIGN KEY (role_id) REFERENCES Role(id) ON DELETE CASCADE,
    CONSTRAINT FK_User_Membership FOREIGN KEY (membership_id) REFERENCES Membership(id) ON DELETE CASCADE
);	

create table Publisher(
	id bigint identity(1,1) primary key,
	name nvarchar(255),
	totalPublishedBooks int,
	averageLoanDays int
);

create table Book(
	id bigint identity(1,1) primary key,
	title nvarchar(255),
	ISBN nvarchar(255),
	language nvarchar(255),
	edition nvarchar(255),
	totalCopies int,
	availableCopies int,
	minLoanDays int,
    maxLoanDays int,
    popularityScore float,

	publisher_id bigint,
	-- Xoa NXB thi cac dau sach cua NXB do cung bi xoa theo
    CONSTRAINT FK_Book_Publisher FOREIGN KEY (publisher_id) REFERENCES Publisher(id) ON DELETE CASCADE
);

create table Author(
	id bigint identity(1,1) primary key,
	name nvarchar(255),
	activeYears nvarchar(255),
	awardsCount int
);

create table Category(
	id bigint identity(1,1) primary key,
	name nvarchar(255),
	averageLoanDays int,
	totalBooks int
);

create table Loan(
	id bigint identity(1,1) primary key,
	loanDate date,
	dueDate date,
	returnDate date,
	status varchar(255),
	totalBooks int,
	overdueDays int,
	fineAmount decimal(10,2),
	reminderSent bit,

	user_id bigint,
	CONSTRAINT FK_Loan_User FOREIGN KEY (user_id) REFERENCES [User](id) ON DELETE CASCADE
);

create table Reservation(
	id bigint identity(1,1) primary key,
	reservationDate date,
	expiryDate date,
	status varchar(255),
	priority int,

	user_id bigint,
	book_id bigint,
	CONSTRAINT FK_Res_User FOREIGN KEY (user_id) REFERENCES [User](id) ON DELETE CASCADE,
    CONSTRAINT FK_Res_Book FOREIGN KEY (book_id) REFERENCES Book(id) ON DELETE CASCADE
);

create table Fine(
	id bigint identity(1,1) primary key,
	amount decimal(10,2),
	reason bit,
	issuedDate date,
	dueDate date,
	status bit,

	loan_id bigint,
	CONSTRAINT FK_Fine_Loan FOREIGN KEY (loan_id) REFERENCES Loan(id) ON DELETE CASCADE
);

create table Payment(
	id bigint identity(1,1) primary key,
	amount decimal(10,2),
	paymentDate date,
	method nvarchar(255),
	receiptNumber nvarchar(255),
	status nvarchar(255),

	fine_id bigint,
	CONSTRAINT FK_Payment_Fine FOREIGN KEY (fine_id) REFERENCES Fine(id) ON DELETE CASCADE
);

create table AuditLog(
	id bigint identity(1,1) primary key,
	actorType bit,
	action nvarchar(255),
	targetType nvarchar(255),
	timestamp DATETIME DEFAULT GETDATE(),
	description nvarchar(255)
);

create table Book_author(
	book_id bigint,
	author_id bigint,
	primary key(book_id, author_id),
	CONSTRAINT FK_BA_Book FOREIGN KEY (book_id) REFERENCES Book(id) ON DELETE CASCADE,
    CONSTRAINT FK_BA_Author FOREIGN KEY (author_id) REFERENCES Author(id) ON DELETE CASCADE
);

create table Book_category(
	book_id bigint, 
	category_id bigint,
	primary key(book_id, category_id),
	CONSTRAINT FK_BC_Book FOREIGN KEY (book_id) REFERENCES Book(id) ON DELETE CASCADE,
    CONSTRAINT FK_BC_Category FOREIGN KEY (category_id) REFERENCES Category(id) ON DELETE CASCADE
);

--insert o day vi bang category co quan he n-n vs bang book nen du lieu cho cot title book va combobox se ko lay o bang book ma la o day
-- NOTE 1: Bat buoc phai chen du lieu vao bang trung gian Book_category 
-- thi Thymeleaf moi co du lieu hien thi o cot 'Title books'.


--MAY CAI O CHO NAY SE DC CHAY CUOI CUNG


-- Gan cuon sach 'Java' (ID 1) vao the loai 'Programming' (ID 1)
INSERT INTO Book_category(book_id, category_id) VALUES (1, 1);

-- Gan cuon sach 'SQL' (ID 2) vao the loai 'Database' (ID 2)
INSERT INTO Book_category(book_id, category_id) VALUES (2, 2);

-- Gan cuon sach 'Python' (ID 3) vao the loai 'Programming' (ID 1) 
-- Mot cuon sach co the thuoc nhieu the loai (Many-to-Many)
INSERT INTO Book_category(book_id, category_id) VALUES (3, 1);
INSERT INTO Book_category(book_id, category_id) VALUES (3, 4);

-- Gan cuon sach 'Spring' (ID 9) vao the loai 'Web Development' (ID 3)
INSERT INTO Book_category(book_id, category_id) VALUES (9, 3);

-- NOTE 2: Sau khi chay xong cac cau lenh tren, hay F5 lai trinh duyet 
-- de kiem tra ket qua o cot Title books.

-- NOTE 3: Neu van khong hien, hay kiem tra lai xem trong file Category.java 
-- da co dong: @ManyToMany(fetch = FetchType.EAGER) chua.


create table Loan_detail(
	id bigint identity(1,1) primary key,
	loan_id bigint,
	book_id bigint,
	quantity int,
	CONSTRAINT FK_LD_Loan FOREIGN KEY (loan_id) REFERENCES Loan(id) ON DELETE CASCADE,
    CONSTRAINT FK_LD_Book FOREIGN KEY (book_id) REFERENCES Book(id) ON DELETE CASCADE
);

USE QLTV
GO

-- 1. bang doc lap (bang cha)
INSERT INTO Role(name, permissionLevel, maxReservationLimit) VALUES
('Student',1,3),('Student',1,3),('Student',1,3),
('Librarian',2,5),('Librarian',2,5),
('Admin',3,10),('Admin',3,10),
('Student',1,3),('Student',1,3),
('Librarian',2,5),('Student',1,3),
('Admin',3,10),('Student',1,3),
('Librarian',2,5),('Student',1,3);

INSERT INTO Membership(startDate,endDate,status,maxLoanBooks,maxReservationBooks) VALUES
('2025-01-01','2026-01-01',1,5,3),('2025-01-01','2026-01-01',1,5,3),
('2025-01-01','2026-01-01',1,5,3),('2025-01-01','2026-01-01',1,10,5),
('2025-01-01','2026-01-01',1,10,5),('2025-01-01','2026-01-01',1,15,10),
('2025-01-01','2026-01-01',1,15,10),('2025-01-01','2026-01-01',1,5,3),
('2025-01-01','2026-01-01',1,5,3),('2025-01-01','2026-01-01',1,10,5),
('2025-01-01','2026-01-01',1,5,3),('2025-01-01','2026-01-01',1,15,10),
('2025-01-01','2026-01-01',1,5,3),('2025-01-01','2026-01-01',1,10,5),
('2025-01-01','2026-01-01',1,5,3);

INSERT INTO Publisher(name,totalPublishedBooks,averageLoanDays) VALUES
('NXB A',100,7),('NXB B',120,10),('NXB C',80,5),
('NXB D',90,6),('NXB E',110,8),('NXB F',70,5),
('NXB G',60,4),('NXB H',50,6),('NXB I',40,7),
('NXB J',30,8),('NXB K',20,6),('NXB L',10,5),
('NXB M',25,6),('NXB N',35,7),('NXB O',45,8);

INSERT INTO Author(name, activeYears, awardsCount) VALUES
('James Gosling', '1984-present', 5), ('Joshua Bloch', '1996-present', 3),
('Robert C. Martin', '1970-present', 4), ('Martin Fowler', '1980-present', 2),
('Linus Torvalds', '1991-present', 10), ('Guido van Rossum', '1980-present', 6),
('Bjarne Stroustrup', '1975-present', 7), ('Anders Hejlsberg', '1980-present', 5),
('Brendan Eich', '1995-present', 2), ('Ken Thompson', '1960-present', 8),
('Dennis Ritchie', '1967-2011', 9), ('Donald Knuth', '1960-present', 12),
('Ada Lovelace', '1830-1852', 1), ('Alan Turing', '1930-1954', 1),
('Grace Hopper', '1940-1992', 4), ('Bill Gates', '1975-present', 3),
('Steve Jobs', '1976-2011', 2), ('Mark Zuckerberg', '2004-present', 1),
('Elon Musk', '1995-present', 1), ('Jeff Bezos', '1994-present', 1);

INSERT INTO Category(name, averageLoanDays, totalBooks) VALUES
('Programming', 14, 50), ('Database', 10, 30), ('Web Development', 7, 40),
('Data Science', 21, 20), ('AI', 30, 15), ('Mobile Apps', 14, 25),
('Networking', 10, 10), ('Security', 15, 12), ('Cloud Computing', 7, 18),
('Operating Systems', 20, 15), ('Hardware', 14, 8), ('Design', 10, 22),
('Business', 14, 35), ('Economics', 14, 28), ('Mathematics', 30, 40),
('Physics', 30, 25), ('History', 14, 60), ('Literature', 14, 100),
('Art', 14, 30), ('Cookbook', 7, 15);

INSERT INTO AuditLog(actorType, action, targetType, description) VALUES
(1, 'LOGIN', 'USER', 'User logged in'), (1, 'SEARCH', 'BOOK', 'User searched for Java'),
(0, 'BACKUP', 'SYSTEM', 'Daily database backup'), (1, 'CREATE', 'LOAN', 'Librarian created loan ID 1'),
(1, 'UPDATE', 'USER', 'User updated profile'), (1, 'DELETE', 'RESERVATION', 'User cancelled reservation'),
(1, 'CREATE', 'BOOK', 'Admin added new book'), (1, 'UPDATE', 'BOOK', 'Admin updated book copies'),
(0, 'ALERT', 'LOAN', 'Overdue reminder sent to User 3'), (1, 'LOGIN', 'LIBRARIAN', 'Librarian logged in'),
(1, 'PAY', 'FINE', 'User paid fine ID 1'), (1, 'RETURN', 'BOOK', 'Book returned for Loan 3'),
(1, 'CREATE', 'MEMBERSHIP', 'Admin created new membership'), (1, 'UPDATE', 'ROLE', 'Admin modified Student role'),
(1, 'LOGOUT', 'USER', 'User logged out'), (0, 'SYSTEM', 'AUTO-CLEAN', 'Expired reservations cleaned'),
(1, 'EXPORT', 'REPORT', 'Librarian exported monthly report'), (1, 'SEARCH', 'AUTHOR', 'User searched for Knuth'),
(1, 'LOGIN', 'ADMIN', 'Admin logged in'), (0, 'ERROR', 'PAYMENT', 'Payment gateway timeout');

--sua lai status cua user tu nvarchar thanh bit
-- 2. B?ng ph? thu?c c?p 1 (Con c?a các b?ng trên)
INSERT INTO [User](name, email, status, membershipLevel, maxLoanLimit, currentLoanCount, overdueCount, reservationLimit, penaltyBalance, role_id, membership_id) VALUES 
('Nguyen Van A', 'a@gmail.com', 'Active', 1, 5, 0, 0, 3, 0, 1, 1),
('Tran Thi B', 'b@gmail.com', 'Active', 1, 5, 1, 0, 3, 0, 2, 2),
('Le Van C', 'c@gmail.com', 'Active', 1, 10, 2, 1, 5, 10, 3, 3),
('Pham Minh D', 'd@gmail.com', 'Active', 1, 5, 0, 0, 3, 0, 4, 4),
('Hoang Lan E', 'e@gmail.com', 'Active', 1, 5, 0, 0, 3, 0, 5, 5),
('Do Hung F', 'f@gmail.com', 'Active', 1, 5, 0, 0, 3, 0, 6, 6),
('Bui Bich G', 'g@gmail.com', 'Active', 1, 10, 0, 0, 5, 0, 7, 7),
('Dang Van H', 'h@gmail.com', 'Active', 1, 5, 0, 0, 3, 0, 8, 8),
('Ngo Kien I', 'i@gmail.com', 'Active', 1, 5, 0, 0, 3, 0, 9, 9),
('Vu Lan J', 'j@gmail.com', 'Active', 1, 5, 0, 0, 3, 0, 10, 10),
('Ly Thanh K', 'k@gmail.com', 'Active', 1, 5, 0, 0, 3, 0, 11, 11),
('Trinh Van L', 'l@gmail.com', 'Active', 1, 5, 0, 0, 3, 0, 12, 12),
('Mai Hong M', 'm@gmail.com', 'Active', 1, 5, 0, 0, 3, 0, 13, 13),
('Dao Duc N', 'n@gmail.com', 'Active', 1, 5, 0, 0, 3, 0, 14, 14),
('Phan Kim O', 'o@gmail.com', 'Active', 1, 5, 0, 0, 3, 0, 15, 15);

INSERT INTO Book(title,ISBN,language,edition,totalCopies,availableCopies,minLoanDays,maxLoanDays,popularityScore,publisher_id) VALUES
('Java','111','EN','1',10,5,1,7,4.5,1),
('SQL','112','EN','1',10,6,1,7,4.2,2),
('Python','113','EN','2',12,7,1,7,4.8,3),
('C++','114','EN','1',8,4,1,7,4.0,4),
('JS','115','EN','3',9,5,1,7,4.3,5),
('HTML','116','EN','1',7,3,1,7,3.9,6),
('CSS','117','EN','1',6,2,1,7,3.8,7),
('React','118','EN','2',11,6,1,7,4.6,8),
('Spring','119','EN','2',13,7,1,7,4.7,9),
('Node','120','EN','1',9,5,1,7,4.1,10),
('PHP','121','EN','1',10,5,1,7,3.7,11),
('Go','122','EN','1',8,4,1,7,4.0,12),
('Rust','123','EN','1',7,3,1,7,4.2,13),
('Kotlin','124','EN','1',6,2,1,7,4.3,14),
('Swift','125','EN','1',5,2,1,7,4.1,15);

-- 3. B?ng ph? thu?c c?p 2 (Con c?a User ho?c Book)
INSERT INTO Loan(loanDate,dueDate,returnDate,status,totalBooks,overdueDays,fineAmount,reminderSent,user_id) VALUES
('2026-01-01','2026-01-10',NULL,'active',2,0,0,0,1),
('2026-01-02','2026-01-11',NULL,'active',1,0,0,0,2),
('2026-01-03','2026-01-12','2026-01-13','completed',1,1,10,1,3),
('2026-01-04','2026-01-13',NULL,'active',3,0,0,0,4),
('2026-01-05','2026-01-14',NULL,'active',2,0,0,0,5),
('2026-01-06','2026-01-15',NULL,'active',1,0,0,0,6),
('2026-01-07','2026-01-16',NULL,'active',2,0,0,0,7),
('2026-01-08','2026-01-17',NULL,'active',1,0,0,0,8),
('2026-01-09','2026-01-18',NULL,'active',2,0,0,0,9),
('2026-01-10','2026-01-19',NULL,'active',1,0,0,0,10),
('2026-01-11','2026-01-20',NULL,'active',2,0,0,0,11),
('2026-01-12','2026-01-21',NULL,'active',1,0,0,0,12),
('2026-01-13','2026-01-22',NULL,'active',2,0,0,0,13),
('2026-01-14','2026-01-23',NULL,'active',1,0,0,0,14),
('2026-01-15','2026-01-24',NULL,'active',2,0,0,0,15);

INSERT INTO Reservation(reservationDate, expiryDate, status, priority, user_id, book_id) VALUES
('2026-03-01', '2026-03-05', 'Pending', 1, 1, 1), ('2026-03-01', '2026-03-05', 'Pending', 2, 2, 2),
('2026-03-02', '2026-03-06', 'Cancelled', 1, 3, 3), ('2026-03-02', '2026-03-06', 'Completed', 3, 4, 4),
('2026-03-03', '2026-03-07', 'Pending', 1, 5, 5), ('2026-03-03', '2026-03-07', 'Pending', 2, 6, 6),
('2026-03-04', '2026-03-08', 'Pending', 1, 7, 7), ('2026-03-04', '2026-03-08', 'Pending', 1, 8, 8),
('2026-03-05', '2026-03-09', 'Pending', 2, 9, 9), ('2026-03-05', '2026-03-09', 'Pending', 1, 10, 10),
('2026-03-06', '2026-03-10', 'Pending', 3, 11, 11), ('2026-03-06', '2026-03-10', 'Pending', 1, 12, 12),
('2026-03-07', '2026-03-11', 'Pending', 2, 13, 13), ('2026-03-07', '2026-03-11', 'Pending', 1, 14, 14),
('2026-03-08', '2026-03-12', 'Pending', 1, 15, 15), ('2026-03-08', '2026-03-12', 'Pending', 2, 1, 5),
('2026-03-09', '2026-03-13', 'Pending', 1, 2, 6), ('2026-03-09', '2026-03-13', 'Pending', 1, 3, 7),
('2026-03-10', '2026-03-14', 'Pending', 2, 4, 8), ('2026-03-10', '2026-03-14', 'Pending', 1, 5, 9);

-- 4. B?ng ph? thu?c c?p 3 (Con c?a Loan)
INSERT INTO Loan_Detail(loan_id,book_id,quantity) VALUES
(1,1,1),(1,2,1),(2,3,1),(3,4,1),(4,5,1),
(5,6,1),(6,7,1),(7,8,1),(8,9,1),(9,10,1),
(10,11,1),(11,12,1),(12,13,1),(13,14,1),(14,15,1);

INSERT INTO Fine(amount, reason, issuedDate, dueDate, status, loan_id) VALUES
(10.0, 0, '2026-02-01', '2026-02-15', 1, 1), (20.0, 0, '2026-02-02', '2026-02-16', 1, 2),
(50.0, 1, '2026-02-03', '2026-02-17', 0, 3), (15.0, 0, '2026-02-04', '2026-02-18', 1, 4),
(10.0, 0, '2026-02-05', '2026-02-19', 0, 5), (30.0, 1, '2026-02-06', '2026-02-20', 1, 6),
(12.0, 0, '2026-02-07', '2026-02-21', 1, 7), (25.0, 0, '2026-02-08', '2026-02-22', 0, 8),
(10.0, 0, '2026-02-09', '2026-02-23', 1, 9), (40.0, 1, '2026-02-10', '2026-02-24', 0, 10),
(18.0, 0, '2026-02-11', '2026-02-25', 1, 11), (22.0, 0, '2026-02-12', '2026-02-26', 1, 12),
(10.0, 0, '2026-02-13', '2026-02-27', 0, 13), (10.0, 0, '2026-02-14', '2026-02-28', 1, 14),
(60.0, 1, '2026-02-15', '2026-03-01', 0, 15), (10.0, 0, '2026-02-16', '2026-03-02', 1, 1),
(15.0, 0, '2026-02-17', '2026-03-03', 1, 2), (20.0, 0, '2026-02-18', '2026-03-04', 0, 3),
(10.0, 0, '2026-02-19', '2026-03-05', 1, 4), (10.0, 0, '2026-02-20', '2026-03-06', 1, 5);

-- 5. B?ng ph? thu?c c?p cu?i (Con c?a Fine)
INSERT INTO Payment(amount, paymentDate, method, receiptNumber, status, fine_id) VALUES
(10.0, '2026-02-10', 'Cash', 'REC001', 'Success', 1), (20.0, '2026-02-11', 'Momo', 'REC002', 'Success', 2),
(15.0, '2026-02-12', 'Bank Transfer', 'REC003', 'Success', 4), (30.0, '2026-02-13', 'Cash', 'REC004', 'Success', 6),
(12.0, '2026-02-14', 'Momo', 'REC005', 'Success', 7), (10.0, '2026-02-15', 'Cash', 'REC006', 'Success', 9),
(18.0, '2026-02-16', 'Bank Transfer', 'REC007', 'Success', 11), (22.0, '2026-02-17', 'Cash', 'REC008', 'Success', 12),
(10.0, '2026-02-18', 'Momo', 'REC009', 'Success', 14), (10.0, '2026-02-19', 'Cash', 'REC010', 'Success', 16),
(15.0, '2026-02-20', 'Bank Transfer', 'REC011', 'Success', 17), (10.0, '2026-02-21', 'Cash', 'REC012', 'Success', 19),
(10.0, '2026-02-22', 'Cash', 'REC013', 'Success', 20), (5.0, '2026-02-23', 'Momo', 'REC014', 'Pending', 3),
(5.0, '2026-02-24', 'Cash', 'REC015', 'Success', 5), (10.0, '2026-02-25', 'Bank Transfer', 'REC016', 'Success', 1),
(20.0, '2026-02-26', 'Cash', 'REC017', 'Success', 2), (10.0, '2026-02-27', 'Momo', 'REC018', 'Success', 4),
(10.0, '2026-02-28', 'Cash', 'REC019', 'Success', 6), (10.0, '2026-03-01', 'Bank Transfer', 'REC020', 'Success', 7);