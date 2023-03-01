import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Date;

public class PokeDB extends Pokedex {

    boolean lapide;

    public PokeDB(Pokedex pokedex) {
        super(pokedex.getID(), pokedex.getName(), pokedex.getGeneration(), pokedex.getHeight(), pokedex.getWeight(),
                pokedex.getType(), pokedex.getCategory(), pokedex.getMega_Evolution_Flag(), pokedex.getTOTAL());
        this.lapide = true;
    }

    public PokeDB() {
        super();
        this.lapide = true;
    }

    // getBytes to use randomAccessFile
    public byte[] toByteArray() throws IOException {
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
        // dos tem um comando de written que descobre o tamanho hmmmmm
    }

    public void fromByteArray(byte[] bytes) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        DataInputStream dis = new DataInputStream(bais);

        this.lapide = dis.readBoolean();
        this.ID = dis.readShort();
        this.Name = dis.readUTF();
        this.Generation = new Date(dis.readLong());
        this.Height = dis.readFloat();
        this.Weight = dis.readFloat();
        int typeSize = dis.readByte();
        for (int i = 0; i < typeSize; i++) {
            this.Type.add(dis.readUTF());
        }
        this.Category = dis.readByte();
        this.Mega_Evolution_Flag = dis.readBoolean();
        this.TOTAL = dis.readInt();
    }

}