package exercise323;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class WordFrequenciesView {
	
	private WordFrequenciesModel model;
	
	WordFrequenciesView(WordFrequenciesModel model){
		this.model = model;
		// register this view to model
		this.model.register(this);
	}
	
	public void render(){
		List<Map.Entry<String, Integer>> freqs = model.freqs;
		if(freqs.size() == 0) return;
		Collections.sort(freqs, new Comparator<Entry<String, Integer>>() {
			public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
				int differ = o1.getValue() - o2.getValue();
				if (differ != 0)
					return - differ;
				else 
					return o1.getKey().compareTo(o2.getKey());
			}
		});
		Iterator<Map.Entry<String, Integer>> iterator = freqs.iterator();
		for(int i = 0; i < 25; i++){
			Map.Entry<String, Integer> entry = iterator.next();
			System.out.print(entry.getKey() + " - " + entry.getValue() + "\n");
		}
	}
	
}
