package Exercise33_Restful_2;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.Map.Entry;

public class Server {
	
	private List<String> stop_words;
	private Map<String, String> handlers;
	private Map<String, List<Map.Entry<String, Integer>>> data;
	
	Server() throws IOException{
		this.stop_words = new ArrayList<String>(Arrays.asList((new String(Files.readAllBytes(Paths.get("stop_words.txt", new String[]{})))).split("[,]")));
		for(char i = 'a'; i <= 'z'; i++)
			this.stop_words.add(Character.toString(i));
		this.handlers = new HashMap<String, String>();
		this.handlers.put("post_execution", "quit_handler");
		this.handlers.put("get_default", "default_get_handler");
		this.handlers.put("get_file_form", "upload_get_handler");
		this.handlers.put("post_file", "upload_post_handler");
		this.handlers.put("get_word", "word_get_handler");
		this.handlers.put("forward_word", "word_forward_handler");
		this.data = new HashMap<String, List<Map.Entry<String, Integer>>>();
	}
	
	private List<Object> error_state(){
		List<Object> response = new ArrayList<Object>();
        response.add("Something wrong");
        List<Object> operation_1 = new ArrayList<Object>();
        operation_1.add("get");
        operation_1.add("default");
        operation_1.add(null);
        response.add(operation_1);
        return response;
	}
	
	public List<Object> default_get_handler(List<Object> args){
		List<Object> response = new ArrayList<Object>();
        String rep = "What would you like to do?\n1 - Quit" + "\n2 - Upload file";
        response.add(rep);
        List<Object> operation_1 = new ArrayList<Object>();
        operation_1.add(1);
        operation_1.add("post");
        operation_1.add("execution");
        operation_1.add(new ArrayList<Object>());
        response.add(operation_1);
        List<Object> operation_2 = new ArrayList<Object>();
        operation_2.add(2);
        operation_2.add("get");
        operation_2.add("file_form");
        operation_2.add(new ArrayList<Object>());
        response.add(operation_2);
        return response;
	}
	
	public void quit_handler(List<Object> args){
		System.out.println("Goodbye cruel world...");
		System.exit(0);
	}
	
	public List<Object> upload_get_handler(List<Object> args){
		List<Object> response = new ArrayList<Object>();
        response.add("Name of file to upload?");
        List<Object> operation_1 = new ArrayList<Object>();
        operation_1.add(1);
        operation_1.add("post");
        operation_1.add("file");
        operation_1.add(new ArrayList<Object>());
        response.add(operation_1);
        return response;
	}
	
	public List<Object> upload_post_handler(List<Object> args){
		if(args == null)
			return error_state();
		String filename = (String)args.get(0);
		if(!data.containsKey(filename)){
			try{
				ArrayList<String> words = new ArrayList<String>(Arrays.asList((new String(Files.readAllBytes(Paths.get(filename, new String[]{})))).replaceAll("[\\W_]+", " ").toLowerCase().split("[\\s+]")));
				Map<String, Integer> word_freqs = new HashMap<String, Integer>();
				for(Iterator<String> iterator = words.iterator(); iterator.hasNext();){
					String word = iterator.next();
					if(!stop_words.contains(word)&& !word.equals("")){
					    if (word_freqs.containsKey(word))
					    	word_freqs.put(word, word_freqs.get(word) + 1);
					    else
					    	word_freqs.put(word, 1);
					}
				}
				List<Map.Entry<String, Integer>> freqs = new ArrayList<Map.Entry<String, Integer>>(word_freqs.entrySet());
				Collections.sort(freqs, new Comparator<Entry<String, Integer>>() {
					public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
						int differ = o1.getValue() - o2.getValue();
						if (differ != 0)
							return - differ;
						else 
							return o1.getKey().compareTo(o2.getKey());
					}
				});
				data.put(filename, freqs);
			} catch (Exception e) {
				return error_state();
			}
		}
        List<Object> response = new ArrayList<Object>();
        response.add(filename);
        response.add(0);
		return word_get_handler(response);
	}

	public List<Object> word_get_handler(List<Object> args){
		String filename = (String)args.get(0);
		int word_index = (int)args.get(1);
		String rep;
		if(word_index < 0)
			word_index = 0;
		if(word_index < data.get(filename).size()){
			Map.Entry<String, Integer> word_info = data.get(filename).get(word_index);
			rep = "\n" + (word_index + 1) + ". " + word_info.getKey() + " " + word_info.getValue();
		}else
			rep = "\nno more words";
		rep += "\n\nWhat would you like to do next?";
		rep += "\n1 - Quit" + "\n2 - Upload file";
		rep += "\n3 - See next most-frequently occurring word";
		rep += "\n4 - See previous most-frequently occurring word";
		List<Object> response = new ArrayList<Object>();
        response.add(rep);
        List<Object> operation_1 = new ArrayList<Object>();
        operation_1.add(1);
        operation_1.add("post");
        operation_1.add("execution");
        operation_1.add(new ArrayList<Object>());
        response.add(operation_1);
        List<Object> operation_2 = new ArrayList<Object>();
        operation_2.add(2);
        operation_2.add("get");
        operation_2.add("file_form");
        operation_2.add(new ArrayList<Object>());
        response.add(operation_2);
        List<Object> operation_3 = new ArrayList<Object>();
        operation_3.add(3);
        operation_3.add("get");
        operation_3.add("word");
        List<Object> operation_3_data = new ArrayList<Object>();
        operation_3_data.add(filename);
        operation_3_data.add(word_index + 1);
        operation_3.add(operation_3_data);
        response.add(operation_3);
        List<Object> operation_4 = new ArrayList<Object>();
        operation_4.add(4);
        operation_4.add("forward");
        operation_4.add("word");
        List<Object> operation_4_data = new ArrayList<Object>();
        operation_4_data.add(filename);
        operation_4_data.add(word_index - 1);
        operation_4.add(operation_4_data);
        response.add(operation_4);
        return response;
	}
	
	public List<Object> word_forward_handler(List<Object> args){
		String filename = (String)args.get(0);
		int word_index = (int)args.get(1);
		String rep;
		if(word_index >= data.get(filename).size())
			word_index = data.get(filename).size() - 1;
		if(word_index >= 0){
			Map.Entry<String, Integer> word_info = data.get(filename).get(word_index);
			rep = "\n" + (word_index + 1) + ". " + word_info.getKey() + " " + word_info.getValue();
		}else
			rep = "\nno more words";
		rep += "\n\nWhat would you like to do next?";
		rep += "\n1 - Quit" + "\n2 - Upload file";
		rep += "\n3 - See next most-frequently occurring word";
		rep += "\n4 - See previous most-frequently occurring word";
		List<Object> response = new ArrayList<Object>();
        response.add(rep);
        List<Object> operation_1 = new ArrayList<Object>();
        operation_1.add(1);
        operation_1.add("post");
        operation_1.add("execution");
        operation_1.add(new ArrayList<Object>());
        response.add(operation_1);
        List<Object> operation_2 = new ArrayList<Object>();
        operation_2.add(2);
        operation_2.add("get");
        operation_2.add("file_form");
        operation_2.add(new ArrayList<Object>());
        response.add(operation_2);
        List<Object> operation_3 = new ArrayList<Object>();
        operation_3.add(3);
        operation_3.add("get");
        operation_3.add("word");
        List<Object> operation_3_data = new ArrayList<Object>();
        operation_3_data.add(filename);
        operation_3_data.add(word_index + 1);
        operation_3.add(operation_3_data);
        response.add(operation_3);
        List<Object> operation_4 = new ArrayList<Object>();
        operation_4.add(4);
        operation_4.add("forward");
        operation_4.add("word");
        List<Object> operation_4_data = new ArrayList<Object>();
        operation_4_data.add(filename);
        operation_4_data.add(word_index - 1);
        operation_4.add(operation_4_data);
        response.add(operation_4);
        return response;
	}
	
	public List<Object> handle_request(List<Object> args){
		String handler_key;
		if(args.size() <= 3)
			handler_key = (String)args.get(0) + '_' +(String)args.get(1);
		else
			handler_key = (String)args.get(1) + '_' +(String)args.get(2);
		String handler;
		if(this.handlers.containsKey(handler_key))
			handler = this.handlers.get(handler_key);
		else
			handler = this.handlers.get("get_default");
		try {
			Method method = this.getClass().getMethod(handler, new Class[]{List.class});
			if(args.size() <= 3)
				return (List<Object>) method.invoke(this, (List<Object>)args.get(2));
			else
				return (List<Object>) method.invoke(this, (List<Object>)args.get(3));
		} catch (Exception e) {
			e.printStackTrace();
			return error_state();
		}
	}
}
