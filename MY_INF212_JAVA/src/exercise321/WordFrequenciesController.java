package exercise321;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

public class WordFrequenciesController {
	
	private WordFrequenciesModel model;
	private WordFrequenciesView view;
	
	WordFrequenciesController(WordFrequenciesModel model, WordFrequenciesView view){
		this.model = model;
		this.view = view;
		this.view.render();
	}
	
	public void run(){
		Pattern pattern = Pattern.compile("\\s*");
		while(true){
			System.out.println("Next file:");
		    BufferedReader reader= new BufferedReader(new InputStreamReader(System.in));
			try {
				String filename = reader.readLine();
				if(pattern.matcher(filename).matches()) continue;
				this.model.update(filename);
				this.view.render();
			} catch (Exception e) {
				//e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		WordFrequenciesModel model = new WordFrequenciesModel(args[0]);
		WordFrequenciesView view = new WordFrequenciesView(model);
		WordFrequenciesController controller = new WordFrequenciesController(model, view);
		controller.run();
	}

}
