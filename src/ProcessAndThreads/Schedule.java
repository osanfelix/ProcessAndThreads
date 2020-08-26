package ProcessAndThreads;

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

// Task object
class Multiplicacio implements Callable<Integer>
{
    private final int operador1;
    private final int operador2;

    public Multiplicacio(int operador1, int operador2) {
        this.operador1 = operador1;
        this.operador2 = operador2;
		
    }

    @Override
    public Integer call() throws Exception {
		System.out.println(Thread.currentThread().getName());
        return operador1 * operador2;
    }
}

// Fil Runnable
class ExecutaFil implements Runnable
{
    @Override
    public void run() {
        Calendar calendario = new GregorianCalendar();
        System.out.println("Hora execució tasca: "+ calendario.get(Calendar.HOUR_OF_DAY) + ":" + calendario.get(Calendar.MINUTE) + ":" + calendario.get(Calendar.SECOND));
        System.out.println("Tasca en execució");
        System.out.println("Execució acabada");
    }
}

public class Schedule
{
	public static void testThreadExecutor() throws InterruptedException, ExecutionException
    {
		// One task => One thread example
		ExecutorService  threadExecutor = (ExecutorService) Executors.newSingleThreadExecutor();
		Future<Integer> resultat = threadExecutor.submit( new Multiplicacio(
				(int) (Math.random() * 10), (int) (Math.random() * 10)));
		if(resultat.isDone())			
			System.out.println("Resultat tasca:" + resultat.get());
		threadExecutor.shutdown();
	}
	
	
    public static void testThreadPoolExecutor() throws InterruptedException, ExecutionException
    {
        // Task Schedule: 3 fils fixos
        ThreadPoolExecutor threadExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(3);
        // Task Schedule: Fils dinàmics (els crea a mesura que són necessaris)
//        ThreadPoolExecutor threadExecutor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        // Task Schedule: Només un fil
//        ExecutorService  threadExecutor = (ExecutorService) Executors.newSingleThreadExecutor();
        
        // Task array
        List<Multiplicacio> llistaTasques = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Multiplicacio calcula = new Multiplicacio((int) (Math.random() * 10), (int) (Math.random() * 10));
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
		
		threadExecutor.shutdown();
        
		for (int i = 0; i < llistaResultats.size(); i++) {
            Future<Integer> resultat = llistaResultats.get(i);
            try {
                System.out.println("Resultat tasca " + i + " és:" + resultat.get());
            } catch (InterruptedException | ExecutionException e) {
            }
        }
    }
    
    
    public static void testScheduledThreadPoolExecutor() throws InterruptedException, ExecutionException
    {
       // Mostrem hora actual abans d'execució
       Calendar calendario = new GregorianCalendar();
       System.out.println("Inici: "+ calendario.get(Calendar.HOUR_OF_DAY) + ":" + calendario.get(Calendar.MINUTE) + ":" + calendario.get(Calendar.SECOND));
            
        // Crea un pool de 2 fils
		ScheduledThreadPoolExecutor schExService = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(2);
        // Crea objecte Runnable.
        final Runnable ob = new ExecutaFil();
        // Programa Fil, s'inicia als 2 segons i després es va executant cada 3 segons
        schExService.scheduleWithFixedDelay(ob, 2, 3, TimeUnit.SECONDS);
        // Espera per acabar 10 segons
        schExService.awaitTermination(10, TimeUnit.SECONDS);
        // shutdown .
        schExService.shutdownNow();
        System.out.println("Completat");
    }
}
