import java.util.*;

public class LZW {

    public static byte[] compress(byte[] data) {
        List<Byte> compressedData = new ArrayList<>();
        Map<String, Integer> dictionary = new HashMap<>();

        // Inicialização do dicionário com os bytes possíveis
        for (int i = 0; i < 256; i++) {
            dictionary.put(String.valueOf((byte) i), i);
        }

        String current = "";
        for (byte b : data) {
            String currentPlusByte = current + b;
            if (dictionary.containsKey(currentPlusByte)) {
                current = currentPlusByte;
            } else {
                compressedData.add((byte) (int) dictionary.get(current));
                dictionary.put(currentPlusByte, dictionary.size());
                current = String.valueOf(b);
            }
        }

        // Adiciona o último código ao array comprimido
        compressedData.add((byte) (int) dictionary.get(current));

        // Criação do array de bytes comprimidos
        byte[] compressedBytes = new byte[compressedData.size()];
        for (int i = 0; i < compressedData.size(); i++) {
            compressedBytes[i] = compressedData.get(i);
        }

        return compressedBytes;
    }

    public static byte[] decompress(byte[] compressedData) {
        List<Byte> decompressedData = new ArrayList<>();
        Map<Integer, String> dictionary = new HashMap<>();

        // Inicialização do dicionário com os bytes possíveis
        for (int i = 0; i < 256; i++) {
            dictionary.put(i, String.valueOf((byte) i));
        }

        int previousCode = compressedData[0] & 0xFF;
        decompressedData.add((byte) previousCode);

        for (int i = 1; i < compressedData.length; i++) {
            int currentCode = compressedData[i] & 0xFF;

            String entry;
            if (dictionary.containsKey(currentCode)) {
                entry = dictionary.get(currentCode);
            } else if (currentCode == dictionary.size()) {
                entry = dictionary.get(previousCode) + dictionary.get(previousCode).charAt(0);
            } else {
                throw new IllegalArgumentException("Dados comprimidos inválidos.");
            }

            decompressedData.addAll(getBytesFromString(entry));

            dictionary.put(dictionary.size(), dictionary.get(previousCode) + entry.charAt(0));
            previousCode = currentCode;
        }

        // Criação do array de bytes descomprimidos
        byte[] decompressedBytes = new byte[decompressedData.size()];
        for (int i = 0; i < decompressedData.size(); i++) {
            decompressedBytes[i] = decompressedData.get(i);
        }

        return decompressedBytes;
    }

    private static List<Byte> getBytesFromString(String str) {
        List<Byte> bytes = new ArrayList<>();
        for (char c : str.toCharArray()) {
            bytes.add((byte) c);
        }
        return bytes;
    }
}