package ProcessAndThreads;
// Modificadors:
// 'synchronized'  => Per mètodes i blocs

import static java.lang.Thread.sleep;



// 'volatile' per membres d'una clase.


public class ThreadSynchronicityExamples
{
	
	static class objeto
	{
		public volatile boolean lock = true;
		
		public synchronized int aDormir() throws InterruptedException
		{
//			synchronized(this)
//			{
				System.out.println("A DORMIR!!");
//				wait();
				while(lock)
				{
					
				}
				System.out.println("LIBRE!!!");
				return 0;
//			}
		}
		
		public synchronized int aJugar()
		{
//			synchronized(this)
//			{
				System.out.println("YEAHH");
				lock = false;
//				notify();
				return 0;
//			}
		}
	}
	
	public static void lockerExample()
	{
		objeto obj = new objeto();
		
		Thread t1 = new Thread( () -> {
			try {
				obj.aDormir();
//				sleep(500);
			} catch (InterruptedException ex) {
				System.out.println("Interromput");
			}
			
		});
		
		Thread t2 = new Thread( () -> {
			try {
				sleep(500);
				obj.aJugar();
			} catch (InterruptedException ex) {
				System.out.println("Interromput");
			}
		});
		
		t1.start();
		t2.start();
	}
	
	static class CustomCounterZerosThreadFromArray extends Thread
	{
		int order;
		public int singleCount = 0;
		
		public CustomCounterZerosThreadFromArray(int order)
		{
			super("Fil "+ order);
			this.order = order;
		}
		
		@Override
		public void run()
		{
			int start = order * (ARRAY_SIZE / NUMBER_OF_THREADS);
			for(int i = 0;i < ARRAY_SIZE / NUMBER_OF_THREADS; ++i)
			{
				if(arraysWithNumbers[start + i]== 0)
				{
					singleCount++;
					// UNCOMMENT WHAT YOU WANT TO TEST
//					foundedZeros++;								// Attack shared variable without lockers. DISASTER!
//					countZero();								// although synchronized, each thread calls its own method (bya bye synchronicity) DISASTER!
					ThreadSynchronicityExamples.countZero();	// Synchronized static method to count zeros. All threads call the same method. WORKS!
				}
			}
		}
		
		synchronized void countZero()
		{
				foundedZeros++;
		}
	}
	
	static final int ARRAY_SIZE = 10000;
	static final int NUMBER_OF_THREADS= 40;
	static /*volatile*/ int foundedZeros = 0;		// volatile is not necessari
	
	static short[] arraysWithNumbers = createArray();
	
	public static void threadsFoundZerosOnArray()
	{
		CustomCounterZerosThreadFromArray[] threads = new CustomCounterZerosThreadFromArray[NUMBER_OF_THREADS];
		
		// Initialize serveral arrays to count zeros:
		for(int i = 0; i < NUMBER_OF_THREADS; ++i)
		{
			threads[i] = new CustomCounterZerosThreadFromArray(i);
			threads[i].start();
		}
		for(int i = 0; i < NUMBER_OF_THREADS; ++i)
		{
			try {
				threads[i].join();
			}catch (InterruptedException ex)
			{
					System.out.println("Interromput");
			}
		}
		System.out.println("Founded Zeros: " + foundedZeros);
		int totalCount = 0;
		for(int i = 0; i <NUMBER_OF_THREADS;++i)
		{
			totalCount += threads[i].singleCount;
		}
		System.out.println("Founded Zeros separatly: " + totalCount);
		System.out.println("Fi de threadsFoundZerosOnArray() ");
	}
	
	public static synchronized void countZero()
	{
		foundedZeros++;
	}
	
	private static short [] createArray()
	{
        short[] ret = new short[ARRAY_SIZE];
        for(int i=0; i<ARRAY_SIZE; i++){
            ret[i] = (short) (Math.random() + 1);
        }
		// insert zeros
		int zerosCount = 0;
		for(int i = 0; i<ARRAY_SIZE/2;i+=2)
		{
			ret[i] = 0;
			++zerosCount;
		}
		System.out.println("Inserted zeros: " + zerosCount);
        return ret;
    }
}
