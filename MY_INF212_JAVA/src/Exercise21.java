import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

public class Exercise21 {
	static//
	// The all-important data stack
	//
	LinkedList stack = new LinkedList();
	static HashMap heap = new HashMap();

	//
	// The new "words" (procedures) of our program
	//
	static void read_file() throws IOException {
		/*
		 * Takes a path to a file on the stack and places the entire contents of the file back on the stack.
		 */
		FileInputStream f = new FileInputStream(stack.pop().toString());
		byte[] buffer = new byte[f.available()];
		f.read(buffer);
		stack.push(new String(buffer));
		f.close();
	}

	static void filter_chars() {
		/*
		 * Takes data on the stack and places back a copy with all nonalphanumeric chars replaced by white space.
		 */
		stack.push(stack.pop().toString().replaceAll("[\\W_]+", " ").toLowerCase());
	}

	static void scan() {
		/*
		 * Takes a string on the stack and scans for words, placing the list of words back on the stack
		 */
		stack.addAll(Arrays.asList(stack.pop().toString().split("\\s+")));
	}

	static void remove_stop_words() throws IOException {
		/*
		 * Takes a list of words on the stack and removes stop words.
		 */
		FileInputStream f = new FileInputStream("stop_words.txt");
		byte[] buffer = new byte[f.available()];
		f.read(buffer);
		stack.push(new ArrayList(Arrays.asList(new String(buffer).split(","))));
		f.close();
		// add single-letter words
		((List) stack.getFirst()).addAll(Arrays.asList(new String[] { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
				"k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z" }));
		heap.put("stop_words", stack.pop());
		heap.put("words", new LinkedList());
		while (stack.size() > 0)
			if (((List) heap.get("stop_words")).contains(stack.getFirst().toString()))
				stack.pop();// pop it and drop it
			else
				((List) heap.get("words")).add(stack.pop().toString());// pop it, store it
		stack.addAll((List) heap.get("words"));// Load the words onto the stack
		heap.remove("stop_words");
		heap.remove("words");
	}

	static void frequencies() {
		// Takes a list of words and returns a dictionary associating
		// words with frequencies of occurrence.
		heap.put("word_freqs", new HashMap());
		// A little flavour of the real Forth style here...
		while (stack.size() > 0) {
			// ... but the following line is not in style, because the
			// naive implementation would be too slow
			if (((HashMap) heap.get("word_freqs")).containsKey(stack.getFirst().toString())) {
				// Increment the frequency, postfix style: f 1 +
				stack.push(((HashMap) heap.get("word_freqs")).get(stack.getFirst())); // push f
				stack.push(1);// push 1
				stack.push((int) stack.pop() + (int) stack.pop()); // pop and add
			} else
				stack.push(1);// Push 1 in stack[2]
			// Load the updated freq back onto the heap
			heap.put("temp",stack.pop());
			((HashMap) heap.get("word_freqs")).put(stack.pop().toString(), heap.get("temp"));

		}
		// Push the result onto the stack
		stack.push(heap.get("word_freqs"));
		heap.remove("word_freqs"); // We dont need this variable anymore
	}

	static void sort() {
		List list = new LinkedList(((HashMap) stack.pop()).entrySet());
		Collections.sort(list, new Comparator<Entry>() {
			@Override
			public int compare(Entry o1, Entry o2) {
				return -((Comparable) o1.getValue()).compareTo(o2.getValue());
			}
		});
		stack.addAll(list);
	}

	public static void main(String[] args) throws IOException {
		stack.push(args[0]);
		//stack.push("pride-and-prejudice.txt");
		read_file();
		filter_chars();
		scan();
		remove_stop_words();
		frequencies();
		sort();
		stack.push(0);
		while ((int) stack.getFirst() < 25) {
			heap.put("i", stack.pop());
			Entry e = (Entry) stack.pop();
			System.out.println(e.getKey() + "-" + e.getValue());
			stack.push(heap.get("i"));
			stack.push(1);
			stack.push((int) stack.pop() + (int) stack.pop());
		}
	}

}
