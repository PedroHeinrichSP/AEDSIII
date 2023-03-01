import java.io.RandomAccessFile;
import java.lang.reflect.Array;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;

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
<<<<<<< Updated upstream
        RandomAccessFile file = new RandomAccessFile(path, "w");
        for (Pokedex entries : pokedex) {
            file.write(entries.getBytes());
            file.write(",\n".getBytes());
=======
        for (Pokedex entry : pokedex) {
            writeToFile(path, entry);
>>>>>>> Stashed changes
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
        while (file.getFilePointer() < file.length()) {
            int length = file.readInt();
            PokeDB aux = new PokeDB();
            byte[] bytes = new byte[length];
            file.read(bytes);
            aux.fromByteArray(bytes);
            result.add(aux);
        }
        file.close();
        return result;
    }
}