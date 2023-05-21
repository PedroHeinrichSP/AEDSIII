import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import javax.swing.*;
import net.miginfocom.swing.*;

public class telaComp extends JFrame {
	public telaComp(Screen main) {
		this.main = main;
		this.bin = main.bin;
		initComponents();
	}

	private void initComponents() {

		this_ = new JFrame();
		String[] comboBoxModel = { "Compressão", "Descompressão" };
		Tipo = new JComboBox<>(comboBoxModel);
		Texto = new JTextArea();
		EscolhaArquivo = new JButton();

		// ======== this ========
		this_.setVisible(true);
		this_.setLocationRelativeTo(null);
		this_.setMinimumSize(new Dimension(800, 600));
		this_.setPreferredSize(new Dimension(800, 600));
		this_.setBackground(new Color(0x666666));
		Container this_ContentPane = this_.getContentPane();
		this_ContentPane.setLayout(new MigLayout(
				"fill,hidemode 3",
				// columns
				"[fill]" +
						"[fill]" +
						"[fill]",
				// rows
				"[]" +
						"[]" +
						"[]" +
						"[]"));

		// ---- Tipo ----
		Tipo.setMaximumRowCount(2);
		Tipo.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		this_.add(Tipo, "cell 0 1,growx");

		// ---- Texto ----
		Texto.setEditable(false);
		Texto.setLineWrap(true);
		Texto.setWrapStyleWord(true);
		Texto.setText("Nenhum arquivo selecionado");
		this_.add(Texto, "cell 1 1 2 2,growy");

		// ---- EscolhaArquivo ----
		EscolhaArquivo.setText("Escolha o arquivo...");
		EscolhaArquivo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		this_.add(EscolhaArquivo, "cell 0 2");

		EscolhaArquivo.addActionListener(e -> {
			// Abre a escolha de arquivos na pasta DATA
			JFileChooser fileChooser = new JFileChooser();
			File currentDir = new File("DATA\\");
			File file;
			fileChooser.setCurrentDirectory(currentDir);
			fileChooser.setDialogTitle("Escolha o arquivo");
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			int retorno = fileChooser.showOpenDialog(this_);
			// Se o arquivo for selecionado, o caminho dele é salvo
			if (retorno == JFileChooser.APPROVE_OPTION) {
				file = fileChooser.getSelectedFile();
				String selectedOption = (String) Tipo.getSelectedItem();
				if (selectedOption.equals("Compressão")) {
					compress(file);
				} else {
					decompress(file);
				}
			}
		});
	}

	private JFrame this_;
	private JComboBox Tipo;
	private JTextArea Texto;
	private JButton EscolhaArquivo;

	public Screen main;
	public Binario bin;

	private void compress(File file) {
		// Compressão
		long startTime;
		long endTime;
		long huffmanTime;
		long lzwTime;
		try {
			FileInputStream fis = new FileInputStream(file);

			byte[] data = new byte[(int) file.length()];
			fis.read(data);

			// Algoritmo Huffman
			startTime = System.nanoTime();
			byte[] compHuffman = Huffman.compress(data);
			endTime = System.nanoTime();
			huffmanTime = endTime - startTime;

			// Algoritmo LZW
			startTime = System.nanoTime();
			byte[] compLZW = LZW.compress(data);
			endTime = System.nanoTime();
			lzwTime = endTime - startTime;

			// Salva os arquivos comprimidos
			File compressedHuffman = new File(file.getAbsolutePath() + "HuffmanCompressao.huff");
			File compressedLZW = new File(file.getAbsolutePath() + "LZWCompressao.lzw");	

			// Huffman
			FileOutputStream fos = new FileOutputStream(compressedHuffman);
			fos.write(compHuffman);
			fos.close();
			// LZW
			fos = new FileOutputStream(compressedLZW);
			fos.write(compLZW);
			fos.close();

			fis.close();
			//Escrever na textbox a porcentagem de ganho/perda de cada algoritmo e o tempo de execução de cada em segundos
			Texto.setText(file.getAbsolutePath() + "\nHuffman: " + (int) (100 - (compressedHuffman.length() * 100.0 / file.length())) + "% de ganho, em " + huffmanTime / 1000000000.0 + " segundos\n" + 
					"LZW: " + (int) (100 - (compressedLZW.length() * 100.0 / file.length())) + "% de ganho, em " + lzwTime / 1000000000.0 + " segundos");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void decompress(File file) {
		// Descompressão
		try {
			FileInputStream fis = new FileInputStream(file);

			byte[] decomp;
			byte[] data = new byte[(int) file.length()];
			fis.read(data);
			long startTime;
			long endTime;


			// Caso .huff fazer Huffman e caso .lzw fazer LZW
			if (file.getName().contains(".huff")) {
				startTime = System.nanoTime();
				decomp = Huffman.decompress(data);
				endTime = System.nanoTime();
				long decompTime = endTime - startTime;

				// Salva os arquivos comprimidos
				File decompressedHuffman = new File(file.getAbsolutePath() + "HuffmanDescompressao." + file.getName().substring(file.getName().lastIndexOf(".") + 1));

				FileOutputStream fos = new FileOutputStream(decompressedHuffman);
				fos.write(decomp);
				fos.close();
				Texto.setText(file.getAbsolutePath() + "\nHuffman: " + (int) (100 - (decompressedHuffman.length() * 100.0 / file.length())) + "% de ganho, em " + decompTime / 1000000000.0 + " segundos\n");
			} else {
				startTime = System.nanoTime();
				decomp = LZW.decompress(data);
				endTime = System.nanoTime();
				long decompTime = endTime - startTime;
				
				// Salva os arquivos comprimidos
				File decompressedLZW = new File(file.getAbsolutePath() + "LZWDescompressao." + file.getName().substring(file.getName().lastIndexOf(".") + 1));	
				

				FileOutputStream fos = new FileOutputStream(decompressedLZW);
				fos.write(decomp);
				fos.close();
				Texto.setText(file.getAbsolutePath() + "\nLZW: " + (int) (100 - (decompressedLZW.length() * 100.0 / file.length())) + "% de ganho, em " + decompTime / 1000000000.0 + " segundos\n");
			}
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
