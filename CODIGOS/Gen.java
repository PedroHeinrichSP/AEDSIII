import java.util.Calendar;
import java.util.Date;

public class Gen {
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

    // Construtor
    public Gen() {
    }

    // Base de dados para conversão, geração (String) para tipo Date (data de lançamento da gen), no parsing do CSV
    public Calendar setTime(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        return cal;
    }

    // Converte a geração (String) para tipo Date (data de lançamento da gen)
    public Date GenToDate(String gen) {
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