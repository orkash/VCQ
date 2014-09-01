package vcm_main;

import edu.cmu.sphinx.frontend.util.Microphone;
import edu.cmu.sphinx.recognizer.Recognizer;
import edu.cmu.sphinx.result.Result;
import edu.cmu.sphinx.util.props.ConfigurationManager;
import edu.cmu.sphinx.util.props.PropertyException;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class VoiceRecognizer implements Runnable
{
	public enum VR_WorkMode
	{
	    PRINT,PULL,PULL_AND_PRINT 
	}
	
	// Public methods
	public VoiceRecognizer(VR_WorkMode workMode)
	{
		m_initialized = false;
		m_workMode = workMode; 
	}
	public void Init(String CurrentWorkingDir)
	{
		try 
        {       	
        	// Initialization         	
            URL url = new File(CurrentWorkingDir).toURI().toURL();           
            ConfigurationManager cm = new ConfigurationManager(url);	System.out.println("ConfigurationManager cm has initialized");
            m_recognizer = (Recognizer) cm.lookup("recognizer");   		System.out.println("Recognizer recognizer has initialized");         
	        m_microphone = (Microphone) cm.lookup("microphone");   		System.out.println("Microphone microphone has initialized");
	        m_recognizer.allocate();	         						System.out.println("recognizer necessary resources allocated");
			
			/* the microphone will keep recording until the program exits */
			if (!StartRecording(m_microphone)){
				return;
			}
			
			printGrammar();
			
			m_initialized = true;
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
	@Override
	public void run() 
	{	
		if(m_initialized)
		{
			while (true) 
			{
				if(VR_WorkMode.PRINT == m_workMode || VR_WorkMode.PULL_AND_PRINT == m_workMode)
				{
					System.out.println("Waiting for a voice command");
				}
				
				m_microphone.clear(); // Clears all cached audio data
				Result result = m_recognizer.recognize(); // return when the end of speech is reached. (endpointer will determine the end of speech)
        
			    if (result != null) 
			    {
			    	String resultText = result.getBestFinalResultNoFiller();
			    	
			    	if(VR_WorkMode.PRINT == m_workMode || VR_WorkMode.PULL_AND_PRINT == m_workMode)
			    	{
			    		System.out.print("\t the system heard you say: \t ");
			    		System.out.println(resultText);
			    	}
					
			    	if(!HandleIncomingCommand(resultText))
					{
						break;
					}
			    } 
			    else 
			    {
			    	System.out.println("I can't hear what you said.\n");
			    }
			}
			StopRecording(m_microphone);
			m_recognizer.deallocate();
			System.out.println("Bye Bye.\n");
		}
		else
		{
			System.out.println("Run Init() first");
		}
	}
	public int GetLastVoiceCommand()
	{
		return m_lastCommand;
	}
	
	// Private methods	
	private boolean StartRecording(Microphone mic)
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
	private void StopRecording(Microphone mic)
	{
		System.out.print("closing mic in order to stop recording - ");
		mic.stopRecording();
		System.out.println("Recording stoped!");
	}
	private void printGrammar()
	{
		System.out.println("Grammar:    ( fly ) ( up | down | forward | backward ) " + 
						  "\n\t OR ( turn ) ( left | right ) " + 
						  "\n\t OR ( fire ) ( missile | bullets )");
	}
	private boolean HandleIncomingCommand(String inString)
	{	
		String trimString = inString.trim(); 			// leading and trailing whitespace omitted		
		if (trimString.isEmpty()){ return true; } 		// do nothing if trimString is empty !	
		String[] wordList = trimString.split("\\s+");	// split to words
		if(wordList.length != 2){ return true; }		// do nothing if not exactly 2 words
	
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
		if(wordList[0].equals("turn") && wordList[1].equals("off"))
		{
			return false;
		}
		return true;
	}
	
	// Private Members
	private Recognizer m_recognizer;
	private Microphone m_microphone; 
	private boolean m_initialized;
	private VR_WorkMode m_workMode;
	private int m_lastCommand; 

}
