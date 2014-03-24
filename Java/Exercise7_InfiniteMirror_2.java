import java.util.*;
import java.util.Map.Entry;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Exercise7_InfiniteMirror_2 {
	
	public static ArrayList<String> stop_words = new ArrayList<String>();
	public static int RECURSION_LIMIT = 5000;
	
	public static void count(List<String> word_list, Map<String, Integer> word_freqs){
		if (word_list.size() == 0)
			return;
		if (word_list.size() == 1){
			String word = word_list.get(0); 
			if (!stop_words.contains(word))
			    if (word_freqs.containsKey(word))
			    	word_freqs.put(word, word_freqs.get(word) + 1);
			    else
			    	word_freqs.put(word, 1);
		}else{
			count(word_list.subList(0, 1), word_freqs);
			count(word_list.subList(1, word_list.size()), word_freqs);
		}
	}
	
	public static void wf_print(List<Map.Entry<String, Integer>> word_freq){
		if (word_freq.size() == 0)
			return;
		if (word_freq.size() == 1)
			System.out.print(word_freq.get(0).getKey() + " - " + word_freq.get(0).getValue() + "\n");
		else{
			wf_print(word_freq.subList(0, 1));
			wf_print(word_freq.subList(1, word_freq.size()));
		}
	}
	/*
	 * The file reading part is hard to be implemented in recursion because there is only one line in the stop_words.txt  
	 * The string splitting part can be done in recursion: we take the first word of the input string and pass the rest to next function
	 */
	public static void load_stop_words(String stop_string){
		if (stop_string.length() == 0) return;
		int i = 0;
		String word = "";
		while(true){
			if(stop_string.charAt(i) == ',') break;
			word = word.concat(Character.toString(stop_string.charAt(i ++ )));
		}
		stop_words.add(word);
		load_stop_words(stop_string.substring(word.length() + 1));
	}

	public static void main(String[] args) {
		try{
			ArrayList<String> words = new ArrayList<String>(Arrays.asList((new String(Files.readAllBytes(Paths.get(args[0], new String[]{})))).replaceAll("[\\W_]+", " ").toLowerCase().split("[\\s+]")));
			// use recursion to load the stop words
			load_stop_words(new String(Files.readAllBytes(Paths.get(args[1], new String[]{}))).trim());
			for(char i = 'a'; i <= 'z'; i++)
				stop_words.add(Character.toString(i));
			Map<String, Integer> word_freqs = new HashMap<String, Integer>();
			// divide the recursion to avoid stackoverflow error
			for (int i = 0; i < words.size(); i += RECURSION_LIMIT){
				int j = i + RECURSION_LIMIT;
				if (j >= words.size())
					j = words.size();
				count(words.subList(i, j), word_freqs);
			}
			List<Map.Entry<String, Integer>> word_freqs_list = new LinkedList<Map.Entry<String, Integer>>(word_freqs.entrySet());
			Collections.sort(word_freqs_list, new Comparator<Entry<String, Integer>>() {
				public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
					return -(o1.getValue()).compareTo(o2.getValue());
				}
			});
			wf_print(word_freqs_list.subList(0, 25));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
