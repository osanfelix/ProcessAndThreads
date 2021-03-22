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
	public static void main(String[] args)
	{
		// Get name of the current thread: 'main'
//		System.out.println("Main thread name: " + Thread.currentThread().getName());
		
		// Ejecutar comandos mediante clase "Runtime":
		// Prova "notepad", "cmd /c dir", "comanda que no existeix" 
		ProcessTest.ExecuteCommand("cmd /c dir");
		
		// Ejecutar comandos mediante clase "ProcessBuilder":
//		ProcessTest.ExecuteProcess("cmd", "/c", "dir");
		//~ProcessTest.ExecuteProcessOutputToFile("C:\\Users\\Oscar\\Desktop\\out.txt","C:\\Users\\Oscar\\Desktop\\err.txt", "cmd", "/c", "dir");
		
		

//		TestSchedulePool();
		
		// Fork - Join example
//		ForkJoinExampleFindMaxShort.test();
		// Thread example
//		TestThread();
//		TestRunnable();
		
		
		// Fork - Join example
//		ForkJoinExampleFindMaxShort.test();
		

		
	}
	
    public static void TestSchedulePool()
	{
        try {
			// Executor: single thread
			System.out.println("######### Executant tasca de multiplicació amb executor (single thread) #######");
			ExecutorExamples.testThreadExecutor();
			
			// Executor 3 threads
			System.out.println("######### Executant tasca de multiplicació amb executor (3 fixed thread) #######");
			ExecutorExamples.testThreadPoolExecutor(ExecutorExamples.POOL_TYPE.FIXED3);
			
			// Executor cache thread
			System.out.println("######### Executant tasca de multiplicació amb executor (dynamic num. threads) #######");
			ExecutorExamples.testThreadPoolExecutor(ExecutorExamples.POOL_TYPE.CACHED);
			
			// Schedule generic
			System.out.println("######### Executant tasca runnable (1 segon) amb schedule executor (single) #######");
			ExecutorExamples.testScheduledThreadPoolExecutor(ExecutorExamples.SCHEDULE_TYPE.SINGLE);
			// Schedule delay
			System.out.println("######### Executant tasca runnable (1 segon) amb schedule executor (fixed delay) #######");
			ExecutorExamples.testScheduledThreadPoolExecutor(ExecutorExamples.SCHEDULE_TYPE.DELAY);
			
			// Schedule rate (parece que el tiempo de la tarea se suma al periodo y no debería ser asi!!)
			System.out.println("######### Executant tasca runnable (1 segon) amb schedule executor (fixed rate) #######");
			ExecutorExamples.testScheduledThreadPoolExecutor(ExecutorExamples.SCHEDULE_TYPE.DELAY);

		} catch (InterruptedException | ExecutionException ex) {
			ex.printStackTrace();} 
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
