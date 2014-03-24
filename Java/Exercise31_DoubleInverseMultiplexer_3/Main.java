package Exercise31_DoubleInverseMultiplexer_3;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Main {
	
	/*
	 *  function: splitWords 
	 *  Input: String
	 *  Output: frequency pairs [(w1, 1), (w2, 1), ..., (wn, 1)]
	 */
	public static List<Frequency> splitWords (String data_str) throws IOException {
		ArrayList<String> words = new ArrayList<String>(Arrays.asList(data_str.replaceAll("[\\W_]+", " ").toLowerCase().split("[\\s+]")));
		ArrayList<String> stop_words = new ArrayList<String>(new ArrayList<String>(Arrays.asList((new String(Files.readAllBytes(Paths.get("stop_words.txt", new String[]{})))).replaceAll("[\\W_]+", " ").toLowerCase().split("[\\s+]"))));
		for(char i = 'a'; i <= 'z'; i++)
			stop_words.add(Character.toString(i));
		Iterator<String> iterator = words.iterator();
		while(iterator.hasNext()){
			String word = iterator.next();
			if(stop_words.contains(word) || word.length() == 0)
				iterator.remove();
		}
		List<Frequency> result = new ArrayList<Frequency>();
		iterator = words.iterator();
		while(iterator.hasNext()){
			Frequency new_frequency = new Frequency();
			new_frequency.setTerm(iterator.next());
			new_frequency.setFrequency(1);
			result.add(new_frequency);
		}
		return result;
	}
	
	/*
	 *  function: regroup
	 *  Input: List of lists of frequency pairs [[(w1, 1), (w2, 1), ..., (wn, 1)],[(w1, 1), (w2, 1), ..., (wn, 1)], ...]
	 *  Output: List of maps of list of frequency pairs Map(A-E):{ w1 : [(w1, 1), (w1, 1)...], w2 : [(w2, 1), (w2, 1)...], ...}
	 */
	public static List<Map<String, List<Frequency>>> regroup (List<List<Frequency>> pairs_list){
		List<Map<String, List<Frequency>>> mapping_list  = new ArrayList<Map<String, List<Frequency>>>();
		for(int i = 0; i < 5; i++)
			mapping_list.add(new HashMap<String, List<Frequency>>());
		Iterator<List<Frequency>> iterator = pairs_list.iterator();
		while(iterator.hasNext()){
			Iterator<Frequency> freq_iterator = iterator.next().iterator();
			while(freq_iterator.hasNext()){
				Frequency freq = freq_iterator.next();
				int which_map = decideMap(freq.getTerm());
				Map<String, List<Frequency>> mapping = mapping_list.get(which_map);
				if(mapping.containsKey(freq.getTerm())){
					List<Frequency> new_frequency = mapping.get(freq.getTerm());
					new_frequency.add(freq);
					mapping.put(freq.getTerm(), new_frequency);
				}else{
					List<Frequency> new_frequency = new ArrayList<Frequency>();
					new_frequency.add(freq);
					mapping.put(freq.getTerm(), new_frequency);
				}
			}
		}
		return mapping_list;
	}
	
	private static int decideMap(String term){
		char first = term.charAt(0);
		if( first >= 'a' && first <= 'e' )
			return 0;
		else 
			if(first >= 'f' && first <= 'j')
				return 1;
			else
				if(first >= 'k' && first <= 'o')
					return 2;
				else
					if(first >= 'p' && first <= 't')
						return 3;
					else
						return 4;
	}
	
	public static Frequency count(Entry<String, List<Frequency>> entry){
		Frequency new_frequency = new Frequency();
		new_frequency.setTerm(entry.getKey());
		new_frequency.setFrequency(entry.getValue().size());
		return new_frequency;
	}
	
	public static String readFile(String path) throws IOException{
		return new String(Files.readAllBytes(Paths.get(path, new String[]{})));
	}
	
	public static List<Frequency> sort(List<Frequency> word_freqs){
		Collections.sort(word_freqs, new Comparator<Frequency>() {
			public int compare(Frequency o1, Frequency o2){
				int differ = o1.getFrequency() - o2.getFrequency();
				if (differ != 0)
					return - differ;
				else 
					return o1.getTerm().compareTo(o2.getTerm());
		}});
		return word_freqs;
	}
	
	public static void main(String[] args) {
		try {
			Partition partition = new Partition(readFile(args[0]), 10);
			List<List<Frequency>> splits = new ArrayList<List<Frequency>>();
			while(partition.hasNext())
				splits.add(splitWords(partition.next()));
			List<Map<String, List<Frequency>>> splits_per_word = regroup(splits);
			List<Frequency> word_freqs = new ArrayList<Frequency>();
			for( Iterator<Map<String, List<Frequency>>> list_iterator = splits_per_word.iterator(); list_iterator.hasNext();){
				Iterator<Entry<String, List<Frequency>>> it = list_iterator.next().entrySet().iterator();
			    while (it.hasNext()) {
			    	Entry<String, List<Frequency>> pair = (Entry<String, List<Frequency>>) it.next();
			        word_freqs.add(count(pair));
			    }
			}
		    word_freqs = sort(word_freqs);
			for(Frequency freq_pair : word_freqs.subList(0, 25)) 
				System.out.print(freq_pair.getTerm() + " - " + freq_pair.getFrequency() + "\n");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
