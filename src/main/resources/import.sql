
INSERT INTO autores (id, nome, email, dataNascimento, biografia) VALUES
(1, 'Machado de Assis', 'machado@academia.com', '1839-06-21', 'Fundador da Academia Brasileira de Letras.'),
(2, 'Clarice Lispector', 'clarice@contos.com', '1920-12-10', 'Autora de romances psicológicos e modernistas.'),
(3, 'José de Alencar', 'alencar@literatura.com', '1829-05-01', 'Romancista e político brasileiro.'),
(4, 'Cecília Meireles', 'cecilia@poesia.com', '1901-11-07', 'Poetisa e educadora.'),
(5, 'Jorge Amado', 'jorge@bahia.com', '1912-08-10', 'Romancista baiano, um dos mais traduzidos do Brasil.'),
(6, 'Graciliano Ramos', 'graciliano@seca.com', '1892-10-27', 'Autor de Vidas Secas.');

INSERT INTO livros (id, titulo, isbn, dataPublicacao, numeroPaginas, disponivel, autor_id) VALUES
(1, 'Dom Casmurro', '9788535921780', '1899-01-01', 256, false, 1),
(2, 'Memórias Póstumas de Brás Cubas', '9788535927850', '1881-01-01', 210, true, 1),
(3, 'A Hora da Estrela', '9788520926355', '1977-01-01', 96, true, 2),
(4, 'Senhora', '9788583862090', '1875-01-01', 320, true, 3),
(5, 'Iracema', '9788525407774', '1865-01-01', 190, true, 3),
(6, 'Romanceiro da Inconfidência', '9788533604630', '1953-01-01', 280, true, 4),
(7, 'Capitães da Areia', '9788520932782', '1937-01-01', 280, false, 5),
(8, 'Gabriela, Cravo e Canela', '9788520933215', '1958-01-01', 360, true, 5),
(9, 'Vidas Secas', '9788520921755', '1938-01-01', 180, true, 6),
(10, 'São Bernardo', '9788520931709', '1934-01-01', 220, true, 6);

INSERT INTO emprestimos (id, nomeUsuario, emailUsuario, dataEmprestimo, dataDevolucaoPrevista, dataDevolucao, observacoes, status, livro_id) VALUES
(1, 'João Pereira', 'joao@email.com', '2025-09-25', '2025-10-05', '2025-10-04', 'Devolvido antes do prazo.', 'DEVOLVIDO', 3),
(2, 'Ana Costa', 'ana@email.com', '2025-09-20', '2025-09-27', NULL, 'Leitura em andamento - aprovado.', 'APROVADO', 1),
(3, 'Carlos Lima', 'carlos@email.com', '2025-10-01', '2025-10-10', NULL, 'Solicitação em análise.', 'PENDENTE', 7);

INSERT INTO usuarios (id, username, nome, email, role) VALUES
(1, 'admin', 'Bibliotecário Admin', 'admin@biblioteca.com', 'ADMIN'),
(2, 'user', 'Leitor Padrão', 'user@biblioteca.com', 'USER');
