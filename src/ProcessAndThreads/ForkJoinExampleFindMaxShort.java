package ProcessAndThreads;

import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ForkJoinPool;

// Tarea a realizar: Buscar el entero más grande de un array:
public class ForkJoinExampleFindMaxShort extends RecursiveTask<Short> 
{
    private static final int ARRAY_MIN_SIZE = 10000000;
	private static int taskCount = 0;
	
    private short[] arr ;
    private int inici, fi;

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
            return getMaxReq();
        }            
    }

	
	
	// Test Methods
    public static void test(){

		taskCount = 1;
        short[] data = createArray(100000000);

        // Mira el número de processadors
        System.out.println("Inici càlcul");
        ForkJoinPool pool = new ForkJoinPool();

        int inici	= 0;
        int fi		= data.length;
        
		// Creamos la tarea
		ForkJoinExampleFindMaxShort tasca = new ForkJoinExampleFindMaxShort(data, inici, fi);
        
        long time = System.currentTimeMillis();
		
        // crida la tasca i espera que es completin
		int result1 = pool.invoke(tasca);
		
        // comença!! 
        int result=  tasca.join();
        
		System.out.println("Temps utilitzat:" +(System.currentTimeMillis()-time));
        
        System.out.println ("Màxim es " + result);
		System.out.println ("La tasca s'ha divisit en : " + taskCount + " subtasques");
    }
    
    
    private static short [] createArray(int size){
        short[] ret = new short[size];
        for(int i=0; i<size; i++){
            ret[i] = (short) (1000 * Math.random());
            if(i==((short)(size*0.9))){
                ret[i]=1005;
            }
        }
        return ret;
    }
}
