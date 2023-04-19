import java.awt.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import net.miginfocom.swing.*;

public class UptadeScreen extends JFrame {
	public UptadeScreen(Pokedex pokedex, int id, Screen screen) {
		this.entry = pokedex;
		this.id = id;
		initComponents();
	}
	
	public UptadeScreen(Pokedex pokedex, int id, THScreen screen) {
		this.entry = pokedex;
		this.id = id;
		initComponents();
	}

	private void initComponents() {
		
		// Variaveis de tela
		setVisible(false);
		setLocationRelativeTo(null);
		setMinimumSize(new Dimension(800, 600));

		// Inicialização dos componentes
		labelID = new JLabel();
		textFieldID = new JTextField();
		labelName = new JLabel();
		textFieldName = new JTextField();
		labelGen = new JLabel();
		textFieldGen = new JTextField();
		labelHt = new JLabel();
		textFieldHt = new JTextField();
		labelWt = new JLabel();
		textFieldWt = new JTextField();
		labelType = new JLabel();
		textFieldType = new JTextField();
		textFieldType1 = new JTextField();
		labelCat = new JLabel();
		textFieldCat = new JTextField();
		labelMega = new JLabel();
		textFieldMega = new JTextField();
		labelTotal = new JLabel();
		textFieldTotal = new JTextField();
		button1 = new JButton();

		//======== this ========
		Container contentPane = getContentPane();
		contentPane.setLayout(new MigLayout(
			"fill,hidemode 3",
			// columns
			"[369,fill]" +
			"[359,fill]",
			// rows
			"[]" +
			"[]" +
			"[]" +
			"[]" +
			"[]" +
			"[]" +
			"[]" +
			"[]" +
			"[]" +
			"[]"));

		//---- labelID ----
		labelID.setText("ID");
		labelID.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(labelID, "cell 0 0");
		contentPane.add(textFieldID, "cell 1 0");

		//---- labelName ----
		labelName.setText("Nome");
		labelName.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(labelName, "cell 0 1");
		contentPane.add(textFieldName, "cell 1 1");

		//---- labelGen ----
		labelGen.setText("Gera\u00e7\u00e3o");
		labelGen.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(labelGen, "cell 0 2");
		contentPane.add(textFieldGen, "cell 1 2");

		//---- labelHt ----
		labelHt.setText("Altura");
		labelHt.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(labelHt, "cell 0 3");
		contentPane.add(textFieldHt, "cell 1 3");

		//---- labelWt ----
		labelWt.setText("Peso");
		labelWt.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(labelWt, "cell 0 4");
		contentPane.add(textFieldWt, "cell 1 4");

		//---- labelType ----
		labelType.setText("Tipos");
		labelType.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(labelType, "cell 0 5");
		contentPane.add(textFieldType, "cell 1 5");
		contentPane.add(textFieldType1, "cell 1 5");

		//---- labelCat ----
		labelCat.setText("Categoria");
		labelCat.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(labelCat, "cell 0 6");
		contentPane.add(textFieldCat, "cell 1 6");

		//---- labelMega ----
		labelMega.setText("Mega evolui?");
		labelMega.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(labelMega, "cell 0 7");
		contentPane.add(textFieldMega, "cell 1 7");

		//---- labelTotal ----
		labelTotal.setText("Total de Atributos");
		labelTotal.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(labelTotal, "cell 0 8");
		contentPane.add(textFieldTotal, "cell 1 8");
		pack();
		setLocationRelativeTo(getOwner());

		//---- button1 ----
		button1.setText("Salvar");
		contentPane.add(button1, "cell 2 9,align center center,grow 0 0");
		pack();
		setLocationRelativeTo(getOwner());

		// Atribuição de valores
		textFieldID.setText(String.valueOf(entry.getID()));
		textFieldName.setText(String.valueOf(entry.getName()));
		textFieldGen.setText(format.format(entry.getGeneration()));
		textFieldHt.setText(String.valueOf(entry.getHeight()));
		textFieldWt.setText(String.valueOf(entry.getWeight()));
		textFieldType.setText(entry.getType().get(0));
		textFieldType1.setText(entry.getType().get(1));
		textFieldCat.setText(String.valueOf(entry.getCategory()));
		textFieldMega.setText(String.valueOf(entry.getMega_Evolution_Flag()));
		textFieldTotal.setText(String.valueOf(entry.getTOTAL()));

		// Ações dos botões
		button1.addActionListener(e -> {
				entry.setID(Short.parseShort(textFieldID.getText()));
				entry.setName(textFieldName.getText());
				DateFormat format = new SimpleDateFormat("EEE MMM dd kk:mm:ss zzz yyyy");
				try {
					entry.setGeneration(format.parse(textFieldGen.getText()));
				} catch (ParseException e1) {
					JOptionPane.showMessageDialog(null, "Digite a data novamente no formato correto");
				}
				entry.setHeight(Float.parseFloat(textFieldHt.getText()));
				entry.setWeight(Float.parseFloat(textFieldWt.getText()));
				List<String> type = new ArrayList<String>();
				type.add(textFieldType.getText());
				type.add(textFieldType1.getText());
				entry.setType(type);
				entry.setCategory(Byte.parseByte(textFieldCat.getText()));
				entry.setMega_Evolution_Flag(Boolean.parseBoolean(textFieldMega.getText()));
				entry.setTOTAL(Integer.parseInt(textFieldTotal.getText()));
				JOptionPane.showMessageDialog(null, "Registro atualizado com sucesso!");
				dispose();
			});
	}


	// Declaração de componentes e variaveis
	private JLabel labelID;
	private JTextField textFieldID;
	private JLabel labelName;
	private JTextField textFieldName;
	private JLabel labelGen;
	private JTextField textFieldGen;
	private JLabel labelHt;
	private JTextField textFieldHt;
	private JLabel labelWt;
	private JTextField textFieldWt;
	private JLabel labelType;
	private JTextField textFieldType;
	private JTextField textFieldType1;
	private JLabel labelCat;
	private JTextField textFieldCat;
	private JLabel labelMega;
	private JTextField textFieldMega;
	private JLabel labelTotal;
	private JTextField textFieldTotal;
	private JButton button1;

	Pokedex entry;
	int id;
	DateFormat format = new SimpleDateFormat("EEE MMM dd kk:mm:ss zzz yyyy");
}
