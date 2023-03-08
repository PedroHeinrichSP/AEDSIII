import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

//Separar o .db em arquivos menores (1/4 do tamanho do .db). Preencher as files temporarias com entrys
//do .db até <= 1/4 do tamanho do .db. Ordenar as files temporarias por intercalação balanceada.
//Juntar as files temporarias em um .db ordenado por ID.
public class Ordenador{
    long tamanhoTotal;
    long tamanhoTemp;
    String filePath;
    String tmpDir;

    public Ordenador(String filePath) {
        try {
            Path path = Paths.get(filePath);
            this.tamanhoTotal = Files.size(path);
            this.tamanhoTemp = tamanhoTotal/4;
            this.filePath = filePath;
            this.tmpDir = System.getProperty("java.io.tmpdir");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void balanceadaComum() throws Exception{
        List<Path> tempFiles = new ArrayList<Path>(1);
        Binario binPai = new Binario(filePath);
        Binario binFilho;
        long tamanhoPercorrido = 0;
        int contTemps = 0;
        try {
            while(tamanhoPercorrido<tamanhoTotal){
                Path temp = Files.createTempFile("pokeTemp" + contTemps, ".db");
                tempFiles.add(temp);
                ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(temp.toString()));
                ObjectInputStream in = new ObjectInputStream(new FileInputStream(temp.toString()));
                while(Files.size(temp)<tamanhoTemp){
                    binFilho.writeToFile(binPai.read());
                }
                tempFiles.add(Paths.get(tmpDir + "temp" + contTemps + ".db"));
                tamanhoPercorrido += Files.size(temp);
                contTemps++;
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    public String toString(){
        return "Tamanho total: " + tamanhoTotal + " bytes\nTamanho temporario: " + tamanhoTemp + " bytes";
    }
}