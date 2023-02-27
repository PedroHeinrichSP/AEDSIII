import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.swing.*;
import net.miginfocom.swing.*;

/**
 * @author marco
 */
public class Screen extends JFrame {
    public Screen() {
        initComponents();
    }

    private void initComponents() {
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		importCSV = new JButton();
		Pokemon = new JLabel();
		readID = new JButton();
		updateID = new JButton();
		textField1 = new JTextField();
		textArea1 = new JTextArea();
		deleteID = new JButton();
		Container contentPane = getContentPane();
		contentPane.setLayout(new MigLayout(
			"fill,hidemode 3,align center center",
			// columns
			"[fill]" +
			"[fill]" +
			"[fill]" +
			"[fill]" +
			"[fill]" +
			"[fill]" +
			"[fill]" +
			"[fill]" +
			"[fill]",
			// rows
			"[]" +
			"[]" +
			"[]" +
			"[]" +
			"[]" +
			"[]" +
			"[]"));

		//---- importCSV ----
		importCSV.setText("Importar o CSV...");
		contentPane.add(importCSV, "cell 1 1");

		//---- Pokemon ----
		Pokemon.setIcon(new ImageIcon("DOCUMENTOS\\International_Pok\u00e9mon_logo.svg.png"));
		contentPane.add(Pokemon, "cell 5 0 3 3,align center center,grow 0 0");

		//---- readID ----
		readID.setText("Ler um registro (ID)");
		contentPane.add(readID, "cell 1 3");

		//---- updateID ----
		updateID.setText("Atualizar um registro (ID)");
		contentPane.add(updateID, "cell 1 4");

		//---- textField1 ----
		textField1.setToolTipText("ID");
		contentPane.add(textField1, "cell 3 4");

		//---- textArea1 ----
		textArea1.setText("Por Pedro Heinrich e Augusto Scardua");
		contentPane.add(textArea1, "cell 5 4 3 1,align center center,grow 0 0");

		//---- deleteID ----
		deleteID.setText("Deletar um registro (ID)");
		contentPane.add(deleteID, "cell 1 5");
		pack();
		setLocationRelativeTo(getOwner());

        importCSV.addActionListener(e -> {
            try {
                Binario.readFile("DOCUMENTOS\\Pokedex.csv");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        
        readID.addActionListener(e -> {
            try {
                List<Pokedex> pokedex = p.leitura(csvFile);
                for (Pokedex entry : pokedex){ 
                    System.out.println(entry);
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }      
        });
    }

    private JButton importCSV;
    private JLabel Pokemon;
    private JButton readID;
    private JButton updateID;
    private JTextField textField1;
    private JTextArea textArea1;
    private JButton deleteID;
    Parser p = new Parser();
    File csvFile = new File("DOCUMENTOS\\Pokedex.csv");
    File saida = new File("DOCUMENTOS\\saida.txt");
}
