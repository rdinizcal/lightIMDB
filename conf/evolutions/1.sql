# --- !Ups

CREATE TABLE TB_USUARIO (
  ID 		INTEGER NOT NULL AUTO_INCREMENT, 
  EMAIL 	VARCHAR(50), 
  SENHA 	VARCHAR(12),
  
  PRIMARY KEY (ID)
); 


CREATE TABLE TB_FILME (
  ID 			INTEGER NOT NULL AUTO_INCREMENT, 
  TITULO 		VARCHAR(50), 
  DIRETOR 		VARCHAR(50),
  ANO_PRODUCAO 	INTEGER(4),
  
  PRIMARY KEY (ID)
); 

CREATE TABLE TB_FILME_USUARIO (
	 ID_USUARIO	INTEGER UNSIGNED REFERENCES TB_USUARIO(ID),
	 ID_FILME	INTEGER UNSIGNED REFERENCES TB_FILME(ID),
	 NOTA 		INTEGER(1)
);

ALTER TABLE TB_FILME_USUARIO
ADD PRIMARY KEY (ID_USUARIO, ID_FILME);

INSERT INTO TB_FILME (id, titulo, diretor, ano_producao) values (0, 'A Era do Gelo 3', 'Carlos Saldanha', 2009)
INSERT INTO TB_FILME (id, titulo, diretor, ano_producao) values (1, 'Laranja Mecânica', 'Stanley Kubrick', 1971)
INSERT INTO TB_FILME (id, titulo, diretor, ano_producao) values (2, 'Piratas do Caribe', 'Espen Sandberg', 2003)
INSERT INTO TB_FILME (id, titulo, diretor, ano_producao) values (3, 'Piratas do Caribe 2', 'Joachim Rønning', 2009)
INSERT INTO TB_FILME (id, titulo, diretor, ano_producao) values (4, 'Mulher Maravilha', 'Patty Jenkins', 2017)

# --- !Downs

DROP TABLE User;