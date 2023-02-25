import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

public class Main {

    public static void main(String[] args) throws IOException {
        // Files
        // File file = new File("D:\\AEDSIII\\DOCUMENTOS\\Pokedex.csv");

        // Só funciona no meu PC nesse caminho
        File file = new File(
                "G:\\Other computers\\Meu PC\\FACULDADE\\PUC\\3 Periodo\\AEDS III\\Git\\AEDSIII\\DOCUMENTOS\\Pokedex.csv");
        // Teste
        Parser p = new Parser(file);
        List<Pokedex> pokedex = p.leitura(file);
        for (Pokedex entry : pokedex) {
            System.out.println(entry);
        }
    }
}
