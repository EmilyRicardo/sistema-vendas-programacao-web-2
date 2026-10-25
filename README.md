# Sistema de Vendas — CRUD de Marcas, Categorias e Produtos

Projeto acadêmico da disciplina Programação Web 2, com o objetivo de desenvolver o módulo base de um Sistema de Vendas utilizando Java, Maven, Jakarta Faces (JSF), PrimeFaces, Hibernate/JPA e PostgreSQL.

## 1. Funcionalidades

- **Marca:** listagem, cadastro, edição, validação de nome único e ativação/inativação.
- **Categoria:** cadastro, edição, hierarquia com categoria pai, caminho hierárquico e validação de dependências.
- **Produto:** cadastro, edição, exclusão e associação obrigatória a marca e categoria, com validação de preço e estoque.
- **Imagens:** suporte previsto para upload de imagens JPG/JPEG, PNG e WEBP, com limite de 5 MB e exibição de miniaturas.
- **Interface:** páginas JSF com componentes PrimeFaces, tabelas, filtros, paginação, mensagens e confirmações.
- **Arquitetura:** separação em entidades, DAOs, serviços, controllers CDI e conversores Faces.
- **Banco de dados:** scripts SQL para criação das tabelas e inserção de dados de teste.

## 2. Tecnologias utilizadas

- Java
- Apache Maven
- Jakarta Faces (JSF)
- PrimeFaces
- Hibernate/JPA
- PostgreSQL
- Git e GitHub

## 3. Estrutura do projeto

```text
pom.xml
mvnw
mvnw.cmd
src/
  main/
    java/br/edu/vendas/
      model/
      dao/
      service/
      controller/
      converter/
      util/
      web/
    resources/META-INF/
      persistence.xml
    webapp/
sql/
  01-criacao-tabelas.sql
  02-dados-teste.sql
docs/
  screenshots/
README.md
.gitignore
```

## 4. Configuração e execução

Para executar a aplicação, são necessários Java JDK 17 ou superior, PostgreSQL e um servidor de aplicação compatível com as dependências do projeto.

1. Instalar e configurar o Java, o PostgreSQL e um servidor de aplicação compatível.
2. Criar um banco de dados chamado `vendas_db` no PostgreSQL.
3. Executar o script `sql/01-criacao-tabelas.sql` no banco criado.
4. Opcionalmente, executar `sql/02-dados-teste.sql` para inserir registros de exemplo.
5. Configurar a URL, o usuário e a senha do banco no arquivo `src/main/resources/META-INF/persistence.xml`. A senha de exemplo deve ser substituída pela senha configurada localmente.
6. Abrir a pasta do projeto no VS Code e acessar **Terminal > New Terminal**.
7. Executar o comando abaixo no terminal do Windows:

```powershell
.\mvnw.cmd clean package
```

8. Se a compilação terminar com `BUILD SUCCESS`, localizar o arquivo WAR gerado na pasta `target`.
9. Configurar o servidor de aplicação, realizar o deploy e testar os cadastros, as validações, a hierarquia de categorias e o upload de imagens.

O Maven Wrapper auxilia no download do Maven, mas não instala o Java, o PostgreSQL nem o servidor de aplicação.

**Estado da validação:** a execução integrada e o deploy ainda precisam ser realizados e testados no ambiente de execução antes de considerar a aplicação validada.

## 5. Banco de dados

A pasta `sql` contém:

- `01-criacao-tabelas.sql`: criação das tabelas e restrições do banco.
- `02-dados-teste.sql`: inserção de registros iniciais para testes.

## 6. Checklist de entrega

- [ ] Conferir a identificação dos integrantes.
- [ ] Verificar se os arquivos Java e as páginas JSF estão no repositório.
- [ ] Executar e testar os CRUDs de Marca, Categoria e Produto.
- [ ] Verificar as validações, a hierarquia de categorias e o upload de imagens.
- [ ] Confirmar a presença dos dois scripts SQL.
- [ ] Adicionar capturas de tela reais da aplicação funcionando em `docs/screenshots/`.
- [ ] Conferir o README e o arquivo `.gitignore`.
- [ ] Manter commits progressivos e descritivos no GitHub.
- [ ] Enviar o link do repositório no AVA, conforme as orientações do professor.

## 7. Capturas de tela

Após executar e testar a aplicação, adicionar capturas reais das seguintes funcionalidades:

1. Listagem e cadastro de marcas.
2. Categorias com uma subcategoria.
3. Cadastro de produto com marca e categoria selecionadas.
4. Listagem de produtos com miniaturas das imagens.

As capturas devem demonstrar a aplicação Java em funcionamento. Uma prévia HTML independente não comprova a execução do backend.

## 8. Integrantes

- **Integrante 1:** Émily Ricardo de Lima
- **Disciplina:** Programação Web 2
- **Professor:** Danilo Souza Almeida

## 9. Repositório

Repositório do projeto: https://github.com/EmilyRicardo/sistema-vendas-programacao-web-2
