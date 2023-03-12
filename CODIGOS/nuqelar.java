import java.io.File;
import java.util.List;

public class nuqelar {
    public static void runNuqelarTest() throws Exception {
        System.out.println("Running nuqelar test");

        List<Pokedex> pokedex;// Cria a lista Pokedex
        String path = "DATA\\pokedex.db";
        File miniDex = new File("DOCUMENTOS\\miniDex.csv");// Define o path do arquivo csv
        Binario bin = new Binario(path);// Define o path do arquivo binario
        Parser parser = new Parser();// Cria o parser
        neoOrdenadorSecond ord = new neoOrdenadorSecond(path, 500);

        bin.clear();// limpa o arquivo binario
        pokedex = parser.leitura(miniDex); // leitura do arquivo csv
        for (Pokedex p : pokedex) {// loop para escrever no arquivo binario
            bin.writeToFile(p);// executa o metodo writeToFile da classe Binario para cada objeto da lista
                               // Pokedex
        }
        ord.sort();// executa o metodo balanceadaComum da classe Ordenador

        List<Pokedex> aux = bin.readFull();// leitura do arquivo binario COMPLETO

        for (Pokedex printDex : aux) {
            System.out.println(printDex.toString());// printa o objeto lido
        }

        System.out.println("Test Finished");
    }
}
