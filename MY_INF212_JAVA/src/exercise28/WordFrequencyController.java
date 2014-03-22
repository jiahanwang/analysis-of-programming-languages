package exercise28;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class WordFrequencyController extends ActiveWFObject{
	
	private DataStorageManager dataStorageManager;
	
	WordFrequencyController(){
		super("WordFrequencyController");
	}
	
	public void dispatch(List<Object> message) throws Exception {
		String info = (String)message.get(0);
		if(info.equals("run"))
			this.run(message.subList(1, message.size()));
		else if(info.equals("top25"))
			this.display(message.subList(1, message.size()));
	}
	
	public void run(List<Object> message){
		this.dataStorageManager = (DataStorageManager) message.get(0);
		List<Object> new_message = new ArrayList<Object>();
		new_message.add("send_word_freqs");
		new_message.add(this);
		Main.send(this.dataStorageManager, new_message);

	}
	
	public void display(List<Object> message){
		List<Map.Entry<String, Integer>> word_freqs_list = (List<Entry<String, Integer>>) message.get(0);
		for(Map.Entry<String, Integer> freq_pair : word_freqs_list.subList(0, 25)) 
			System.out.print(freq_pair.getKey() + " - " + freq_pair.getValue() + "\n");
		List<Object> new_message = new ArrayList<Object>();
		new_message.add("die");
		Main.send(this.dataStorageManager, new_message);
		this.stop = true;
	}

}
