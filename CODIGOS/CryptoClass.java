import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

public class CryptoClass {
    // Define os algoritmos de criptografia
    private static final String DES = "DES";
    private static final String AES = "AES";

    // Define a chave secreta
    private static final String KEY = "2B7E151628AED2A6ABF7158809CF4F3C";

    // Método para criptografar dados usando o algoritmo DES
    public String encryptDES(String data) throws Exception {
        // Cria um objeto DESKeySpec a partir da chave secreta
        DESKeySpec desKeySpec = new DESKeySpec(KEY.getBytes());

        // Cria um objeto SecretKeyFactory
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);

        // Gera uma SecretKey DES a partir do SecretKeyFactory
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);

        // Cria um objeto Cipher e o inicializa para usar o algoritmo DES
        Cipher cipher = Cipher.getInstance(DES);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        // Converte os dados em bytes e os criptografa
        byte[] dataBytes = data.getBytes();
        byte[] encryptedDataBytes = cipher.doFinal(dataBytes);

        // Retorna os dados criptografados como uma string codificada em Base64
        return Base64.getEncoder().encodeToString(encryptedDataBytes);
    }

    // Método para criptografar dados usando o algoritmo AES
    public String encryptAES(String data) throws Exception {
        // Cria um objeto SecretKeySpec a partir da chave secreta
        SecretKeySpec secretKey = new SecretKeySpec(KEY.getBytes(), AES);

        // Cria um objeto Cipher e o inicializa para usar o algoritmo AES
        Cipher cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        // Converte os dados em bytes e os criptografa
        byte[] dataBytes = data.getBytes();
        byte[] encryptedDataBytes = cipher.doFinal(dataBytes);

        // Retorna os dados criptografados como uma string codificada em Base64
        return Base64.getEncoder().encodeToString(encryptedDataBytes);
    }

    // Método para salvar os dados criptografados em um arquivo .db
    public void saveEncryptedData(String data) throws Exception {
        // Criptografa os dados usando o algoritmo DES
        String encryptedData = encryptDES(data);

        // Escreve os dados criptografados em um arquivo .db
        Files.write(Paths.get("encryptedData.db"), encryptedData.getBytes());
    }

    // Método para ler os dados criptografados de um arquivo .db
    public String readEncryptedData() throws Exception {
        // Lê os dados criptografados de um arquivo .db
        byte[] encryptedDataBytes = Files.readAllBytes(Paths.get("encryptedData.db"));

        // Retorna os dados criptografados como uma string
        return new String(encryptedDataBytes);
    }

    // Método para descriptografar dados criptografados com o algoritmo DES
    public String decryptDES(String encryptedData) throws Exception {
        // Decodifica os dados criptografados codificados em Base64
        byte[] encryptedDataBytes = Base64.getDecoder().decode(encryptedData);

        // Cria um objeto DESKeySpec a partir da chave secreta

        DESKeySpec desKeySpec = new DESKeySpec(KEY.getBytes());

        // Criar um objeto SecretKeyFactory
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);

        // Gerar uma SecretKey DES a partir do SecretKeyFactory
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);

        // Criar um objeto Cipher e inicializá-lo para usar o algoritmo DES
        Cipher cipher = Cipher.getInstance(DES);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        // Descriptografar os dados
        byte[] decryptedDataBytes = cipher.doFinal(encryptedDataBytes);

        // Retornar os dados descriptografados como uma string
        return new String(decryptedDataBytes);
    }

    // Método para descriptografar dados criptografados com o algoritmo AES
    public String decryptAES(String encryptedData) throws Exception {
        // Decodificar os dados criptografados codificados em Base64
        byte[] encryptedDataBytes = Base64.getDecoder().decode(encryptedData);

        // Criar um objeto SecretKeySpec a partir da chave secreta
        SecretKeySpec secretKey = new SecretKeySpec(KEY.getBytes(), AES);

        // Criar um objeto Cipher e inicializá-lo para usar o algoritmo AES
        Cipher cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        // Descriptografar os dados
        byte[] decryptedDataBytes = cipher.doFinal(encryptedDataBytes);

        // Retornar os dados descriptografados como uma string
        return new String(decryptedDataBytes);
    }
}