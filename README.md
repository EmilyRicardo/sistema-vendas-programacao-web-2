# Sistema de Vendas — CRUD de Marcas, Categorias e Produtos

Projeto acadêmico para Programação Web 2, organizado conforme o enunciado da atividade. Tecnologias: Java, Maven, Jakarta Faces (JSF), PrimeFaces, Hibernate/JPA e PostgreSQL.

## Funcionalidades no código-fonte

- **Marca:** listagem, cadastro, edição, validação de nome único, ativação/inativação e bloqueio de exclusão física quando existem produtos vinculados.
- **Categoria:** cadastro, edição e exclusão com categoria pai recursiva, caminho hierárquico, prevenção de ciclos e proteção contra exclusão quando há dependências.
- **Produto:** cadastro, edição, exclusão, preço maior que zero, estoque não negativo e associação obrigatória a marca/categoria.
- **Imagem:** componente PrimeFaces de upload para JPG/JPEG, PNG e WEBP, com limite de 5 MB e exibição da miniatura.
- **Interface:** tabelas PrimeFaces com paginação, ordenação e filtros, mensagens de sucesso/erro e diálogos de confirmação.
- **Arquitetura:** entidades em `model`, acesso a dados em `dao`, regras em `service`, beans CDI em `controller` e conversores Faces em `converter`.
- **Banco de dados:** scripts PostgreSQL DDL e DML na pasta `sql`.

> **Importante:** o código e os arquivos de configuração foram preparados, mas não foi possível executar o build e testar o deploy de ponta a ponta neste ambiente. Portanto, não considere a aplicação validada até realizar as etapas de execução abaixo. A prévia visual HTML fornecida separadamente é apenas uma demonstração e não substitui esta aplicação Java.

## Estrutura

```text
pom.xml
mvnw / mvnw.cmd
src/main/java/br/edu/vendas/
  model/       Entidades JPA e validações
  dao/         Acesso a dados com EntityManager
  service/     Regras de negócio e validações
  controller/  Managed Beans CDI usados pelas telas
  converter/   Conversores Faces para Marca e Categoria
  util/        Gerenciamento do EntityManager
  web/         Servlet para exibir imagens
src/main/resources/META-INF/persistence.xml
src/main/webapp/              Páginas JSF/PrimeFaces e CSS
sql/01-criacao-tabelas.sql    DDL do PostgreSQL
sql/02-dados-teste.sql        Dados iniciais opcionais
```

## Como configurar e executar (Windows)

**Requisitos:** JDK 17 ou superior, PostgreSQL e um servidor compatível com Jakarta Faces/Servlet 6.1 e as dependências da aplicação. O primeiro build precisa de acesso à internet para baixar Maven e bibliotecas.

1. Instale o JDK 17 ou superior e o PostgreSQL.
2. No PostgreSQL, crie um banco chamado `vendas_db`.
3. Abra o banco no pgAdmin e execute `sql/01-criacao-tabelas.sql`.
4. Opcionalmente, execute `sql/02-dados-teste.sql` para inserir dados de exemplo.
5. Edite `src/main/resources/META-INF/persistence.xml` e ajuste URL, usuário e senha para o seu PostgreSQL. O valor de senha presente no arquivo é só exemplo; altere antes de usar.
6. Abra esta pasta no VS Code e abra `Terminal > New Terminal`.
7. Execute:

   ```powershell
   .\mvnw.cmd clean package
   ```

8. Se o build terminar com `BUILD SUCCESS`, o WAR será criado em `target/sistema-vendas.war`.
9. Faça o deploy do WAR em um servidor compatível, configure o acesso ao banco e teste todos os CRUDs e o upload antes de considerar a aplicação pronta.

O Maven Wrapper automatiza o download do Maven, mas **não instala o Java, PostgreSQL ou servidor web**. Se ocorrer `BUILD FAILURE`, leia a mensagem `[ERROR]` no terminal e corrija antes do deploy.

## Checklist obrigatório para a entrega

- [ ] Preencher os nomes reais dos integrantes abaixo.
- [ ] Executar a aplicação e verificar os três CRUDs, hierarquia, validações e upload.
- [ ] Salvar capturas de tela reais do sistema funcionando em `docs/screenshots/` e inseri-las nesta documentação.
- [ ] Confirmar que os dois scripts SQL estão no repositório.
- [ ] Publicar o projeto no GitHub e conferir que `README.md`, `pom.xml`, `src`, `sql` e `.gitignore` aparecem no repositório.
- [ ] Criar commits progressivos e descritivos durante a organização/validação; não inventar histórico de commits.
- [ ] Enviar o link do repositório do GitHub no AVA, conforme a orientação do professor.

### Capturas de tela a adicionar

1. Listagem/cadastro de marcas.
2. Árvore de categorias mostrando pelo menos uma subcategoria.
3. Formulário de cadastro de produto com marca e categoria.
4. Listagem de produtos mostrando as miniaturas das imagens.

Não use a prévia HTML independente como evidência de que o backend Java foi executado.

## Integrantes

- Integrante 1: ÉMILY RICARDO DE LIMA
- Disciplina: Programação Web 2
- Professor: Danilo Souza Almeida
