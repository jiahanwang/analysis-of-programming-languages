import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

public class Exercise11 {

	public static void main(String[] args) throws IOException {
		// The constrained memory should have no more than 1024 cells
		Object[] data = new Object[8];
		/*
		 * We're lucky: The stop words are only 556 characters and the lines are all less than 80 characters, so we can
		 * use that knowledge to simplify the problem: we can have the stop words loaded in memory while processing one
		 * line of the input at a time. If these two assumptions didn't hold, the algorithm would need to be changed
		 * considerably.
		 * 
		 * Overall stragety: (PART 1) read the input file, count the words, increment/store counts in secondary memory
		 * (a file) (PART 2) find the 25 most frequent words in secondary memory
		 * 
		 * PART 1: - read the input file one line at a time - filter the characters, normalize to lower case - identify
		 * words, increment corresponding counts in file
		 */
		// Load the list of stop words
		BufferedReader f = new BufferedReader(new FileReader("../stop_words.txt"));
		data[0] = new char[1024];
		f.read((char[]) data[0]);
		data[0] = (new String((char[]) data[0])).trim().split(",");
		f.close();
		data[1] = ""; 		// data[1] is line (max 80 characters)
		data[2] = null; 	// data[2] is index of the start_char of word
		data[3] = 0; 		// data[3] is index on characters, i = 0
		data[4] = false; 	// data[4] is flag indicating if word was found
		data[5] = ""; 		// data[5] is the word
		data[6] = ""; 		// data[6] is word,NNNN
		data[7] = 0; 		// data[7] is frequency
		// Open the secondary memory
		RandomAccessFile word_freqs = new RandomAccessFile("word_freqs", "rw");
		//This is needed to purge the RandomAccessFile, otherwise the frequency counter will accumulate over runs.
		word_freqs.setLength(0);
		// Open the input file
		f = new BufferedReader(new FileReader((args[0])));
		//f = new BufferedReader(new FileReader("pride-and-prejudice.txt"));
		// Loop over input file's lines
		while (true) {
			data[1] = f.readLine();
			if (data[1] == null) // end of input file
				break;
			data[2] = null;
			data[3] = 0;
			// Loop over characters in the line
			for (char c : data[1].toString().toCharArray()) {
				if (data[2] == null) {
					if (Character.isAlphabetic(c))
						data[2] = data[3];
				} else {
					if (!Character.isAlphabetic(c)) {
						data[4] = false;
						data[5] = data[1].toString().substring((int) data[2], (int) data[3]).toLowerCase();

						// Ignore words with len < 2, and stop words
						if (data[5].toString().length() >= 2
								&& !Arrays.asList((String[]) data[0]).contains(data[5].toString())) {
							// Let's see if it already exists
							while (true) {
								data[6] = word_freqs.readLine();
								if (data[6] == null || data[6].toString().trim().isEmpty())
									break;
								data[6] = data[6].toString().trim();
								//Change back to int for increment
								data[7] = Integer.parseInt(data[6].toString().split(",")[1]);
								// word, no white space
								data[6] = data[6].toString().split(",")[0].trim();
								if (data[5].equals(data[6])) {
									//words are the same, increment counter and set flag.
									data[7] = (int) data[7] + 1;
									data[4] = true;
									break;
								}
							}
							if (!(boolean) data[4]) {
								word_freqs.writeBytes(String.format("%20s,%04d\n", data[5], 1));
							} else {
								//moves back cursor to rewrite line
								word_freqs.seek(word_freqs.getFilePointer() - 26);
								word_freqs.writeBytes(String.format("%20s,%04d\n", data[5], data[7]));
							}
							word_freqs.seek(0);
						}
						// Let's reset
						data[2] = null;
					}
				}
				data[3] = (int) data[3] + 1;
			}
		}
		// We're done with the input file
		f.close();

		// PART 2
		// Now we need to find the 25 most frequently occuring words.
		// We don't need anything from the previous values in memory

		// Let's use the first 25 entries for the top 25 words
		// 28 is used for the first 25 + use of 2 temp vars and 1 extra for insertion.
		data = new Object[28];
		Arrays.fill(data, 0, 25, null);
		data[25] = "";
		data[26] = 0;

		// Loop over secondary memory file
		while (true) {
			data[25] = word_freqs.readLine();
			if (data[25] == null || data[25].toString().trim().isEmpty())
				break;
			data[25] = data[25].toString().trim();
			data[26] = Integer.parseInt((data[25].toString().split(",")[1])); // Read it as integer
			data[25] = data[25].toString().split(",")[0].trim();// word
			// Check if this word has more counts than the ones in memory
			for (int i = 0; i < 25; i++) { // elimination of symbol i is exercise
				if (data[i] == null || (int) (((Object[]) data[i])[1]) < (int) data[26]) {
					System.arraycopy(data, i, data, i + 1, 27 - i);
					//data[25] -> data[26] and data[26] -> data[27] due to creating spot for insertion.
					data[i] = new Object[] { data[26], data[27] }; 
					//clear the last element
					data[27] = 0;
					break;
				}
			}
		}
		for (int tf = 0; tf < 25; tf++)
			// elimination of symbol tf is exercise
			if (data[tf] != null)
				System.out.println(((Object[]) data[tf])[0] + "-" + ((Object[]) data[tf])[1]);
		// We're done
		word_freqs.close();
	}
};
