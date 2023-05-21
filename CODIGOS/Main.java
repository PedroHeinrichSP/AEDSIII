
import java.io.File;
import java.io.PrintStream;

public class Main extends debug {

        public static void main(String[] args) throws Exception {
                File btreeFile = new File("DATA\\bptli.data");
                File hashFile = new File("DATA\\hashcestos.data");
                File hashFile_ = new File("DATA\\hashID.data");
                btreeFile.delete();
                hashFile.delete();
                hashFile_.delete();
                Screen main = new Screen();
                // Saída (Saida num arquivo em txt)
                File saida = new File("DATA\\saida.txt");
                PrintStream stream = new PrintStream(saida);
                System.setOut(stream); // Redireciona a saída padrão para o arquivo
                System.setErr(stream); // Redireciona a saída de erro padrão para o arquivo
        }

}