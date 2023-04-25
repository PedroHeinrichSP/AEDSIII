import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/*********
 * ARVORE B+ SI
 * String key, int dado
 * 
 * Os nomes dos métodos foram mantidos em inglês
 * apenas para manter a coerência com o resto da
 * disciplina:
 * - boolean create(String key, int dado)
 * - int read(String key)
 * - boolean update(String key, int dado)
 * - boolean delete(String key)
 * 
 * Implementado pelo Prof. Marcos Kutova
 * v1.0 - 2018
 */

// Árvore B+ para ser usada como índice indireto de algum file de entidades
// key: String  (usado para algum atributo textual da entidade como Nome, Título, ...)
// VALOR: Int     (usado para o identificador dessa entidade)

public class BPTree {

    private int  order;                 // Número máximo de sons que uma página pode conter
    private int  maxElements;          // Variável igual a order - 1 para facilitar a clareza do código
    private int  maxSons;             // Variável igual a order para facilitar a clareza do código
    private RandomAccessFile file;   // file em que a árvore será armazenada
    private String fileName;
    
    // Variáveis usadas nas funções recursivas (já que não é possível passar valores por referência)
    private String  keyAux;
    private int     dataAux;
    private long    pageAux;
    private boolean increase;
    private boolean decrease;
    
    // Esta classe representa uma página da árvore (folha ou não folha). 
    private class Page {

        protected int      order;                 // Número máximo de sons que uma página pode ter
        protected int      maxElements;          // Variável igual a order - 1 para facilitar a clareza do código
        protected int      maxSons;             // Variável igual a order  para facilitar a clareza do código
        protected int      n;                     // Número de elementos presentes na página
        protected String[] keys;                // keys
        protected int[]    datas;                 // datas associados às keys
        protected long     next;               // Próxima folha, quando a página for uma folha
        protected long[]   sons;                // Vetor de ponteiros para os sons
        protected int      KEY_SIZE;         // Tamanho da string máxima usada como key
        protected int      REGISTRY_SIZE;      // Os elementos são de tamanho fixo
        protected int      PAGE_SIZE;        // A página será de tamanho fixo, calculado a partir da order

        // Construtor da página
        public Page(int o) {

            // Inicialização dos atributos
            n = 0;
            order = o;
            maxSons = o;
            maxElements = o-1;
            keys = new String[maxElements];
            datas  = new int[maxElements];
            sons = new long[maxSons];
            next = -1;
            
            // Criação de uma página vázia
            for(int i=0; i<maxElements; i++) {  
                keys[i] = "";
                datas[i]  = -1;
                sons[i] = -1;
            }
            sons[maxSons-1] = -1;
            
            // Cálculo do tamanho (fixo) da página
            // n -> 4 bytes
            // cada elemento -> 104 bytes (string + int)
            // cada ponteiro de filho -> 8 bytes (long)
            // último filho -> 8 bytes (long)
            // ponteiro próximo -> 8 bytes
            KEY_SIZE = 100;
            REGISTRY_SIZE = 104;
            PAGE_SIZE = 4 + maxElements*REGISTRY_SIZE + maxSons*8 + 16;
        }
        
        // Como uma key string tem tamanho variável (por causa do Unicode),
        // provavelmente não será possível ter uma string de 100 caracteres.
        // Os caracteres excedentes (já que a página tem que ter tamanho fixo)
        // são preenchidos com espaços em branco
        private byte[] fillBlanks(String str) {
            byte[] aux;
            byte[] buffer = new byte[KEY_SIZE];
            aux = str.getBytes();
            int i=0; while(i<aux.length) { buffer[i] = aux[i]; i++; }
            while(i<KEY_SIZE) { buffer[i] = 0x20; i++; }
            return buffer;
        }
        
        // Retorna o vetor de bytes que representa a página para armazenamento em file
        protected byte[] getBytes() throws IOException {
            
            // Um fluxo de bytes é usado para construção do vetor de bytes
            ByteArrayOutputStream ba = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(ba);
            
            // Quantidade de elementos presentes na página
            out.writeInt(n);
            
            // Escreve todos os elementos
            int i=0;
            while(i<n) {
                out.writeLong(sons[i]);
                out.write(fillBlanks(keys[i]));
                out.writeInt(datas[i]);
                i++;
            }
            out.writeLong(sons[i]);
            
            // Completa o restante da página com registros vazios
            byte[] emptyRegistry = new byte[REGISTRY_SIZE];
            while(i<maxElements){
                out.write(emptyRegistry);
                out.writeLong(sons[i+1]);
                i++;
            }

            // Escreve o ponteiro para a próxima página
            out.writeLong(next);
            
            // Retorna o vetor de bytes que representa a página
            return ba.toByteArray();
        }

        
        // Reconstrói uma página a partir de um vetor de bytes lido no file
        public void setBytes(byte[] buffer) throws IOException {
            
            // Usa um fluxo de bytes para leitura dos atributos
            ByteArrayInputStream ba = new ByteArrayInputStream(buffer);
            DataInputStream in = new DataInputStream(ba);
            byte[] bs = new byte[KEY_SIZE];
            
            // Lê a quantidade de elementos da página
            n = in.readInt();
            
            // Lê todos os elementos (reais ou vazios)
            int i=0;
            while(i<maxElements) {
                sons[i]  = in.readLong();
                in.read(bs);
                keys[i] = (new String(bs)).trim();
                datas[i]   = in.readInt(); 
                i++;
            }
            sons[i] = in.readLong();
            next = in.readLong();
        }
    }
    
    // ------------------------------------------------------------------------------
        
    
    public BPTree(int o, String na) throws IOException {
        
        // Inicializa os atributos da árvore
        order = o;
        maxElements = o-1;
        maxSons = o;
        fileName = na;
        
        // Abre (ou cria) o file, escrevendo uma root empty, se necessário.
        file = new RandomAccessFile(fileName,"rw");
        if(file.length()<8) 
            file.writeLong(-1);  // root empty
    }
    
    // Testa se a árvore está empty. Uma árvore empty é identificada pela root == -1
    public boolean empty() throws IOException {
        long root;
        file.seek(0);
        root = file.readLong();
        return root == -1;
    }
    
        
    // Busca recursiva por um elemento a partir da key. Este metodo invoca 
    // o método recursivo read1, passando a root como referência.
    public int read(String c) throws IOException {
        
        // Recupera a root da árvore
        long root;
        file.seek(0);
        root = file.readLong();
        
        // Executa a busca recursiva
        if(root!=-1)
            return read1(c,root);
        else
            return -1;
    }
    
    // Busca recursiva. Este método recebe a referência de uma página e busca
    // pela key na mesma. A busca continua pelos sons, se houverem.
    private int read1(String key, long pagina) throws IOException {
        
        // Como a busca é recursiva, a descida para um filho inexistente
        // (filho de uma página folha) retorna um valor negativo.
        if(pagina==-1)
            return -1;
        
        // Reconstrói a página passada como referência a partir 
        // do registro lido no file
        file.seek(pagina);
        Page pa = new Page(order);
        byte[] buffer = new byte[pa.PAGE_SIZE];
        file.read(buffer);
        pa.setBytes(buffer);
 
        // Encontra o ponto em que a key deve estar na página
        // Primeiro passo - todas as keys menores que a key buscada são ignoradas
        int i=0;
        while(i<pa.n && key.compareTo(pa.keys[i])>0) {
            i++;
        }
        
        // key encontrada (ou pelo menos o ponto onde ela deveria estar).
        // Segundo passo - testa se a key é a key buscada e se está em uma folha
        // Obs.: em uma árvore B+, todas as keys válidas estão nas folhas
        // Obs.: a comparação exata só será possível se considerarmos a menor string
        //       entre a key e a string na página
        if(i<pa.n && pa.sons[0]==-1 
                  && key.compareTo(pa.keys[i])==0) {
            return pa.datas[i];
        }
        
        // Terceiro passo - ainda não é uma folha, continua a busca recursiva pela árvore
        if(i==pa.n || key.compareTo(pa.keys[i])<0)
            return read1(key, pa.sons[i]);
        else
            return read1(key, pa.sons[i+1]);
    }
        
    // Atualiza recursivamente um valor a partir da sua key. Este metodo invoca 
    // o método recursivo update1, passando a root como referência.
    public boolean update(String c, int d) throws IOException {
        
        // Recupera a root da árvore
        long root;
        file.seek(0);
        root = file.readLong();
        
        // Executa a busca recursiva
        if(root!=-1)
            return update1(c,d,root);
        else
            return false;
    }
    
    // Atualização recursiva. Este método recebe a referência de uma página, uma
    // key de busca e o dado correspondente a ela. 
    private boolean update1(String key, int dado, long pagina) throws IOException {
        
        // Como a busca é recursiva, a descida para um filho inexistente
        // (filho de uma página folha) retorna um valor negativo.
        if(pagina==-1)
            return false;
        
        // Reconstrói a página passada como referência a partir 
        // do registro lido no file
        file.seek(pagina);
        Page pa = new Page(order);
        byte[] buffer = new byte[pa.PAGE_SIZE];
        file.read(buffer);
        pa.setBytes(buffer);
 
        // Encontra o ponto em que a key deve estar na página
        // Primeiro passo - todas as keys menores que a key buscada são ignoradas
        int i=0;
        while(i<pa.n && key.compareTo(pa.keys[i])>0) {
            i++;
        }
        
        // key encontrada (ou pelo menos o ponto onde ela deveria estar).
        // Segundo passo - testa se a key é a key buscada e se está em uma folha
        // Obs.: em uma árvore B+, todas as keys válidas estão nas folhas
        if(i<pa.n && pa.sons[0]==-1 
                  && key.compareTo(pa.keys[i].substring(0,Math.min(key.length(),pa.keys[i].length())))==0) {
            pa.datas[i] = dado;
            file.seek(pagina);
            file.write(pa.getBytes());
            return true;
        }
        
        // Terceiro passo - ainda não é uma folha, continua a busca recursiva pela árvore
        if(i==pa.n || key.compareTo(pa.keys[i])<0)
            return update1(key, dado, pa.sons[i]);
        else
            return update1(key, dado, pa.sons[i+1]);
    }
        
    
    // Inclusão de novos elementos na árvore. A inclusão é recursiva. A primeira
    // função chama a segunda recursivamente, passando a root como referência.
    // Eventualmente, a árvore pode crescer para cima.
    public boolean create(String c, int d) throws IOException {

        // key não pode ser empty
        if(c.compareTo("")==0) {
            System.out.println( "key não pode ser vazia" );
            return false;
        }
            
        // Carrega a root
        file.seek(0);       
        long pagina;
        pagina = file.readLong();

        // O processo de inclusão permite que os valores passados como referência
        // sejam substituídos por outros valores, para permitir a divisão de páginas
        // e crescimento da árvore. Assim, são usados os valores globais keyAux 
        // e dataAux. Quando há uma divisão, a key e o valor promovidos são armazenados
        // nessas variáveis.
        keyAux = c;
        dataAux = d;
        
        // Se houver crescimento, então será criada uma página extra e será mantido um
        // ponteiro para essa página. Os valores também são globais.
        pageAux = -1;
        increase = false;
                
        // Chamada recursiva para a inserção da key e do valor
        // A key e o valor não são passados como parâmetros, porque são globais
        boolean inserido = create1(pagina);
        
        // Testa a necessidade de criação de uma nova root.
        if(increase) {
            
            // Cria a nova página que será a root. O ponteiro esquerdo da root
            // será a root antiga e o seu ponteiro direito será para a nova página.
            Page novaPage = new Page(order);
            novaPage.n = 1;
            novaPage.keys[0] = keyAux;
            novaPage.datas[0]  = dataAux;
            novaPage.sons[0] = pagina;
            novaPage.sons[1] = pageAux;
            
            // Acha o espaço em disco. Nesta versão, todas as novas páginas
            // são escrita no fim do file.
            file.seek(file.length());
            long root = file.getFilePointer();
            file.write(novaPage.getBytes());
            file.seek(0);
            file.writeLong(root);
        }
        
        return inserido;
    }
    
    
    // Função recursiva de inclusão. A função passa uma página de referência.
    // As inclusões são sempre feitas em uma folha.
    private boolean create1(long pagina) throws IOException {
        
        // Testa se passou para o filho de uma página folha. Nesse caso, 
        // inicializa as variáveis globais de controle.
        if(pagina==-1) {
            increase = true;
            pageAux = -1;
            return false;
        }
        
        // Lê a página passada como referência
        file.seek(pagina);
        Page pa = new Page(order);
        byte[] buffer = new byte[pa.PAGE_SIZE];
        file.read(buffer);
        pa.setBytes(buffer);
        
        // Busca o próximo ponteiro de descida. Como pode haver repetição
        // da primeira key, a segunda também é usada como referência.
        // Nesse primeiro passo, todos os pares menores são ultrapassados.
        int i=0;
        while(i<pa.n && keyAux.compareTo(pa.keys[i])>0) {
            i++;
        }
        
        // Testa se a key já existe em uma folha. Se isso acontecer, então 
        // a inclusão é cancelada.
        if(i<pa.n && pa.sons[0]==-1 && keyAux.compareTo(pa.keys[i])==0) {
            increase = false;
            return false;
        }
        
        // Continua a busca recursiva por uma nova página. A busca continuará até o
        // filho inexistente de uma página folha ser alcançado.
        boolean inserido;
        if(i==pa.n || keyAux.compareTo(pa.keys[i])<0)
            inserido = create1(pa.sons[i]);
        else
            inserido = create1(pa.sons[i+1]);
        
        // A partir deste ponto, as chamadas recursivas já foram encerradas. 
        // Assim, o próximo código só é executado ao retornar das chamadas recursivas.

        // A inclusão já foi resolvida por meio de uma das chamadas recursivas. Nesse
        // caso, apenas retorna para encerrar a recursão.
        // A inclusão pode ter sido resolvida porque a key já existia (inclusão inválida)
        // ou porque o novo elemento coube em uma página existente.
        if(!increase)
            return inserido;
        
        // Se tiver espaço na página, faz a inclusão nela mesmo
        if(pa.n<maxElements) {

            // Puxa todos elementos para a direita, começando do último
            // para gerar o espaço para o novo elemento
            for(int j=pa.n; j>i; j--) {
                pa.keys[j] = pa.keys[j-1];
                pa.datas[j] = pa.datas[j-1];
                pa.sons[j+1] = pa.sons[j];
            }
            
            // Insere o novo elemento
            pa.keys[i] = keyAux;
            pa.datas[i] = dataAux;
            pa.sons[i+1] = pageAux;
            pa.n++;
            
            // Escreve a página atualizada no file
            file.seek(pagina);
            file.write(pa.getBytes());
            
            // Encerra o processo de crescimento e retorna
            increase=false;
            return true;
        }
        
        // O elemento não cabe na página. A página deve ser dividida e o elemento
        // do meio deve ser promovido (sem retirar a referência da folha).
        
        // Cria uma nova página
        Page np = new Page(order);
        
        // Copia a metade superior dos elementos para a nova página,
        // considerando que maxElements pode ser ímpar
        int meio = maxElements/2;
        for(int j=0; j<(maxElements-meio); j++) {    
            
            // copia o elemento
            np.keys[j] = pa.keys[j+meio];
            np.datas[j] = pa.datas[j+meio];   
            np.sons[j+1] = pa.sons[j+meio+1];  
            
            // limpa o espaço liberado
            pa.keys[j+meio] = "";
            pa.datas[j+meio] = 0;
            pa.sons[j+meio+1] = -1;
        }
        np.sons[0] = pa.sons[meio];
        np.n = maxElements-meio;
        pa.n = meio;
        
        // Testa o lado de inserção
        // Caso 1 - Novo registro deve ficar na página da esquerda
        if(i<=meio) {   
            
            // Puxa todos os elementos para a direita
            for(int j=meio; j>0 && j>i; j--) {
                pa.keys[j] = pa.keys[j-1];
                pa.datas[j] = pa.datas[j-1];
                pa.sons[j+1] = pa.sons[j];
            }
            
            // Insere o novo elemento
            pa.keys[i] = keyAux;
            pa.datas[i] = dataAux;
            pa.sons[i+1] = pageAux;
            pa.n++;
            
            // Se a página for folha, seleciona o primeiro elemento da página 
            // da direita para ser promovido, mantendo-o na folha
            if(pa.sons[0]==-1) {
                keyAux = np.keys[0];
                dataAux = np.datas[0];
            }
            
            // caso contrário, promove o maior elemento da página esquerda
            // removendo-o da página
            else {
                keyAux = pa.keys[pa.n-1];
                dataAux = pa.datas[pa.n-1];
                pa.keys[pa.n-1] = "";
                pa.datas[pa.n-1] = 0;
                pa.sons[pa.n] = -1;
                pa.n--;
            }
        } 
        
        // Caso 2 - Novo registro deve ficar na página da direita
        else {
            int j;
            for(j=maxElements-meio; j>0 && keyAux.compareTo(np.keys[j-1])<0; j--) {
                np.keys[j] = np.keys[j-1];
                np.datas[j] = np.datas[j-1];
                np.sons[j+1] = np.sons[j];
            }
            np.keys[j] = keyAux;
            np.datas[j] = dataAux;
            np.sons[j+1] = pageAux;
            np.n++;

            // Seleciona o primeiro elemento da página da direita para ser promovido
            keyAux = np.keys[0];
            dataAux = np.datas[0];
            
            // Se não for folha, remove o elemento promovido da página
            if(pa.sons[0]!=-1) {
                for(j=0; j<np.n-1; j++) {
                    np.keys[j] = np.keys[j+1];
                    np.datas[j] = np.datas[j+1];
                    np.sons[j] = np.sons[j+1];
                }
                np.sons[j] = np.sons[j+1];
                
                // apaga o último elemento
                np.keys[j] = "";
                np.datas[j] = 0;
                np.sons[j+1] = -1;
                np.n--;
            }

        }
        
        // Se a página era uma folha e apontava para outra folha, 
        // então atualiza os ponteiros dessa página e da página nova
        if(pa.sons[0]==-1) {
            np.next=pa.next;
            pa.next = file.length();
        }

        // Grava as páginas no files file
        pageAux = file.length();
        file.seek(pageAux);
        file.write(np.getBytes());

        file.seek(pagina);
        file.write(pa.getBytes());
        
        return true;
    }

    
    // Remoção elementos na árvore. A remoção é recursiva. A primeira
    // função chama a segunda recursivamente, passando a root como referência.
    // Eventualmente, a árvore pode reduzir seu tamanho, por meio da exclusão da root.
    public boolean delete(String key) throws IOException {
                
        // Encontra a root da árvore
        file.seek(0);       
        long pagina;                
        pagina = file.readLong();

        // variável global de controle da redução do tamanho da árvore
        decrease = false;  
                
        // Chama recursivamente a exclusão de registro (na key1Aux e no 
        // key2Aux) passando uma página como referência
        boolean excluido = delete1(key, pagina);
        
        // Se a exclusão tiver sido possível e a página tiver reduzido seu tamanho,
        // por meio da fusão das duas páginas filhas da root, elimina essa root
        if(excluido && decrease) {
            
            // Lê a root
            file.seek(pagina);
            Page pa = new Page(order);
            byte[] buffer = new byte[pa.PAGE_SIZE];
            file.read(buffer);
            pa.setBytes(buffer);
            
            // Se a página tiver 0 elementos, apenas atualiza o ponteiro para a root,
            // no cabeçalho do file, para o seu primeiro filho.
            if(pa.n == 0) {
                file.seek(0);
                file.writeLong(pa.sons[0]);  
            }
        }
         
        return excluido;
    }
    

    // Função recursiva de exclusão. A função passa uma página de referência.
    // As exclusões são sempre feitas em folhas e a fusão é propagada para cima.
    private boolean delete1(String key, long pagina) throws IOException {
        
        // Declaração de variáveis
        boolean excluido=false;
        int diminuido;
        
        // Testa se o registro não foi encontrado na árvore, ao alcançar uma folha
        // inexistente (filho de uma folha real)
        if(pagina==-1) {
            decrease=false;
            return false;
        }
        
        // Lê o registro da página no file
        file.seek(pagina);
        Page pa = new Page(order);
        byte[] buffer = new byte[pa.PAGE_SIZE];
        file.read(buffer);
        pa.setBytes(buffer);

        // Encontra a página em que a key está presente
        // Nesse primeiro passo, salta todas as keys menores
        int i=0;
        while(i<pa.n && key.compareTo(pa.keys[i])>0) {
            i++;
        }

        // keys encontradas em uma folha
        if(i<pa.n && pa.sons[0]==-1 && key.compareTo(pa.keys[i])==0) {

            // Puxa todas os elementos seguintes para uma posição anterior, sobrescrevendo
            // o elemento a ser excluído
            int j;
            for(j=i; j<pa.n-1; j++) {
                pa.keys[j] = pa.keys[j+1];
                pa.datas[j] = pa.datas[j+1];
            }
            pa.n--;
            
            // limpa o último elemento
            pa.keys[pa.n] = "";
            pa.datas[pa.n] = 0;
            
            // Atualiza o registro da página no file
            file.seek(pagina);
            file.write(pa.getBytes());
            
            // Se a página contiver menos elementos do que o mínimo necessário,
            // indica a necessidade de fusão de páginas
            decrease = pa.n<maxElements/2;
            return true;
        }

        // Se a key não tiver sido encontrada (observar o return true logo acima),
        // continua a busca recursiva por uma nova página. A busca continuará até o
        // filho inexistente de uma página folha ser alcançado.
        // A variável diminuído mantem um registro de qual página eventualmente 
        // pode ter ficado com menos elementos do que o mínimo necessário.
        // Essa página será filha da página atual
        if(i==pa.n || key.compareTo(pa.keys[i])<0) {
            excluido = delete1(key, pa.sons[i]);
            diminuido = i;
        } else {
            excluido = delete1(key, pa.sons[i+1]);
            diminuido = i+1;
        }
        
        
        // A partir deste ponto, o código é executado após o retorno das chamadas
        // recursivas do método
        
        // Testa se há necessidade de fusão de páginas
        if(decrease) {

            // Carrega a página filho que ficou com menos elementos do 
            // do que o mínimo necessário
            long paginaFilho = pa.sons[diminuido];
            Page pFilho = new Page(order);
            file.seek(paginaFilho);
            file.read(buffer);
            pFilho.setBytes(buffer);
            
            // Cria uma página para o irmão (da direita ou esquerda)
            long paginaIrmao;
            Page pIrmao;
            
            // Tenta a fusão com irmão esquerdo
            if(diminuido>0) {
                
                // Carrega o irmão esquerdo
                paginaIrmao = pa.sons[diminuido-1];
                pIrmao = new Page(order);
                file.seek(paginaIrmao);
                file.read(buffer);
                pIrmao.setBytes(buffer);
                
                // Testa se o irmão pode ceder algum registro
                if(pIrmao.n>maxElements/2) {
                    
                    // Move todos os elementos do filho aumentando uma posição
                    // à esquerda, gerando espaço para o elemento cedido
                    for(int j=pFilho.n; j>0; j--) {
                        pFilho.keys[j] = pFilho.keys[j-1];
                        pFilho.datas[j] = pFilho.datas[j-1];
                        pFilho.sons[j+1] = pFilho.sons[j];
                    }
                    pFilho.sons[1] = pFilho.sons[0];
                    pFilho.n++;
                    
                    // Se for folha, copia o elemento do irmão, já que o do pai
                    // será extinto ou repetido
                    if(pFilho.sons[0]==-1) {
                        pFilho.keys[0] = pIrmao.keys[pIrmao.n-1];
                        pFilho.datas[0] = pIrmao.datas[pIrmao.n-1];
                    }
                    
                    // Se não for folha, rotaciona os elementos, descendo o elemento do pai
                    else {
                        pFilho.keys[0] = pa.keys[diminuido-1];
                        pFilho.datas[0] = pa.datas[diminuido-1];
                    }

                    // Copia o elemento do irmão para o pai (página atual)
                    pa.keys[diminuido-1] = pIrmao.keys[pIrmao.n-1];
                    pa.datas[diminuido-1] = pIrmao.datas[pIrmao.n-1];
                        
                    
                    // Reduz o elemento no irmão
                    pFilho.sons[0] = pIrmao.sons[pIrmao.n];
                    pIrmao.n--;
                    decrease = false;
                }
                
                // Se não puder ceder, faz a fusão dos dois irmãos
                else {

                    // Se a página reduzida não for folha, então o elemento 
                    // do pai deve ser copiado para o irmão
                    if(pFilho.sons[0] != -1) {
                        pIrmao.keys[pIrmao.n] = pa.keys[diminuido-1];
                        pIrmao.datas[pIrmao.n] = pa.datas[diminuido-1];
                        pIrmao.sons[pIrmao.n+1] = pFilho.sons[0];
                        pIrmao.n++;
                    }
                    
                    
                    // Copia todos os registros para o irmão da esquerda
                    for(int j=0; j<pFilho.n; j++) {
                        pIrmao.keys[pIrmao.n] = pFilho.keys[j];
                        pIrmao.datas[pIrmao.n] = pFilho.datas[j];
                        pIrmao.sons[pIrmao.n+1] = pFilho.sons[j+1];
                        pIrmao.n++;
                    }
                    pFilho.n = 0;   // aqui o endereço do filho poderia ser incluido em uma lista encadeada no cabeçalho, indicando os espaços reaproveitáveis
                    
                    // Se as páginas forem folhas, copia o ponteiro para a folha seguinte
                    if(pIrmao.sons[0]==-1)
                        pIrmao.next = pFilho.next;
                    
                    // puxa os registros no pai
                    int j;
                    for(j=diminuido-1; j<pa.n-1; j++) {
                        pa.keys[j] = pa.keys[j+1];
                        pa.datas[j] = pa.datas[j+1];
                        pa.sons[j+1] = pa.sons[j+2];
                    }
                    pa.keys[j] = "";
                    pa.datas[j] = -1;
                    pa.sons[j+1] = -1;
                    pa.n--;
                    decrease = pa.n<maxElements/2;  // testa se o pai também ficou sem o número mínimo de elementos
                }
            }
            
            // Faz a fusão com o irmão direito
            else {
                
                // Carrega o irmão
                paginaIrmao = pa.sons[diminuido+1];
                pIrmao = new Page(order);
                file.seek(paginaIrmao);
                file.read(buffer);
                pIrmao.setBytes(buffer);
                
                // Testa se o irmão pode ceder algum elemento
                if(pIrmao.n>maxElements/2) {
                    
                    // Se for folha
                    if( pFilho.sons[0]==-1 ) {
                    
                        //copia o elemento do irmão
                        pFilho.keys[pFilho.n] = pIrmao.keys[0];
                        pFilho.datas[pFilho.n] = pIrmao.datas[0];
                        pFilho.sons[pFilho.n+1] = pIrmao.sons[0];
                        pFilho.n++;

                        // sobe o próximo elemento do irmão
                        pa.keys[diminuido] = pIrmao.keys[1];
                        pa.datas[diminuido] = pIrmao.datas[1];
                        
                    } 
                    
                    // Se não for folha, rotaciona os elementos
                    else {
                        
                        // Copia o elemento do pai, com o ponteiro esquerdo do irmão
                        pFilho.keys[pFilho.n] = pa.keys[diminuido];
                        pFilho.datas[pFilho.n] = pa.datas[diminuido];
                        pFilho.sons[pFilho.n+1] = pIrmao.sons[0];
                        pFilho.n++;
                        
                        // Sobe o elemento esquerdo do irmão para o pai
                        pa.keys[diminuido] = pIrmao.keys[0];
                        pa.datas[diminuido] = pIrmao.datas[0];
                    }
                    
                    // move todos os registros no irmão para a esquerda
                    int j;
                    for(j=0; j<pIrmao.n-1; j++) {
                        pIrmao.keys[j] = pIrmao.keys[j+1];
                        pIrmao.datas[j] = pIrmao.datas[j+1];
                        pIrmao.sons[j] = pIrmao.sons[j+1];
                    }
                    pIrmao.sons[j] = pIrmao.sons[j+1];
                    pIrmao.n--;
                    decrease = false;
                }
                
                // Se não puder ceder, faz a fusão dos dois irmãos
                else {

                    // Se a página reduzida não for folha, então o elemento 
                    // do pai deve ser copiado para o irmão
                    if(pFilho.sons[0] != -1) {
                        pFilho.keys[pFilho.n] = pa.keys[diminuido];
                        pFilho.datas[pFilho.n] = pa.datas[diminuido];
                        pFilho.sons[pFilho.n+1] = pIrmao.sons[0];
                        pFilho.n++;
                    }
                    
                    // Copia todos os registros do irmão da direita
                    for(int j=0; j<pIrmao.n; j++) {
                        pFilho.keys[pFilho.n] = pIrmao.keys[j];
                        pFilho.datas[pFilho.n] = pIrmao.datas[j];
                        pFilho.sons[pFilho.n+1] = pIrmao.sons[j+1];
                        pFilho.n++;
                    }
                    pIrmao.n = 0;   // aqui o endereço do irmão poderia ser incluido em uma lista encadeada no cabeçalho, indicando os espaços reaproveitáveis
                    
                    // Se a página for folha, copia o ponteiro para a próxima página
                    pFilho.next = pIrmao.next;
                    
                    // puxa os registros no pai
                    for(int j=diminuido; j<pa.n-1; j++) {
                        pa.keys[j] = pa.keys[j+1];
                        pa.datas[j] = pa.datas[j+1];
                        pa.sons[j+1] = pa.sons[j+2];
                    }
                    pa.n--;
                    decrease = pa.n<maxElements/2;  // testa se o pai também ficou sem o número mínimo de elementos
                }
            }
            
            // Atualiza todos os registros
            file.seek(pagina);
            file.write(pa.getBytes());
            file.seek(paginaFilho);
            file.write(pFilho.getBytes());
            file.seek(paginaIrmao);
            file.write(pIrmao.getBytes());
        }
        return excluido;
    }
    
    
    // Imprime a árvore, usando uma chamada recursiva.
    // A função recursiva é chamada com uma página de referência (root)
    public void print() throws IOException {
        long root;
        file.seek(0);
        root = file.readLong();
        if(root!=-1)
            print1(root);
        System.out.println();
    }
    
    // Impressão recursiva
    private void print1(long pagina) throws IOException {
        
        // Retorna das chamadas recursivas
        if(pagina==-1)
            return;
        int i;

        // Lê o registro da página passada como referência no file
        file.seek(pagina);
        Page pa = new Page(order);
        byte[] buffer = new byte[pa.PAGE_SIZE];
        file.read(buffer);
        pa.setBytes(buffer);
        
        // Imprime a página
        String endereco = String.format("%04d", pagina);
        System.out.print(endereco+"  " + pa.n +":"); // endereço e número de elementos
        for(i=0; i<maxElements; i++) {
            System.out.print("("+String.format("%04d",pa.sons[i])+") "+pa.keys[i]+","+String.format("%2d",pa.datas[i])+" ");
        }
        System.out.print("("+String.format("%04d",pa.sons[i])+")");
        if(pa.next==-1)
            System.out.println();
        else
            System.out.println(" --> ("+String.format("%04d", pa.next)+")");
        
        // Chama recursivamente cada filho, se a página não for folha
        if(pa.sons[0] != -1) {
            for(i=0; i<pa.n; i++)
                print1(pa.sons[i]);
            print1(pa.sons[i]);
        }
    }
       
    
    // Apaga o file do índice, para que possa ser reconstruído
    public void apagar() throws IOException {

        File f = new File(fileName);
        f.delete();

        file = new RandomAccessFile(fileName,"rw");
        file.writeLong(-1);  // root empty
    }
    
}
