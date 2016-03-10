package Simul;

import java.util.LinkedList;
import java.util.NoSuchElementException;

class EventList extends LinkedList
{
	private static final long serialVersionUID=1L;
	 public EventList() {
	        super();
	    }
	    
	    public Object getMin() 
	    {  
	    	try
	    	{
	    	return getFirst();
	    	}
	    	catch(NoSuchElementException e)
	    	{
	    		return null;
	    	}
	    }
	        
	    public void enqueue(Object o) {
	        add(o);
	    }
	    
	    public void dequeue() {
	    	try
	    	{
	        removeFirst();
	    	}
	    	catch(NoSuchElementException e)
	    	{
	    		e.printStackTrace();
	    	}
	    }
}