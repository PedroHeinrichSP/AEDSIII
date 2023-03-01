import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

public class Main extends Binario {

        public static void main(String[] args) throws IOException {
                // new Screen();
                // Saída (Saida num arquivo em txt)
                File saida = new File("DOCUMENTOS\\saida.txt");
                PrintStream stream = new PrintStream(saida);

                System.setOut(stream); // Redireciona a saída padrão para o arquivo

                List<Pokedex> pokedex;
                Parser parser = new Parser();
                File path = new File("DOCUMENTOS\\Pokedex.csv");

                pokedex = parser.leitura(path);

                writePokedexToFile("DOCUMENTOS\\Pokedex.db", pokedex);

                System.out.println(readFile("DOCUMENTOS\\Pokedex.db"));

        }

}
