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

<<<<<<< Updated upstream
    private void initComponents() {
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
=======
	private void initComponents() {
		setVisible(true);
		setLocationRelativeTo(null);
		setMinimumSize(new Dimension(800, 600));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
>>>>>>> Stashed changes
		importCSV = new JButton();
		Pokemon = new JLabel();
		readID = new JButton();
		updateID = new JButton();
		ID = new JTextArea();
		idCampo = new JTextField();
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

		// ---- importCSV ----
		importCSV.setText("Importar o CSV...");
		contentPane.add(importCSV, "cell 1 1");

		// ---- Pokemon ----
		Pokemon.setIcon(new ImageIcon("DOCUMENTOS\\International_Pokémon_logo.svg.png"));
		contentPane.add(Pokemon, "cell 7 0 1 3,align center center,grow 0 0");

		// ---- readID ----
		readID.setText("Ler um registro (ID)");
		contentPane.add(readID, "cell 1 3");

		// ---- updateID ----
		updateID.setText("Atualizar um registro (ID)");
		contentPane.add(updateID, "cell 1 4");

		// ---- ID ----
		ID.setEditable(false);
		ID.setLineWrap(true);
		ID.setWrapStyleWord(true);
		contentPane.add(ID, "cell 3 1 3 5,growy");

		// ---- idCampo ----
		idCampo.setToolTipText("ID");
		contentPane.add(idCampo, "cell 1 2,growy");

		// ---- textArea1 ----
		textArea1.setText("Por Pedro Heinrich e Augusto Scardua");
		contentPane.add(textArea1, "cell 7 4,align center center,grow 0 0");

		// ---- deleteID ----
		deleteID.setText("Deletar um registro (ID)");
		contentPane.add(deleteID, "cell 1 5");
		pack();
		setLocationRelativeTo(getOwner());

<<<<<<< Updated upstream
        importCSV.addActionListener(e -> {
            try {
                pokedex = p.leitura(csvFile);
                Binario.readFile("DOCUMENTOS\\Pokedex.csv");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        
        readID.addActionListener(e -> {
            try {
                String id = idCampo.getText();
                for (Pokedex entry : pokedex) {
                    if (Short.parseShort(id) == entry.getID()) {
                        ID.setText(entry.toString());
                    }
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }      
        });
    }
=======
		importCSV.addActionListener(e -> {
			try {
				pokedex = p.leitura(csvFile);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});

		readID.addActionListener(e -> {
			try {
				String id = idCampo.getText();
				for (Pokedex entry : pokedex) {
					if (Short.parseShort(id) == entry.getID()) {
						ID.setText(entry.toString());
					}
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		});

		updateID.addActionListener(e -> {
			try {
				Binario.writePokedexToFile("DOCUMENTOS\\saida.db", pokedex);
			} catch (Exception e3) {
				e3.printStackTrace();
			}
		});

		deleteID.addActionListener(e -> {
			try {
				Binario.readFile("DOCUMENTOS\\saida.db");
			} catch (Exception e4) {
				e4.printStackTrace();
			}
		});
	}
>>>>>>> Stashed changes

	private JButton importCSV;
	private JLabel Pokemon;
	private JButton readID;
	private JButton updateID;
	private JTextArea ID;
	private JTextField idCampo;
	private JTextArea textArea1;
	private JButton deleteID;
	Parser p = new Parser();
	File csvFile = new File("DOCUMENTOS\\Pokedex.csv");
	File saida = new File("DOCUMENTOS\\saida.txt");
	List<Pokedex> pokedex;
}
