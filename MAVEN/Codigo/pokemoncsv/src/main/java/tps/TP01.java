package tps;
import java.util.Calendar;
import java.util.Date;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

public class TP01 {
    public class Pokedex{
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
        private String[] Type;
        private byte Category;
        private boolean Mega_Evolution_Flag;
        private int TOTAL;

        // Construtor
        public Pokedex(short ID, String Name, Date Generation, float Height, float Weight, String[] Type, byte Category, boolean Mega_Evolution_Flag, int TOTAL){
            this.ID = ID;
            this.Name = Name;
            this.Generation = Generation;
            this.Height = Height;
            this.Weight = Weight;
            this.Type = Type;
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
        public String[] getType(){
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
        public void setType(String[] Type){
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
            return "ID: " + this.ID + " | Name: " + this.Name + " | Generation: " + this.Generation + " | Height: " + this.Height + " | Weight: " + this.Weight + " | Type: " + this.Type + " | Category: " + this.Category + " | Mega_Evolution_Flag: " + this.Mega_Evolution_Flag + " | TOTAL: " + this.TOTAL;
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
            return (this.Category == 1);
        }

        // isLegendary
        public boolean isLegendary(){
            return (this.Category == 2);
        }

        // isMythical
        public boolean isMythical(){
            return (this.Category == 3);
        }
    }

    public class Gen{
        final Date gen1 = setTime(1996, 1, 27).getTime();
        final Date gen2 = setTime(1999, 10, 21).getTime();
        final Date gen3 = setTime(2002, 10, 21).getTime();
        final Date gen4 = setTime(2006, 8, 28).getTime();
        final Date gen5 = setTime(2010, 8, 18).getTime();
        final Date gen6 = setTime(2013, 11, 12).getTime();
        final Date gen7 = setTime(2016, 10, 18).getTime();
        final Date gen8 = setTime(2019, 10, 15).getTime();
        final Date gen9 = setTime(2022, 10, 18).getTime();
        // Base de dados para conversão, geração (int) para tipo Date (data de lançamento da gen), no parsing do CSV
        public Calendar setTime (int year, int month, int day){
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, month);
            cal.set(Calendar.DAY_OF_MONTH, day);
            return cal;
        }
    }

    public static class Parser{
        // Construtor   
        public Parser(File file){
            try {
                teste(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void teste(File file) throws IOException{
            CSVReader reader = new CSVReader(new FileReader(file));
            String[] line;
            try {
                while((line = reader.readNext()) != null){
                    for (String column : line) {
                        System.out.print(column + " ");
                    }
                }
            } catch (CsvValidationException e) {
                e.printStackTrace();
            }
            System.out.println();
        }
    }

    public static void main(String[] args) throws IOException{
        // File
        File file = new File("D:\\AEDSIII\\CSV\\Pokedex.csv");
        // Parser
        Parser p = new Parser(file);
        p.teste(file);
    }
}
