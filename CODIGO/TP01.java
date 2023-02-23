import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

public class TP01 {
    public static class Pokedex{
        /*
         * N° (ID - número da pokedex) - tipo Inteiro (short)
         * Name (Nome - Nome do Pokemon, formas diferentes incluídas) - tipo Fixo (String)
         * Generation (Geração - Data de lançamento da geração) - tipo Data (Date)
         * Height (Altura - Altura do pokemon em metros) - tipo Float (Float)
         * Weight (Peso - Peso do pokemon em kilogramas) - tipo Float (Float)
         * Type1 & Type2 (Tipos - Tipagem do pokemon) - tipo Variavel (String[])
         * Category (Categoria - Categoria do pokemon) - tipo Valores (byte)
         * Mega_Evolution_Flag (Pode MegaEvoluir - O pokemon tem mega evolução) - tipo Valores (boolean)
         * TOTAL (Status - Total da soma de status do pokemon) - tipo Valores (int)
         */
        private short ID; 
        private String Name;
        private Date Generation;
        private float Height;
        private float Weight;
        private List<String> Type;
        private byte Category;
        private boolean Mega_Evolution_Flag;
        private int TOTAL;

        // Construtor
        public Pokedex(){
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

        public Pokedex(short ID, String Name, Date Generation, float Height, float Weight, List<String> type2, byte Category, boolean Mega_Evolution_Flag, int TOTAL){
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
        public short getID(){
            return this.ID;
        }
        public String getName(){
            return this.Name;
        }
        public Date getGeneration(){
            return this.Generation;
        }
        public float getHeight(){
            return this.Height;
        }
        public float getWeight(){
            return this.Weight;
        }
        public List<String> getType(){
            return this.Type;
        }
        public byte getCategory(){
            return this.Category;
        }
        public boolean getMega_Evolution_Flag(){
            return this.Mega_Evolution_Flag;
        }
        public int getTOTAL(){
            return this.TOTAL;
        }
        
        // Setters
        public void setID(short ID){
            this.ID = ID;
        }
        public void setName(String Name){
            this.Name = Name;
        }
        public void setGeneration(Date Generation){
            this.Generation = Generation;
        }
        public void setHeight(float Height){
            this.Height = Height;
        }
        public void setWeight(float Weight){
            this.Weight = Weight;
        }
        public void setType(List<String> Type){
            this.Type = Type;
        }
        public void setCategory(byte Category){
            this.Category = Category;
        }
        public void setMega_Evolution_Flag(boolean Mega_Evolution_Flag){
            this.Mega_Evolution_Flag = Mega_Evolution_Flag;
        }
        public void setTOTAL(int TOTAL){
            this.TOTAL = TOTAL;
        }

        // toString
        public String toString(){
            return "ID: " + this.ID + " | Name: " + this.Name + " | Generation: " + this.Generation + " | Height: " + this.Height + "m | Weight: " + this.Weight + "kg | Type: " + this.Type + " | Category: " + byteCatToString(this.Category) + " | Mega_Evolution_Flag: " + this.Mega_Evolution_Flag + " | TOTAL: " + this.TOTAL;
        }

        // Clone
        public Pokedex clone(){
            Pokedex clone = new Pokedex(this.ID, this.Name, this.Generation, this.Height, this.Weight, this.Type, this.Category, this.Mega_Evolution_Flag, this.TOTAL);
            return clone;
        }

        // equals
        public boolean equals(Object obj){
            if(obj == null){
                return false;
            }
            if(obj == this){
                return true;
            }
            if(obj.getClass() != this.getClass()){
                return false;
            }
            Pokedex p = (Pokedex)obj;
            return (this.ID == p.ID && this.Name.equals(p.Name) && this.Generation.equals(p.Generation) && this.Height == p.Height && this.Weight == p.Weight && this.Type.equals(p.Type) && this.Category == p.Category && this.Mega_Evolution_Flag == p.Mega_Evolution_Flag && this.TOTAL == p.TOTAL);
        }

        // compareTo
        public int compareTo(Pokedex p){
            if(this.ID > p.ID){
                return 1;
            }else if(this.ID < p.ID){
                return -1;
            }else{
                return 0;
            }
        }

        // isMega
        public boolean isMega(){
            return this.Mega_Evolution_Flag;
        }

        // isOrdinary
        public boolean isOrdinary(){
            return (this.Category == 0);
        }

        // isLegendary
        public boolean isLegendary(){
            return (this.Category == 1);
        }

        // isMythical
        public boolean isMythical(){
            return (this.Category == 2);
        }

        // isSemilegendary
        public boolean isSemilegendary(){
            return (this.Category == 3);
        }

        // Auxiliar Methods
        // Auxilia o método toString para retornar o nome da categoria do pokemon a partir do seu byte
        public String byteCatToString (byte cat){
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
    }

    public static class Gen{
        // Gerações
        final Date gen1 = setTime(1996, 1, 27).getTime();
        final Date gen2 = setTime(1999, 10, 21).getTime();
        final Date gen3 = setTime(2002, 10, 21).getTime();
        final Date gen4 = setTime(2006, 8, 28).getTime();
        final Date gen5 = setTime(2010, 8, 18).getTime();
        final Date gen6 = setTime(2013, 11, 12).getTime();
        final Date gen7 = setTime(2016, 10, 18).getTime();
        final Date gen8 = setTime(2019, 10, 15).getTime();
        final Date gen9 = setTime(2022, 10, 18).getTime();
        //Construtor
        public Gen(){
        }
        // Base de dados para conversão, geração (String) para tipo Date (data de lançamento da gen), no parsing do CSV
        public Calendar setTime (int year, int month, int day){
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, month);
            cal.set(Calendar.DAY_OF_MONTH, day);
            return cal;
        }

        public Date GenToDate (String gen){
            switch (gen) {
                case "1":
                    return gen1;
                case "2":
                    return gen2;
                case "3":
                    return gen3;
                case "4":
                    return gen4;
                case "5":
                    return gen5;
                case "6":
                    return gen6;
                case "7":
                    return gen7;
                case "8":
                    return gen8;
                case "9":
                    return gen9;
                default:
                    return null;
            }
        }
    }
    
    public static class Cat{
        // Categorias
        final byte ordinary = 0;
        final byte legendary = 1;
        final byte mythical = 2;
        final byte semi_legendary = 3;
        //Construtor
        public Cat(){
        }
        // Base de dados para conversão, categoria (String) para tipo byte (0 - ordinary, 1 - legendary, 2 - mythical), no parsing do CSV
        public byte whatCategory (String cat){
            switch (cat) {
                case "Ordinary":
                    return ordinary;
                case "Legendary":
                    return legendary;
                case "Mythical":
                    return mythical;
                case "Semi-Legendary":
                    return semi_legendary;
                default:
                    return 0;
            }
        }

    }
    
    public static class Parser{
        // Construtor   
        public Parser(File file){}
        // Leitura e parsing do CSV
        public List<Pokedex> leitura(File file) throws IOException{
            CSVReader reader = new CSVReader(new FileReader(file));
            String[] line;
            Gen gen = new Gen();
            Cat cat = new Cat();
            List <Pokedex> pokedex = new ArrayList<Pokedex>();
            try {
                reader.readNext(); // Ignora a primeira linha do CSV
                int i=0;
                while((line = reader.readNext()) != null){
                    int j=0;
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
                                if(column.equals("Mega")){
                                    aux.Mega_Evolution_Flag = true;
                                }else{
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

    public static void main(String[] args) throws IOException{
        // File
        File file = new File("D:\\AEDSIII\\DOCUMENTOS\\Pokedex.csv");
        // Teste
        Parser p = new Parser(file);
        List<Pokedex> pokedex = p.leitura(file);
        for (Pokedex entry: pokedex) {
            System.out.println(entry);
        }
    }
}
