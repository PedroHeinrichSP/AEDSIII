import java.util.List;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

public class Main extends Binario {

    public static void main(String[] args) throws IOException {
        new Screen();
        // Saída (Saida num arquivo em txt)
        File saida = new File("DOCUMENTOS\\saida.txt");
        PrintStream stream = new PrintStream(saida);
        System.setOut(stream);

        // Teste
        /*Parser p = new Parser(csvFile);
        List<Pokedex> pokedex = p.leitura(csvFile);
        for (Pokedex entry : pokedex) {
            System.out.println(entry);
        }*/

        //readFile("DOCUMENTOS\\Pokedex.csv");
    }
}
