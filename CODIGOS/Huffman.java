import java.util.*;

public class Huffman {

    static class Node implements Comparable<Node> {
        byte data;
        int frequency;
        Node left, right;

        public Node(byte data, int frequency) {
            this.data = data;
            this.frequency = frequency;
        }

        public Node(byte data, int frequency, Node left, Node right) {
            this.data = data;
            this.frequency = frequency;
            this.left = left;
            this.right = right;
        }

        public boolean isLeaf() {
            return left == null && right == null;
        }

        @Override
        public int compareTo(Node other) {
            return this.frequency - other.frequency;
        }
    }

    public static byte[] compress(byte[] data) {
        // Contagem da frequência de cada byte no array de dados
        Map<Byte, Integer> frequencyMap = new HashMap<>();
        for (byte b : data) {
            frequencyMap.put(b, frequencyMap.getOrDefault(b, 0) + 1);
        }

        // Criação da árvore de Huffman
        PriorityQueue<Node> priorityQueue = new PriorityQueue<>();
        for (Map.Entry<Byte, Integer> entry : frequencyMap.entrySet()) {
            priorityQueue.offer(new Node(entry.getKey(), entry.getValue()));
        }

        while (priorityQueue.size() > 1) {
            Node left = priorityQueue.poll();
            Node right = priorityQueue.poll();
            Node parent = new Node((byte) 0, left.frequency + right.frequency, left, right);
            priorityQueue.offer(parent);
        }

        Node root = priorityQueue.poll();

        // Construção da tabela de codificação
        Map<Byte, String> encodingTable = new HashMap<>();
        buildEncodingTable(root, "", encodingTable);

        // Compressão dos dados
        StringBuilder compressedData = new StringBuilder();
        for (byte b : data) {
            compressedData.append(encodingTable.get(b));
        }

        // Criação do array de bytes comprimidos
        int numBytes = (compressedData.length() + 7) / 8;
        byte[] compressedBytes = new byte[numBytes];
        for (int i = 0; i < compressedData.length(); i += 8) {
            String byteString = compressedData.substring(i, Math.min(i + 8, compressedData.length()));
            byte compressedByte = (byte) Integer.parseInt(byteString, 2);
            compressedBytes[i / 8] = compressedByte;
        }

        return compressedBytes;
    }

    private static void buildEncodingTable(Node node, String code, Map<Byte, String> encodingTable) {
        if (node.isLeaf()) {
            encodingTable.put(node.data, code);
        } else {
            buildEncodingTable(node.left, code + "0", encodingTable);
            buildEncodingTable(node.right, code + "1", encodingTable);
        }
    }

    public static byte[] decompress(byte[] compressedData) {
        // Reconstrução da árvore de Huffman a partir dos dados comprimidos
        int numBytes = compressedData.length;
        StringBuilder compressedBits = new StringBuilder();
        for (int i = 0; i < numBytes; i++) {
            String byteString = String.format("%8s", Integer.toBinaryString(compressedData[i] & 0xFF)).replace(' ', '0');
            compressedBits.append(byteString);
        }

        Node root = reconstructHuffmanTree(compressedBits);

        // Descompressão dos dados
        List<Byte> decompressedDataList = new ArrayList<>();
        Node current = root;
        for (int i = 0; i < compressedBits.length(); i++) {
            if (compressedBits.charAt(i) == '0') {
                current = current.left;
            } else {
                current = current.right;
            }

            if (current.isLeaf()) {
                decompressedDataList.add(current.data);
                current = root;
            }
        }

        // Criação do array de bytes descomprimidos
        byte[] decompressedData = new byte[decompressedDataList.size()];
        for (int i = 0; i < decompressedDataList.size(); i++) {
            decompressedData[i] = decompressedDataList.get(i);
        }

        return decompressedData;
    }

    private static Node reconstructHuffmanTree(StringBuilder compressedBits) {
        if (compressedBits.charAt(0) == '0') {
            compressedBits.deleteCharAt(0);
            return new Node((byte) 0, 0);
        } else {
            compressedBits.deleteCharAt(0);
            Node left = reconstructHuffmanTree(compressedBits);
            Node right = reconstructHuffmanTree(compressedBits);
            return new Node((byte) 0, 0, left, right);
        }
    }

}
