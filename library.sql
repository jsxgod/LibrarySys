USE Library;

DROP DATABASE Library;

CREATE DATABASE Library;

USE Library;
SET NAMES 'utf8';
SET CHARACTER SET 'utf8';

CREATE TABLE Employees (
  pesel CHAR(11) NOT NULL,
  name VARCHAR(32) NOT NULL,
  surname VARCHAR(32) NOT NULL,
  email VARCHAR(64),
  birthday DATE,
  salary INT UNSIGNED,
  bankAccount VARCHAR(34),
  PRIMARY KEY(pesel)
);

CREATE TABLE Users (
  pesel CHAR(11)  NOT NULL,
  hash CHAR(64),
  salt VARCHAR(64) NOT NULL,
  perm INT UNSIGNED,
  FOREIGN KEY(pesel) REFERENCES Employees(pesel)
);

CREATE TABLE Readers (
  pesel CHAR(11) NOT NULL,
  name VARCHAR(32) NOT NULL,
  surname VARCHAR(32) NOT NULL,
  email VARCHAR(64),
  birthday DATE,
  isBlocked BOOLEAN,
  PRIMARY KEY(pesel)
);

CREATE TABLE Titles (
  isbn VARCHAR(13),
  authorName VARCHAR(64),
  title VARCHAR(64),
  publisher VARCHAR(64),
  year INT UNSIGNED,
  PRIMARY KEY(isbn)
);

CREATE TABLE Books (
  bookID INT UNSIGNED AUTO_INCREMENT,
  isbn VARCHAR(13),
  PRIMARY KEY(bookID),
  status ENUM ('free','borrowed'),
  FOREIGN KEY(isbn) REFERENCES Titles(isbn)
);

CREATE TABLE Borrows (
  borrowID INT UNSIGNED AUTO_INCREMENT,
  bookID INT UNSIGNED NOT NULL,
  pesel CHAR(11) NOT NULL,
  start DATE NOT NULL,
  stop DATE,
  PRIMARY KEY(borrowID),
  FOREIGN KEY(pesel) REFERENCES Readers(pesel),
  FOREIGN KEY(bookID) REFERENCES Books(bookID)
);

CREATE TABLE Authors (
  authorName VARCHAR(64),
  description VARCHAR(512),
  PRIMARY KEY(authorName)
);

CREATE TABLE AuthorsTitles (
  isbn VARCHAR(13),
  authorName VARCHAR(64),
  FOREIGN KEY(isbn) REFERENCES Titles(isbn),
  FOREIGN KEY(authorName) REFERENCES Authors(authorName)
);

DELIMITER $$;
DROP TRIGGER IF EXISTS updateAuthor;
CREATE TRIGGER updateAuthor BEFORE INSERT ON titles FOR EACH ROW
  BEGIN
    IF NEW.authorName NOT IN (SELECT authorName from authors) THEN
      INSERT INTO authors VALUES (NEW.authorName, '');
    END IF;
  END $$;
DELIMITER ;

DELIMITER $$
DROP TRIGGER IF EXISTS updateAuthorsTitles;
CREATE TRIGGER updateAuthorsTitles AFTER INSERT ON titles FOR EACH ROW
  BEGIN
    INSERT INTO AuthorsTitles VALUES (NEW.isbn, NEW.authorName);
  END $$;
DELIMITER ;

DELIMITER $$
CREATE TRIGGER setBirthdayEmployee BEFORE INSERT ON Employees FOR EACH ROW
  BEGIN
    SET NEW.birthday = generateBirthdayFromPesel(NEW.pesel);
  END $$
DELIMITER ;

DELIMITER $$
CREATE TRIGGER setBirthdayReaders BEFORE INSERT ON Readers FOR EACH ROW
  BEGIN
    SET NEW.birthday = generateBirthdayFromPesel(NEW.pesel);
  END $$
DELIMITER ;

DROP TRIGGER IF EXISTS checkReadersPESEL;
DELIMITER $$
CREATE TRIGGER checkReadersPESEL BEFORE INSERT ON Readers FOR EACH ROW
     BEGIN
          IF SUBSTR(NEW.PESEL,1,2) >99
             OR SUBSTR(NEW.PESEL,1,2) < 30
             OR SUBSTR(NEW.PESEL,3,2) < 1
             OR SUBSTR(NEW.PESEL,3,2) > 12
             OR SUBSTR(NEW.PESEL,5,2) < 1
             OR SUBSTR(NEW.PESEL,5,2) > 31
             OR CHAR_LENGTH(NEW.PESEL) <> 11
             OR MOD(SUBSTR(NEW.PESEL,1,1)*9
                + SUBSTR(NEW.PESEL,2,1)*7
                + SUBSTR(NEW.PESEL,3,1)*3
                + SUBSTR(NEW.PESEL,4,1)*1
                + SUBSTR(NEW.PESEL,5,1)*9
                + SUBSTR(NEW.PESEL,6,1)*7
                + SUBSTR(NEW.PESEL,7,1)*3
                + SUBSTR(NEW.PESEL,8,1)*1
                + SUBSTR(NEW.PESEL,9,1)*9
                + SUBSTR(NEW.PESEL,10,1)*7,10)
                <> SUBSTR(NEW.PESEL,11,1)
          THEN
               SIGNAL SQLSTATE '45000';
               SET NEW.PESEL = NULL;
          END IF;
     END;
DELIMITER ;

DELIMITER $$
CREATE TRIGGER checkEmployeePESEL BEFORE INSERT ON Employees FOR EACH ROW
     BEGIN
          IF SUBSTR(NEW.PESEL,1,2) >99
             OR SUBSTR(NEW.PESEL,1,2) < 30
             OR SUBSTR(NEW.PESEL,3,2) < 1
             OR SUBSTR(NEW.PESEL,3,2) > 12
             OR SUBSTR(NEW.PESEL,5,2) < 1
             OR SUBSTR(NEW.PESEL,5,2) > 31
             OR CHAR_LENGTH(NEW.PESEL) <> 11
             OR MOD(SUBSTR(NEW.PESEL,1,1)*9
                + SUBSTR(NEW.PESEL,2,1)*7
                + SUBSTR(NEW.PESEL,3,1)*3
                + SUBSTR(NEW.PESEL,4,1)*1
                + SUBSTR(NEW.PESEL,5,1)*9
                + SUBSTR(NEW.PESEL,6,1)*7
                + SUBSTR(NEW.PESEL,7,1)*3
                + SUBSTR(NEW.PESEL,8,1)*1
                + SUBSTR(NEW.PESEL,9,1)*9
                + SUBSTR(NEW.PESEL,10,1)*7,10)
                <> SUBSTR(NEW.PESEL,11,1)
          THEN
               SIGNAL SQLSTATE '45000';
               SET NEW.PESEL = NULL;
          END IF;
     END;
DELIMITER ;

DELIMITER $$
CREATE TRIGGER checkISBN BEFORE INSERT ON Titles FOR EACH ROW
  BEGIN
    IF CHAR_LENGTH(NEW.isbn) <> 13 AND CHAR_LENGTH(NEW.ISBN) <> 10 THEN
      SIGNAL SQLSTATE '45000';
      SET NEW.isbn = NULL;
    END IF;
  END;
DELIMITER ;

DELIMITER $$
CREATE TRIGGER setStartAndStop BEFORE INSERT ON Borrows FOR EACH ROW
  BEGIN
    IF ( ISNULL(NEW.start) ) THEN
      SET NEW.start=CURDATE();
    END IF;

    IF ( ISNULL(NEW.stop) ) THEN
      SET NEW.stop=DATE_ADD(CURDATE(), INTERVAL 1 MONTH);
    END IF;
  END $$
DELIMITER ;

DELIMITER $$
CREATE FUNCTION generateBirthdayFromPesel(pesel CHAR(11)) RETURNS DATE
  BEGIN
    RETURN(
     SELECT
       DATE_ADD(DATE_ADD(
                MAKEDATE(1900+SUBSTR(pesel,1,2)*1, 1),
                INTERVAL (SUBSTR(pesel,3,2))-1 MONTH),
                INTERVAL (SUBSTR(pesel,5,2))-1 DAY)
    );
  END $$
DELIMITER ;

DELIMITER $$
DROP PROCEDURE IF EXISTS paySalary;
CREATE PROCEDURE paySalary(IN startBudget INT)
  BEGIN
    DECLARE tBudget INT;
    DECLARE tBankAccount VARCHAR(34);
    DECLARE tSalary INT;
    DECLARE isEnd BOOL;
    DECLARE kursor CURSOR FOR (SELECT bankAccount, salary FROM Employees);
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET isEnd = TRUE;

    SET tBudget = startBudget;
    SET isEnd = FALSE;

    CREATE TEMPORARY TABLE tmp ( fBankAccount VARCHAR(34) ,fSalary INT );

    START TRANSACTION;
    OPEN kursor;
    petla: LOOP
      FETCH kursor INTO tBankAccount, tSalary;
      IF (isEnd) THEN LEAVE petla; END IF;
      SET tBudget = tBudget - tSalary;

      IF (tBudget < 0) THEN
        DROP TABLE tmp;
        ROLLBACK;
        LEAVE petla;
      END IF;
      INSERT INTO tmp VALUES (tBankAccount,tSalary);
    END LOOP;
    CLOSE kursor;
    COMMIT;
    SELECT * FROM tmp;
    DROP TABLE tmp;
  END $$
DELIMITER ;


INSERT INTO Titles (isbn, authorName, title, publisher, year) VALUES ('9788324589463', 'Agata Christie', 'I nie było już nikogo', 'Dolnośląskie', '2010');
INSERT INTO Titles (isbn, authorName, title, publisher, year) VALUES ('9788375780673', 'Andrzej Sapkowski', 'Chrzest ognia', 'Supernowa', '2014');
INSERT INTO Titles (isbn, authorName, title, publisher, year) VALUES ('9788375780659', 'Andrzej Sapkowski', 'Krew elfów', 'Supernowa', '2014');
INSERT INTO Titles (isbn, authorName, title, publisher, year) VALUES ('9788375780642', 'Andrzej Sapkowski', 'Miecz przeznaczenia', 'Supernowa', '2014');
INSERT INTO Titles (isbn, authorName, title, publisher, year) VALUES ('9788375780635', 'Andrzej Sapkowski', 'Ostatnie życzenie', 'Supernowa', '2014');
INSERT INTO Titles (isbn, authorName, title, publisher, year) VALUES ('9788375780697', 'Andrzej Sapkowski', 'Pani jeziora', 'Supernowa', '2014');
INSERT INTO Titles (isbn, authorName, title, publisher, year) VALUES ('9788375780598', 'Andrzej Sapkowski', 'Sezon burz', 'Supernowa', '2013');
INSERT INTO Titles (isbn, authorName, title, publisher, year) VALUES ('9788375781557', 'Andrzej Sapkowski', 'Szpony i kły', 'Supernowa', '2017');
INSERT INTO Titles (isbn, authorName, title, publisher, year) VALUES ('9788375780680', 'Andrzej Sapkowski', 'Wieża jaskółki', 'Supernowa', '2014');
INSERT INTO Titles (isbn, authorName, title, publisher, year) VALUES ('9788375175851', 'Antoine de Saint-Exupéry', 'Mały Książę', 'Greg', '2017');
INSERT INTO Titles (isbn, authorName, title, publisher, year) VALUES ('9788379930012', 'Arthur Conan Doyle', 'Sherlock Holmes. Księga wszystkich dokonań', 'Rea', '2014');
INSERT INTO Titles (isbn, authorName, title, publisher, year) VALUES ('9788372781758', 'C.S. Lewis', 'Opowieści z Narnii. Lew, Czarownica i stara szafa', 'Media Rodzina', '2005');
INSERT INTO Titles (isbn, authorName, title, publisher, year) VALUES ('9788328324800', 'Cay S. Horstmann', 'Java. Podstawy', 'Helion', '2016');
INSERT INTO Titles (isbn, authorName, title, publisher, year) VALUES ('9788328334793', 'Cay S. Horstmann', 'Java. Techniki zaawansowane', 'Helion', '2017');
INSERT INTO Titles (isbn, authorName, title, publisher, year) VALUES ('9788374959056', 'George Orwell ', 'Rok 1984', 'Muza', '2010');
INSERT INTO Titles (isbn, authorName, title, publisher, year) VALUES ('9788380082175', 'J. K. Rowling', 'Harry Potter i Czara Ognia', 'Media Rodzina', '2016');
INSERT INTO Titles (isbn, authorName, title, publisher, year) VALUES ('9788380082236', 'J. K. Rowling', 'Harry Potter i Insygnia Śmierci', 'Media Rodzina', '2016');
INSERT INTO Titles (isbn, authorName, title, publisher, year) VALUES ('9788380082113', 'J. K. Rowling', 'Harry Potter i Kamień Filozoficzny', 'Media Rodzina', '2016');
INSERT INTO Titles (isbn, authorName, title, publisher, year) VALUES ('9788380082137', 'J. K. Rowling', 'Harry Potter i Komnata Tajemnic', 'Media Rodzina', '2016');
INSERT INTO Titles (isbn, authorName, title, publisher, year) VALUES ('9788380082212', 'J. K. Rowling', 'Harry Potter i Książę Półkrwi', 'Media Rodzina', '2016');
INSERT INTO Titles (isbn, authorName, title, publisher, year) VALUES ('9788380082151', 'J. K. Rowling', 'Harry Potter i więzień Azkabanu', 'Media Rodzina', '2016');
INSERT INTO Titles (isbn, authorName, title, publisher, year) VALUES ('9788380082199', 'J. K. Rowling', 'Harry Potter i Zakon Feniksa', 'Media Rodzina', '2016');
INSERT INTO Titles (isbn, authorName, title, publisher, year) VALUES ('9788377856802', 'J.R.R.Tolkien', 'Władca pierścieni. Bractwo Pierścienia', 'Zysk i S-ka', '2015');
INSERT INTO Titles (isbn, authorName, title, publisher, year) VALUES ('9788377856819', 'J.R.R.Tolkien', 'Władca pierścieni. Dwie wieże', 'Zysk i S-ka', '2015');
INSERT INTO Titles (isbn, authorName, title, publisher, year) VALUES ('9788377856826', 'J.R.R.Tolkien', 'Władca pierścieni. Powrót króla', 'Zysk i S-ka', '2015');
INSERT INTO Titles (isbn, authorName, title, publisher, year) VALUES ('9788371251114', 'Jacek Cichoń', 'Wykłady z wstępu do matematyki', 'Dolnośląskie Wydawnictwo Edukacyjne', '2003');
INSERT INTO Titles (isbn, authorName, title, publisher, year) VALUES ('9788375174830', 'Lucy Maud Montgomery', 'Ania z Zielonego Wzgórza', 'Greg', '2016');
INSERT INTO Titles (isbn, authorName, title, publisher, year) VALUES ('9788379859603', 'Margaret Mitchell', 'Przeminęło z wiatrem. Tom 1', 'Albatros', '2017');
INSERT INTO Titles (isbn, authorName, title, publisher, year) VALUES ('9788364488924', 'Paulo Coelho', 'Alchemik ', 'Drzewo Babel', '2015');

INSERT INTO Readers (pesel, name, surname, email, birthday) VALUES ('89021101455', 'Wojciech', 'Wodo', 'wojciech.wodo@drop.database', '1989-02-11');
INSERT INTO Readers (pesel, name, surname, email, birthday) VALUES ('46040138259', 'Halina', 'Laskowska', 'halina.laskowska@gazeta.pl', '1946-04-01');
INSERT INTO Readers (pesel, name, surname, email, birthday) VALUES ('41081949813', 'Filip', 'Gajda', 'filip.gajda@gmail.com', '1941-08-19');
INSERT INTO Readers (pesel, name, surname, email, birthday) VALUES ('90111974545', 'Angelika', 'Maciejewska', 'angelika.maciejewska@poczta.onet.pl', '1990-11-19');
INSERT INTO Readers (pesel, name, surname, email, birthday) VALUES ('85102898696', 'Renata', 'Piotrowska', 'renata.piotrowska@interia.fm', '1985-10-28');
INSERT INTO Readers (pesel, name, surname, email, birthday) VALUES ('50092116695', 'Konrad', 'Wójtowicz', 'konrad.wojtowicz@poczta.onet.pl', '1950-09-21');
INSERT INTO Readers (pesel, name, surname, email, birthday) VALUES ('51092375471', 'Krystyna', 'Kaźmierczak', 'krystyna.kazmierczak@gmail.com', '1951-09-23');
INSERT INTO Readers (pesel, name, surname, email, birthday) VALUES ('90081072919', 'Kacper', 'Rogowski', 'kacper.rogowski@wp.pl', '1990-08-10');
INSERT INTO Readers (pesel, name, surname, email, birthday) VALUES ('55020769138', 'Bożena', 'Tomczyk', 'bozena.tomczyk@gmail.com', '1955-02-07');
INSERT INTO Readers (pesel, name, surname, email, birthday) VALUES ('69032031879', 'Marta', 'Sikora', 'marta.sikora@o2.pl', '1969-03-20');
INSERT INTO Readers (pesel, name, surname, email, birthday) VALUES ('94070924399', 'Kajetan', 'Michalski', 'kajetan.michalski@gmail.com', '1994-07-09');
INSERT INTO Readers (pesel, name, surname, email, birthday) VALUES ('91041441354', 'Dagmara', 'Brzozowska', 'dagmara.brzozowska@gmail.com', '1991-04-14');
INSERT INTO Readers (pesel, name, surname, email, birthday) VALUES ('63071321672', 'Irmina', 'Michalak', 'irmina.michalak@wp.pl', '1963-07-13');
INSERT INTO Readers (pesel, name, surname, email, birthday) VALUES ('93010331301', 'Albert', 'Wilczyński', 'albert.wilczynski@o2.pl', '1993-01-03');
INSERT INTO Readers (pesel, name, surname, email, birthday) VALUES ('46101272939', 'Barbara', 'Mazurek', 'barbara.mazurek@interia.fm', '1946-10-12');
INSERT INTO Readers (pesel, name, surname, email, birthday) VALUES ('71080273242', 'Adrian', 'Zieliński', 'adrian.zielinski@gmail.com', '1971-08-02');
INSERT INTO Readers (pesel, name, surname, email, birthday) VALUES ('99031475512', 'Marta', 'Kowal', 'marta.kowal@gmail.com', '1999-03-14');
INSERT INTO Readers (pesel, name, surname, email, birthday) VALUES ('44110751690', 'Halina', 'Kowalczyk', 'halina.kowalczyk@interia.fm', '1944-11-07');
INSERT INTO Readers (pesel, name, surname, email, birthday) VALUES ('53120927656', 'Regina', 'Majchrzak', 'regina.majchrzak@gmail.com', '1953-12-09');
INSERT INTO Readers (pesel, name, surname, email, birthday) VALUES ('53021752065', 'Apolonia', 'Górska', 'apolonia.gorska@gmail.com', '1953-02-17');
INSERT INTO Readers (pesel, name, surname, email, birthday) VALUES ('65060820977', 'Olga', 'Olejniczak', 'olga.olejniczak@o2.pl', '1965-06-08');
INSERT INTO Readers (pesel, name, surname, email, birthday) VALUES ('77070173833', 'Lucjan', 'Jóźwiak', 'lucjan.jozwiak@gmail.com', '1977-07-01');
INSERT INTO Readers (pesel, name, surname, email, birthday) VALUES ('94102633929', 'Adrian', 'Majewski', 'adrian.majewski@gmail.com', '1994-10-26');
INSERT INTO Readers (pesel, name, surname, email, birthday) VALUES ('81080829411', 'Stefania', 'Markiewicz', 'stefania.markiewicz@gmail.com', '1981-08-08');
INSERT INTO Readers (pesel, name, surname, email, birthday) VALUES ('74010316255', 'Kamila', 'Mazur', 'kamila.mazur@gazeta.pl', '1974-01-03');
INSERT INTO Readers (pesel, name, surname, email, birthday) VALUES ('82090488609', 'Anzelm', 'Woźniak', 'anzelm.wozniak@gmail.com', '1982-09-04');
INSERT INTO Readers (pesel, name, surname, email, birthday) VALUES ('75031811796', 'Mateusz', 'Nawrocki', 'mateusz.nawrocki@gmail.com', '1975-03-18');
INSERT INTO Readers (pesel, name, surname, email, birthday) VALUES ('50011895096', 'Juliusz', 'Sobolewski', 'juliusz.sobolewski@gmail.com', '1950-01-18');
INSERT INTO Readers (pesel, name, surname, email, birthday) VALUES ('64080804819', 'Dionizy', 'Laskowski', 'dionizy.laskowski@interia.fm', '1964-08-08');
INSERT INTO Readers (pesel, name, surname, email, birthday) VALUES ('61061069652', 'Żaneta', 'Majchrzak', 'zaneta.majchrzak@wp.pl', '1961-06-10');
INSERT INTO Readers (pesel, name, surname, email, birthday) VALUES ('79122896794', 'Wiktor', 'Żak', 'wiktor.zak@interia.fm', '1979-12-28');
INSERT INTO Readers (pesel, name, surname, email, birthday) VALUES ('58072828150', 'Leopold', 'Szymański', 'leopold.szymanski@gmail.com', '1958-07-28');
INSERT INTO Readers (pesel, name, surname, email, birthday) VALUES ('46072263435', 'Gwidon', 'Bielecki', 'gwidon.bielecki@gmail.com', '1946-07-22');
INSERT INTO Readers (pesel, name, surname, email, birthday) VALUES ('59080344010', 'Maja', 'Czerwińska', 'maja.czerwinska@o2.pl', '1959-08-03');
INSERT INTO Readers (pesel, name, surname, email, birthday) VALUES ('58061526805', 'Apolonia', 'Wawrzyniak', 'apolonia.wawrzyniak@interia.fm', '1958-06-15');
INSERT INTO Readers (pesel, name, surname, email, birthday) VALUES ('81101893571', 'Olgierd', 'Błaszczyk', 'olgierd.blaszczyk@gmail.com', '1981-10-18');
INSERT INTO Readers (pesel, name, surname, email, birthday) VALUES ('79112635561', 'Aureliusz', 'Chmielewski', 'aureliusz.chmielewski@gmail.com', '1979-11-26');
INSERT INTO Readers (pesel, name, surname, email, birthday) VALUES ('77030848490', 'Ireneusz', 'Krupa', 'ireneusz.krupa@interia.fm', '1977-03-08');
INSERT INTO Readers (pesel, name, surname, email, birthday) VALUES ('48041397475', 'Konrad', 'Gajda', 'konrad.gajda@poczta.onet.pl', '1948-04-13');
INSERT INTO Readers (pesel, name, surname, email, birthday) VALUES ('92110841411', 'Stefan', 'Mróz', 'stefan.mroz@wp.pl', '1992-11-08');
INSERT INTO Readers (pesel, name, surname, email, birthday) VALUES ('64011647478', 'Wit', 'Kozak', 'wit.kozak@gmail.com', '1964-01-16');
INSERT INTO Readers (pesel, name, surname, email, birthday) VALUES ('61062507380', 'Adolf', 'Karpiński', 'adolf.karpinski@o2.pl', '1961-06-25');
INSERT INTO Readers (pesel, name, surname, email, birthday) VALUES ('46080693374', 'Ewa', 'Zielińska', 'ewa.zielinska@gmail.com', '1946-08-06');
INSERT INTO Readers (pesel, name, surname, email, birthday) VALUES ('64020621478', 'Ernest', 'Zieliński', 'ernest.zielinski@gmail.com', '1964-02-06');
INSERT INTO Readers (pesel, name, surname, email, birthday) VALUES ('63101764992', 'Krystian', 'Bednarczyk', 'krystian.bednarczyk@gmail.com', '1963-10-17');
INSERT INTO Readers (pesel, name, surname, email, birthday) VALUES ('99031865715', 'Klaudia', 'Marciniak', 'klaudia.marciniak@poczta.onet.pl', '1999-03-18');
INSERT INTO Readers (pesel, name, surname, email, birthday) VALUES ('80071964799', 'Dariusz', 'Krupa', 'dariusz.krupa@gmail.com', '1980-07-19');
INSERT INTO Readers (pesel, name, surname, email, birthday) VALUES ('59091274128', 'Alicja', 'Urbaniak', 'alicja.urbaniak@gmail.com', '1959-09-12');
INSERT INTO Readers (pesel, name, surname, email, birthday) VALUES ('70040530155', 'Ludmiła', 'Bednarek', 'ludmila.bednarek@interia.fm', '1970-04-05');


INSERT INTO Employees (pesel, name, surname, email, birthday, salary, bankAccount) VALUES ('97101612726', 'Aneta', 'Kurek', 'aneta.kurek@gmail.com', '1997-10-16', 2613, '48 2044 8749 0902 3682 2562 8066');
INSERT INTO Employees (pesel, name, surname, email, birthday, salary, bankAccount) VALUES ('51082146614', 'Katarzyna', 'Wójcik', 'katarzyna.wojcik@gmail.com', '1951-08-21', 3809, '80 6159 9326 4561 7271 9400 3366');
INSERT INTO Employees (pesel, name, surname, email, birthday, salary, bankAccount) VALUES ('58090958297', 'Krystian', 'Wróblewski', 'krystian.wroblewski@poczta.onet.pl', '1958-09-09', 4888, '62 9918 6891 5235 3976 1159 7129');
INSERT INTO Employees (pesel, name, surname, email, birthday, salary, bankAccount) VALUES ('93071141510', 'Kacper', 'Urban', 'kacper.urban@gmail.com', '1993-07-11', 4429, '51 0119 4081 4342 4063 4574 6566');
INSERT INTO Employees (pesel, name, surname, email, birthday, salary, bankAccount) VALUES ('59071397696', 'Sandra', 'Bednarska', 'sandra.bednarska@gmail.com', '1959-07-13', 4798, '88 8810 4733 7503 7874 2339 6715');
INSERT INTO Employees (pesel, name, surname, email, birthday, salary, bankAccount) VALUES ('76021738378', 'Krzysztof', 'Adamczyk', 'krzysztof.adamczyk@o2.pl', '1976-02-17', 2441, '86 1120 5083 6269 8396 3496 4152');
INSERT INTO Employees (pesel, name, surname, email, birthday, salary, bankAccount) VALUES ('71032665518', 'Nina', 'Śliwińska', 'nina.sliwinska@gazeta.pl', '1971-03-26', 2595, '90 4345 1457 7690 4129 6559 6455');
INSERT INTO Employees (pesel, name, surname, email, birthday, salary, bankAccount) VALUES ('88011363994', 'Iza', 'Woźniak', 'iza.wozniak@gazeta.pl', '1988-01-13', 4292, '87 9784 7846 9718 5475 0012 9755');
INSERT INTO Employees (pesel, name, surname, email, birthday, salary, bankAccount) VALUES ('75020696517', 'Rozalia', 'Dębska', 'rozalia.debska@interia.fm', '1975-02-06', 2306, '90 0834 2371 7145 7005 5197 4943');
INSERT INTO Employees (pesel, name, surname, email, birthday, salary, bankAccount) VALUES ('96022103041', 'Artur', 'Barański', 'artur.baranski@poczta.onet.pl', '1996-02-21', 3568, '46 8849 0686 1701 1390 8996 1844');
INSERT INTO Employees (pesel, name, surname, email, birthday, salary, bankAccount) VALUES ('76122604493', 'Witold', 'Brzeziński', 'witold.brzezinski@gmail.com', '1976-12-26', 4029, '97 2491 0924 3789 6092 4067 0603');
INSERT INTO Employees (pesel, name, surname, email, birthday, salary, bankAccount) VALUES ('49012763213', 'Bogusław', 'Milewski', 'boguslaw.milewski@poczta.onet.pl', '1949-01-27', 3931, '50 2292 9302 4317 3489 4867 6388');
INSERT INTO Employees (pesel, name, surname, email, birthday, salary, bankAccount) VALUES ('50041759810', 'Roksana', 'Czerwińska', 'roksana.czerwinska@gmail.com', '1950-04-17', 4230, '38 2260 9847 9430 6372 5362 0589');
INSERT INTO Employees (pesel, name, surname, email, birthday, salary, bankAccount) VALUES ('81031660490', 'Rozalia', 'Śliwińska', 'rozalia.sliwinska@gmail.com', '1981-03-16', 4832, '55 9113 1750 5050 8143 8792 0942');
INSERT INTO Employees (pesel, name, surname, email, birthday, salary, bankAccount) VALUES ('42072212394', 'Joanna', 'Woźniak', 'joanna.wozniak@gmail.com', '1942-07-22', 2629, '52 2789 8019 1044 8025 5355 5875');
INSERT INTO Employees (pesel, name, surname, email, birthday, salary, bankAccount) VALUES ('89090610498', 'Ewa', 'Kaźmierczak', 'ewa.kazmierczak@gazeta.pl', '1989-09-06', 4723, '67 1050 5133 0511 1579 7546 0691');
INSERT INTO Employees (pesel, name, surname, email, birthday, salary, bankAccount) VALUES ('86042891895', 'Urszula', 'Borkowska', 'urszula.borkowska@gmail.com', '1986-04-28', 2799, '61 4758 5814 9342 9616 0104 7249');
INSERT INTO Employees (pesel, name, surname, email, birthday, salary, bankAccount) VALUES ('52011660214', 'Iza', 'Brzezińska', 'iza.brzezinska@gmail.com', '1952-01-16', 3145, '33 9031 2882 0468 3568 3430 4387');
INSERT INTO Employees (pesel, name, surname, email, birthday, salary, bankAccount) VALUES ('82111232318', 'Eliza', 'Jabłońska', 'eliza.jablonska@gmail.com', '1982-11-12', 4071, '28 8102 6336 0905 0401 3561 0957');
INSERT INTO Employees (pesel, name, surname, email, birthday, salary, bankAccount) VALUES ('70022615379', 'Mariusz', 'Czech', 'mariusz.czech@gazeta.pl', '1970-02-26', 2805, '88 3280 3293 1294 7931 3542 0249');

INSERT INTO Users (pesel, hash, salt, perm) VALUES ('97101612726', '3a2d1585deedc84033fbe696819061083cc652129ff383f251816ebfbd7f9b13', 'gXJJUvvIKxrb3pPxUxsR1N4W1ROd9bQP', '0');
INSERT INTO Users (pesel, hash, salt, perm) VALUES ('51082146614', '3a2d1585deedc84033fbe696819061083cc652129ff383f251816ebfbd7f9b13', 'gXJJUvvIKxrb3pPxUxsR1N4W1ROd9bQP', '0');
INSERT INTO Users (pesel, hash, salt, perm) VALUES ('58090958297', '3a2d1585deedc84033fbe696819061083cc652129ff383f251816ebfbd7f9b13', 'gXJJUvvIKxrb3pPxUxsR1N4W1ROd9bQP', '0');
INSERT INTO Users (pesel, hash, salt, perm) VALUES ('93071141510', '3a2d1585deedc84033fbe696819061083cc652129ff383f251816ebfbd7f9b13', 'gXJJUvvIKxrb3pPxUxsR1N4W1ROd9bQP', '0');
INSERT INTO Users (pesel, hash, salt, perm) VALUES ('59071397696', '3a2d1585deedc84033fbe696819061083cc652129ff383f251816ebfbd7f9b13', 'gXJJUvvIKxrb3pPxUxsR1N4W1ROd9bQP', '0');
INSERT INTO Users (pesel, hash, salt, perm) VALUES ('76021738378', '3a2d1585deedc84033fbe696819061083cc652129ff383f251816ebfbd7f9b13', 'gXJJUvvIKxrb3pPxUxsR1N4W1ROd9bQP', '0');
INSERT INTO Users (pesel, hash, salt, perm) VALUES ('71032665518', '3a2d1585deedc84033fbe696819061083cc652129ff383f251816ebfbd7f9b13', 'gXJJUvvIKxrb3pPxUxsR1N4W1ROd9bQP', '0');
INSERT INTO Users (pesel, hash, salt, perm) VALUES ('88011363994', '3a2d1585deedc84033fbe696819061083cc652129ff383f251816ebfbd7f9b13', 'gXJJUvvIKxrb3pPxUxsR1N4W1ROd9bQP', '0');
INSERT INTO Users (pesel, hash, salt, perm) VALUES ('75020696517', '3a2d1585deedc84033fbe696819061083cc652129ff383f251816ebfbd7f9b13', 'gXJJUvvIKxrb3pPxUxsR1N4W1ROd9bQP', '0');
INSERT INTO Users (pesel, hash, salt, perm) VALUES ('96022103041', '3a2d1585deedc84033fbe696819061083cc652129ff383f251816ebfbd7f9b13', 'gXJJUvvIKxrb3pPxUxsR1N4W1ROd9bQP', '0');
INSERT INTO Users (pesel, hash, salt, perm) VALUES ('76122604493', '3a2d1585deedc84033fbe696819061083cc652129ff383f251816ebfbd7f9b13', 'gXJJUvvIKxrb3pPxUxsR1N4W1ROd9bQP', '0');
INSERT INTO Users (pesel, hash, salt, perm) VALUES ('49012763213', '3a2d1585deedc84033fbe696819061083cc652129ff383f251816ebfbd7f9b13', 'gXJJUvvIKxrb3pPxUxsR1N4W1ROd9bQP', '0');
INSERT INTO Users (pesel, hash, salt, perm) VALUES ('50041759810', '3a2d1585deedc84033fbe696819061083cc652129ff383f251816ebfbd7f9b13', 'gXJJUvvIKxrb3pPxUxsR1N4W1ROd9bQP', '0');
INSERT INTO Users (pesel, hash, salt, perm) VALUES ('81031660490', '3a2d1585deedc84033fbe696819061083cc652129ff383f251816ebfbd7f9b13', 'gXJJUvvIKxrb3pPxUxsR1N4W1ROd9bQP', '0');
INSERT INTO Users (pesel, hash, salt, perm) VALUES ('42072212394', '3a2d1585deedc84033fbe696819061083cc652129ff383f251816ebfbd7f9b13', 'gXJJUvvIKxrb3pPxUxsR1N4W1ROd9bQP', '0');
INSERT INTO Users (pesel, hash, salt, perm) VALUES ('89090610498', '3a2d1585deedc84033fbe696819061083cc652129ff383f251816ebfbd7f9b13', 'gXJJUvvIKxrb3pPxUxsR1N4W1ROd9bQP', '0');
INSERT INTO Users (pesel, hash, salt, perm) VALUES ('86042891895', '3a2d1585deedc84033fbe696819061083cc652129ff383f251816ebfbd7f9b13', 'gXJJUvvIKxrb3pPxUxsR1N4W1ROd9bQP', '0');
INSERT INTO Users (pesel, hash, salt, perm) VALUES ('52011660214', '3a2d1585deedc84033fbe696819061083cc652129ff383f251816ebfbd7f9b13', 'gXJJUvvIKxrb3pPxUxsR1N4W1ROd9bQP', '0');
INSERT INTO Users (pesel, hash, salt, perm) VALUES ('82111232318', '3a2d1585deedc84033fbe696819061083cc652129ff383f251816ebfbd7f9b13', 'gXJJUvvIKxrb3pPxUxsR1N4W1ROd9bQP', '0');
INSERT INTO Users (pesel, hash, salt, perm) VALUES ('70022615379', '3a2d1585deedc84033fbe696819061083cc652129ff383f251816ebfbd7f9b13', 'gXJJUvvIKxrb3pPxUxsR1N4W1ROd9bQP', '0');



