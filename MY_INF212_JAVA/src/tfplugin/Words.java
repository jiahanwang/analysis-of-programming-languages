package tfplugin;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class Words {
	public static ArrayList<String> extract_words(String path, String stop_words_path) throws IOException{
		ArrayList<String> words = new ArrayList<String>(Arrays.asList((new String(Files.readAllBytes(Paths.get(path, new String[]{})))).replaceAll("[\\W_]+", " ").toLowerCase().split("[\\s+]")));
		ArrayList<String> stop_words = new ArrayList<String>(Arrays.asList((new String(Files.readAllBytes(Paths.get(stop_words_path, new String[]{})))).split("[,]")));
		for(char i = 'a'; i <= 'z'; i++)
			stop_words.add(Character.toString(i));
		for(Iterator<String> iterator = words.iterator(); iterator.hasNext();)
			if(stop_words.contains(iterator.next()))
				iterator.remove();
		return words;
	}
}
