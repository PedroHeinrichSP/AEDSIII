import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

public class Main {

        public static void main(String[] args) throws IOException {
                new Screen();
                // Saída (Saida num arquivo em txt)
                File saida = new File("DOCUMENTOS\\saida.txt");
                PrintStream stream = new PrintStream(saida);
                System.setOut(stream); // Redireciona a saída padrão para o arquivo

                Pokedex aux;
                Binario bin = new Binario("DOCUMENTOS\\pokedex.db");
                System.out.println("\nAtualizado");
                while ((aux = bin.read()) != null) {
                        System.out.println(aux);
                }
                /*
                 * Pokedex aux;
                 * while ((aux = bin.read()) != null) {
                 * System.out.println(aux);
                 * }
                 */

        }

}
