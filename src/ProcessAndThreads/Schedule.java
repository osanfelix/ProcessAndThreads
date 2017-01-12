package ProcessAndThreads;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

// Task object
class Multiplicacio implements Callable<Integer>
{
    private int operador1;
    private int operador2;

    public Multiplicacio(int operador1, int operador2) {
        this.operador1 = operador1;
        this.operador2 = operador2;
    }

    @Override
    public Integer call() throws Exception {
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
    public static void testThreadPoolExecutor() throws InterruptedException, ExecutionException
    {
        // Task Schedule
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(3);
        
        // Task array
        List<Multiplicacio> llistaTasques = new ArrayList<Multiplicacio>();
        for (int i = 0; i < 10; i++) {
            Multiplicacio calcula = new Multiplicacio((int) (Math.random() * 10), (int) (Math.random() * 10));
            llistaTasques.add(calcula);
        }
        
        List<Future<Integer>> llistaResultats;
        llistaResultats = executor.invokeAll(llistaTasques);

        executor.shutdown();
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
       //mostrem hora actual abans d'execució
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
