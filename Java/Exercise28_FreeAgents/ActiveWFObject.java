package Exercise28_FreeAgents;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class ActiveWFObject extends Thread{
	
	private Queue<List<Object>> queue;
	public String name;
	protected boolean stop;
	
	public ActiveWFObject(String name){
		this.name = name;
		// using this built-in blocking queue is more reliable
		this.queue = new LinkedBlockingQueue<List<Object>>();
		this.stop = false;
		this.start();
	}
	
	public void addMessage(List<Object> message){
		this.queue.add(message);
	}
	
	public List<Object> getMessage(){
		return this.queue.poll();
	}
	
	public void run(){
		while(!this.stop){
			List<Object> message = this.getMessage();
			try {
				if(message == null)
					continue;
				//System.out.println(this.name + ": " + message);
				this.dispatch(message);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(((String)(message.get(0))).equals("die"))
				this.stop = true;
		}
	}
	
	public abstract void dispatch(List<Object> message) throws Exception;

}
