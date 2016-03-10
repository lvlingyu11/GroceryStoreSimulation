<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Select the Queuing Model</title>
</head>
<body>
<h1>Parameters Selection</h1>
<%
   String error_message = "";
   Object error = request.getAttribute("error");
   if(error!=null)
	   error_message=error.toString();
%>
<form action="Model_Servlet">
<table>
<tr>
<td>Please select one of the numbers as the server number:</td>
<td><input type="checkbox" name="server_num" value="1-Server">1-Server</td>
</tr>
<tr>
<td></td>
<td><input type="checkbox" name="server_num" value="2-Servers">2-Servers</td>
</tr>
<tr>
<td></td>
<td><input type="checkbox" name="server_num" value="3-Servers">3-Servers</td>
</tr>
<tr>
<td>Please enter a integer number as the seed for the stream(e.g. 1234567):</td>
<td><input name="seed" type="text" size="10"></td>
</tr>
<tr>
<td>Please enter the total customer number you want to simulate(e.g. 1000):</td>
<td><input name="CN" type="text" size="5"></td>
</tr>
<tr>
<td>Please enter the Mean Interarrival Time for the simulator(e.g. 4.5):</td>
<td><input name="MIT" type="text" size="5"></td>
</tr>
<tr>
<td>Please enter the Mean Service Time you want for the simulaotr(e.g. 3.2):</td>
<td><input name="MST" type="text" size="5"></td>
<td style="color:red"><%=error_message%></td>
</tr>
<tr>
<td>Please enter the standard deviation of the service time(e.g. 0.6):</td>
<td><input name="Sigma" type="text" size="5"></td>
</tr>
<tr>
<td></td>
<td><input type="submit" value="Start"></td>
</tr>
</table>
</form>
</body>
</html>