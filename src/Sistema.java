import Classes.Autor;
import Classes.Biblioteca;
import Classes.Livro;
import Dados.PersistenciaService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Sistema {
    public static void main(String[] args) {

        PersistenciaService persistenciaService = new PersistenciaService();
        Biblioteca biblioteca = persistenciaService.carregarDados(); // Declara e inicializa a biblioteca com os dados salvos
        
        boolean sistemaOnline = true;
        Scanner scanner = new Scanner(System.in);

        do{
            System.out.println("\n===== MENU PRINCIPAL =====");
            System.out.println("1. Cadastrar Livro");
            System.out.println("2. Listar Livros");
            System.out.println("3. Buscar Livro");
            System.out.println("4. Realizar Empréstimo");
            System.out.println("5. Registrar Devolução");
            System.out.println("6. Excluir Livro");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");
        try{
            int opcao = scanner.nextInt();
            scanner.nextLine(); // Consome a quebra de linha

            switch (opcao){
                case 1:
                    System.out.println("\n--- Cadastro de Livro ---");

                    System.out.print("Digite o título do livro: ");
                    String titulo = scanner.nextLine(); // Agora funciona corretamente

                    Autor autor = obterOuCriarAutor(scanner, biblioteca);
                    biblioteca.cadastrarLivro(titulo, autor);

                    break;
                case 2:
                    System.out.println("\n--- Lista de Livros Cadastrados ---");
                    List<Livro> livros = biblioteca.listarLivros();
                    if (livros.isEmpty()) {
                        System.out.println("Nenhum livro cadastrado no sistema.");
                        break; // guard clause
                    }

                    for (Livro livro : livros) {
                        exibirDetalhesLivro(livro);
                    }
                    break;

                case 3:
                    System.out.println("\n--- Buscar Livro ---");
                    System.out.println("Como deseja buscar?");
                    System.out.println("1. Por Título");
                    System.out.println("2. Por ID");
                    System.out.print("Escolha uma opção: ");
                    obterLivro(scanner, biblioteca);
                    break;

                case 4:
                    System.out.println("\n--- Realizar Empréstimo ---");
                    System.out.print("Digite o ID do livro a ser emprestado: ");
                    int idLivroEmprestimo = scanner.nextInt();
                    scanner.nextLine(); // Consome a quebra de linha

                    System.out.print("Digite o nome do cliente: ");
                    String nomeCliente = scanner.nextLine();

                    biblioteca.realizarEmprestimo(idLivroEmprestimo, nomeCliente);
                    break;
                case 5:
                    System.out.println("\n--- Registrar Devolução ---");
                    System.out.print("Digite o ID do livro a ser devolvido: ");
                    int idLivroDevolucao = scanner.nextInt();
                    scanner.nextLine(); // Consome a quebra de linha
                    biblioteca.realizarDevolucao(idLivroDevolucao);
                    break;
                case 6:
                    System.out.println("\n--- Excluir Livro ---");
                    System.out.print("Digite o ID do livro a ser excluído: ");
                    int idLivroExcluir = scanner.nextInt();
                    scanner.nextLine(); // Consome a quebra de linha
                    biblioteca.deletarLivro(idLivroExcluir);
                    break;
                case 0:
                    System.out.println("Saindo do sistema.");
                    sistemaOnline = false;
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
                    break;
            }
        }catch (InputMismatchException e){
            System.out.println("\nErro: Entrada inválida. Por favor, digite um número para a opção.");
            scanner.nextLine(); // Limpa o buffer do scanner
        } catch (DateTimeParseException e) {
            System.out.println("\nErro: Formato de data inválido. Use dd/MM/yyyy.");
        } catch (Exception e) {
            System.out.println("\nOcorreu um erro inesperado: " + e.getMessage());
        }
        }while (sistemaOnline);
        scanner.close();

        // Salva o estado atual da biblioteca no arquivo JSON antes de sair
        persistenciaService.salvarDados(biblioteca);
    }

    /**
     * metodo que faz a busca do livro pelo titulo ou ID
     *
     * @param scanner    A instância do Scanner para ler a entrada do usuário.
     * @param biblioteca A instância da Biblioteca para buscar e cadastrar o autor.
     */
    private static void obterLivro(Scanner scanner, Biblioteca biblioteca){
        try {
            int subOpcao = scanner.nextInt();
            scanner.nextLine(); // consome a quebra de linha

            switch (subOpcao) {
                case 1:
                    System.out.print("Digite o título do livro: ");
                    String tituloBusca = scanner.nextLine();
                    biblioteca.buscarLivroPorTitulo(tituloBusca)
                            .ifPresentOrElse(
                                    Sistema::exibirDetalhesLivro,
                                    () -> System.out.println("Livro com o título '" + tituloBusca + "' não encontrado.")
                            );
                    break;
                case 2:
                    System.out.print("Digite o ID do livro: ");
                    int idBusca = scanner.nextInt();
                    scanner.nextLine(); // consome a quebra de linha
                    biblioteca.buscarLivroPorID(idBusca)
                            .ifPresentOrElse(
                                    Sistema::exibirDetalhesLivro,
                                    () -> System.out.println("Livro com o ID " + idBusca + " não encontrado.")
                            );
                    break;
                default:
                    System.out.println("Opção de busca inválida.");
                    break;
            }
        } catch (InputMismatchException e) {
            System.out.println("Erro: A entrada para a opção de busca deve ser um número.");
            scanner.nextLine(); // Limpa o buffer do scanner
        }
    }



    /**
     * Obtém um autor da biblioteca. Se o autor não existir, solicita os dados,
     * cria um novo, cadastra na biblioteca e o retorna.
     *
     * @param scanner    A instância do Scanner para ler a entrada do usuário.
     * @param biblioteca A instância da Biblioteca para buscar e cadastrar o autor.
     * @return O objeto Autor (existente ou recém-criado).
     */
    private static Autor obterOuCriarAutor(Scanner scanner, Biblioteca biblioteca) {
        System.out.print("Digite o nome do autor: ");
        String nomeAutor = scanner.nextLine();

        Optional<Autor> autorExistente = biblioteca.buscarAutorPorNome(nomeAutor);

        // --- Guard Clause ---
        // Se o autor já existe, retorna ele imediatamente.
        if (autorExistente.isPresent()) {
            System.out.println("Autor '" + nomeAutor + "' já consta no sistema.");
            return autorExistente.get();
        }

        // --- Caminho "Feliz" (ou de criação) ---
        // Se o código chegou até aqui, o autor não existe. Não precisamos de 'else'.
        System.out.println("Autor não encontrado. Vamos cadastrá-lo.");
        System.out.print("Digite a data de nascimento do autor (dd/MM/yyyy): ");
        String dataNascStr = scanner.nextLine();
        LocalDate dataNascimento = LocalDate.parse(dataNascStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        Autor novoAutor = biblioteca.cadastrarNovoAutor(nomeAutor, dataNascimento);

        return novoAutor;
    }

    /**
     * Exibe os detalhes formatados de um objeto Livro no console.
     *
     * @param livro O livro cujos detalhes serão exibidos.
     */

    private  static  void exibirDetalhesLivro(Livro livro){
        System.out.println("--------------------");
        System.out.println("ID: " + livro.getId());
        System.out.println("Título: " + livro.getTitulo());
        System.out.println("Autor: " + livro.getAutor().getNome());
        System.out.println("Disponível: " + (livro.isDisponivel() ? "Sim" : "Não"));
        System.out.println("Data de Cadastro: " + livro.getDataCadastro().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        System.out.println("--------------------");
    }
}
