import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Pokedex {
    /*
     * N° (ID - número da pokedex) - tipo Inteiro (short)
     * Name (Nome - Nome do Pokemon, formas diferentes incluídas) - tipo Fixo
     * (String)
     * Generation (Geração - Data de lançamento da geração) - tipo Data (Date)
     * Height (Altura - Altura do pokemon em metros) - tipo Float (Float)
     * Weight (Peso - Peso do pokemon em kilogramas) - tipo Float (Float)
     * Type1 & Type2 (Tipos - Tipagem do pokemon) - tipo Variavel (String[])
     * Category (Categoria - Categoria do pokemon) - tipo Valores (byte)
     * Mega_Evolution_Flag (Pode MegaEvoluir - O pokemon tem mega evolução) - tipo
     * Valores (boolean)
     * TOTAL (Status - Total da soma de status do pokemon) - tipo Valores (int)
     */
    protected short ID;
    protected String Name;
    protected Date Generation;
    protected float Height;
    protected float Weight;
    protected List<String> Type;
    protected byte Category;
    protected boolean Mega_Evolution_Flag;
    protected int TOTAL;

    // Construtor
    public Pokedex() {
        this.ID = 0;
        this.Name = "";
        this.Generation = new Date();
        this.Height = 0;
        this.Weight = 0;
        this.Type = new ArrayList<String>();
        this.Category = 0;
        this.Mega_Evolution_Flag = false;
        this.TOTAL = 0;
    }

    public Pokedex(short ID, String Name, Date Generation, float Height, float Weight, List<String> type2,
            byte Category, boolean Mega_Evolution_Flag, int TOTAL) {
        this.ID = ID;
        this.Name = Name;
        this.Generation = Generation;
        this.Height = Height;
        this.Weight = Weight;
        this.Type = type2;
        this.Category = Category;
        this.Mega_Evolution_Flag = Mega_Evolution_Flag;
        this.TOTAL = TOTAL;
    }

    // Getters
    public short getID() {
        return this.ID;
    }

    public String getName() {
        return this.Name;
    }

    public Date getGeneration() {
        return this.Generation;
    }

    public float getHeight() {
        return this.Height;
    }

    public float getWeight() {
        return this.Weight;
    }

    public List<String> getType() {
        return this.Type;
    }

    public byte getCategory() {
        return this.Category;
    }

    public boolean getMega_Evolution_Flag() {
        return this.Mega_Evolution_Flag;
    }

    public int getTOTAL() {
        return this.TOTAL;
    }

    // Setters
    public void setID(short ID) {
        this.ID = ID;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public void setGeneration(Date Generation) {
        this.Generation = Generation;
    }

    public void setHeight(float Height) {
        this.Height = Height;
    }

    public void setWeight(float Weight) {
        this.Weight = Weight;
    }

    public void setType(List<String> Type) {
        this.Type = Type;
    }

    public void setCategory(byte Category) {
        this.Category = Category;
    }

    public void setMega_Evolution_Flag(boolean Mega_Evolution_Flag) {
        this.Mega_Evolution_Flag = Mega_Evolution_Flag;
    }

    public void setTOTAL(int TOTAL) {
        this.TOTAL = TOTAL;
    }

    // toString
    public String toString() {
        return "ID: " + this.ID + " | Name: " + this.Name + " | Generation: " + this.Generation + " | Height: "
                + this.Height + "m | Weight: " + this.Weight + "kg | Type: " + this.Type + " | Category: "
                + byteCatToString(this.Category) + " | Mega Evolution: " + this.Mega_Evolution_Flag
                + " | TOTAL: " + this.TOTAL;
    }

    // Clone
    public Pokedex clone() {
        Pokedex clone = new Pokedex(this.ID, this.Name, this.Generation, this.Height, this.Weight, this.Type,
                this.Category, this.Mega_Evolution_Flag, this.TOTAL);
        return clone;
    }

    // equals
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        Pokedex p = (Pokedex) obj;
        return (this.ID == p.ID && this.Name.equals(p.Name) && this.Generation.equals(p.Generation)
                && this.Height == p.Height && this.Weight == p.Weight && this.Type.equals(p.Type)
                && this.Category == p.Category && this.Mega_Evolution_Flag == p.Mega_Evolution_Flag
                && this.TOTAL == p.TOTAL);
    }

    // compareTo
    public int compareTo(Pokedex p) {
        if (this.ID > p.ID) {
            return 1;
        } else if (this.ID < p.ID) {
            return -1;
        } else {
            return 0;
        }
    }

    // isMega
    public boolean isMega() {
        return this.Mega_Evolution_Flag;
    }

    // isOrdinary
    public boolean isOrdinary() {
        return (this.Category == 0);
    }

    // isLegendary
    public boolean isLegendary() {
        return (this.Category == 1);
    }

    // isMythical
    public boolean isMythical() {
        return (this.Category == 2);
    }

    // isSemilegendary
    public boolean isSemilegendary() {
        return (this.Category == 3);
    }

    // Auxiliar Methods
    // Auxilia o método toString para retornar o nome da categoria do pokemon a
    // partir do seu byte
    public String byteCatToString(byte cat) {
        switch (cat) {
            case 0:
                return "Ordinary";
            case 1:
                return "Legendary";
            case 2:
                return "Mythical";
            case 3:
                return "Semi-Legendary";
            default:
                return "Ordinary";
        }
    }

    //getBytes to use randomAccessFile
    public byte[] getBytes() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeShort(this.ID);
        dos.writeUTF(this.Name);
        dos.writeLong(this.Generation.getTime());
        dos.writeFloat(this.Height);
        dos.writeFloat(this.Weight);
        dos.writeByte(this.Type.size());
        for (String type : this.Type) {
            dos.writeUTF(type);
        }
        dos.writeByte(this.Category);
        dos.writeBoolean(this.Mega_Evolution_Flag);
        dos.writeInt(this.TOTAL);
        return baos.toByteArray();
    }
}
