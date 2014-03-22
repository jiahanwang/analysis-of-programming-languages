import java.util.*;
import java.util.Map.Entry;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class KickYourTeammateForward {
	
	public static String read_file(String path_to_file) throws IOException{
		return new String(Files.readAllBytes(Paths.get(path_to_file, new String[]{})));
	}
	
	public static String filter_chars_and_normalize(String str_data){
		return str_data.replaceAll("[\\W_]+", " ");
	}
	
	public static String normalize(String str_data){
		return str_data.toLowerCase();
	}
	
	public static String[] scan(String str_data){
		return str_data.split("[\\s+]");
	}
	
	public static ArrayList<String> remove_stop_words(String[] word_array)throws IOException{
		ArrayList<String> word_list = new ArrayList<String>(Arrays.asList(word_array));
		ArrayList<String> stop_words = new ArrayList<String>(Arrays.asList((new String(Files.readAllBytes(Paths.get("stop_words.txt", new String[]{})))).split("[,]")));
		for(char i = 'a'; i <= 'z'; i++)
			stop_words.add(Character.toString(i));
		for(Iterator<String> iterator = word_list.iterator(); iterator.hasNext();)
		    if (stop_words.contains(iterator.next()))
		        iterator.remove();
		return word_list;
	}
	
	public static Map<String, Integer> frequencies(ArrayList<String> word_list){
		Map<String, Integer> word_freqs = new HashMap<String, Integer>();
		for(Iterator<String> iterator = word_list.iterator(); iterator.hasNext();){
			String word = iterator.next();
		    if (word_freqs.containsKey(word))
		    	word_freqs.put(word, word_freqs.get(word) + 1);
		    else
		    	word_freqs.put(word, 1);
		}
		return word_freqs;	
	}
	
	public static List<Map.Entry<String, Integer>> sort(Map<String, Integer> word_freqs){
		List<Map.Entry<String, Integer>> words_freds_list = new LinkedList<Map.Entry<String, Integer>>(word_freqs.entrySet());
		Collections.sort(words_freds_list, new Comparator<Entry<String, Integer>>() {
			public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
				return -(o1.getValue()).compareTo(o2.getValue());
			}
		});
		return words_freds_list;
	}

	public static void main(String[] args) {
		try{
			List<Map.Entry<String, Integer>> top_words = sort(frequencies(remove_stop_words(scan(filter_chars_and_normalize(read_file(args[0]))))));	
			Iterator<Map.Entry<String, Integer>> iterator = top_words.iterator();
			for(int i = 0; i < 25; i++){
				Map.Entry<String, Integer> entry = iterator.next();
				System.out.print(entry.getKey() + " - " + entry.getValue() + "\n");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	

}
