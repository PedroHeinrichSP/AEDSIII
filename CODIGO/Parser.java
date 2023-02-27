import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

public class Parser {
    // Construtor
    public Parser(File file) {
    }

    // Leitura e parsing do CSV
    public List<Pokedex> leitura(File file) throws IOException {
        CSVReader reader = new CSVReader(new FileReader(file));
        String[] line;
        Gen gen = new Gen();
        Cat cat = new Cat();
        List<Pokedex> pokedex = new ArrayList<Pokedex>();
        try {
            reader.readNext(); // Ignora a primeira linha do CSV
            int i = 0;
            while ((line = reader.readNext()) != null) {
                int j = 0;
                Pokedex aux = new Pokedex();
                for (String column : line) {
                    System.out.println("Linha: " + i + " Coluna: " + j + " Valor: " + column);
                    String numeric; // Variável auxiliar para conversão de String para Float
                    switch (j) {
                        case 0: // ID
                            aux.ID = Short.parseShort(column);
                            break;
                        case 2: // Name
                            aux.Name = column;
                            break;
                        case 3: // Generation
                            aux.Generation = gen.GenToDate(column);
                            break;
                        case 4: // Height
                            numeric = column.replaceAll("m", ""); // Remove todos os caracteres não numéricos
                            aux.Height = Float.parseFloat(numeric);
                            break;
                        case 5: // Weight
                            numeric = column.replaceAll("kg", ""); // Remove todos os caracteres não numéricos
                            aux.Weight = Float.parseFloat(numeric);
                            break;
                        case 6: // Type1
                            aux.Type.add(column);
                            break;
                        case 7: // Type2
                            aux.Type.add(column);
                            break;
                        case 21: // Category
                            aux.Category = cat.whatCategory(column);
                            break;
                        case 22: // Mega_Evolution_Flag
                            if (column.equals("Mega")) {
                                aux.Mega_Evolution_Flag = true;
                            } else {
                                aux.Mega_Evolution_Flag = false;
                            }
                            break;
                        case 30: // TOTAL
                            aux.TOTAL = Integer.parseInt(column);
                            break;
                    }
                    j++;
                }
                pokedex.add(i, aux);
                i++;
            }
        } catch (CsvValidationException e) {
            e.printStackTrace();
        }
        System.out.println();
        return pokedex;
    }
}
