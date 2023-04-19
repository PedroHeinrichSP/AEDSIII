import java.awt.*;
import javax.swing.*;
import net.miginfocom.swing.*;

public class THScreen extends JFrame  {

	public THScreen(Screen principal) {
		this.principal = principal;
		this.bin = principal.bin;
		initComponents();
	}

	private void initComponents() {

		setVisible(true);
		setLocationRelativeTo(null);
		setMinimumSize(new Dimension(800, 600));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this2 = new JFrame();
		Pokemon = new JLabel();
		entry = new JTextField();
		CREATE = new JButton();
		scrollPane1 = new JScrollPane();
		textArea2 = new JTextArea();
		READ = new JButton();
		UPDATE = new JButton();
		DELETE = new JButton();
		textArea1 = new JTextArea();

		//======== this2 ========
		{
			this2.setVisible(true);
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

			//---- Pokemon image ----
			Pokemon.setIcon(new ImageIcon("DOCUMENTOS\\International_Pokemon_logo.png"));
			this2ContentPane.add(Pokemon, "cell 7 0 1 4,align center center,grow 0 0");

			//---- ENTRY ----
			this2ContentPane.add(entry, "cell 1 1 2 1");

			//---- CREATE ----
			CREATE.setText("CREATE");
			this2ContentPane.add(CREATE, "cell 1 2 2 1");

			//======== scrollPane1 ========
			{
				//---- textArea2 ----
				textArea2.setVisible(false);
				textArea2.setEditable(false);
				textArea2.setLineWrap(true);
				textArea2.setWrapStyleWord(true);
				scrollPane1.setViewportView(textArea2);
			}
			this2ContentPane.add(scrollPane1, "cell 4 2 3 4");

			//---- READ ----
			READ.setText("READ");
			this2ContentPane.add(READ, "cell 1 3 2 1");

			//---- UPDATE ----
			UPDATE.setText("UPDATE");
			this2ContentPane.add(UPDATE, "cell 1 4 2 1");

			//---- DELETE ----
			DELETE.setText("DELETE");
			this2ContentPane.add(DELETE, "cell 1 5 2 1");

			//---- Nome ----
			textArea1.setText("Por Pedro Heinrich e Augusto Scardua");
			this2ContentPane.add(textArea1, "cell 7 5,align center center,grow 0 0");
			this2.pack();
			this2.setLocationRelativeTo(this2.getOwner());
		}

		//---- CREATE ----
		CREATE.addActionListener(e -> {
			try {
				if(entryType()=="id"){
					for(Pokedex p : principal.pokedex){
						if(p.getID()==entryIValue()){
							bin.writeToFile(p);
						}
					}
				} else if (entryType()=="name"){
					for(Pokedex p : principal.pokedex){
						if(p.getName().equals(entrySValue())){
							bin.writeToFile(p);
						}
					}
				}	
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(null, "Ocorreu um erro!\nTente novamente!\nO padrão é tipo:valor\n\nExemplo:\nid:1\nname:bulbasaur");
				
			}
		});

		//---- READ ----
		READ.addActionListener(e -> {
			try {
				System.out.println(entryType()+" "+entrySValue()+" "+entryIValue()+"\n");
				if(entryType()=="id"){
					aux = bin.read(entryIValue());
				} else if (entryType()=="name"){
					aux = bin.read(entrySValue());
				}
				textArea2.setText(aux.toString());			
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(null, "Ocorreu um erro!\nTente novamente!\nO padrão é tipo:valor\n\nExemplo:\nid:1\nname:bulbasaur");
			}
		});

		//---- UPDATE ----
		UPDATE.addActionListener(e -> {
			try {
				if(entryType()=="id"){
					aux = bin.read(entryIValue());
				} else if (entryType()=="name"){
					aux = bin.read(entrySValue());
				}
				UptadeScreen uptade = new UptadeScreen(aux, aux.getID(), this);
					uptade.setVisible(true);

					uptade.addWindowListener(new java.awt.event.WindowAdapter() {
						@Override
						public void windowClosed(java.awt.event.WindowEvent windowEvent) {
							// lê da entrada aux ID.setText(aux.toString());
							try {
								bin.update(aux);
							} catch (Exception e1) {
								e1.printStackTrace();
							}
						}
					});
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(null, "Ocorreu um erro!\nTente novamente!\nO padrão é tipo:valor\n\nExemplo:\nid:1\nname:bulbasaur");
			}
		});

		//---- DELETE ----
		DELETE.addActionListener(e -> {
			try {
				if(entryType()=="id"){
					bin.delete(entryIValue());
				} else if (entryType()=="name"){
					bin.delete(entrySValue());
				}
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(null, "Ocorreu um erro!\nTente novamente!\nO padrão é tipo:valor\n\nExemplo:\nid:1\nname:bulbasaur");
			}
		});
	}
	private JFrame this2;
	private JLabel Pokemon;
	private JTextField entry;
	private JButton CREATE;
	private JScrollPane scrollPane1;
	private JTextArea textArea2;
	private JButton READ;
	private JButton UPDATE;
	private JButton DELETE;
	private JTextArea textArea1;
	private Binario bin;
	private Pokedex aux;
	private Screen principal;

	//---- GET ENTRY TYPE AND VALUE ----
	String entryType() {
		return entry.getText().split(":")[0];
	}
	String entrySValue() {
		return entry.getText().split(":")[1];
	}
	int entryIValue() {
		return Integer.parseInt(entry.getText().split(":")[1]);
	}
}
