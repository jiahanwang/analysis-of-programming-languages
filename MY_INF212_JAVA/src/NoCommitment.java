import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.ini4j.Ini;
import org.ini4j.InvalidFileFormatException;


public class NoCommitment {
	
	private static Class[] load() throws InvalidFileFormatException, IOException, ClassNotFoundException{
			Ini ini = new Ini(new File("config.ini"));
			URL myJarUrl = new URL("file:///" + ini.get("tfplugin", "location"));
			URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{myJarUrl});
			Class Words = classLoader.loadClass(ini.get("Words", "name"));
			Class Frequencies = classLoader.loadClass(ini.get("Frequencies", "name"));
			return new Class[]{Words, Frequencies};
	}

	public static void main(String[] args) {
		try{
			Class[] classArray = load();
			Method extract_words = classArray[0].getMethod("extract_words", new Class[]{String.class, String.class});
			ArrayList<String> words = (ArrayList<String>) extract_words.invoke(classArray[0].newInstance(), args[0], args[1]);
			Method top_25 = classArray[1].getMethod("top_25",new Class[]{ArrayList.class});
			List<Map.Entry<String, Integer>> word_freqs = (List<Entry<String, Integer>>) top_25.invoke(classArray[1].newInstance(), words);
			for(Map.Entry<String, Integer> entry: word_freqs)
				System.out.format("%-20s %d\n", entry.getKey(), entry.getValue());
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
