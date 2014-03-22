package exercise28;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataStorageManager extends ActiveWFObject{

	private StopWordManager stop_word_manager;
	private String data;
	
	public DataStorageManager() {
		super("DataStorageManager");
	}
	
	public void dispatch(List<Object> message) throws Exception{
		String info = (String)message.get(0);
		if(info.equals("init"))
			this.init(message.subList(1, message.size()));
		else if(info.equals("send_word_freqs"))
			this.processWords(message.subList(1, message.size()));
		else
			Main.send(this.stop_word_manager, message);
	}
	
	private void init(List<Object> message) throws IOException{
		String path = (String)message.get(0);
		this.stop_word_manager = (StopWordManager)message.get(1);
		this.data =	(new String(Files.readAllBytes(Paths.get(path, new String[]{})))).replaceAll("[\\W_]+", " ").toLowerCase();
	}
	
	private void processWords(List<Object> message){
		ArrayList<String> words = new ArrayList<String>(Arrays.asList(this.data.split("[\\s+]")));
		for(String word : words){
			List<Object> new_message = new ArrayList<Object>();
			new_message.add("filter");
			new_message.add(word);
			Main.send(this.stop_word_manager, new_message);
		}
		List<Object> new_message = new ArrayList<Object>();
		new_message.add("top25");
		new_message.add(message.get(0));
		Main.send(this.stop_word_manager, new_message);
	}

}
