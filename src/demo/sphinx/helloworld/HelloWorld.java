/*
 * Copyright 1999-2004 Carnegie Mellon University.
 * Portions Copyright 2004 Sun Microsystems, Inc.
 * Portions Copyright 2004 Mitsubishi Electric Research Laboratories.
 * All Rights Reserved.  Use is subject to license terms.
 *
 * See the file "license.terms" for information on usage and
 * redistribution of this file, and for a DISCLAIMER OF ALL
 * WARRANTIES.
 *
 */

package demo.sphinx.helloworld;

import edu.cmu.sphinx.frontend.util.Microphone;
import edu.cmu.sphinx.recognizer.Recognizer;
import edu.cmu.sphinx.result.Result;
import edu.cmu.sphinx.util.props.ConfigurationManager;
import edu.cmu.sphinx.util.props.PropertyException;

import java.io.File;
import java.io.IOException;
import java.net.URL;


/**
 * A simple HelloWorld demo showing a simple speech application 
 * built using Sphinx-4. This application uses the Sphinx-4 endpointer,
 * which automatically segments incoming audio into utterances and silences.
 */
public class HelloWorld {

	public static void AnalyzeCommand(String inString)
	{
		System.out.println("HelloWorld::AnalyzeCommand() - input: "+ inString);
		
		String trimString = inString.trim(); 
		if (trimString.isEmpty()){
			//System.out.println("HelloWorld::AnalyzeCommand() - Empty String");
			return;
		}
		
		String[] wordList = trimString.split("\\s+");
		if(wordList.length != 3){
			System.out.println("\tHelloWorld::AnalyzeCommand() - wordList.length != 3");
			return;
		}
		
		if(wordList[1].equals("shoot")){
			if( wordList[2].equals("philip") || wordList[2].equals("rita") || wordList[2].equals("john") ){
				System.out.println("\t"+wordList[0] + "'s Quadcopter shot " + wordList[2]);
			}
			return;
		}
		else if(wordList[1].equals("go")){
			if( wordList[2].equals("up") ){
				System.out.println("\t"+wordList[0] + "'s Quadcopter flying high");	
			}
			if( wordList[2].equals("down") ){
				System.out.println("\t"+wordList[0] + "'s Quadcopter flying low");	
			}		
			return;
		}
		else{
			System.out.println("\t"+"HelloWorld::AnalyzeCommand() - wordList[1] == " + wordList[1]);
			return;
		}
	}
	
    /**
     * Main method for running the HelloWorld demo.
     */
    public static void main(String[] args) {
        try {
            URL url;
            if (args.length > 0) {
            	System.out.println("HelloWorld::main() - Using input url");
                url = new File(args[0]).toURI().toURL();
            } 
            else {
            	System.out.println("HelloWorld::main() - using local file");
                //url = HelloWorld.class.getResource("helloworld.config.xml");
                url = new File("Config\\helloworld.config.xml").toURI().toURL();
            }

            System.out.println("Loading...");

            ConfigurationManager cm = new ConfigurationManager(url); 
            System.out.println("HelloWorld::main() - ConfigurationManager cm has initialized");

            Recognizer recognizer = (Recognizer) cm.lookup("recognizer");
	        System.out.println("HelloWorld::main() - Recognizer recognizer has initialized");
            
	        Microphone microphone = (Microphone) cm.lookup("microphone");
	        System.out.println("HelloWorld::main() - Microphone microphone has initialized");


            /* allocate the resource necessary for the recognizer */
            recognizer.allocate();
            System.out.println("HelloWorld::main() - recognizer.allocate() completed");

            /* the microphone will keep recording until the program exits */
	    if (microphone.startRecording()) {

		System.out.println
		    ("Say: ( Rita | Philip | John ) Shoot ( Rita | Philip | John ) " +
                     "\n OR ( Rita | Philip | John ) Go ( Up | Down )");

		while (true) {
		    //System.out.println("Start speaking.\n");

                    /*
                     * This method will return when the end of speech
                     * is reached. Note that the endpointer will determine
                     * the end of speech.
                     */ 
		    Result result = recognizer.recognize();
		    
		    if (result != null) {
			String resultText = result.getBestFinalResultNoFiller();
			//System.out.println("You said: " + resultText + "\n");
			AnalyzeCommand(resultText);
		    } else {
			System.out.println("I can't hear what you said.\n");
		    }
		}
	    } else {
		System.out.println("Cannot start microphone.");
		recognizer.deallocate();
		System.exit(1);
	    }
        } catch (IOException e) {
            System.err.println("Problem when loading HelloWorld: " + e);
            e.printStackTrace();
        } catch (PropertyException e) {
            System.err.println("Problem configuring HelloWorld: " + e);
            e.printStackTrace();
        } catch (InstantiationException e) {
            System.err.println("Problem creating HelloWorld: " + e);
            e.printStackTrace();
        }
    }
}
