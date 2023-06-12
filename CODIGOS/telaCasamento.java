import java.awt.*;

import javax.swing.*;
import net.miginfocom.swing.*;

public class telaCasamento extends JFrame {
	public telaCasamento(Screen main) {
        this.main = main;
		initComponents();
	}

	private void initComponents() {

		this2 = new JFrame();
		Pokemon = new JLabel();
		entry = new JTextField();
		INDEX = new JButton();
		scrollPane1 = new JScrollPane();
		textArea2 = new JTextArea();

		// ======== this2 ========
		{
			this2.setLocationRelativeTo(principal);
			this2.setVisible(true);
			this2.setMinimumSize(new Dimension(800, 600));
			this2.setPreferredSize(new Dimension(800, 600));
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this2.setBackground(new Color(0x666666));
			Container this2ContentPane = this2.getContentPane();
			this2ContentPane.setLayout(new MigLayout(
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
							"[]" +
							"[]"));

			// ---- Pokemon image ----
			Pokemon.setIcon(new ImageIcon("DOCUMENTOS\\International_Pokemon_logo.png"));
			this2ContentPane.add(Pokemon, "cell 7 0 1 4,align center center,grow 0 0");

			// ---- ENTRY ----
			this2ContentPane.add(entry, "cell 1 1 2 1");

			// ---- INDEX ----
			INDEX.setText("INDEX");
			this2ContentPane.add(INDEX, "cell 1 2 2 1");

			// ======== scrollPane1 ========
			{
				// ---- textArea2 ----
				textArea2.setVisible(false);
				textArea2.setEditable(false);
				textArea2.setLineWrap(true);
				textArea2.setWrapStyleWord(true);
				scrollPane1.setViewportView(textArea2);
			}
			this2ContentPane.add(scrollPane1, "cell 4 1 3 5,growy");

		}

		// ---- INDEX ----
		INDEX.addActionListener(e -> {
			try {
                String tela = "";
                tela += casamento.KMP(getAllString(), entry.getText()) + "\n";
                tela += casamento.bruteForce(getAllString(), entry.getText()) + "\n";
                textArea2.setText(tela);
			} catch (Exception ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(null, "Ocorreu um erro!\nTente novamente! " + ex);
			}
		});
	}

	private JFrame this2;
	private JLabel Pokemon;
	private JTextField entry;
	private JButton INDEX;
	private JScrollPane scrollPane1;
	private Screen principal;
	private JTextArea textArea2;
    
    public Screen main;

    // get all pokemons strings
    private String getAllString() {
        String warning = "";
        for (int i = 0; i < main.pokedex.size(); i++) {
            warning += main.pokedex.get(i).toString() + "\n";
        }
        return warning;
    }
}
