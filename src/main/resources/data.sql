-- USER
-- hashed password: letmein
INSERT INTO usuario_seguranca (id, username, password, primeiro_nome, ultimo_nome) VALUES
(1,  'admin', '$2a$12$ZhGS.zcWt1gnZ9xRNp7inOvo5hIT0ngN7N.pN939cShxKvaQYHnnu', 'Administrador', 'Adminstrador');

-- ROLES

INSERT INTO funcao_seguranca (id, nome_funcao, descricao) VALUES (1, 'ROLE_ADMIN_SIGO', 'Administrador');
INSERT INTO funcao_seguranca (id, nome_funcao, descricao) VALUES (2, 'ROLE_SIGO_GPI', 'Gestão de Processo Industrial');
INSERT INTO funcao_seguranca (id, nome_funcao, descricao) VALUES (3, 'ROLE_SIGO_CA', 'Consultorias e Assessorias');
INSERT INTO funcao_seguranca (id, nome_funcao, descricao) VALUES (4, 'ROLE_SIGO_GN', 'Gestão de Normas');
INSERT INTO usuario_funcao (usuario_id, funcao_id) VALUES (1, 1);
INSERT INTO usuario_funcao (usuario_id, funcao_id) VALUES (1, 2);
INSERT INTO usuario_funcao(usuario_id, funcao_id) VALUES (1, 3);
INSERT INTO usuario_funcao(usuario_id, funcao_id) VALUES (1, 4);
