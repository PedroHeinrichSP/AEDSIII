import java.io.File;
import java.io.IOException;
import java.util.List;

public class nuqelar {
    public static void runNuqelarTest() throws IOException {
        System.out.println("Running nuqelar test");

        List<Pokedex> pokedex;
        Binario bin = new Binario("DATA\\pokedex.db");
        File miniDex = new File("DOCUMENTOS\\miniDex.csv");
        Parser parser = new Parser();

        pokedex = parser.leitura(miniDex);
        System.out.println();
        for (Pokedex p : pokedex) {
            bin.writeToFile(p);
        }
        for (Pokedex aux : pokedex) {
            System.out.println(aux.toString());
        }

        System.out.println("Test Finished");
    }
}
