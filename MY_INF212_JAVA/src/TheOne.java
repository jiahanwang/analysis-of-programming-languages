
import java.util.*;
import java.util.Map.Entry;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
/*
 * 
 * This is just my try to use java to mimic The One style. I am no sure this is exactly this style.
 * 
 * */
public class TheOne {
	
	private static class TFTheOne{
		
		private Object value;
		
		public TFTheOne(Object v){
			this.value = v;
		}
		
		public TFTheOne bind(String func){
			this.value = FunctionFactory.getFunction(func).execute(this.value);
			return this;
		}
		public void print_me(){
			System.out.print(this.value);
		}
		
	}
	
	// Declare a interface so we return the instance of this interface and call the function
	private interface Interim{
		public Object execute(Object input);
	}
	
	// FunctionFacroty has only one static method: getFunction
	private static class FunctionFactory{
		//getFunction returns an anonymous instance of Interim interface based on the input string indicating the function name
		public static Interim getFunction(String func){
			switch(func){
				case "read_file":
					return new Interim(){
						public Object execute(Object input){
							try {
								return (Object)new String(Files.readAllBytes(Paths.get((String)input, new String[]{})));
							} catch (IOException e) {
								e.printStackTrace();
								return null;
							}
						}
					};
				case "filter_chars":
					return new Interim(){
						public Object execute(Object input){
								return ((String)input).replaceAll("[\\W_]+", " ");
						}
					};
				case "normalize":
					return new Interim(){
						public Object execute(Object input){
								return ((String)input).toLowerCase();
						}
					};
				case "scan":
					return new Interim(){
						public Object execute(Object input){
								return ((String)input).split("[\\s+]");
						}
					};
				case "remove_stop_words":
					return new Interim(){
						public Object execute(Object input){
							ArrayList<String> word_list = new ArrayList<String>(Arrays.asList((String[])input));
							ArrayList<String> stop_words;
							try {
								stop_words = new ArrayList<String>(Arrays.asList((new String(Files.readAllBytes(Paths.get("stop_words.txt", new String[]{})))).split("[,]")));
								for(char i = 'a'; i <= 'z'; i++)
									stop_words.add(Character.toString(i));
								for(Iterator<String> iterator = word_list.iterator(); iterator.hasNext();)
								    if (stop_words.contains(iterator.next()))
								        iterator.remove();
								return word_list;
							
							} catch (IOException e) {
								e.printStackTrace();
								return null;
							}
						}
					};
				case "frequencies":
					return new Interim(){
						public Object execute(Object input){
							Map<String, Integer> word_freqs = new HashMap<String, Integer>();
							for(Iterator<String> iterator = ((ArrayList<String>)input).iterator(); iterator.hasNext();){
								String word = iterator.next();
							    if (word_freqs.containsKey(word))
							    	word_freqs.put(word, word_freqs.get(word) + 1);
							    else
							    	word_freqs.put(word, 1);
							}
							return word_freqs;	
						}
					};
				case "sort":
					return new Interim(){
						public Object execute(Object input){
								List<Map.Entry<String, Integer>> words_freqs_list = new LinkedList<Map.Entry<String, Integer>>(((Map<String, Integer>)input).entrySet());
								Collections.sort(words_freqs_list, new Comparator<Entry<String, Integer>>() {
									public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
										return -(o1.getValue()).compareTo(o2.getValue());
									}
								});
								return words_freqs_list;
							}
					};
				case "top25_freqs":
					return new Interim(){
						public Object execute(Object input){
							List<Map.Entry<String, Integer>> words_freqs_list = (List<Map.Entry<String, Integer>>)input;
							String output = "";
							for (int i = 0; i < 25; i++){
								output += words_freqs_list.get(i).getKey() + " - " + words_freqs_list.get(i).getValue() + "\n";	
							}
							return output;
						}
					};
				default:
					return new Interim(){
						public Object execute(Object input){
							return null;
						}
					};			
			}
		}
	}

	public static void main(String[] args) {
		TFTheOne tf_the_one = new TFTheOne((Object)args[0]);
		tf_the_one.bind("read_file").bind("filter_chars").bind("normalize").bind("scan").bind("remove_stop_words").bind("frequencies").bind("sort").bind("top25_freqs").print_me();
		
	}
	
}
