

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
	public vcq_main(){}
	
	static long m_Localtimer;
	static void tik()
	{
		m_Localtimer = System.nanoTime();
	}
	static long tak()
	{
		return (System.nanoTime() - m_Localtimer)/100000;  //divide by 1000000 to get milliseconds. 
	}
	public static boolean HandleIncomingCommand(String inString)
	{	
		String trimString = inString.trim(); 			// leading and trailing whitespace omitted		
		if (trimString.isEmpty()){ return false; } 		// do nothing if trimString is empty !	
		String[] wordList = trimString.split("\\s+");	// split to words
		if(wordList.length != 2){ return false; }		// do nothing if not exactly 2 words
	
		if(wordList[0].equals("fly"))
		{
			if(wordList[1].equals("up")){
				// Handle fly up command
				return true;
			}
			else if(wordList[1].equals("down")){
				// Handle Fly Down command
				return true;
			}
		}
		return false;
	}
	public static void printGrammar()
	{
		System.out.println("Grammar:    ( fly ) ( up | down | forward | backward ) " + 
						  "\n\t OR ( turn ) ( left | right ) " + 
						  "\n\t OR ( fire ) ( missile | bullets )");
	}
	public static void Go2Sleep(long sleepTime_millsec )
	{
			try	{
				Thread.sleep(sleepTime_millsec);}
			catch (InterruptedException e){
				e.printStackTrace();}	
	}
	public static boolean StartRecording(Microphone mic)
	{
		System.out.print("Opening mic in order to start recording - ");
		if(mic.startRecording())
		{
			System.out.println("Recording started!");
			return true;
		}
		else
		{
			System.out.println("Failed!");
			return false;
		}
			
	}
	public static void StopRecording(Microphone mic)
	{
		System.out.print("closing mic in order to stop recording - ");
		mic.stopRecording();
		System.out.println("Recording stoped!");
	}
	
	public static void main(String[] args) 
    {
        try 
        {   
        	System.out.println("Version 1.4.46");
        	
        	// Check how many arguments were passed in
        	String CurrentWorkingDir = "";
            if(args.length == 0)
            {
            	CurrentWorkingDir = "Config\\VCQ.config.xml"; 
            }
            else if(args.length == 1)
            {
            	CurrentWorkingDir = args[0] + "\\Config\\VCQ.config.xml";
            } 
            else
            {
                System.out.println("Proper Usage is: java program filename [working dir]");
                System.exit(0);
            }       	
        	
            // Initialization         	
            URL url = new File(CurrentWorkingDir).toURI().toURL();           
            ConfigurationManager cm = new ConfigurationManager(url);			System.out.println("ConfigurationManager cm has initialized");
            Recognizer recognizer = (Recognizer) cm.lookup("recognizer");   	System.out.println("Recognizer recognizer has initialized");         
	        Microphone microphone = (Microphone) cm.lookup("microphone");   	System.out.println("Microphone microphone has initialized");
            recognizer.allocate();	         									System.out.println("recognizer necessary resources allocated");
				
			/* the microphone will keep recording until the program exits */
			if (!StartRecording(microphone)){
				return;
			}
			
			printGrammar();
			
			while (true) 
			{
				System.out.println("Waiting for a voice command");
				microphone.clear(); // Clears all cached audio data
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
