package Exercise28_FreeAgents;

import java.util.ArrayList;
import java.util.List;

public class Main {

	public static void send(ActiveWFObject receiver, List<Object> message){
		receiver.addMessage(message);
	}
	
	public static void main(String[] args) {
		WordFrequencyManager word_freq_manager = new WordFrequencyManager();
		StopWordManager stop_word_manager = new StopWordManager();
		List<Object> message = new ArrayList<Object>();
		message.add("init");
		message.add(word_freq_manager);
		Main.send(stop_word_manager, message);
		DataStorageManager storage_manager = new DataStorageManager();
		message = new ArrayList<Object>();
		message.add("init");
		message.add(args[0]);
		message.add(stop_word_manager);
		Main.send(storage_manager, message);
		WordFrequencyController wfcontroller = new WordFrequencyController();
		message = new ArrayList<Object>();;
		message.add("run");
		message.add(storage_manager);
		Main.send(wfcontroller, message);
		try {
			word_freq_manager.join();
			stop_word_manager.join();
			storage_manager.join();
			wfcontroller.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
