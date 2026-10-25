-- Dados iniciais opcionais para testar a aplicação.
-- Execute 01-criacao-tabelas.sql antes deste arquivo.
INSERT INTO tb_marca (nome, descricao, ativo)
SELECT 'Acme', 'Marca de exemplo para testes', TRUE
WHERE NOT EXISTS (SELECT 1 FROM tb_marca WHERE LOWER(nome) = LOWER('Acme'));
INSERT INTO tb_marca (nome, descricao, ativo)
SELECT 'Norte Tech', 'Equipamentos e acessórios', TRUE
WHERE NOT EXISTS (SELECT 1 FROM tb_marca WHERE LOWER(nome) = LOWER('Norte Tech'));

INSERT INTO tb_categoria (nome, categoria_id)
SELECT 'Eletrônicos', NULL
WHERE NOT EXISTS (SELECT 1 FROM tb_categoria WHERE nome = 'Eletrônicos' AND categoria_id IS NULL);
INSERT INTO tb_categoria (nome, categoria_id)
SELECT 'Acessórios', c.id FROM tb_categoria c
WHERE c.nome = 'Eletrônicos' AND c.categoria_id IS NULL
AND NOT EXISTS (SELECT 1 FROM tb_categoria x WHERE x.nome = 'Acessórios' AND x.categoria_id = c.id);
INSERT INTO tb_categoria (nome, categoria_id)
SELECT 'Informática', NULL
WHERE NOT EXISTS (SELECT 1 FROM tb_categoria WHERE nome = 'Informática' AND categoria_id IS NULL);

INSERT INTO tb_produto (nome, descricao, preco, quantidade_estoque, marca_id, categoria_id)
SELECT 'Fone de exemplo', 'Produto demonstrativo. Pode ser editado ou excluído.', 149.90, 15, m.id, c.id
FROM tb_marca m JOIN tb_categoria c ON c.nome = 'Acessórios'
WHERE LOWER(m.nome) = LOWER('Acme')
AND NOT EXISTS (SELECT 1 FROM tb_produto p WHERE p.nome = 'Fone de exemplo');
