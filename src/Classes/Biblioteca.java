package Classes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Biblioteca {
    private List<Livro> livros = new ArrayList<>();
    private List<Autor> autores = new ArrayList<>();
    private List<Emprestimo> emprestimos = new ArrayList<>();


    //Construtor padrao necesario para a desserializacao pelo GSON.
    public Biblioteca(){}

    // --- MÉTODOS PRIVADOS PARA GERAÇÃO DE ID ---

    private int gerarProximoIdLivro() {
        // Encontra o ID máximo na lista de livros e adiciona 1.
        // Se a lista estiver vazia, começa com 1.
        return this.livros.stream()
                .mapToInt(Livro::getId)
                .max()
                .orElse(0) + 1;
    }

    private int gerarProximoIdAutor() {
        return this.autores.stream()
                .mapToInt(Autor::getId)
                .max()
                .orElse(0) + 1;
    }

    private int gerarProximoIdEmprestimo() {
        return this.emprestimos.stream()
                .mapToInt(Emprestimo::getId)
                .max()
                .orElse(0) + 1;
    }

    //--- Deletar livro---
    public void deletarLivro(int idLivro){
        //Guard Clause 1: verifica se o livro com esse ID existe
        Optional<Livro> livroParaDeletar = buscarLivroPorID(idLivro);
        if(livroParaDeletar.isEmpty()){
            System.out.println("Erro: Livro com o ID " + idLivro + " não encontrado para exclusão");
            return;
        }

        //Guard Clause 2: Verifica se o livro está com emprestimo ativo.
        boolean estaEmprestado = this.emprestimos.stream()
                .anyMatch(emp -> emp.getLivro().getId() == idLivro && emp.getDataDevolucao() == null);

        if(estaEmprestado){
            System.out.println("Erro: Livro '" + livroParaDeletar.get().getTitulo() + "' está emprestado e não pode ser excluído");
            return;
        }

        //Logica principal
        //O metodo 'removeIF' e uma forma eficiente e limpa de remover elementos de uma colecao
        this.livros.removeIf(livro -> livro.getId() == idLivro);

        System.out.println("Livro '" + livroParaDeletar.get().getTitulo() + "' deletado com sucesso!");

     }


    // --- Metodos de emprestimo e devolucao---

    public void realizarEmprestimo(int idLivro,String nomeCLiente){
        Optional<Livro> emprestarLivro = buscarLivroPorID(idLivro);

        //guard clause 1: verifica se o livro com esse ID existe
        if(emprestarLivro.isEmpty()){
            System.out.println("Livro com o ID " + idLivro + " não encontrado.");
            return;

        }
        Livro livro = emprestarLivro.get();

        //guard clause 2: verifica se o livro esta disponivel
        if(!livro.isDisponivel()){
            System.out.println("Livro '" + livro.getTitulo() + "' não está disponível para empréstimo.");
            return;

        }

        // logica principal
        livro.setDisponivel(false);
        int novoIDEmprestimo = this.emprestimos.size() + 1;
        Emprestimo novoEmprestimo = new Emprestimo(novoIDEmprestimo,livro,nomeCLiente);
        this.emprestimos.add(novoEmprestimo);

        System.out.println("Livro '" + livro.getTitulo() + "' emprestado para " + nomeCLiente + " com sucesso");

    }

    public void realizarDevolucao(int idLivro){
        // encontra o emprestimo ativo (sem data de devolucao) para o ID buscado do livro

        Optional<Emprestimo> emprestimoAtivo = this.emprestimos.stream() // stream converte a lista em um fluxo de dados para ser processado um a um
                .filter(emp ->emp.getLivro().getId() == idLivro && emp.getDataDevolucao() == null)//funcao lambda
                .findFirst();

        // guard clause: verifica se o emprestimo existe para este livro
        if(emprestimoAtivo.isEmpty()){
            System.out.println("Erro: Não foi encontrado um empréstimo ativo para o livro com ID " + idLivro + ".");
            return;
        }

        //Logica principal
        Emprestimo emprestimoParaDevolver = emprestimoAtivo.get();
        emprestimoParaDevolver.getLivro().setDisponivel(true);
        emprestimoParaDevolver.setDataDevolucao(LocalDate.now());

        System.out.println("Livro '" + emprestimoParaDevolver.getLivro().getTitulo() + "' devolvido com sucesso!");


    }


    // --- metodos para CADASTRAR Livros E AUTORES ---
    public void cadastrarLivro(String titulo, Autor autor){
        int novoIdLivro = gerarProximoIdLivro();
        Livro livro = new Livro(novoIdLivro, titulo, autor);
        this.livros.add(livro);
        System.out.println("Livro '" + livro.getTitulo() + "' cadastrado com sucesso!");
    }

    // --- Métodos para Autores ---
    public void cadastrarAutor(Autor autor) {
        this.autores.add(autor);
    }
    public Autor cadastrarNovoAutor(String nome, LocalDate dataNascimento) {
        int novoIdAutor = gerarProximoIdAutor();
        Autor novoAutor = new Autor(novoIdAutor, nome, dataNascimento);
        this.autores.add(novoAutor);
        System.out.println("Autor '" + nome + "' cadastrado com sucesso!");
        return novoAutor;
    }
    public List<Autor> listarAutores() {
        return Collections.unmodifiableList(this.autores); // retorna uma cópia para proteger a lista original
    }

    public List<Livro> listarLivros(){
        return Collections.unmodifiableList(this.livros); // retorna uma cópia para proteger a lista original
    }


    // --- BUSCAR LIVRO E AUTOR INICIO ---
    public Optional<Livro> buscarLivroPorTitulo(String titulo) {
        return this.livros.stream()
                .filter(livro -> livro.getTitulo().equalsIgnoreCase(titulo))
                .findFirst();
    }

    public Optional<Livro> buscarLivroPorID(int id){
        return this.livros.stream()
                .filter(livro -> livro.getId() == id)
                .findFirst();
    }

    public Optional<Autor> buscarAutorPorNome(String nome){
        return this.autores.stream()
                .filter(autor -> autor.getNome().equalsIgnoreCase(nome))
                .findFirst();
    }
    // --- BUSCAR LIVRO E AUTOR FIM ---

}
