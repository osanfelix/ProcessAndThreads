/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProcessAndThreads;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Oscar
 */
public class ProcessTest
{	
	// Pàg. 26
	public static void ExecuteCommand(String comanda)
	{
		Runtime r = Runtime.getRuntime();
		java.lang.Process p = null;
		
		try
		{
			p = r.exec(comanda);
			// Print output process
			println(p.getInputStream());
			
			// Wait until 'r' finishes.
			System.out.println("Exit value: " + p.waitFor());
			
		} catch (IOException ex)
		{
			System.err.println("Error en " + comanda);
			ex.printStackTrace();
		}
		catch(InterruptedException ex)		// waitFor
		{
			ex.printStackTrace();
		}
		
		try {
			InputStream er = p.getErrorStream();		
			println(er);
		}
		catch (IOException ex) {
			Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	
	public static void ExecuteProcess(String comanda, String... args)
	{
			// La clase 'PorcessBuilder' se utiliza para crear procesos con su método 'start()'
			// Ex: Process p = ProcessBuilder("CMD", "/C DIR").start();
			ProcessBuilder pb;
			
			// insert in array command and arguments
			String[] commandAndArgs = new String[args.length + 1];
			commandAndArgs[0] = comanda;
			for(int i = 1; i < args.length + 1; i++)	commandAndArgs[i] = args[i-1];
			
			// Create ProcessBuilder
			pb = new ProcessBuilder(commandAndArgs);
			
			// Get environment variables
			Map<String, String> environment = pb.environment();
			System.out.println("Variables de entorno");
			System.out.println(environment);
			
			// Get command information
			List<String> info = pb.command();
			System.out.println("Argumentos comando:");
			for(String item : info)	System.out.println(item);
			
		try
		{
			Process p = pb.start();
			println(p.getInputStream());
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
	}
	
	public static void ExecuteProcessOutputToFile(String outputFile, 
			String outputError, String comanda, String... args)
	{
		// insert in array command and arguments
		String[] commandAndArgs = new String[args.length + 1];
		commandAndArgs[0] = comanda;
		for(int i = 1; i < args.length + 1; i++)	commandAndArgs[i] = args[i-1];
			
		ProcessBuilder pb = new ProcessBuilder(commandAndArgs);
		
		File fOut = new File(outputFile);
		File fError = new File(outputError);
		
		// Redirect output
		pb.redirectOutput(fOut);
		pb.redirectError(fError);
		
		try
		{
			Process p = pb.start();
			println(p.getInputStream());
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
	}

	static void println(InputStream is) throws IOException
	{
		if(is != null)
		{
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line;
			while((line = br.readLine()) != null)
			{
				System.out.println(line);
			}
		}
	}
}
