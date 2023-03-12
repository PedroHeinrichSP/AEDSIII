import java.io.RandomAccessFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Binario {
    // Bit de lápide, para saber se o registro está ativo ou não
    // Indicador de tamanho do registro
    // Vetor de bytes

    private String path;
    private RandomAccessFile file;
    private long posicao = Integer.BYTES;
    private long ultimaPosicao = Integer.BYTES;

    public void resetar() throws IOException {
        this.posicao = Integer.BYTES;
    }

    public void setLength(long length) throws IOException {
        this.file = new RandomAccessFile(path, "rw");
        file.setLength(length);
        this.file.close();
    }

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

    public long getPosicao() {
        return posicao;
    }

    public List<Pokedex> readFull() throws IOException {
        long aux = this.posicao;
        this.resetar();
        ArrayList<Pokedex> pokedex = new ArrayList<Pokedex>();
        Pokedex entry = this.read();
        while (entry != null) {
            pokedex.add(entry);
            entry = this.read();
        }
        this.posicao = aux;
        return pokedex;
    }

    protected void _returnOneRegister() throws IOException {
        this.posicao = this.ultimaPosicao;
    }

    public Pokedex read() throws IOException {
        this.file = new RandomAccessFile(path, "rw");

        if (this.posicao >= this.file.length()) { // verifica se a posicao atual do ponteiro nao ultrapassa o tamanho do
                                                  // arquivo
            return null;
        }

        this.ultimaPosicao = this.posicao; // guarda a posicao atual do ponteiro
        this.file.seek(this.posicao); // vai ate a posição do ponteiro

        int length; // tamanho do registro

        boolean lapide = this.file.readBoolean(); // le o bit de lápide

        // verifica se o registro esta ativo ou nao
        while (!lapide) { // enquanto o registro estiver desativado
            length = this.file.readInt(); // ler a quantidade de bytes do registro
            this.file.skipBytes(length); // pula a quantidade de bytes do registro inativo

            lapide = this.file.readBoolean(); // le o proximo bit de lápide
        }

        length = this.file.readInt(); // le a quantidade de bytes do registro
        byte[] bytes = new byte[length]; // cria um novo array de bytes com o tamanho do registro
        this.file.read(bytes); // le o registro com o tamanho predeterminado

        Pokedex aux = new Pokedex(); // cria uma nova pokedex
        aux.fromByteArray(bytes); // armaena o registro na pokedex

        this.posicao = this.file.getFilePointer(); // atualiza a posicao do ponteiro
        file.close(); // fecha o arquivo
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

    public boolean update(int id, Pokedex pokedex) throws IOException {
        try {
            delete(id);
            writeToFile(pokedex);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

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

    public boolean isEOF() throws IOException {
        return this.posicao >= this.length();
    }

    public void copy(Binario arc) throws IOException {
        this.clear();

        while (!arc.isEOF())
            writeToFile(arc.read());
    }

    /**
     * Tests if the file pointer is at the end of the file.
     * 
     * @return {@code true} if the file pointer is at the end of the file,
     *         {@code false} otherwise.
     * 
     * @see {@link java.io.IOException}
     */
    private Boolean _isEOF() throws IOException {
        return this.file.getFilePointer() >= this.file.length();
    }

    public long length() throws IOException {
        this.file = new RandomAccessFile(this.path, "rw");
        long aux = this.file.length();
        this.file.close();
        return aux;
    }

}