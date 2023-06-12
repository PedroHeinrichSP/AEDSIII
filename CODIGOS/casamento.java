public class casamento {
    
    // Método de força bruta para strings para correspondência de padrões
    // que retorna a quantidade total de operações e o tempo gasto nelas
    public static int bruteForce(String text, String pattern) {
        int n = text.length(); // Tamanho do texto
        int m = pattern.length(); // Tamanho do padrão
        int operations = 0; // Contador de operações
        long startTime = System.nanoTime(); // Tempo inicial
        for (int i = 0; i < n - m + 1; i++) { // Percorre o texto
            int j = 0; // Índice do padrão
            while (j < m && text.charAt(i + j) == pattern.charAt(j)) { // Enquanto houver uma correspondência parcial
                j++; // Avança para o próximo elemento no padrão
                operations++; // Conta as operações realizadas
            }
            if (j == m) { // Se todos os elementos do padrão correspondem, retorna a quantidade de operações e o tempo gasto
                long endTime = System.nanoTime(); // Tempo final
                System.out.println("Total time: " + (endTime - startTime) + " nanoseconds");
                return operations;
            }
        }
        long endTime = System.nanoTime(); // Tempo final
        System.out.println("Total time: " + (endTime - startTime) + " nanoseconds");
        return operations;
    }

    // Método KMP para strings para correspondência de padrões
    // que retorna a quantidade total de operações e o tempo gasto nelas
    public static int KMP(String text, String pattern) {
        int n = text.length(); // Tamanho do texto
        int m = pattern.length(); // Tamanho do padrão
        int operations = 0; // Contador de operações
        long startTime = System.nanoTime(); // Tempo inicial
        int[] pi = prefixFunction(pattern); // Vetor de prefixo
        int q = 0; // Índice do padrão
        for (int i = 0; i < n; i++) { // Percorre o texto
            while (q > 0 && pattern.charAt(q) != text.charAt(i)) { // Enquanto houver uma não correspondência parcial
                q = pi[q - 1]; // Atualiza o índice do padrão usando a função de prefixo
                operations++; // Conta as operações realizadas
            }
            if (pattern.charAt(q) == text.charAt(i)) { // Se os elementos correspondentes do padrão são iguais
                q++; // Avança para o próximo elemento no padrão
                operations++; // Conta as operações realizadas
            }
            if (q == m) { // Se todos os elementos do padrão correspondem, retorna a quantidade de operações e o tempo gasto
                long endTime = System.nanoTime(); // Tempo final
                System.out.println("Total time: " + (endTime - startTime) + " nanoseconds");
                return operations;
            }
        }
        long endTime = System.nanoTime(); // Tempo final
        System.out.println("Total time: " + (endTime - startTime) + " nanoseconds");
        return operations;
    }
    //create the prefix function method
    private static int[] prefixFunction(String pattern) {
        int m = pattern.length(); // Tamanho do padrão
        int[] pi = new int[m]; // Vetor de prefixo
        pi[0] = 0; // O primeiro elemento do vetor de prefixo é sempre 0
        int k = 0; // Índice do padrão
        for (int q = 1; q < m; q++) { // Percorre o padrão
            while (k > 0 && pattern.charAt(k) != pattern.charAt(q)) { // Enquanto houver uma não correspondência parcial
                k = pi[k - 1]; // Atualiza o índice do padrão
            }
            if (pattern.charAt(k) == pattern.charAt(q)) { // Se os elementos correspondentes do padrão são iguais
                k++; // Avança para o próximo elemento no padrão
            }
            pi[q] = k; // Atualiza o vetor de prefixo
        }
        return pi; // Retorna o vetor de prefixo
    }
}
