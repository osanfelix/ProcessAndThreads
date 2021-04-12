package ProcessAndThreads;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;

// Classe ForkJoinPool <== RecursiveTask o RecursiveAction (abstractes)
// Aquesta combinació es fa servir quan es necessiten fils molt lleugers
// per atacar tasques grans que es poden dividir en més petites.
// Al mètode compute() és defineix la tasca en qüestió.

// Executa tasques de tipus ForkJoinTask<V>:
//	- RecursiveTask<V>	(returns a value type V; extends ForkJoinTask<V>)
//	- RecursiveAction   (returns no value; extends ForkJoinTask<Void>)

// Tarea a realizar: Buscar el entero más grande de un array:
public class ForkJoinExampleFindMaxShort extends RecursiveTask<Short> 
{
    private static final int ARRAY_MIN_SIZE = 100000;
	private static int taskCount = 0;
	
    final private short[] arr ;
    final private int inici, fi;

    public ForkJoinExampleFindMaxShort(short[] arr, int inici, int fi) {
            this.arr = arr;
            this.inici = inici;
            this.fi = fi;
    }
	
	// Devuelve el entero más grande de manera secuencial
    private short getMaxSeq(){
        short max = arr[inici];
        for (int i = inici+1; i < fi; i++) {
            if (arr[i] > max) {
                    max = arr[i];
            }
        }
        return max;            
    }

	// Divide la tarea en 2 y las ejecuta esperando el resultado del
	// entero más grande
    private short getMaxReq()
	{
        taskCount++;
		ForkJoinExampleFindMaxShort task1;
        ForkJoinExampleFindMaxShort task2;
        int mig = (inici+fi)/2+1;
        task1 = new ForkJoinExampleFindMaxShort(arr, inici, mig);
        task1.fork();
        task2 = new ForkJoinExampleFindMaxShort(arr, mig, fi);
        task2.fork();
        return (short) Math.max(task1.join(), task2.join());
		
    }
	
	
    
    @Override
    protected Short compute()
	{
        if(fi - inici <= ARRAY_MIN_SIZE){
            return getMaxSeq();
        }else{
//			return (short)ForkJoinTask.invokeAll(getMaxReqList()).stream().mapToInt(ForkJoinTask::join).max().getAsInt();
            return getMaxReq();
        }            
    }
private List<ForkJoinExampleFindMaxShort> getMaxReqList()
	{
        taskCount++;
		ForkJoinExampleFindMaxShort task1;
        ForkJoinExampleFindMaxShort task2;
        int mig = (inici+fi)/2+1;
        task1 = new ForkJoinExampleFindMaxShort(arr, inici, mig);
        task2 = new ForkJoinExampleFindMaxShort(arr, mig, fi);
        
        return new ArrayList<ForkJoinExampleFindMaxShort>(){{add(task1); add(task2);}};
		
    }
	
	// Test Methods
    public static void test(){
		taskCount = 1;
        short[] data = createArray(200000000);
        // Mira el número de processadors
        System.out.println("Inici càlcul");
        ForkJoinPool pool = new ForkJoinPool();

        int inici	= 0;
        int fi		= data.length;
        
		// Creamos la tarea
		ForkJoinExampleFindMaxShort tasca = new ForkJoinExampleFindMaxShort(data, inici, fi);
        
        long time = System.currentTimeMillis();
		
        // crida la tasca i espera que es completin
		int result = pool.invoke(tasca);
		
        
		System.out.println("Temps utilitzat:" +(System.currentTimeMillis()-time));
        
        System.out.println ("Màxim es " + result);
		System.out.println ("La tasca s'ha divisit en : " + taskCount + " subtasques");
    }
    
    
    private static short [] createArray(int size){
        short[] ret = new short[size];
        for(int i=0; i<size; i++){
            ret[i] = (short) (1000 * Math.random());
            if(i==(size*0.9)){
                ret[i]=1005;
            }
        }
        return ret;
    }
}