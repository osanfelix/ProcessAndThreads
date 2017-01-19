/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProcessAndThreads;

import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Oscar
 */


public class MainApp
{
	public static void main(String[] args) throws InterruptedException{
		
		// Ejecutar comandos mediante clase "Runtime":
		// Prova "notepad", "cmd /c dir", "comanda que no existeix" 
		ProcessTest.ExecuteCommand("cmd /c dir");
		
		// Ejecutar comandos mediante clase "ProcessBuilder":
		ProcessTest.ExecuteProcess("cmd", "/c", "dir");
		//~ProcessTest.ExecuteProcessOutputToFile("C:\\Users\\Oscar\\Desktop\\out.txt","C:\\Users\\Oscar\\Desktop\\err.txt", "cmd", "/c", "dir");
		
		/*
		try
		{
			// Schedule generic
			Schedule.testThreadPoolExecutor();
			
			// Timed Schedule
			Schedule.testScheduledThreadPoolExecutor();
		}
		catch (InterruptedException ex)
		{
			ex.printStackTrace();
		}
		catch (ExecutionException ex)
		{
			ex.printStackTrace();
		}
		
		// Fork - Join example
		ForkJoinExampleFindMaxShort.test();
		*/
		
		
		// Thread example
		/*
		TestThread();
		TestRunnable();
		*/
	}
	
	public static void TestThread()
	{
		// Test Threads
		ThreadClass t1 = new ThreadClass("Fil 1");
		ThreadClass t2 = new ThreadClass("Fil 2");
		
		t1.start();
		t2.start();

		try {
			t1.join();
			t2.join();
		}
		catch (InterruptedException ex)
		{
				System.out.println("Interrumpit");
		}
		
		System.out.println("Fi de TestThread() ");
	}
	
	public static void TestRunnable()
	{
		RunnableClass r1 = new RunnableClass("Run 1");
		RunnableClass r2 = new RunnableClass("Run 2");
		
		// Los Runnables por si solos no arrancan, necesitan de la 
		// clase Thread para utilizarlos como tal 
		Thread t1 = new Thread(r1);
		Thread t2 = new Thread(r2);
		Thread t3 = new Thread(r1);		// Se puede lanzar otro Thread con el
										// mismo objeto
		
		t1.start();
		t2.start();
		t3.start();

		try 
		{
			t1.join();
			t2.join();
			t3.join();
		}
		catch (InterruptedException ex)
		{
				System.out.println("Interrumpit");
		}
		
		System.out.println("Fi de TestRunnable()");
	}
}
