import java.awt.*;
import java.io.File;
import java.util.List;
import javax.swing.*;
import net.miginfocom.swing.*;

public class Screen extends JFrame {
	public Screen() {
		initComponents();
	}

	private void initComponents() {

		// Variáveis de tela
		setVisible(true);
		setLocationRelativeTo(null);
		setMinimumSize(new Dimension(800, 600));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Init dos componentes
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
		Pokemon.setIcon(new ImageIcon("DOCUMENTOS\\International_Pokemon_logo.png"));
		contentPane.add(Pokemon, "cell 7 0 1 3,align center center,grow 0 0");

		// ---- readID ----
		readID.setText("Ler um registro (ID)");
		contentPane.add(readID, "cell 1 3");

		// ---- updateID ----
		updateID.setText("Atualizar um registro (ID)");
		contentPane.add(updateID, "cell 1 4");

		// ---- ID ----
		ID.setVisible(false);
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

		importCSV.addActionListener(e -> {
			try {
				bin.clear();
				pokedex = parser.leitura(miniDex);
				System.out.println();
                for (Pokedex p : pokedex) {
                        bin.writeToFile(p);
                }
				System.out.println("BINPAI");
				while ((aux = bin.read()) != null) {
					System.out.println(aux);
				}
				System.out.println("TEMPS");
				ord.balanceadaComum();

			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});

		readID.addActionListener(e -> {
			try {
				ID.setVisible(true);
				int id = Integer.parseInt(idCampo.getText());
				aux = bin.seekID(id);
				if (aux != null)
					ID.setText(aux.toString());	
				else 
					ID.setText("ID não encontrado");
				/*System.out.println("ID LIDO:" + aux.toString() + "\n Binário:");
				while ((aux = bin.read()) != null) {
					System.out.println(aux);
				}*/
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		});

		updateID.addActionListener(e -> {
			try {
				int id = Integer.parseInt(idCampo.getText());
				aux = bin.seekID(id);
				UptadeScreen uptadeScreen = new UptadeScreen(aux, id, this);
				uptadeScreen.setVisible(true);
				
				// window listener for when updateScreen is closed
				uptadeScreen.addWindowListener(new java.awt.event.WindowAdapter() {
					@Override
					public void windowClosed(java.awt.event.WindowEvent windowEvent) {
						//lê da entrada aux ID.setText(aux.toString());
						try {
							bin.update(id, aux);
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				});
			} catch (Exception e3) {
				e3.printStackTrace();
			}
		});

		deleteID.addActionListener(e -> {
			try {
				bin.delete(Integer.parseInt(idCampo.getText()));
			} catch (Exception e4) {
				e4.printStackTrace();
			}
		});
	}


	private JButton importCSV;
	private JLabel Pokemon;
	private JButton readID;
	private JButton updateID;
	private JTextArea ID;
	private JTextField idCampo;
	private JTextArea textArea1;
	private JButton deleteID;
	Parser parser = new Parser();
	File miniDex = new File("DOCUMENTOS\\miniDex.csv");
	File pokeDex = new File("DOCUMENTOS\\Pokedex.csv");
	String path = "DOCUMENTOS\\pokedex.db";
	List<Pokedex> pokedex;
	Pokedex aux;
	Binario bin = new Binario(path);
	Ordenador ord =	new Ordenador(path);
}
