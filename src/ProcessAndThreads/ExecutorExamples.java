package ProcessAndThreads;

import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

// Callable Task
class MultiplyOp implements Callable<Integer>
{
    private final int operator1;
    private final int operator2;

    public MultiplyOp(int operator1, int operator2) {
        this.operator1 = operator1;
        this.operator2 = operator2;
		
    }

    @Override
    public Integer call() throws Exception {
        return operator1 * operator2;
    }
}

// Runnable Task
class simpleRunnableTask implements Runnable
{
    @Override
    public void run() {
        Calendar currentCalendar = new GregorianCalendar();
        System.out.println("Hora execució tasca: " +
				currentCalendar.get(Calendar.HOUR_OF_DAY) + ":" +
				currentCalendar.get(Calendar.MINUTE) + ":" +
				currentCalendar.get(Calendar.SECOND));
        System.out.println("Tasca en execució");
		try {
			sleep(1000);	// Sleep 1 second.
		} catch (InterruptedException ex) {
			System.err.println("Tasca interrompuda... :" + ex);
		}
		 currentCalendar = new GregorianCalendar();
		 System.out.println("Hora finalització tasca: " +
				currentCalendar.get(Calendar.HOUR_OF_DAY) + ":" +
				currentCalendar.get(Calendar.MINUTE) + ":" +
				currentCalendar.get(Calendar.SECOND));
        System.out.println("Execució acabada");
    }
}

public class ExecutorExamples
{
	public enum POOL_TYPE{FIXED3, CACHED}
	
	public enum SCHEDULE_TYPE{RATE, DELAY, SINGLE}
	
	// Simple Task on one thread. Returns a value (Callable task)
	public static void testThreadExecutor() throws InterruptedException, ExecutionException
    {
		// One task => One thread example
		ExecutorService  threadExecutor = (ExecutorService) Executors.newSingleThreadExecutor();
		Future<Integer> resultat = threadExecutor.submit(new MultiplyOp(
				(int) (Math.random() * 10), (int) (Math.random() * 10)));
		
		// Shutting down the thread...
		threadExecutor.shutdown();
		threadExecutor.awaitTermination(5, TimeUnit.SECONDS);
		if(resultat.isDone())
			System.out.println("Resultat tasca:" + resultat.get());
	}
	
	// Multyples tasks on a thread pool
    public static void testThreadPoolExecutor(POOL_TYPE type) throws InterruptedException, ExecutionException
    {
		ThreadPoolExecutor threadExecutor = null;
		switch(type)
		{
			case FIXED3:	// 3 fixed threads
				threadExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(3);
				break;
			default:		// CACHED => dynamic pool: depending in the tasks more or less threads
				threadExecutor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
				break;
			
		}
               
        // Task array
        List<MultiplyOp> llistaTasques = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            MultiplyOp calcula = new MultiplyOp((int) (Math.random() * 10), (int) (Math.random() * 10));
            llistaTasques.add(calcula);
        }
        
		// All the tasks invoked ==> invokeAll(Collection<Callable>)
		List<Future<Integer>> llistaResultats;
		llistaResultats = threadExecutor.invokeAll(llistaTasques);
		
		// Or execute tasks "one after the one" ==> submit(Callable)
		/*
		List<Future<Integer>> llistaResultats = new ArrayList<Future<Integer>>();
		for (int i = 0; i < llistaTasques.size(); i++) {
			llistaResultats.add(executorFixed.submit(llistaTasques.get(i)));
		}
		*/
		
		// Shutting down the thread...
		threadExecutor.shutdown();
		threadExecutor.awaitTermination(5, TimeUnit.SECONDS);
        
		for (int i = 0; i < llistaResultats.size(); i++) {
            Future<Integer> resultat = llistaResultats.get(i);
            try {
                System.out.println("Resultat tasca " + i + " és:" + resultat.get());
            } catch (InterruptedException | ExecutionException e) {
            }
        }
    }
    
    
    public static void testScheduledThreadPoolExecutor(SCHEDULE_TYPE type) throws InterruptedException, ExecutionException
    {
       // Mostrem hora actual abans d'execució
       Calendar calendario = new GregorianCalendar();
       System.out.println("Inici: "+ calendario.get(Calendar.HOUR_OF_DAY) + ":" + calendario.get(Calendar.MINUTE) + ":" + calendario.get(Calendar.SECOND));
            
        // Crea un pool de 2 fils
		ScheduledThreadPoolExecutor schExService = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(2);
        // Crea objecte Runnable.
        final Runnable ob = new simpleRunnableTask();
        // Programa Fil, s'inicia als 2 segons i després es va executant cada 3 segons
        if(type == SCHEDULE_TYPE.DELAY)		// El temps de la tasca s'acumula al retard final.
											// Comença a contar 3 seg. després de la finalització de la tasca anterior
			schExService.scheduleWithFixedDelay(ob, 2, 3, TimeUnit.SECONDS);
		else if(type == SCHEDULE_TYPE.RATE)	// La tasca s'executa a un ritme fixe (cada 3 seg. indep. del temps de la tasca)
			schExService.scheduleAtFixedRate(ob, 2, 3, TimeUnit.SECONDS);
		else	// 
			schExService.schedule(ob, 2, TimeUnit.SECONDS);
		// Espera per acabar 10 segons
		schExService.awaitTermination(10, TimeUnit.SECONDS);
        // Force Shutdown.
        schExService.shutdownNow();
        System.out.println("Completat");
    }
}
