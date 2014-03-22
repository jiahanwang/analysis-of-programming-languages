package exercise29;

import java.util.HashMap;
import java.util.Map;
// Every work is a thread
public class Worker extends Thread{
	
	private Map<String, Integer> word_freqs;
	private DataSpace dataSpaces;
	
	Worker(DataSpace dataSpaces){
		this.word_freqs = new HashMap<String, Integer>();
		// Get the reference to the data spaces
		this.dataSpaces = dataSpaces;
	}
	
	public void run(){
		while(true){
			String word = this.dataSpaces.word_space.poll();
			if(word == null) 
				break;
			else if(!this.dataSpaces.stop_words.contains(word)){
					if(this.word_freqs.containsKey(word))
						word_freqs.put(word, word_freqs.get(word) + 1);
				    else
				    	word_freqs.put(word, 1);
				}
		}
		this.dataSpaces.freq_space.add(this.word_freqs);
	}
}
