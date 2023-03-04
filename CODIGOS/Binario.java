import java.io.RandomAccessFile;
import java.io.IOException;
import java.util.List;

public class Binario {
    // Bit de lápide, para saber se o registro está ativo ou não
    // Indicador de tamanho do registro
    // Vetor de bytes

    private final String path;
    private RandomAccessFile file;
    private long posicao = Integer.BYTES;

    public Binario(String path) {
        this.path = path;
    }

    /**
     * Limpa o arquivo binário de uma pokedex
     */
    public void clear() throws IOException {
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
    public void writePokedexToFile(List<Pokedex> pokedex) throws IOException {
        clear();
        for (Pokedex entry : pokedex) {
            writeToFile(entry);
        }
    }

    private int cabecalho() throws IOException {
        this.file.seek(0);

        int id = 0;
        if (this.file.length() == 0) {
            this.file.writeInt(0);
        } else {
            id = this.file.readInt();
        }

        return id;
    }

    private void updateCabecalho(int id) throws IOException {
        this.file.seek(0);
        this.file.write(id);
    }

    /**
     * Escreve em um arquivo binário de uma pokedex
     * 
     * @param path    Caminho do arquivo
     * @param pokedex Lista de pokemons
     */
    public void writeToFile(Pokedex pokedex) throws IOException {
        this.file = new RandomAccessFile(path, "rw");
        cabecalho();

        this.file.seek(file.length());
        byte[] arr = pokedex.toByteArray();
        this.file.writeBoolean(true);
        this.file.writeInt(arr.length);
        this.file.write(arr);

        updateCabecalho(pokedex.getID());
        this.file.close();
    }

    public Pokedex read() throws IOException {
        this.file = new RandomAccessFile(path, "rw");

        if (this.posicao >= this.file.length()) {
            return null;
        }

        this.file.seek(this.posicao);

        int length;

        boolean lapide = this.file.readBoolean();
        while (!lapide) {
            length = this.file.readInt();
            this.file.skipBytes(length);

            lapide = this.file.readBoolean();
        }

        length = this.file.readInt();
        byte[] bytes = new byte[length];
        this.file.read(bytes);

        Pokedex aux = new Pokedex();
        aux.fromByteArray(bytes);

        this.posicao = this.file.getFilePointer();
        file.close();
        return aux;
    }

    public boolean delete(int id) throws IOException {
        this.file = new RandomAccessFile(this.path, "rw");

        if (this.file.length() == 0) {
            throw new IllegalStateException("O arquivo está vazio");
        }

        this.file.seek(Integer.BYTES);

        long pos = -1;

        Pokedex aux;
        boolean lapide;

        do {
            pos = this.file.getFilePointer();
            lapide = this.file.readBoolean();
            int length = this.file.readInt();

            byte[] arr = new byte[length];
            this.file.read(arr);

            aux = new Pokedex();
            aux.fromByteArray(arr);
        } while ((aux.getID() != id || !lapide) && this.file.getFilePointer() < this.file.length());

        if (aux.getID() == id && lapide) {
            this.file.seek(pos);
            this.file.writeBoolean(false);
        } else {
            this.file.close();
            return false;
        }

        this.file.close();
        return true;
    }

    // provavel nao funcionar
    public boolean update(int id, Pokedex pokedex) throws IOException {
        try {
            delete(id);
            writeToFile(pokedex);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    // ta dando erro aqui
    public Pokedex seekID(int id) throws IOException {
        this.file = new RandomAccessFile(this.path, "rw");

        if (this.file.length() == 0) {
            throw new IllegalStateException("O arquivo está vazio");
        }

        this.file.seek(Integer.BYTES);

        Pokedex aux;
        boolean lapide;

        do {
            lapide = this.file.readBoolean();
            int length = this.file.readInt();

            byte[] arr = new byte[length];
            this.file.read(arr);

            aux = new Pokedex();
            aux.fromByteArray(arr);
        } while ((aux.getID() != id || !lapide) && this.file.getFilePointer() < this.file.length());

        if (aux.getID() == id && lapide) {
            return aux;
        } else {
            return null;
        }
    }
}