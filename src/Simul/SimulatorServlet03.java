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
 * Servlet implementation class SimulatorServlet03
 */
@WebServlet("/SimulatorServlet03")
public class SimulatorServlet03 extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	public static double Clock_1,Clock_2,Clock_3,Clock, MeanInterArrivalTime, MeanServiceTime, LastEventTime_1,LastEventTime_2,LastEventTime_3, TotalBusy_1,TotalBusy_2,TotalBusy_3,
	MaxQueueLength_1,MaxQueueLength_2, MaxQueueLength_3,SumResponseTime,SIGMA;
	public static long  NumberOfCustomers, NumberOfCustomers_1, NumberOfCustomers_2,QueueLength_1,QueueLength_2, QueueLength_3,NumberInService_1,NumberInService_2, NumberInService_3,TotalCustomers, NumberOfDepartures, LongService;
	public final static int arrival=1;
	public final static int departure=2;
	public static EventList FutureEventList_Server1, FutureEventList_Server2,FutureEventList_Server3;
	public static Queue Customers_1,Customers_2,Customers_3;
	public static Random stream;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SimulatorServlet03() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String url="/Queuing_Model_Selection.jsp";
		String seedS=request.getParameter("seed");
		long seed=Long.parseLong(seedS);
		String CN=request.getParameter("CN");
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
		if(MeanServiceTime>3*MeanInterArrivalTime)
		{
			request.setAttribute("error", "In order to make sure that the system is stable, please make sure that mean service time is less than 3*(mean interarrival time)!");
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
		FutureEventList_Server1 = new EventList();
		FutureEventList_Server2 = new EventList();
		FutureEventList_Server3 = new EventList();
		Customers_1= new Queue();
		Customers_2=new Queue();
		Customers_3=new Queue();
		Initialization();
		while(NumberOfDepartures < TotalCustomers)
		{
			Event evt_1 = (Event) FutureEventList_Server1.getMin(); //get imminent event
			if(evt_1!=null)
			{
			FutureEventList_Server1.dequeue();
			Clock_1=evt_1.getTime();
			if(evt_1.getType()=="arrival")
				ProcessArrival(evt_1,1);
			else
				ProcessDeparture(evt_1,1);
			}
			Event evt_2 = (Event) FutureEventList_Server2.getMin();
			if(evt_2!=null)
			{
			FutureEventList_Server2.dequeue();
			Clock_2=evt_2.getTime();
			if(evt_2.getType()=="arrival")
				ProcessArrival(evt_2,2);
			else
				ProcessDeparture(evt_2,2);
			}
			Event evt_3=(Event) FutureEventList_Server3.getMin();
			if(evt_3!=null)
			{
				FutureEventList_Server3.dequeue();
				Clock_3=evt_3.getTime();
				if(evt_3.getType()=="arrival")
					ProcessArrival(evt_3,3);
				else
					ProcessDeparture(evt_3,3);
			}
			if(Clock_1<=Clock_2)
			{
				Clock=Clock_2;
				if(Clock<=Clock_3)
					Clock=Clock_3;
			}
			else
			{
				Clock=Clock_1;
				if(Clock<Clock_3)
					Clock=Clock_3;
			}
		}
		
		PrintWriter out=response.getWriter();
		out.println("<CENTER>");
		out.println("<b>");
		out.println("MULTI (3) SERVERS QUEUE SIMULATION - GROCERY STORE CHECKOUT COUNTER");
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
		double RHO_1=TotalBusy_1/Clock;
		out.println("SERVER UTILIZATION OF SERVER1: "+RHO_1);
		out.println("<BR>");
		double RHO_2=TotalBusy_2/Clock;
		out.println("SERVER UTILIZATION OF SERVER2: "+RHO_2);
		out.println("<BR>");
		double RHO_3=TotalBusy_3/Clock;
		out.println("SERVER UTILIZATION OF SERVER2: "+RHO_3);
		out.println("<BR>");
		out.println("NUMBER OF CUSTOMERS SERVED: "+TotalCustomers);
		out.println("<BR>");
		out.println("NUMBER OF CUSTOMERS SERVER1 SERVED: "+NumberOfCustomers_1);
		out.println("<BR>");
		out.println("NUMBER OF CUSTOMERS SERVER2 SERVED: "+NumberOfCustomers_2);
		out.println("<BR>");
		long NumberOfCustomers_3=TotalCustomers-NumberOfCustomers_1-NumberOfCustomers_2;
		out.println("NUMBER OF CUSTOMERS SERVER3 SERVED: "+NumberOfCustomers_3);
		out.println("<BR>");
		out.println("MAXIMUM LINE LENGTH OF SERVER1: "+MaxQueueLength_1);
		out.println("<BR>");
		out.println("MAXIMUM LINE LENGTH OF SERVER2: "+MaxQueueLength_2);
		out.println("<BR>");
		out.println("MAXIMUM LINE LENGTH OF SERVER3: "+MaxQueueLength_3);
		out.println("<BR>");
		double AVGR=SumResponseTime/TotalCustomers;
		out.println("AVERAGE RESPONSE TIME: "+AVGR);
		out.println("<BR>");
		double PC4=((double)LongService)/TotalCustomers;
		out.println("PROPORTION WHO SEPND TIME MORE THAN MEAN INTERARRIVAL TIME IN SYSTEM: "+PC4);
		out.println("<BR>");
		out.println("SIMULATION RUN LENGTH: "+Clock+" MINUTES");
		out.println("<BR>");
		out.println("NUMBER OF DEPARTURES: "+TotalCustomers);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request,response);
	}
	
	public static void Initialization() 
	{
		// TODO Auto-generated method stub
		Clock_1=0.0;
		Clock_2=0.0;
		Clock_3=0.0;
		Clock=0.0;
		QueueLength_1=0;
		QueueLength_2=0;
		QueueLength_3=0;
		NumberInService_1=0;
		NumberInService_2=0;
		NumberInService_3=0;
		LastEventTime_1=0.0;
		LastEventTime_2=0.0;
		LastEventTime_3=0.0;
		TotalBusy_1=0;
		TotalBusy_2=0;
		TotalBusy_3=0;
		MaxQueueLength_1=0;
		MaxQueueLength_2=0;
		MaxQueueLength_3=0;
		SumResponseTime=0;
		NumberOfDepartures=0;
		LongService=0;
		NumberOfCustomers_1=0;
		NumberOfCustomers_2=0;
		Event evt_1=new Event (arrival, exponential(stream, MeanInterArrivalTime));
		FutureEventList_Server1.enqueue(evt_1);
	}
	
	public static double exponential(Random rng, double mean) 
	{
		// TODO Auto-generated method stub
		return -mean*Math.log(rng.nextDouble());
	}
	
	public static void ProcessArrival(Event evt,int i) 
	{
		// TODO Auto-generated method stub
		if(i==1)
		{
		Customers_1.enqueue(evt);
		QueueLength_1++;
		NumberOfCustomers_1++;
		//If the server is idle, fetch the event, do statistics and put into service
		if(NumberInService_1==0)
			ScheduleDeparture(1);
		else
			TotalBusy_1+=(Clock_1-LastEventTime_1);
		//adjust max queue length statistics 
		if(MaxQueueLength_1 < QueueLength_1)
			MaxQueueLength_1=QueueLength_1;
		LastEventTime_1=Clock_1;
		}
		else if(i==2)
		{
			Customers_2.enqueue(evt);
			QueueLength_2++;
			NumberOfCustomers_2++;
			//If the server is idle, fetch the event, do statistics and put into service
			if(NumberInService_2==0)
				ScheduleDeparture(2);
			else
				TotalBusy_2+=(Clock_2-LastEventTime_2);
			//adjust max queue length statistics 
			if(MaxQueueLength_2 < QueueLength_2)
				MaxQueueLength_2=QueueLength_2;
			LastEventTime_2=Clock_2;
		}
		else
		{
			Customers_3.enqueue(evt);
			QueueLength_3++;
			//If the server is idle, fetch the event, do statistics and put into service
			if(NumberInService_3==0)
				ScheduleDeparture(3);
			else
				TotalBusy_3+=(Clock_3-LastEventTime_3);
			//adjust max queue length statistics 
			if(MaxQueueLength_3 < QueueLength_3)
				MaxQueueLength_3=QueueLength_3;
			LastEventTime_3=Clock_3;
		}
		double random=Math.random();
		Event next_arrival=new Event(arrival,Clock+exponential(stream,MeanInterArrivalTime));
		if(QueueLength_1<QueueLength_2)
		{
			if(QueueLength_1<QueueLength_3)
				FutureEventList_Server1.enqueue(next_arrival);
			else if(QueueLength_1>QueueLength_3)
				FutureEventList_Server3.enqueue(next_arrival);
			else
			{
				if(random>=0 && random<=0.5)
					FutureEventList_Server1.enqueue(next_arrival);
				else
					FutureEventList_Server3.enqueue(next_arrival);
			}
		}
		else if(QueueLength_2<QueueLength_1)
		{
			if(QueueLength_2<QueueLength_3)
				FutureEventList_Server2.enqueue(next_arrival);
			else if(QueueLength_2>QueueLength_3)
				FutureEventList_Server3.enqueue(next_arrival);
			else
			{
				if(random>=0 && random<=0.5)
					FutureEventList_Server2.enqueue(next_arrival);
				else
					FutureEventList_Server3.enqueue(next_arrival);
			}
		}
		else if(QueueLength_2==QueueLength_1)
		{
			if(QueueLength_3>QueueLength_1)
			{
			if(random>=0 && random<=0.5)
				FutureEventList_Server1.enqueue(next_arrival);
			else
				FutureEventList_Server2.enqueue(next_arrival);
			}
			else if(QueueLength_3==QueueLength_1)
			{
				Random rand=new Random();
				double value=3*rand.nextDouble();
				if(value>=0 && value<=1)
					FutureEventList_Server1.enqueue(next_arrival);
				else if(value>1 && value<=2)
					FutureEventList_Server2.enqueue(next_arrival);
				else
					FutureEventList_Server3.enqueue(next_arrival);
			}
		}
	}
	
	public static void ScheduleDeparture(int i) 
	{
		// TODO Auto-generated method stub
		double ServiceTime;
		//get the job at the head of the queue
		while((ServiceTime=normal(stream,MeanServiceTime,SIGMA))<0);
		if(i==1)
		{
		Event depart=new Event (departure, Clock_1+ServiceTime);
		FutureEventList_Server1.enqueue(depart);
		NumberInService_1=1;
		QueueLength_1--;
		}
		else if(i==2)
		{
			Event depart=new Event (departure, Clock_2+ServiceTime);
			FutureEventList_Server2.enqueue(depart);
			NumberInService_2=1;
			QueueLength_2--;
		}
		else
		{
			Event depart=new Event (departure, Clock_3+ServiceTime);
			FutureEventList_Server3.enqueue(depart);
			NumberInService_3=1;
			QueueLength_3--;
		}
	}
	
	public static void ProcessDeparture(Event e, int i) 
	{
		// TODO Auto-generated method stub
		//get the customer description
		if(i==1)
		{
			Event finished=(Event) Customers_1.dequeue();
			//if there are customers in the queue then schedule the departure of the next one
			if(QueueLength_1>0)
				ScheduleDeparture(1);
			else
				NumberInService_1=0;
				//measure the response time and add to the sum
			double response=(Clock_1- finished.getTime());
			SumResponseTime+=response;
			if(response>=MeanInterArrivalTime)
				LongService++; //record the long service
			TotalBusy_1+=(Clock_1-LastEventTime_1);
			NumberOfDepartures++;
		    LastEventTime_1=Clock_1;
		}
		else if(i==2)
		{
			Event finished=(Event) Customers_2.dequeue();
			//if there are customers in the queue then schedule the departure of the next one
			if(QueueLength_2>0)
				ScheduleDeparture(2);
			else
				NumberInService_2=0;
				//measure the response time and add to the sum
			double response=(Clock_2- finished.getTime());
			SumResponseTime+=response;
			if(response>=MeanInterArrivalTime)
				LongService++; //record the long service
			TotalBusy_2+=(Clock_2-LastEventTime_2);
			NumberOfDepartures++;
		    LastEventTime_2=Clock_2;
		}
		else
		{
			Event finished=(Event) Customers_3.dequeue();
			//if there are customers in the queue then schedule the departure of the next one
			if(QueueLength_3>0)
				ScheduleDeparture(3);
			else
				NumberInService_3=0;
				//measure the response time and add to the sum
			double response=(Clock_3- finished.getTime());
			SumResponseTime+=response;
			if(response>=MeanInterArrivalTime)
				LongService++; //record the long service
			TotalBusy_3+=(Clock_3-LastEventTime_3);
			NumberOfDepartures++;
		    LastEventTime_3=Clock_3;
		}
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
}
