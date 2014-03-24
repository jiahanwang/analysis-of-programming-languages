package Exercise31_DoubleInverseMultiplexer_3;

import java.util.Iterator;

// Implements the partition function by implements Iterator interface
public class Partition implements Iterator<String>{

	private String[] lines;
	int nlines;
	int current;
	
	Partition(String data_str, int nlines){
		this.lines = data_str.split("\\n");
		this.nlines = nlines;
		this.current = 0;
	}
	
	@Override
	public boolean hasNext() {
		if(this.current + 1 > lines.length)
			return false;
		else
			return true;
	}

	@Override
	public String next() {
		int end;
		if(this.current + nlines > this.lines.length)
			end = this.lines.length;
		else
			end = this.current + nlines - 1;
		String output = "";
		for(int i = current; i < end; i++)
			output += this.lines[i] + "\n";
		this.current = end;
		return output;
	}

	@Override
	public void remove() {
		// do nothing
		return;
	}
	
}