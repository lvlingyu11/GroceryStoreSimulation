package Simul;

import java.util.Random;

public class Sim 
{
	//Variables
	public static double Clock, MeanInterArrivalTime, MeanServiceTime, LastEventTime, TotalBusy,
	MaxQueueLength, SumResponseTime,SIGMA;
	public static long  NumberOfCustomers, QueueLength, NumberInService, TotalCustomers, NumberOfDepartures, LongService;
	public final static int arrival=1;
	public final static int departure=2;
	public static EventList FutureEventList;
	public static Queue Customers;
	public static Random stream;
	
	public static void Initialization()
	{
		Clock=0.0;
		QueueLength=0;
		NumberInService=0;
		LastEventTime=0.0;
		TotalBusy=0;
		MaxQueueLength=0;
		SumResponseTime=0;
		NumberOfDepartures=0;
		LongService=0;
		//Create first arrival event
		Event evt=new Event (arrival, exponential(stream, MeanInterArrivalTime));
		FutureEventList.enqueue(evt);
	}
	public static double exponential(Random rng, double mean) 
	{
		// TODO Auto-generated method stub
		return -mean*Math.log(rng.nextDouble());
	}
	public static void ProcessArrival(Event evt)
	{
		Customers.enqueue(evt);
		QueueLength++;
		//If the server is idle, fetch the event, do statistics and put into service
		if(NumberInService==0)
			ScheduleDeparture();
		else
			TotalBusy+=(Clock-LastEventTime);
		//adjust max queue length statistics 
		if(MaxQueueLength < QueueLength)
			MaxQueueLength=QueueLength;
		//Schedule the next arrival
		Event next_arrival=new Event(arrival, Clock+exponential(stream, MeanInterArrivalTime));
		FutureEventList.enqueue(next_arrival);
		LastEventTime=Clock;		
	}
	public static void ScheduleDeparture() 
	{
		// TODO Auto-generated method stub
		double ServiceTime;
		//get the job at the head of the queue
		while((ServiceTime=normal(stream,MeanServiceTime,SIGMA))<0);
		Event depart=new Event (departure, Clock+ServiceTime);
		FutureEventList.enqueue(depart);
		NumberInService=1;
		QueueLength--;
		
	}
	public static void ProcessDeparture(Event e)
	{
		//get the customer description
		Event finished=(Event) Customers.dequeue();
		//if there are customers in the queue then schedule the departure of the next one
		if(QueueLength>0)
			ScheduleDeparture();
		else
			NumberInService=0;
		//measure the response time and add to the sum
		double response=(Clock- finished.getTime());
		SumResponseTime+=response;
		if(response>4.0)
			LongService++; //record the long service
		TotalBusy+=(Clock-LastEventTime);
		NumberOfDepartures++;
		LastEventTime=Clock;
		
	}
	
	public static double SaveNormal;
	public static int NumNormals=0;
	public static final double PI=3.1415927;
	public static double normal(Random rng, double mean, double sigma) 
	{
		// TODO Auto-generated method stub
		double ReturnNormal;
		//should we generate two normals
		if(NumNormals==0)
		{
			double r1=rng.nextDouble();
			double r2=rng.nextDouble();
			ReturnNormal=Math.sqrt(-2*Math.log(r1))*Math.cos(2*PI*r2);
			SaveNormal=Math.sqrt(-2*Math.log(r1))*Math.sin(2*PI*r2);
			NumNormals=1;
		}
		else
		{
			NumNormals=0;
			ReturnNormal=SaveNormal;
		}
		return ReturnNormal*sigma+mean;
	}
	public static void ReportGeneration()
	{
		double RHO=TotalBusy/Clock;
		double AVGR=SumResponseTime/TotalCustomers;
		double PC4=((double)LongService)/TotalCustomers;
		System.out.print("SINGLE SERVER QUEUE SIMULATION");
		System.out.println("- GROCERY STORE CHECKOUT COUNTER");
		System.out.println("\tMEAN INTERARRIVAL TIME: "+MeanInterArrivalTime);
		System.out.println("\tMean SERVICE Time: "+MeanServiceTime);
		System.out.println("\tStandard DEVIATION OF SERVICE TIMES: "+SIGMA);
		System.out.println("\tNUMBER OF CUSTOMERS SERVED: "+TotalCustomers);
		System.out.println();
		System.out.println("\tSERVER UTILIZATION: "+RHO);
		System.out.println("\tMAXIMUM LINE LENGTH: "+MaxQueueLength);
		System.out.println("\tAVERAGE RESPONSE TIME: "+AVGR+" MINUTES");
		System.out.println("\tPROPORTION WHO SEPND FOUR");
		System.out.println("\t MINUTES OR MORE IN SYSTEM: "+PC4);
		System.out.println("\tSIMULATION RUN LENGTH: "+Clock+" MINUTES");
		System.out.println("\tNUMBER OF DEPARTURES: "+TotalCustomers);
	}
	
	public static void main(String argv[])
	{
		MeanInterArrivalTime=4.5;
		MeanServiceTime = 3.2;
		SIGMA = 0.6;
		TotalCustomers = 1000;
		//long seed=Long.parseLong(argv[0]);
		long seed = 1234567;
		stream = new Random(seed);
		FutureEventList = new EventList();
		Customers = new Queue();
		Initialization();
		//Loop until first "TotalCustomers" have departed
		while(NumberOfDepartures < TotalCustomers)
		{
			Event evt = (Event) FutureEventList.getMin(); //get imminent event
			FutureEventList.dequeue();
			Clock=evt.getTime();
			if(evt.getType()=="arrival")
				ProcessArrival(evt);
			else
				ProcessDeparture(evt);
		}
		ReportGeneration();
	}

}
