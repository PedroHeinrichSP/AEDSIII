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

    // Indices Direto e Indireto armazenado em uma Hash e ArvoreB+
    private HashExt ld;
    private BPTree li;

    public void resetar() throws IOException {
        this.posicao = Integer.BYTES;
    }

    public void setLength(long length) throws IOException {
        this.file = new RandomAccessFile(path, "rw");
        file.setLength(length);
        this.file.close();
    }

    public Binario(String path) throws Exception {
        this.path = path;

        ld = new HashExt(10, "data/" + file + "ID" + ".data",
                "data/" + file + "cestos" + ".data");

        li = new BPTree(10, "data/" + file + "Ii" + ".data");
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
     * @throws Exception
     */
    public void writePokedexToFile(List<Pokedex> pokedex) throws Exception {
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
     * @throws Exception
     */
    public void writeToFile(Pokedex pokedex) throws Exception {
        this.file = new RandomAccessFile(path, "rw");
        cabecalho();
        long pos = file.length();
        this.file.seek(pos);

        ld.create(pokedex.getID(), pos + 2);
        li.create(pokedex.getSecKey(), pokedex.getID());

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

    /*
     * Lendo um objeto a partir da chave secundaria
     * 
     * A funcao recebe uma String(chave secundaria). Como
     * a chave secundaria guarda a ID do objeto, o programa
     * basicamente encontra a ID a partir da chave secundaria
     * e executa a funcao "read(int id)".
     */
    public Pokedex read(String secKey) throws IOException {

        long pos = -1;
        Pokedex object = null;

        try {

            pos = li.read(secKey);
        } catch (Exception e) {

            System.err.println("Nao foi possivel ler a chave");
        }

        if (pos >= 0) {

            object = seekID((int) pos);
        } else {

            System.err.println("Esse registro nao foi encontrado ou foi deletado");
        }
        return object;
    }

    // Lendo um objeto a partir da ID
    /*
     * A funcao recebe a ID(chave primaria),e apartir da ID
     * encontra a posicao do registro no indice direto. Com a
     * posicao encontrada, o programa entao vai na posicao e
     * faz a leitura dos dados e entao armazena tais dados no
     * objeto que será retornado.
     */
    public Pokedex read(int id) {

        Pokedex object = null;
        int size = 0;
        long pos = -1;

        try {

            pos = ld.read(id);
            if (pos < 0) {

                throw new Exception("Esse registro nao existe ou foi apagado");
            }

            file.seek(pos);

            size = file.readInt();
            byte[] data = new byte[size];
            file.read(data);

            object = new Pokedex();
            object.fromByteArray(data);

        } catch (Exception e) {

            System.err.println("Nao foi possivel fazer a leitura do registro");
        }

        return object;
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

    // Deleta um registro a partir da chave secundaria
    /*
     * Procura pela chave secundaria, se tiver encontrado,
     * manda a ID para a funcao "delete(int id)"
     */
    public void delete(String secKey) throws Exception {

        int pos = -1;

        try {

            pos = li.read(secKey);
        } catch (Exception e) {

            System.err.println("Nao foi possivel ler a chave");
        }

        if (pos >= 0) {

            delete((int) pos);
        } else {

            System.err.println("Esse registro nao foi encontrado ou foi deletado");
        }
    }

    public boolean delete(int id) throws Exception {
        this.file = new RandomAccessFile(this.path, "rw");
        long posi = -1;
        Pokedex auxObject = null;

        posi = ld.read(id) - 2;
        if (posi < 0) {

            System.err.println("Esse registro não existe ou foi apagado");
        }

        if (this.file.length() == 0) {
            throw new IllegalStateException("O arquivo está vazio");
        }

        this.file.seek(Integer.BYTES);

        ld.delete(id);
        li.delete(auxObject.getSecKey());

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

    // Atualiza um registro
    /*
     * Primeiro ele verifica se o objeto que o
     * usuario quer atualizar existe no registro,
     * depois de confirmado a existencia do objeto,
     * ele agora precisa verificar se o novo objeto
     * tem o mesmo tamanho(ou menor tamanho) do antigo objeto.
     * Se o objeto que tem que ser atualizado atender essa
     * especificacao, entao o objeto e apenas rescrito.
     * Caso o novo objeto seja maior do que o registro,
     * entao o objeto atual no registro sera deletado,
     * e o novo objeto sera adicionado no fim do arquivo
     */
    public boolean update(Pokedex object) {

        boolean resp = false;

        int id = -1;
        long pos = -1;
        int tam = 0;
        byte[] objectData;

        try {

            id = object.getID();
            pos = ld.read(id);
            objectData = object.toByteArray();

            if (pos < 0) {

                System.err.println("Erro, objeto não existe ou foi deletado");
                resp = false;
            } else {

                file.seek(pos);
                tam = file.readInt();
                if (objectData.length <= tam) {

                    file.write(objectData);
                    resp = true;
                } else {

                    delete(id);
                    writeToFile(object);
                    resp = true;

                }

            }

        } catch (Exception e) {

            System.err.println("Nao foi possivel atualizar o registro");
        }

        return resp;
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

    public void copy(Binario arc) throws Exception {
        this.clear();

        while (!arc.isEOF())
            writeToFile(arc.read());
    }

    public long length() throws IOException {
        this.file = new RandomAccessFile(this.path, "rw");
        long aux = this.file.length();
        this.file.close();
        return aux;
    }

}