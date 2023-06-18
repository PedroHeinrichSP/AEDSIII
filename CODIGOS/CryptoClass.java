import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

public class CryptoClass {
    // Define the encryption algorithms
    private static final String DES = "DES";
    private static final String AES = "AES";

    // Define the secret key
    private static final String KEY = "YourSecretKey"; // This should be replaced with your secret key

    // Method to encrypt data using DES algorithm
    public String encryptDES(String data) throws Exception {
        // Create a DESKeySpec object from the secret key
        DESKeySpec desKeySpec = new DESKeySpec(KEY.getBytes());

        // Create a SecretKeyFactory object
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);

        // Generate a DES SecretKey from the SecretKeyFactory
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);

        // Create a Cipher object and initialize it to use the DES algorithm
        Cipher cipher = Cipher.getInstance(DES);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        // Convert the data to bytes and encrypt it
        byte[] dataBytes = data.getBytes();
        byte[] encryptedDataBytes = cipher.doFinal(dataBytes);

        // Return the encrypted data as a Base64 encoded string
        return Base64.getEncoder().encodeToString(encryptedDataBytes);
    }

    // Method to encrypt data using AES algorithm
    public String encryptAES(String data) throws Exception {
        // Create a SecretKeySpec object from the secret key
        SecretKeySpec secretKey = new SecretKeySpec(KEY.getBytes(), AES);

        // Create a Cipher object and initialize it to use the AES algorithm
        Cipher cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        // Convert the data to bytes and encrypt it
        byte[] dataBytes = data.getBytes();
        byte[] encryptedDataBytes = cipher.doFinal(dataBytes);

        // Return the encrypted data as a Base64 encoded string
        return Base64.getEncoder().encodeToString(encryptedDataBytes);
    }

    // Method to save encrypted data to a .db file
    public void saveEncryptedData(String data) throws Exception {
        // Encrypt the data using the DES algorithm
        String encryptedData = encryptDES(data);

        // Write the encrypted data to a .db file
        Files.write(Paths.get("encryptedData.db"), encryptedData.getBytes());
    }

    // Method to read encrypted data from a .db file
    public String readEncryptedData() throws Exception {
        // Read the encrypted data from a .db file
        byte[] encryptedDataBytes = Files.readAllBytes(Paths.get("encryptedData.db"));

        // Return the encrypted data as a string
        return new String(encryptedDataBytes);
    }

    // Method to decrypt data encrypted with DES algorithm
    public String decryptDES(String encryptedData) throws Exception {
        // Decode the Base64 encoded encrypted data
        byte[] encryptedDataBytes = Base64.getDecoder().decode(encryptedData);

        // Create a DESKeySpec object from the secret key
        DESKeySpec desKeySpec = new DESKeySpec(KEY.getBytes());

        // Create a SecretKeyFactory object
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);

        // Generate a DES SecretKey from the SecretKeyFactory
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);


        // Create a Cipher object and initialize it to use the DES algorithm
        Cipher cipher = Cipher.getInstance(DES);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        // Decrypt the data
        byte[] decryptedDataBytes = cipher.doFinal(encryptedDataBytes);

        // Return the decrypted data as a string
        return new String(decryptedDataBytes);
    }

    // Method to decrypt data encrypted with AES algorithm
    public String decryptAES(String encryptedData) throws Exception {
        // Decode the Base64 encoded encrypted data
        byte[] encryptedDataBytes = Base64.getDecoder().decode(encryptedData);

        // Create a SecretKeySpec object from the secret key
        SecretKeySpec secretKey = new SecretKeySpec(KEY.getBytes(), AES);

        // Create a Cipher object and initialize it to use the AES algorithm
        Cipher cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        // Decrypt the data
        byte[] decryptedDataBytes = cipher.doFinal(encryptedDataBytes);

        // Return the decrypted data as a string
        return new String(decryptedDataBytes);
    }
}
