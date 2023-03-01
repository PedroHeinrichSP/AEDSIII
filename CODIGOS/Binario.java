import java.io.RandomAccessFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Binario {
    // Bit de lápide, para saber se o registro está ativo ou não
    // Indicador de tamanho do registro
    // Vetor de bytes

    protected String path;

    /**
     * Limpa o arquivo binário de uma pokedex
     * 
     * @param path Caminho do arquivo
     */
    public static void clear(String path) throws IOException {
        RandomAccessFile file = new RandomAccessFile(path, "rw");
        file.setLength(0);
        file.close();
    }

    /**
     * Constroi o arquivo binário de uma pokedex
     * 
     * @param path    Caminho do arquivo
     * @param pokedex Lista de pokemons
     */
    public static void writePokedexToFile(String path, List<Pokedex> pokedex) throws IOException {
        clear(path);
        for (Pokedex entry : pokedex) {
            writeToFile(path, entry);
        }
    }

    /**
     * Escreve em um arquivo binário de uma pokedex
     * 
     * @param path    Caminho do arquivo
     * @param pokedex Lista de pokemons
     */
    public static void writeToFile(String path, Pokedex pokedex) throws IOException {
        RandomAccessFile file = new RandomAccessFile(path, "rw");
        PokeDB aux = new PokeDB(pokedex);
        file.seek(file.length());
        byte[] arr = aux.toByteArray();
        file.writeInt(arr.length);
        file.write(arr);
        file.close();
    }

    public static PokeDB readFirstEntry(String path) throws IOException {
        RandomAccessFile file = new RandomAccessFile(path, "r");
        file.seek(0);
        int length = file.readInt();

        byte[] bytes = new byte[length];
        file.read(bytes);
        PokeDB aux = new PokeDB();
        aux.fromByteArray(bytes);
        file.close();
        return aux;
    }

    public static ArrayList<PokeDB> readFile(String path) throws IOException {
        RandomAccessFile file = new RandomAccessFile(path, "r");
        file.seek(0);
        ArrayList<PokeDB> result = new ArrayList<PokeDB>();
        long fileSize = file.length();
        long getFilePointer = file.getFilePointer();
        PokeDB aux = new PokeDB();

        try {
            while (getFilePointer < fileSize) {
                int length = file.readInt();
                aux = new PokeDB();
                byte[] bytes = new byte[length];
                file.read(bytes);
                aux.fromByteArray(bytes);
                System.out.println(aux.toString());
                result.add(aux);
                getFilePointer = file.getFilePointer();
            }
        } catch (Exception e) {
            System.out.println("Erro no registro: " + getFilePointer);
        }
        file.close();
        return result;
    }
}