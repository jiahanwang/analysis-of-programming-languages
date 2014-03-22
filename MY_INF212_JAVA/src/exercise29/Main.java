package exercise29;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Main {
	
	public static void main(String[] args){
		try {
			DataSpace dataSpaces = new DataSpace(args[0], args[1]);
			List<Worker> workers = new ArrayList<Worker>();
			// Create workers and start every worker
			for(int i = 0; i < 5; i++)
				workers.add(new Worker(dataSpaces));
			for(int i = 0; i < 5; i++)
				workers.get(i).start();
			for(int i = 0; i < 5; i++)
				workers.get(i).join();
			// Merge partial frequency results
			Map<String, Integer> word_freqs = new HashMap<String, Integer>();
			while(!dataSpaces.freq_space.isEmpty()){
				Map<String, Integer> freqs = dataSpaces.freq_space.poll();
				Iterator<Entry<String, Integer>> it = freqs.entrySet().iterator();
			    while (it.hasNext()) {
			        Entry<String, Integer> pair = (Entry<String, Integer>) it.next();
			        if(word_freqs.containsKey(pair.getKey()))
			        	word_freqs.put(pair.getKey(), pair.getValue() + word_freqs.get(pair.getKey()));
			        else
			        	word_freqs.put(pair.getKey(), pair.getValue());
			    }
			}
			// Sort the final frequency list 
			List<Map.Entry<String, Integer>> word_freqs_list = new LinkedList<Map.Entry<String, Integer>>(word_freqs.entrySet());
			Collections.sort(word_freqs_list, new Comparator<Entry<String, Integer>>() {
				public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
					int differ = o1.getValue() - o2.getValue();
					if (differ != 0)
						return - differ;
					else 
						return o1.getKey().compareTo(o2.getKey());
				}
			});
			// Display the result
			for(Map.Entry<String, Integer> freq_pair : word_freqs_list.subList(0, 25)) 
				System.out.print(freq_pair.getKey() + " - " + freq_pair.getValue() + "\n");
		} catch (Exception e){
			e.printStackTrace();
		}		
	}

}
