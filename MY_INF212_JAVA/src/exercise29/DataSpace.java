package exercise29;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
// Shared dataspace 
public class DataSpace {
	public Queue<String> word_space;
	public Queue<Map<String, Integer>> freq_space;
	public List<String> stop_words;
	
	DataSpace(String words_path, String stop_words_path) throws IOException{
		this.word_space = new LinkedBlockingQueue<String>(new ArrayList<String>(Arrays.asList((new String(Files.readAllBytes(Paths.get(words_path, new String[]{})))).replaceAll("[\\W_]+", " ").toLowerCase().split("[\\s+]"))));
		this.freq_space = new LinkedBlockingQueue<Map<String, Integer>>();
		this.stop_words = new ArrayList<String>(new ArrayList<String>(Arrays.asList((new String(Files.readAllBytes(Paths.get(stop_words_path, new String[]{})))).replaceAll("[\\W_]+", " ").toLowerCase().split("[\\s+]"))));
		for(char i = 'a'; i <= 'z'; i++)
			stop_words.add(Character.toString(i));
	}

}
