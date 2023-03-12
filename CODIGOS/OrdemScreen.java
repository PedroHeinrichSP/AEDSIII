import java.awt.*;
import java.io.IOException;
import javax.swing.*;
import net.miginfocom.swing.*;

public class OrdemScreen extends JFrame{
	public OrdemScreen(Screen screen) throws IOException {
		this.screen = screen;
		this.ordenador = new neoOrdenador(screen.path, 500);
		this.ordenadorSecond = new neoOrdenadorSecond(screen.path, 500);
		initComponents();
	}
	private void initComponents() {

		// Variaveis de tela
		setVisible(false);
		setLocationRelativeTo(screen);
		setMinimumSize(new Dimension(800, 600));

		thisScreen = new JFrame();
		Pokemon = new JLabel();
		comum = new JButton();
		heap = new JButton();
		subst = new JButton();
		textArea1 = new JTextArea();

		//======== thisScreen ========
		{
			thisScreen.setVisible(true);
			Container thisScreenContentPane = thisScreen.getContentPane();
			thisScreenContentPane.setLayout(new MigLayout(
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

			//---- Pokemon ----
			Pokemon.setIcon(new ImageIcon("DOCUMENTOS\\International_Pokemon_logo.png"));
			thisScreenContentPane.add(Pokemon, "cell 7 0 1 3,align center center,grow 0 0");

			//---- comum ----
			comum.setText("Intercala\u00e7\u00e3o balanceada comum");
			thisScreenContentPane.add(comum, "cell 1 2");

			//---- heap ----
			heap.setText("Intercala\u00e7\u00e3o com blocos de tamanho vari\u00e1vel");
			thisScreenContentPane.add(heap, "cell 1 3");

			//---- subst ----
			subst.setText("Intercala\u00e7\u00e3o com sele\u00e7\u00e3o por substitui\u00e7\u00e3o");
			thisScreenContentPane.add(subst, "cell 1 4");

			//---- textArea1 ----
			textArea1.setText("Por Pedro Heinrich e Augusto Scardua");
			thisScreenContentPane.add(textArea1, "cell 7 4,align center center,grow 0 0");
			thisScreen.pack();
			thisScreen.setLocationRelativeTo(thisScreen.getOwner());
		}

		comum.addActionListener( e ->{
			try {
				ordenador.sort();
				JOptionPane.showMessageDialog(null, "Ordenado com sucesso!");
				dispose();
			} catch (Exception e1) {
				e1.printStackTrace();				
				JOptionPane.showMessageDialog(null, "Ocorreu um erro!\nTente novamente!");
			}
		});

		heap.addActionListener( e ->{
			try {
				JOptionPane.showMessageDialog(null, "Sinto muito mas o ordenador está em outro castelo!");
			} catch (Exception e1) {
				e1.printStackTrace();
				JOptionPane.showMessageDialog(null, "Ocorreu um erro!\nTente novamente!");
			}
		});

		subst.addActionListener( e ->{
			try {
				ordenadorSecond.sort();
				JOptionPane.showMessageDialog(null, "Ordenado com sucesso!");
				dispose();
			} catch (Exception e1) {
				e1.printStackTrace();
				JOptionPane.showMessageDialog(null, "Ocorreu um erro!\nTente novamente!");
			}
		});

		}


	private JFrame thisScreen;
	private JLabel Pokemon;
	private JButton comum;
	private JButton heap;
	private JButton subst;
	private JTextArea textArea1;
	public Screen screen;
	public neoOrdenador ordenador;
	public neoOrdenadorSecond ordenadorSecond;
}