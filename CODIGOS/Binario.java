import java.io.RandomAccessFile;
import java.io.IOException;
import java.util.List;

public class Binario {
    // Bit de lápide, para saber se o registro está ativo ou não
    // Indicador de tamanho do registro
    // Vetor de bytes

    protected String path;

    /**
     * Constroi o arquivo binário de uma pokedex
     * 
     * @param path    Caminho do arquivo
     * @param pokedex Lista de pokemons
     */
    public static void writePokedexToFile(String path, List<Pokedex> pokedex) throws IOException {
        RandomAccessFile file = new RandomAccessFile(path, "rw");
        for (Pokedex entries : pokedex) {
            file.write(entries.getBytes());
            file.write(",\n".getBytes());
        }
        file.close();
    }

    /**
     * Escreve em um arquivo binário de uma pokedex
     * 
     * @param path    Caminho do arquivo
     * @param pokedex Lista de pokemons
     */
    public static void writeToFile(String path, Pokedex pokedex) throws IOException {
        RandomAccessFile file = new RandomAccessFile(path, "w");
        file.seek(file.length());
        file.write(pokedex.getBytes());
        file.write(",\n".getBytes());

        file.close();
    }

    /**
     * Lê o arquivo binário
     * 
     * @param path Caminho do arquivo
     * @throws IOException
     */
    public static void readFile(String path) throws IOException {
        RandomAccessFile file = new RandomAccessFile(path, "r");
        int pos = 0;
        for(int i = 0; i < file.length(); i++) {
            file.seek(i);
            if(file.read() == 10) {
                byte[] bytes = new byte[i - pos];
                file.seek(pos);
                file.read(bytes);
                System.out.println(new String(bytes));
                pos = i + 1;
            }
        }
        file.close();
    }
}