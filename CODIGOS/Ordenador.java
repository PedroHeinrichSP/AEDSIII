import java.io.FileOutputStream;
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
    String filePath;
    String tmpDir;

    public Ordenador(String filePath) {
        try {
            Path path = Paths.get(filePath);
            this.tamanhoTotal = Files.size(path);
            this.filePath = filePath;
            this.tmpDir = System.getProperty("java.io.tmpdir");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void balanceadaComum() throws Exception{
        List<Path> tempFiles = new ArrayList<Path>(1);
        Binario binPai = new Binario(filePath);
        long tamanhoPercorrido = 0;
        int contTemps = 0;
        try {
            while(tamanhoPercorrido<tamanhoTotal){
                Path temp = Files.createTempFile("pokeTemp" + contTemps, ".db");
                List<Pokedex> pokedex = new ArrayList<Pokedex>(1);
                tempFiles.add(temp);
                ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(temp.toString()));
                for (int i = 0; i < 4; i++) {
                    Pokedex entry = binPai.read();
                    if(entry!=null){
                        pokedex.add(entry);
                    }
                }
                pokedex.sort((a, b) -> a.getID() - b.getID());
                for(Pokedex entry : pokedex){
                    out.writeObject(entry);
                }
                tempFiles.add(Paths.get(tmpDir + "temp" + contTemps + ".db"));
                tamanhoPercorrido += Files.size(temp);
                contTemps++;
            }
            int i=0;
            for (Path path : tempFiles) {
                System.out.println("Arquivo" + i + ":\n" + path + "\n");
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    public String toString(){
        return "Tamanho total: " + tamanhoTotal + " bytes";
    }
}