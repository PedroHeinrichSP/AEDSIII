import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PokeDB extends Pokedex{
    
    boolean lapide;
    short byteSize;

    public PokeDB(Pokedex pokedex){
        super(pokedex.getID(), pokedex.getName(), pokedex.getGeneration(), pokedex.getHeight(), pokedex.getWeight(), pokedex.getType(), pokedex.getCategory(), pokedex.getMega_Evolution_Flag(), pokedex.getTOTAL());
        this.lapide = false;
        this.byteSize = byteSizeInShort(pokedex);
    }

    // Syze in bytes function (return is a short)
    public short byteSizeInShort(Pokedex pokedex) {
        short size = 0;
        size += 2; // ID
        size += 2; // NameSize
        size += (this.getName().length()*2); // Name
        size += 8; // Generation
        size += 4; // Height
        size += 4; // Weight
        for (String type : this.Type) {
            size += 2; // TypeSize
            size += (type.length()*2); // Type
        }
        size += 1; // Category
        size += 1; // Mega_Evolution_Flag
        size += 4; // TOTAL
        return size;
    }

    //getBytes to use randomAccessFile
    public byte[] getBytes() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeBoolean(lapide);
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
        //dos tem um comando de written que descobre o tamanho hmmmmm
    }

}