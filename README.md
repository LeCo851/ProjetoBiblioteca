# Sistema de Gerenciamento de Biblioteca 📚

Este é um sistema de gerenciamento de biblioteca desenvolvido em Java, executado via console. O projeto permite realizar as operações essenciais de uma biblioteca, como cadastrar livros, gerenciar autores, controlar empréstimos e devoluções, com persistência de dados em um arquivo JSON.

## ✨ Funcionalidades Principais

O sistema oferece um menu interativo com as seguintes funcionalidades:

### 📖 Gerenciamento de Livros (CRUD)
- **Cadastrar Livro:** Adiciona um novo livro ao acervo. Se o autor informado não existir, o sistema solicita os dados para cadastrá-lo automaticamente.
- **Listar Livros:** Exibe todos os livros cadastrados no sistema, com seus respectivos detalhes (ID, Título, Autor, Disponibilidade e Data de Cadastro).
- **Buscar Livro:** Permite a busca de um livro específico por **ID** ou por **Título**.
- **Excluir Livro:** Remove um livro do sistema. A exclusão só é permitida se o livro não estiver atualmente emprestado, garantindo a integridade dos dados.

### 👤 Gerenciamento de Autores
- **Criação Dinâmica:** Um novo autor é criado e salvo automaticamente quando um livro é cadastrado com um autor que ainda não existe no sistema.

### ↔️ Gerenciamento de Empréstimos
- **Realizar Empréstimo:** Registra a saída de um livro para um cliente. O sistema verifica se o livro existe e se está disponível antes de confirmar o empréstimo.
- **Registrar Devolução:** Marca um livro como devolvido, tornando-o disponível para novos empréstimos e registrando a data de devolução no histórico.

### 💾 Persistência de Dados
- **Salvar e Carregar:** Todos os dados (livros, autores e empréstimos) são salvos em um arquivo `dados_biblioteca.json` ao fechar o programa.
- **Inicialização Inteligente:** Ao iniciar, o sistema carrega automaticamente os dados salvos na sessão anterior, garantindo a continuidade do trabalho. Caso o arquivo de dados não exista, uma nova biblioteca vazia é criada.

### ⚙️ Validações e Regras de Negócio
- **IDs Únicos:** O sistema garante que os IDs de livros, autores e empréstimos sejam sempre únicos, mesmo após a exclusão de registros.
- **Integridade de Dados:** Não é possível excluir um livro que está emprestado.
- **Tratamento de Erros:** O sistema trata entradas inválidas do usuário (como texto em campos numéricos ou formatos de data incorretos) sem quebrar a execução.

---

## 🚀 Como Executar o Projeto

### Pré-requisitos
- **Java Development Kit (JDK)** 11 ou superior.
- Uma IDE Java, como **IntelliJ IDEA**, **Eclipse** ou **VS Code**.

### Passos para Configuração

1.  **Clone o Repositório:**
    ```bash
    git clone <URL_DO_SEU_REPOSITORIO_AQUI>
    ```

2.  **Adicione a Biblioteca Gson:**
    - Baixe a biblioteca **Gson** (ex: `gson-2.10.1.jar`) do Maven Central.
    - No seu projeto, crie uma pasta `lib` na raiz.
    - Copie o arquivo `.jar` baixado para dentro da pasta `lib`.
    - Na sua IDE, adicione o JAR ao *build path* do projeto.
      - No **IntelliJ IDEA**: Clique com o botão direito no arquivo `.jar` > `Add as Library...`.

3.  **Execute o Programa:**
    - Abra o arquivo `src/Sistema.java`.
    - Execute o método `main`.

---

## 🛠️ Tecnologias Utilizadas

- **Java:** Linguagem principal do projeto.
- **Gson:** Biblioteca do Google para serialização e desserialização de objetos Java para o formato JSON.

---

## 📂 Estrutura do Projeto

```
ProjetoBiblioteca/
├── lib/
│   └── gson-2.10.1.jar       # Biblioteca externa
├── src/
│   ├── Sistema.java          # Classe principal (View/Controller)
│   ├── Classes/              # Pacote com as classes de modelo (Model)
│   │   ├── Biblioteca.java   # Classe central que gerencia a lógica de negócio
│   │   ├── Livro.java
│   │   ├── Autor.java
│   │   └── Emprestimo.java
│   └── Dados/                # Pacote para persistência de dados
│       ├── PersistenciaService.java
│       └── LocalDateAdapter.java
└── dados_biblioteca.json     # Arquivo de dados gerado automaticamente
```

---

## 🔮 Possíveis Melhorias Futuras

- Implementar um sistema de gerenciamento de usuários/clientes.
- Adicionar mais filtros de busca (por autor, por data de cadastro).
- Criar uma interface gráfica (GUI) utilizando JavaFX ou Swing.
- Migrar a persistência de dados de um arquivo JSON para um banco de dados (como H2, SQLite ou PostgreSQL).
- Adicionar testes unitários com JUnit para garantir a qualidade e o comportamento esperado dos métodos.

---

*Projeto desenvolvido como parte da formação em Java.*