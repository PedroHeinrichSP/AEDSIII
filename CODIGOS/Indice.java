import java.util.*;

public class Indice {
    Map<Integer, String> sources;
    HashMap<String, HashSet<Integer>> index;
    THScreen screen;

    Indice(THScreen screen) {
        sources = new HashMap<Integer, String>();
        index = new HashMap<String, HashSet<Integer>>();
        this.screen = screen;
    }

    public void buildIndex(List<Pokedex> pokeList) {
        int i = 0;
        for (Pokedex pokemon : pokeList) {
            try {
                String poke = pokemon.toIndexString();
                sources.put(i, poke);
                String[] words = poke.split("\\W+");
                for (String word : words) {
                    word = word.toLowerCase();
                    if (!index.containsKey(word))
                        index.put(word, new HashSet<Integer>());
                    index.get(word).add(i);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            i++;
        }
    }

    public String find(String phrase) {
        String[] words = phrase.split("\\W+");
        HashSet<Integer> res = new HashSet<Integer>(index.get(words[0].toLowerCase()));
        try {
            for (String word : words) {
            res.retainAll(index.get(word));
        }
        } catch (Exception e) {
            e.printStackTrace();
        }
        

        if (res.size() == 0) {
            return "Não encontrado";
        }
        
        StringBuffer result = new StringBuffer("Encontrado em\n");
        int i=0;
        result.append(res.size() + " resultados\n");
        result.append("---------------\n");
        for (int num : res) {
            result.append("Número " + i + "\n" + sources.get(num) + "\n");
            result.append("---------------\n");
            i++;
        }

        return result.toString();
    }
}
