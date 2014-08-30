

/* Copyright 1999-2004 Carnegie Mellon University. See the file "license.terms" for information on usage */

package vcm_main;

import vcm_main.VoiceRecognizer;

// This application uses the Sphinx-4 endpointer which automatically segments incoming audio into utterances and silences
public class vcq_main 
{
	public static void Go2Sleep(long sleepTime_millsec)
	{
			try	{
				Thread.sleep(sleepTime_millsec);}
			catch (InterruptedException e){
				e.printStackTrace();}	
	}

	public static void main(String[] args) 
    {  
        	System.out.println("Version 1.4.51");
        	
        	// Check how many arguments were passed in
        	String currentWorkingDir = "";
            if(args.length == 0)
            {
            	currentWorkingDir = "Config\\VCQ.config.xml"; 
            }
            else if(args.length == 1)
            {
            	currentWorkingDir = args[0] + "\\Config\\VCQ.config.xml";
            } 
            else
            {
                System.out.println("Proper Usage is: java program filename [working dir]");
                System.exit(0);
            }
            
            
            Runnable vr = new VoiceRecognizer();
            ((VoiceRecognizer)vr).Init(currentWorkingDir);          
            Thread thread = new Thread(vr);
            thread.start();            
    }
}
