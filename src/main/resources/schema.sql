
CREATE TABLE funcao_seguranca (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  descricao varchar(100) DEFAULT NULL,
  nome_funcao varchar(100) DEFAULT NULL
);


CREATE TABLE usuario_seguranca (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  username varchar(30) NOT NULL,
  password varchar(200) NOT NULL,
  primeiro_nome varchar(50) NOT NULL,
  ultimo_nome varchar(50) NOT NULL
);


CREATE TABLE usuario_funcao (
  usuario_id BIGINT NOT NULL,
  funcao_id BIGINT NOT NULL,
  CONSTRAINT FK_SEGURANCA_USUARIO_ID FOREIGN KEY (usuario_id) REFERENCES usuario_seguranca (id),
  CONSTRAINT FK_SEGURANCA_FUNCAO_ID FOREIGN KEY (funcao_id) REFERENCES funcao_seguranca (id)
);


