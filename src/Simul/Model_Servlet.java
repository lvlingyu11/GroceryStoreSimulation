package Simul;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Model_Servlet
 */
@WebServlet("/Model_Servlet")
public class Model_Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Model_Servlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		// TODO Auto-generated method stub
		String server_number=request.getParameter("server_num");
		ServletContext context = this.getServletContext();
		if(server_number.equals("1-Server"))
		{
			RequestDispatcher dispatcher = context.getRequestDispatcher("/SimulatorServlet");
			dispatcher.forward(request, response);
		}
		else if(server_number.equals("2-Servers"))
		{
			RequestDispatcher dispatcher=context.getRequestDispatcher("/SimulatorServlet02");
			dispatcher.forward(request,response);
		}
		else
		{
			RequestDispatcher dispatcher=context.getRequestDispatcher("/SimulatorServlet03");
			dispatcher.forward(request,response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		// TODO Auto-generated method stub
		doGet(request,response);
	}

}
