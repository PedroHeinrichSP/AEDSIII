public class Cat {
    // Categorias
    final byte ordinary = 0;
    final byte legendary = 1;
    final byte mythical = 2;
    final byte semi_legendary = 3;

    // Construtor
    public Cat() {
    }

    // Base de dados para conversão, categoria (String) para tipo byte (0 -
    // ordinary, 1 - legendary, 2 - mythical), no parsing do CSV
    public byte whatCategory(String cat) {
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