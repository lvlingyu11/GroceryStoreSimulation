package Simul;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class SimulatorServlet
 */
@WebServlet("/SimulatorServlet")
public class SimulatorServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public static double Clock, MeanInterArrivalTime, MeanServiceTime, LastEventTime, TotalBusy,
	MaxQueueLength, SumResponseTime,SIGMA;
	public static long  NumberOfCustomers, QueueLength, NumberInService, TotalCustomers, NumberOfDepartures, LongService;
	public final static int arrival=1;
	public final static int departure=2;
	public static EventList FutureEventList;
	public static Queue Customers;
	public static Random stream;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SimulatorServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		// TODO Auto-generated method stub
		String url="/Queuing_Model_Selection.jsp";
		String CN=request.getParameter("CN");
		String seedS=request.getParameter("seed");
		long seed=Long.parseLong(seedS);
		int customer_number=Integer.parseInt(CN);
		TotalCustomers=customer_number;
		String MIT=request.getParameter("MIT");
		double meanInterArrivalTime=Double.parseDouble(MIT);
		MeanInterArrivalTime = meanInterArrivalTime;
		String MST=request.getParameter("MST");
		double meanServiceTime=Double.parseDouble(MST);
		MeanServiceTime=meanServiceTime;
		String Sigma=request.getParameter("Sigma");
		SIGMA=Double.parseDouble(Sigma);
		if(MeanServiceTime>MeanInterArrivalTime)
		{
			request.setAttribute("error", "In order to make sure that the system is stable, please make sure that mean service time is less than mean interarrival time!");
			ServletContext context=getServletContext();
			RequestDispatcher dispatcher=context.getRequestDispatcher(url);
			dispatcher.forward(request, response);
		}
		else
		{
		
//		MeanInterArrivalTime=4.5;
//		MeanServiceTime = 3.2;
//		SIGMA = 0.6;
//		TotalCustomers = 1000;
//		long seed = 1234567;
		stream = new Random(seed);
		FutureEventList = new EventList();
		Customers = new Queue();
		Initialization();
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
		
		PrintWriter out=response.getWriter();
		out.println("<CENTER>");
		out.println("<b>");
		out.println("SINGLE SERVER QUEUE SIMULATION - GROCERY STORE CHECKOUT COUNTER");
		out.println("</b>");
		out.println("<BR>");
		out.println("</CENTER>");
		out.println("MEAN INTERARRIVAL TIME: "+MeanInterArrivalTime);
		out.println("<BR>");
		out.println("MEAN SERVICE TIME: "+MeanServiceTime);
		out.println("<BR>");
		out.println("STANDARD DEVIATION OF SERVICE TIMES: "+SIGMA);
		out.println("<BR>");
		out.println("NUMBER OF CUSTOMERS SERVED: "+TotalCustomers);
		out.println("<BR>");
		double RHO=TotalBusy/Clock;
		out.println("SERVER UTILIZATION: "+RHO);
		out.println("<BR>");
		out.println("MAXIMUM LINE LENGTH: "+MaxQueueLength);
		out.println("<BR>");
		double AVGR=SumResponseTime/TotalCustomers;
		out.println("AVERAGE RESPONSE TIME: "+AVGR);
		out.println("<BR>");
		double PC4=((double)LongService)/TotalCustomers;
		out.println("PROPORTION WHO SEPND TIME LONGER THAN MEAN INTERARRIVAL TIME IN SYSTEM: "+PC4);
		out.println("<BR>");
		out.println("SIMULATION RUN LENGTH: "+Clock+" MINUTES");
		out.println("<BR>");
		out.println("NUMBER OF DEPARTURES: "+TotalCustomers);
		
//		ReportGeneration();
		}
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
	
	
	public static void ProcessDeparture(Event e) 
	{
		// TODO Auto-generated method stub
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
			if(response>=MeanInterArrivalTime)
				LongService++; //record the long service
			TotalBusy+=(Clock-LastEventTime);
			NumberOfDepartures++;
		    LastEventTime=Clock;
		
	}

	public static void ProcessArrival(Event evt) 
	{
		// TODO Auto-generated method stub
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

	public static void Initialization() 
	{
		// TODO Auto-generated method stub
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

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
