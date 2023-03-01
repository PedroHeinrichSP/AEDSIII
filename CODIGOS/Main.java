import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import com.opencsv.exceptions.CsvValidationException;

public class Main {

        public static void main(String[] args) throws IOException, CsvValidationException {
                // new Screen();
                // Saída (Saida num arquivo em txt)
                File saida = new File("DATA\\saida.txt");
                PrintStream stream = new PrintStream(saida);
                System.setOut(stream); // Redireciona a saída padrão para o arquivo

                List<Pokedex> pokedex;
                Parser parser = new Parser();
                File path = new File("DATA\\miniDex.csv");

                pokedex = parser.leitura(path);

                Binario bin = new Binario("DATA\\pokedex.db");

                for (Pokedex p : pokedex) {
                        bin.writeToFile(p);
                }

                System.out.println(bin.delete(9));

                Pokedex aux;
                while ((aux = bin.read()) != null) {
                        System.out.println(aux);
                }
        }

}
