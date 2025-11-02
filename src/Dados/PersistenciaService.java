package Dados;

import Classes.Biblioteca;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.time.LocalDate;

public class PersistenciaService {

    // Define o nome do arquivo onde os dados serão salvos.
    private static final String ARQUIVO_DADOS = "dados_biblioteca.json";

    // Cria uma instância do Gson configurada para formatar o JSON de forma legível ("pretty printing").
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter()) // Registra o adaptador para LocalDate
            .setPrettyPrinting()
            .create();

    /**
     * Salva o estado atual do objeto Biblioteca em um arquivo JSON.
     * @param biblioteca O objeto Biblioteca a ser salvo.
     */
    public void salvarDados(Biblioteca biblioteca) {
        try (FileWriter writer = new FileWriter(ARQUIVO_DADOS)) {
            gson.toJson(biblioteca, writer);
            System.out.println("\nDados salvos com sucesso em " + ARQUIVO_DADOS);
        } catch (IOException e) {
            System.err.println("Erro ao salvar os dados: " + e.getMessage());
        }
    }

    /**
     * Carrega os dados de uma biblioteca a partir de um arquivo JSON.
     * Se o arquivo não existir, retorna uma nova instância de Biblioteca.
     * @return Um objeto Biblioteca com os dados carregados ou um novo objeto se o arquivo não existir.
     */
    public Biblioteca carregarDados() {
        try (Reader reader = new FileReader(ARQUIVO_DADOS)) {
            Biblioteca biblioteca = gson.fromJson(reader, Biblioteca.class);
            System.out.println("Dados carregados com sucesso de " + ARQUIVO_DADOS);
            // Se o arquivo JSON estiver vazio ou malformado, fromJson pode retornar null.
            return biblioteca != null ? biblioteca : new Biblioteca();
        } catch (IOException | JsonSyntaxException e) {
            // Se o arquivo não existe (IOException) ou tem erro de sintaxe (JsonSyntaxException),
            // consideramos que é a primeira execução e criamos uma nova biblioteca.
            System.out.println("Arquivo de dados não encontrado ou inválido. Iniciando uma nova biblioteca.");
            return new Biblioteca();
        }
    }
}
