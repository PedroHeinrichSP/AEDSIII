import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

public class Main extends nuqelar {

        public static void main(String[] args) throws Exception {
                // new Screen();
                // Saída (Saida num arquivo em txt)
                File saida = new File("DATA\\saida.txt");

                PrintStream stream = new PrintStream(saida);
                System.setOut(stream); // Redireciona a saída padrão para o arquivo

                runNuqelarTest();
        }

}
