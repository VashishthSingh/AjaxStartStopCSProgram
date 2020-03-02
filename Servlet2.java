package com.ajaxstartstopjersey.AjaxStartStopJersey;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

@WebServlet("/Servlet2")
public class Servlet2 extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public Servlet2() { super(); }
	protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String jsonInputString=null;
		try {
		       URL url = new URL("http://localhost:8080/AjaxStartStopJersey/muni/getResource1");
		       //URL url = new URL("http://localhost:8181/AjaxStartStopJersey/getResource1");
		       StringBuilder postData;
		       Readings readings;

		       postData=new StringBuilder();
		       readings=new Readings();

		       LocalDateTime currDateTime = LocalDateTime.now();
		       readings.setReadDateTime(currDateTime+"");

		       long freespace = new File("/").getFreeSpace();
		       long memorySize=new File("/").getTotalSpace();
		       long usedRamSpace=memorySize-freespace; 
		       float rup=(float)(usedRamSpace*100)/memorySize;   
		       rup = (float) (Math.round(rup * 100.0) / 100.0);
		       readings.setRamUsed(rup);

		       long diskSize= ((com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean()).getTotalPhysicalMemorySize();
		       long feeSize = ((com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean()).getFreePhysicalMemorySize();
		       long usedDiskSpace=diskSize-feeSize;     
		       float dup=(float)(usedDiskSpace*100)/diskSize;  
		       dup = (float) (Math.round(dup * 100.0) / 100.0);
		       readings.setDiskUsed(dup);

		       double CPU=((com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean()).getSystemCpuLoad(); 
		       CPU=CPU*100;
		       CPU =Math.round(CPU * 100.0) / 100.0;
		       readings.setCpuUtilization(CPU);

		       jsonInputString=JsonUtility.convertToJSON(readings);

		       postData.append(jsonInputString);

		       byte [] postDataBytes=postData.toString().getBytes("UTF-8");

		       HttpURLConnection con = (HttpURLConnection) url.openConnection();
		       con.setRequestMethod("POST");
		       con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		       con.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
		       con.setDoOutput(true);

		       con.getOutputStream().write(postDataBytes); 

		       BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(),"UTF-8"));
		       String inputLine;
		       StringBuffer response=new StringBuffer();
		       while ((inputLine = in.readLine()) != null) {
		         response.append(inputLine);
		       }

		       //System.out.println(response.toString());

		       // Parsing JSON string  ---------------------------------------------------
		       JSONObject myresponse=new JSONObject(response.toString());
		       System.out.println(myresponse.getDouble("ramUsed")+" "+myresponse.getDouble("diskUsed")+" "+myresponse.getDouble("cpuUtilization")+" "+myresponse.getString("readDateTime"));
		       //-------------------------------------------------------------------------

		       in.close();
		       postData.setLength(0);
			}//end of try
			catch(Exception e) {
				System.out.println("Ex. "+e);
			}
		res.getWriter().println("From Server : ");
	}
}
