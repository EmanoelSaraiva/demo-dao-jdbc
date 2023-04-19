------TABLES
CREATE TABLE Department_ww(
       id number,
       name varchar2(100),
       constraint pk_id_Department_ww primary key (id)
);

CREATE TABLE Seller_ww(
       id number,
       name varchar2(100),
       email varchar2(100),
       birthDate Date,
       baseSalary number(10,2),
       department_id number,
       constraint pk_id_Seller_ww primary key (id),
       constraint fk_id_Department_ww foreign key (department_id) references Department_ww(id)
);

------SEQUENCES
create sequence SEQ_id_Department_ww
minvalue 1
maxvalue 9999999999
start with 1
increment by 1
nocache
cycle;

create sequence SEQ_id_Seller_ww
minvalue 1
maxvalue 9999999999
start with 1
increment by 1
nocache
cycle;

------TRIGGERS
CREATE OR REPLACE TRIGGER TRG_Department_ww
    BEFORE 
    INSERT 
    ON Department_ww
    FOR EACH ROW    
BEGIN
   
    :NEW.id := SEQ_id_Department_ww.Nextval;

END;

CREATE OR REPLACE TRIGGER TRG_Seller_ww
    BEFORE 
    INSERT 
    ON Seller_ww
    FOR EACH ROW    
BEGIN
   
    :NEW.id := SEQ_id_Seller_ww.Nextval;

END;

commit;

------

select
*
from Seller_ww;

select 
*
from Department_ww;


INSERT INTO Department_ww (Name) VALUES ('Computers');
  ('Electronics'),
  ('Fashion'),
  ('Books');
  
  
INSERT ALL
   INTO Department_ww (Name) VALUES ('Electronics')
   INTO Department_ww (Name) VALUES ('Fashion')
   INTO Department_ww (Name) VALUES ('Books')
SELECT 1 FROM DUAL;  

INSERT ALL
   INTO Seller_ww (Name, Email, BirthDate, BaseSalary, Department_Id) 
        VALUES ('Bob Brown','bob@gmail.com','21-04-1998',1000,1)
   INTO Seller_ww (Name, Email, BirthDate, BaseSalary, Department_Id) 
        VALUES ('Maria Green','maria@gmail.com','31-12-1979',3500,2)
   INTO Seller_ww (Name, Email, BirthDate, BaseSalary, Department_Id) 
        VALUES ('Alex Grey','alex@gmail.com','15-01-1988',2200,1)
   INTO Seller_ww (Name, Email, BirthDate, BaseSalary, Department_Id) 
        VALUES ('Martha Red','martha@gmail.com','30-11-1993',3000,4)
   INTO Seller_ww (Name, Email, BirthDate, BaseSalary, Department_Id) 
        VALUES ('Donald Blue','donald@gmail.com','09-01-2000',4000,3)
   INTO Seller_ww (Name, Email, BirthDate, BaseSalary, Department_Id) 
        VALUES ('Alex Pink','bob@gmail.com','04-03-1997',3000,2)
SELECT 1 FROM DUAL;  
  













