

/* Copyright 1999-2004 Carnegie Mellon University. See the file "license.terms" for information on usage */

package vcm_main;

import vcm_main.VoiceRecognizer;
import vcm_main.VoiceRecognizer.VR_WorkMode;

// This application uses the Sphinx-4 endpointer which automatically segments incoming audio into utterances and silences
public class vcq_main 
{
	static Runnable vr;
	public static void main(String[] args) 
    {  
        	System.out.println("Version 1.5.22");
        	
        	VR_WorkMode workMode = VR_WorkMode.PRINT;
        	String currentWorkingDir = "";
        	
        	// Check how many arguments were passed in
        	if(args.length == 0)
            {
            	currentWorkingDir = "Config\\VCQ.config.xml";
            	workMode = VR_WorkMode.PRINT;
            }
            else if(args.length == 1)
            {
            	currentWorkingDir = args[0] + "\\Config\\VCQ.config.xml";
            	workMode = VR_WorkMode.PULL;
            } 
            else
            {
                System.out.println("Proper Usage is: java program filename [working dir]");
                System.exit(0);
            }
                  
            vr = new VoiceRecognizer(workMode);
            ((VoiceRecognizer)vr).Init(currentWorkingDir);          
            Thread thread = new Thread(vr);
            thread.start();            
    }
	
	public int GetLastCommand()
	{
		return ((VoiceRecognizer)vr).GetLastVoiceCommand();
	}
}
