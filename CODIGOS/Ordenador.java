import java.io.File;
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
public class Ordenador {
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

    public void balanceadaComum() throws Exception {
        List<Path> tempFiles = new ArrayList<Path>(1);
        List<ObjectOutputStream> outStreams = new ArrayList<ObjectOutputStream>(1);
        List<ObjectInputStream> inStreams = new ArrayList<ObjectInputStream>(1);
        Binario binPai = new Binario(filePath);
        long tamanhoPercorrido = 0;
        int contTemps = 0;
        int entrySize = 0;
        for (int i = 0; i < 4; i++) {
            Path temp = Files.createTempFile("temp" + i, ".db");
            tempFiles.add(temp);
        }
        for (Path temp : tempFiles) {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(temp.toString()));
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(temp.toString()));
            outStreams.add(out);
            inStreams.add(in);
        }
        try {
            while (binPai.isEOF()) {
                List<Pokedex> pokedex = new ArrayList<Pokedex>(1);
                System.out.println("ContTemps: " + contTemps % 4);
                ObjectOutputStream out = outStreams.get(contTemps % 4);
                System.out.println("Entradas lidas: ");
                for (int i = 0; i < 4; i++) {
                    Pokedex entry = binPai.read();
                    System.out.print(i + ", ");
                    if (entry != null) {
                        pokedex.add(entry);
                    }
                }
                System.out.println("\n Ordenando...");
                pokedex.sort((a, b) -> a.getID() - b.getID());
                int i = 0;
                System.out.println("Escritas: ");
                for (Pokedex entry : pokedex) {
                    out.writeObject(entry);
                    entrySize++;
                    i++;
                    System.out.println(i + ";");
                }
                contTemps++;
                System.out.println("\n Tamanho percorrido: " + tamanhoPercorrido + " bytes\nTamanho Total: "
                        + tamanhoTotal + " bytes");
            }
            System.out.println("entry" + entrySize);
            // debug(inStreams);
            List<Pokedex> pokeList = pokeListInitializer(inStreams);
            ;
            intercalacao(tempFiles, binPai, entrySize, outStreams, inStreams, pokeList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (Path path : tempFiles) {
            File file = new File(path.toString());
            file.delete();
        }
    }

    private void intercalacao(List<Path> tempFiles, Binario binPai, int entrySize, List<ObjectOutputStream> oosList,
            List<ObjectInputStream> oisList, List<Pokedex> pokedexList) throws Exception {
        List<Path> tempFiles2 = new ArrayList<Path>(1);
        List<ObjectInputStream> oisList2 = new ArrayList<ObjectInputStream>(1);
        List<ObjectOutputStream> oosList2 = new ArrayList<ObjectOutputStream>(1);
        for (int i = 5; i < 9; i++) {
            Path temp = Files.createTempFile("temp" + i, ".db");
            tempFiles2.add(temp);
        }
        for (Path path : tempFiles) {
            oisList2.add(new ObjectInputStream(new FileInputStream(path.toString())));
        }
        for (Path path : tempFiles) {
            oosList2.add(new ObjectOutputStream(new FileOutputStream(path.toString())));
        }

        int tamanhoBloco = 4;
        boolean flagOos = true;
        while (tamanhoBloco < entrySize) {
            int lidos = 0;
            int indexTemp = 0;
            while (lidos < (tamanhoBloco * 4)) {
                while (lidos < tamanhoBloco) {
                    Pokedex menor = pokedexList.get(0);
                    int index = 0;
                    for (int i = 0; i < tamanhoBloco; i++) {
                        if (pokedexList.get(i).getID() < menor.getID()) {
                            menor = pokedexList.get(i);
                            index = i;
                        }
                    }
                    if (!flagOos) {
                        oosList.get(indexTemp).writeObject(menor);
                        try {
                            pokedexList.set(index, (Pokedex) oisList.get(index).readObject());
                        } catch (Exception e) {
                            pokedexList.remove(index);
                            oisList.remove(index);
                        }
                    } else {
                        oosList2.get(indexTemp).writeObject(menor);
                        try {
                            pokedexList.set(index, (Pokedex) oisList2.get(index).readObject());
                        } catch (Exception e) {
                            pokedexList.remove(index);
                            oisList2.remove(index);
                        }
                    }
                    lidos++;
                    try {
                        pokedexList.set(index, (Pokedex) oisList.get(index).readObject());
                    } catch (Exception e) {
                        pokedexList.remove(index);
                        oisList.remove(index);
                    }
                }
                indexTemp++;
            }
            tamanhoBloco *= 2;
            flagOos = !flagOos;
        }
        Pokedex aux;
        System.out.println("ordenado");
        while ((aux = binPai.read()) != null) {
            System.out.println(aux);
        }
    }

    public String toString() {
        return "Tamanho total: " + tamanhoTotal + " bytes";
    }

    private void debug(List<ObjectInputStream> inList) throws Exception {
        int i = 0;
        for (ObjectInputStream in : inList) {
            System.out.println("Arquivo" + i + ":\n");
            while (true) {
                try {
                    Pokedex aux = (Pokedex) in.readObject();
                    System.out.println(aux);
                } catch (Exception e) {
                    break;
                }
            }
            i++;
        }
    }

    private List<Pokedex> pokeListInitializer(List<ObjectInputStream> inList) throws Exception {
        int i = 0;
        List<Pokedex> pokedexList = new ArrayList<Pokedex>(1);
        for (ObjectInputStream in : inList) {
            System.out.println("Arquivo" + i + ":\n");
            while (true) {
                try {
                    Pokedex aux = (Pokedex) in.readObject();
                    pokedexList.add(aux);
                    System.out.println(aux.toString());
                } catch (Exception e) {
                    break;
                }
            }
            i++;
        }
        return pokedexList;
    }
}