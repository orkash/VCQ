

/* Copyright 1999-2004 Carnegie Mellon University. See the file "license.terms" for information on usage */

package vcm_main;

import edu.cmu.sphinx.frontend.util.Microphone;
import edu.cmu.sphinx.recognizer.Recognizer;
import edu.cmu.sphinx.result.Result;
import edu.cmu.sphinx.util.props.ConfigurationManager;
import edu.cmu.sphinx.util.props.PropertyException;

import java.io.File;
import java.io.IOException;
import java.net.URL;

// This application uses the Sphinx-4 endpointer which automatically segments incoming audio into utterances and silences
public class vcq_main 
{
	public static void HandleIncomingCommand(String inString)
	{
		String trimString = inString.trim(); 
		if (trimString.isEmpty()){
			System.out.println("AnalyzeCommand() - inString is empty");
			return;
		}
		
		String[] wordList = trimString.split("\\s+");
		if(wordList.length != 2){
			System.out.println("\tAnalyzeCommand() - wordList.length != 2");
			return;
		}
	
		if(wordList[0].equals("fly")){
			if(wordList[1].equals("up")){
				System.out.println("\tKfir's Quadcopter flying high");
				return;
			}
			else if(wordList[1].equals("down")){
				System.out.println("\tKfir's Quadcopter flying low");
				return;
			}
			else if(wordList[1].equals("forward")){
				System.out.println("\tKfir's Quadcopter goes toward target");
				return;
			}
			else if(wordList[1].equals("backward")){
				System.out.println("\tKfir's Quadcopter retreats");
				return;
			}
		}
		else if(wordList[0].equals("turn")){
			if(wordList[1].equals("left")){
				System.out.println("\tKfir's Quadcopter locked on the target to the left");
				return;
			}
			else if(wordList[1].equals("right")){
				System.out.println("\tKfir's Quadcopter locked on the target to the right");
				return;
			}
		}
		else if (wordList[0].equals("fire")){
			if(wordList[1].equals("missile")){
				System.out.println("\tTarget destroyed");
				return;
			}
			else if(wordList[1].equals("bullets")){
				System.out.println("\tTarget neutralized");
				return;
			}
		}
		
		System.out.println("\tCan't execute command: " + inString);	
	}
	public static void printGrammar()
	{
		System.out.println("Say:\t ( fly ) ( up | down | forward | backward ) " + 
						  "\n\t OR ( turn ) ( left | right ) " + 
						  "\n\t OR ( fire ) ( missile | bullets )");
	}
	public static void Go2Sleep(long sleepTime_millsec )
	{
			try 
			{
				Thread.sleep(sleepTime_millsec);
			}
			catch (InterruptedException e)	
			{
				e.printStackTrace();
			}	
	}
	
    public static void main(String[] args) 
    {
        try 
        {            
            // Initialization 
        	System.out.println("Loading...");
            URL url = new File("Config\\VCQ.config.xml").toURI().toURL();           
            ConfigurationManager cm = new ConfigurationManager(url);			System.out.println("ConfigurationManager cm has initialized");
            Recognizer recognizer = (Recognizer) cm.lookup("recognizer");   	System.out.println("Recognizer recognizer has initialized");         
	        Microphone microphone = (Microphone) cm.lookup("microphone");   	System.out.println("Microphone microphone has initialized");
            recognizer.allocate();	         									System.out.println("recognizer necessary resources allocated");         
            printGrammar();
				
			/* the microphone will keep recording until the program exits */
			if (!microphone.startRecording())
			{
				System.err.println("microphone.startRecording() failed!");
				return;
			}
			
			while (true) 
			{
				System.out.println("Waiting for a voice command");   
				Result result = recognizer.recognize(); // return when the end of speech is reached. (endpointer will determine the end of speech)
        
			    if (result != null) 
			    {
			    	System.out.print("\t the system heard you say: \t ");
					String resultText = result.getBestFinalResultNoFiller();
					System.out.println(resultText);
					//HandleIncomingCommand(resultText);
			    } 
			    else 
			    {
			    	System.out.println("I can't hear what you said.\n");
			    }
			}
        } 
        catch (IOException e){
            System.err.println("Problem when loading HelloWorld: " + e);
            e.printStackTrace();} 
        catch (PropertyException e){
            System.err.println("Problem configuring HelloWorld: " + e);
            e.printStackTrace();} 
        catch (InstantiationException e){
            System.err.println("Problem creating HelloWorld: " + e);
            e.printStackTrace();}
    }
}
