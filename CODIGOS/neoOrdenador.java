import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * The {@code SortedFile} class represents a file that can be sorted by any
 * attribute from type {@code T} register.
 * 
 * @author Augusto Scardua & Pedro Heinrich feat.(Fernando Campos)
 * @version 1.0.0
 * 
 * @see {@link components.interfaces.Register}
 * @see {@link crud.RandomAccessFile}
 */

public class neoOrdenador {
    private static final int BLOCK_SIZE = 4096; // 4KB
    private static final int NUMBER_OF_BRANCHES = 4; // Number of branches for the sort algorithm
    private static final String TEMPORARY_FILES_DIRECTORY = "DATA\\temp"; // Directory where the temporary
                                                                          // files will be stored
    private static final String TEMPORARY_FILES_PATH = "DATA\\temp\\temp"; // Path of the temporary files

    private Binario database; // Original data file
    private int registerSize; // Size of each register in bytes
    private int originalNumberOfRegistersPerBlock; // Number of registers per block calculated for the first step
                                                   // of the sort algorithm

    private int numberOfRegistersPerBlock; // Number of registers per block in the temporary files

    private Binario[] originalFiles; // Files used to store the registers that will be interpolated
    private Binario[] tmpFiles; // Temporary files used to store the sorted registers

    // Constructors

    /**
     * Constructs a new {@code SortedFile} with the given file path, register size,
     * comparator and constructor.
     * 
     * @param path         the file path of the archive.
     * @param registerSize the size of each register in bytes.
     * @param comparator   the comparator used to sort the registers.
     * @param constructor  the constructor of the register.
     * @throws IOException if an I/O error occurs.
     */
    public neoOrdenador(String path, int registerSize) throws IOException {
        if (!path.endsWith(".db"))
            throw new IllegalArgumentException("The file must have the extension \".db\".");

        File f = new File(TEMPORARY_FILES_DIRECTORY);
        if (!f.exists())
            f.mkdir();

        this.database = new Binario(path);

        this.registerSize = registerSize;
        this.originalNumberOfRegistersPerBlock = (int) Math.floor(BLOCK_SIZE / (double) this.registerSize);

        this.numberOfRegistersPerBlock = this.originalNumberOfRegistersPerBlock;

        this.__createArchives();
    }

    // Public Methods

    /**
     * Returns the number of registers per block in the temporary files.
     * 
     * @return the number of registers per block in the temporary files.
     * @throws IOException if an I/O error occurs.
     */
    public int getNumberOfReistersPerBlock() {
        return this.originalNumberOfRegistersPerBlock;
    }

    /**
     * Sorts the file.
     * 
     * @throws IOException if an I/O error occurs.
     */
    public void sort() throws IOException {
        int numberOfBlocks = this.distribute();

        while (numberOfBlocks > 1)
            numberOfBlocks = this.interpolate();

        this.database.copy(this.originalFiles[0]);
        this.__close();
    }

    // Private Methods

    /**
     * Checks if all registers from a block were readed.
     * 
     * @param values the array of booleans that represents the registers.
     * @return {@code true} if all registers were readed, {@code false} otherwise.
     */
    private Boolean __blockWasReaded(Boolean[] values) {
        Boolean value = true;

        for (int i = 0; value && i < values.length; i++)
            value = values[i];

        return value;
    }

    /**
     * Initialize the {@see originalFiles} and {@see tmpFiles} arrays with a set of
     * {@link crud.RandomAccessFile}.
     * 
     * @throws IOException if an I/O error occurs.
     */

    private void __createArchives() throws IOException {
        this.originalFiles = new Binario[NUMBER_OF_BRANCHES];
        this.tmpFiles = new Binario[NUMBER_OF_BRANCHES];

        for (int i = 0; i < this.originalFiles.length; i++) {
            this.originalFiles[i] = new Binario(TEMPORARY_FILES_PATH + (i + 1) + ".dat");

            this.tmpFiles[i] = new Binario(TEMPORARY_FILES_PATH + (i + 1 + NUMBER_OF_BRANCHES) + ".dat");
        }
    }

    /**
     * Changes the file pointers of the {@see originalFiles} and {@see tmpFiles}
     * arrays.
     * 
     * @throws IOException if an I/O error occurs.
     */

    private void __changeOriginalFiles() throws IOException {
        Binario[] arr = new Binario[NUMBER_OF_BRANCHES];
        for (int i = 0; i < this.originalFiles.length; i++) {
            this.originalFiles[i].setLength(0);
            arr[i] = this.originalFiles[i];
        }

        for (int i = 0; i < this.originalFiles.length; i++) {
            this.originalFiles[i] = this.tmpFiles[i];
            this.tmpFiles[i] = arr[i];
        }
    }

    /**
     * Checks if there is at least one register in the files from
     * {@see originalFiles} array.
     * 
     * @return {@code true} if there is at least one register, {@code false}
     *         otherwise.
     * @throws IOException if an I/O error occurs.
     */
    private Boolean __haveRegister() throws IOException {
        Boolean value = false;

        for (int i = 0; !value && i < this.originalFiles.length; i++)
            value = !this.originalFiles[i].isEOF();

        return value;
    }

    /**
     * Distributes the registers from the original file to the temporary files.
     * 
     * @return the number of blocks in the temporary files.
     * @throws IOException if an I/O error occurs.
     */

    private int distribute() throws IOException {
        int numberOfBlocks = 0,
                j = 0;

        ArrayList<Pokedex> arr = new ArrayList<>();

        while (this.database.getPosicao() < this.database.length()) {
            arr.add(this.database.read());

            if (arr.size() == this.originalNumberOfRegistersPerBlock) {

                arr.sort((a, b) -> a.getID() - b.getID());

                for (Pokedex pokedex : arr) {
                    this.originalFiles[j].writeToFile(pokedex);
                }

                arr.clear();
                j = ++numberOfBlocks % NUMBER_OF_BRANCHES;
            }
        }

        if (arr.size() != 0) {
            arr.sort((a, b) -> a.getID() - b.getID());
            for (Pokedex pokedex : arr) {
                this.originalFiles[j].writeToFile(pokedex);
            }

            numberOfBlocks++;
        }

        this._resetFilePointers(this.originalFiles);

        return numberOfBlocks;
    }

    protected void _resetFilePointers(Binario[] arr) throws IOException {
        for (int i = 0; i < arr.length; i++)
            arr[i].resetar();
    }

    /**
     * Interpolates the registers from the original files to the temporary files and
     * changes it`s pointers.
     * 
     * @return the number of blocks in the original files.
     * @throws IOException if an I/O error occurs.
     */
    private int interpolate() throws IOException {
        int numberOfBlocks = 0,
                i = 0;

        while (this.__haveRegister()) {
            this.readRegistersAndWriteOrdered(this.tmpFiles[i]);
            i = ++numberOfBlocks % this.tmpFiles.length;
        }

        this.numberOfRegistersPerBlock *= NUMBER_OF_BRANCHES;

        this._resetFilePointers(this.tmpFiles);
        this.__changeOriginalFiles();
        this._resetFilePointers(this.originalFiles);

        return numberOfBlocks;
    }

    /**
     * Reads the registers from the original files and writes them in the temporary
     * files in order.
     * 
     * @param arc the temporary file.
     * @throws IOException if an I/O error occurs.
     */
    private void readRegistersAndWriteOrdered(Binario arc) throws IOException {
        Boolean[] restrictions = new Boolean[this.originalFiles.length];
        for (int i = 0; i < restrictions.length; i++)
            restrictions[i] = false;

        int[] numberOfReadedRegisters = new int[this.originalFiles.length];

        int positionOfMinObj = -1;
        while (!this.__blockWasReaded(restrictions)) {
            Pokedex min = null;

            for (int i = 0; i < this.originalFiles.length; i++) {
                if (!this.originalFiles[i].isEOF() && numberOfReadedRegisters[i] < this.numberOfRegistersPerBlock) {
                    numberOfReadedRegisters[i]++;
                    Pokedex obj = this.originalFiles[i].read();

                    if (min == null) {
                        min = obj;
                        positionOfMinObj = i;
                    } else if (obj.getID() < min.getID()) {
                        min = obj;

                        this.originalFiles[positionOfMinObj]._returnOneRegister();
                        numberOfReadedRegisters[positionOfMinObj]--;

                        positionOfMinObj = i;
                    } else {
                        this.originalFiles[i]._returnOneRegister();
                        numberOfReadedRegisters[i]--;
                    }
                } else
                    restrictions[i] = true;
            }

            if (min != null)
                arc.writeToFile(min);
        }
    }

    /**
     * Closes the {@see originalFiles} and {@see tmpFiles} arrays.
     * 
     * @throws IOException if an I/O error occurs.
     */
    private void __close() throws IOException {

        File[] list = new File(TEMPORARY_FILES_DIRECTORY).listFiles();
        for (int i = 0; i < list.length; i++)
            list[i].delete();
    }

}
