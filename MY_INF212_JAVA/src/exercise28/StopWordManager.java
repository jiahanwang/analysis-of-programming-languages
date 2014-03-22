package exercise28;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StopWordManager extends ActiveWFObject{

	WordFrequencyManager word_freqs_manager;
	ArrayList<String> stop_words;
	
	public StopWordManager() {
		super("StopWordManager");
	}
	
	public void dispatch(List<Object> message) throws Exception {
		String info = (String)message.get(0);
		if(info.equals("init"))
			this.init(message.subList(1, message.size()));
		else if(info.equals("filter"))
				this.filter(message.subList(1, message.size()));
			else
				Main.send(this.word_freqs_manager, message);
	}
	
	public void init(List<Object> message) throws IOException{
		this.stop_words = new ArrayList<String>(Arrays.asList((new String(Files.readAllBytes(Paths.get("stop_words.txt", new String[]{})))).split("[,]")));
		for(char i = 'a'; i <= 'z'; i++)
			this.stop_words.add(Character.toString(i));
		this.word_freqs_manager = (WordFrequencyManager)message.get(0);
	}
	
	public void filter(List<Object> message){
		String word = (String)message.get(0);
		if(!this.stop_words.contains(word)){
			List<Object> new_message = new ArrayList<Object>();
			new_message.add("word");
			new_message.add(word);
			Main.send(this.word_freqs_manager, new_message);
		}
	}

}
