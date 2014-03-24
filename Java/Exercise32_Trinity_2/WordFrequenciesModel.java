package Exercise32_Trinity_2;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class WordFrequenciesModel {
	
	public List<Map.Entry<String, Integer>> freqs;
	private List<WordFrequenciesView> observers = new ArrayList<WordFrequenciesView>();
	
	public void register(WordFrequenciesView observer){
		this.observers.add(observer);
	}
	
	public void update(String path_to_file){
		try {
			ArrayList<String> words = new ArrayList<String>(Arrays.asList((new String(Files.readAllBytes(Paths.get(path_to_file, new String[]{})))).replaceAll("[\\W_]+", " ").toLowerCase().split("[\\s+]")));
			ArrayList<String> stop_words = new ArrayList<String>(Arrays.asList((new String(Files.readAllBytes(Paths.get("stop_words.txt", new String[]{})))).split("[,]")));
			for(char i = 'a'; i <= 'z'; i++)
				stop_words.add(Character.toString(i));
			Map<String, Integer> word_freqs = new HashMap<String, Integer>();
			for(Iterator<String> iterator = words.iterator(); iterator.hasNext();){
				String word = iterator.next();
			    if (word_freqs.containsKey(word) && !stop_words.contains(word))
			    	word_freqs.put(word, word_freqs.get(word) + 1);
			    else
			    	word_freqs.put(word, 1);
			}
			this.freqs = new ArrayList<Map.Entry<String, Integer>>(word_freqs.entrySet());
			// reflesh all the views
			for(Iterator<WordFrequenciesView> it = this.observers.iterator(); it.hasNext();){
				it.next().render();
			}
		} catch (Exception e) {
			this.freqs = new ArrayList<Map.Entry<String, Integer>>();
			System.out.println("File cannot be found");
		}
	}
	
}
