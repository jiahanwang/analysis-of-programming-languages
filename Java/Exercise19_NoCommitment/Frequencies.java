package Exercises19_NoCommitment;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Frequencies {

	public static List<Map.Entry<String, Integer>> top_25(ArrayList<String> word_list){
		Map<String, Integer> word_freqs = new HashMap<String, Integer>();
		for(Iterator<String> iterator = word_list.iterator(); iterator.hasNext();){
			String word = iterator.next();
		    if (word_freqs.containsKey(word))
		    	word_freqs.put(word, word_freqs.get(word) + 1);
		    else
		    	word_freqs.put(word, 1);
		}
		List<Map.Entry<String, Integer>> words_freqs_list = new LinkedList<Map.Entry<String, Integer>>(word_freqs.entrySet());
		Collections.sort(words_freqs_list, new Comparator<Entry<String, Integer>>() {
			public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
				int differ = o1.getValue() - o2.getValue();
				if (differ != 0)
					return - differ;
				else 
					return o1.getKey().compareTo(o2.getKey());
			}
		});
		return words_freqs_list.subList(0, 25);
	}	
}
