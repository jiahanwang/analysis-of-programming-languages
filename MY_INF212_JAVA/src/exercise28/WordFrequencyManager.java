package exercise28;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class WordFrequencyManager extends ActiveWFObject{

	private Map<String, Integer> word_freqs = new HashMap<String, Integer>();
	
	public WordFrequencyManager() {
		super("WordFrequencyManager");
	}
	
	public void dispatch(List<Object> message) throws Exception {
		String info = (String)message.get(0);
		if(info.equals("word")){
			this.incrementCount(message.subList(1, message.size()));
		}
		else if(info.equals("top25"))
				this.top25(message.subList(1, message.size()));
	}
	
	public void incrementCount(List<Object> message) {
		String word = (String)message.get(0);
		if(this.word_freqs.containsKey(word))
			word_freqs.put(word, word_freqs.get(word) + 1);
	    else
	    	word_freqs.put(word, 1);
		
	}
	
	public void top25(List<Object> message){
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
		List<Object> new_message = new ArrayList<Object>();
		new_message.add("top25");
		new_message.add(word_freqs_list);
		Main.send((ActiveWFObject) message.get(0),new_message);

	}

}
