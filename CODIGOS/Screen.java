import java.awt.*;
import java.io.File;
import java.util.List;
import javax.swing.*;
import net.miginfocom.swing.*;

public class Screen extends JFrame {
	public Screen() throws Exception {
		initComponents();
		bin = new Binario(path);
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
		ordMenu = new JButton();
		compMenu = new JButton();
		casaMenu = new JButton();

		// ======== this ========
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
		importCSV.setText("Criar DB e Hash/BTree");
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
		contentPane.add(ID, "cell 4 1 3 4,growy");

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
				pokedex = parser.leitura(pokeDex);
				for (Pokedex p : pokedex) {
					bin.writeToFile(p);
				}
				bin.printShits();
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
						// lê da entrada aux ID.setText(aux.toString());
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

		//---- ordMenu ----
		ordMenu.setText("Ordena\u00e7\u00e3o e BTree/Hash");
		ordMenu.setToolTipText("Abre os menus de ordena\u00e7\u00e3o externa e BTree/Hash, junto a opção de pesquisa por indice invertido");
		contentPane.add(ordMenu, "cell 6 5");
		pack();
		setLocationRelativeTo(getOwner());

		ordMenu.addActionListener(e -> {
			try {
				OrdemScreen ordScreen = new OrdemScreen(this);
				new THScreen(this);
				ordScreen.setMinimumSize(this.getMinimumSize());
			} catch (Exception e5) {
				e5.printStackTrace();
			}
		});
		
		//---- compMenu ----
		compMenu.setText("(Des)Compressão");
		contentPane.add(compMenu, "cell 7 5");
		pack();
		setLocationRelativeTo(getOwner());

		compMenu.addActionListener(e -> {
			try {
				telaComp telaComp = new telaComp(this);
			} catch (Exception e5) {
				e5.printStackTrace();
			}
		});

		//---- casaMenu ----
		casaMenu.setText("Casamento de Padrões");
		contentPane.add(casaMenu, "cell 8 5");
		pack();
		setLocationRelativeTo(getOwner());

		casaMenu.addActionListener(e -> {
			try {
				telaCasamento telaCasamento = new telaCasamento(this);
			} catch (Exception e5) {
				e5.printStackTrace();
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
	private JButton ordMenu;
	private JButton compMenu;
	private JButton casaMenu;

	Parser parser = new Parser();
	File miniDex = new File("DOCUMENTOS\\miniDex.csv");
	File pokeDex = new File("DOCUMENTOS\\Pokedex.csv");
	String path = "DATA\\pokedex.db";
	List<Pokedex> pokedex;
	Pokedex aux;
	Binario bin;
}
