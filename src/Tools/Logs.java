package Tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logs {
	
	String logPath = "Program-Together-Data/Logs/";
	
	public Logs(String error, Exception exception, boolean warning) {
		make(error, null, exception, warning);
	}
	
	public Logs(String error, String exception, boolean warning) {
		make(error, exception, null, warning);
	}
	
	public Logs(String debug) {
		System.out.println(debug);
	}
	
	private void make(String error, String exceptionString, Exception exception, boolean warning) {
		File logsFolder = new File(logPath);
		if(!logsFolder.exists()) logsFolder.mkdirs();
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy"); 
		SimpleDateFormat hourFormat = new SimpleDateFormat("HH-mm-ss"); 
		
	    Date date = new Date();  
		
	    System.err.println("[" + dateFormat.format(date) + "/" + hourFormat.format(date) + "]: " + error + "\n" + exception.getMessage());
	    
	    if(warning) {
	    	File logFile = new File(logPath + dateFormat.format(date));
	    	
	    	if(!logFile.exists()) logFile.mkdirs();
	    	
			try {
				FileWriter newFile = new FileWriter(logPath + dateFormat.format(date) + "/" + hourFormat.format(date) + ".log", true);
				BufferedWriter buffFile = new BufferedWriter(newFile);
				PrintWriter printFile = new PrintWriter(buffFile, true);
				printFile.println(error);
				
				if(exception != null) exception.printStackTrace(printFile);
				if(exceptionString != null) printFile.println(exceptionString);
				
			} catch (IOException e) {
				System.err.println("Error to create log: " + dateFormat.format(date) + "/" + hourFormat.format(date) + ".log");
				return;
			}
			
			System.exit(0);
	    }
	}
}
