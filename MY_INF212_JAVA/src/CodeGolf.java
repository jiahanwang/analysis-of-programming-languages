import java.util.*;
import java.util.Map.Entry;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.apache.commons.collections4.CollectionUtils;


public class CodeGolf {
	public static void main(String[] args){
		try{ 
			ArrayList<String> words = new ArrayList<String>(Arrays.asList((new String(Files.readAllBytes(Paths.get(args[0], new String[]{})))).replaceAll("[\\W_]+", " ").toLowerCase().split("[\\s+]")));
			ArrayList<String> stop_words = new ArrayList<String>(Arrays.asList((new String(Files.readAllBytes(Paths.get(args[1], new String[]{})))).split("[,]")));
			for(char i = 'a'; i <= 'z'; i++)
				stop_words.add(Character.toString(i));
			ArrayList<Map.Entry<String, Integer>> word_freqs = new ArrayList<Map.Entry<String, Integer>>((CollectionUtils.getCardinalityMap(CollectionUtils.removeAll(words, stop_words))).entrySet());
			Collections.sort(word_freqs, new Comparator<Entry<String, Integer>>() {
				public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2){
					return -(o1.getValue()).compareTo(o2.getValue());
			}});
			for(Map.Entry<String, Integer> freq_pair : word_freqs.subList(0, 25)) 
				System.out.print(freq_pair.getKey() + " - " + freq_pair.getValue() + "\n");
		}catch(Exception e){
			System.out.print(e.getMessage());
		}
	}
}
