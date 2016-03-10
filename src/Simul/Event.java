package Simul;

public class Event 
{
	private double time;
    private int type;

    public Event(int type, double time) {
        this.type = type;
        this.time = time;
    }
    
    public Event(String t, double time)
    {
    	if(t=="arrival")
    	{
    		type=1;
    	}
    	else 
    		type=2;
    	this.time= time;
    }

    public String getType() {
        if(type==1)
        	return "arrival";
        else 
        	return "departure";
    }

    public double getTime() {
        return time;
    }
}
