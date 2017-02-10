package spatial.test;

/**
 * @author Sooraj
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

class PromptDriver
{	
	Query q = new Query();
	
	void run(){
	    String choice = "";
	    System.out.println(); System.out.println(); 
	    System.out.println("Type your query below. Type \'Exit\' to quit.");    
	    while(true) { 
	    	menu(); 	      
	    	try{
	    		choice = Input.getReturn();
	    		if(choice == null)
	    			continue;
	    		if(choice.equalsIgnoreCase("Exit"))
	    			break;
	    		else if(choice.equalsIgnoreCase("\n") || choice.equalsIgnoreCase(""))
	    			continue;
	    		else 
	    			q.processQuery(choice);
	    	}
	    	catch(Exception e) {
	    		e.printStackTrace();
	    		System.out.println("       !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
	    		System.out.println("       !!        		 Something is wrong                  !!");
	    		System.out.println("       !!     Is your DB full? Then exit. Rerun program!     !!");
	    		System.out.println("       !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
	    	}      
	    }
	}
	      
	private void menu() {
		System.out.print(">>>");
	 } 	
}

class Input {

	static String getReturn () {
	    BufferedReader in = new BufferedReader (new InputStreamReader(System.in));
	    String ret = "";
	    try {
	      ret = in.readLine();
	    }
	    catch (IOException e) {
	    	System.err.println ("Error in reading input:\n");
	    	return null;
	    }
		return ret;
	} 
}

public class Prompt{

	public static void main(String [] argvs) {
		try{ 
			TestSpatialCreateInsertStmt cr = new TestSpatialCreateInsertStmt();
			cr.createDB();
			PromptDriver prompt = new PromptDriver();
			prompt.run();	
			System.out.print ("\n" + "..." + " Finished ");
		}
		catch (Exception e) {
			e.printStackTrace();
			System.err.println ("Error encountered while loading prompt:\n");
			Runtime.getRuntime().exit(1);
		}
	}
}
