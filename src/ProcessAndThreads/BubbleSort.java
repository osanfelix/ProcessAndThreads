
// Original Bubble sort implememntation (int)
// Max complexity O(n^2).

// Activity: Two threads for ordering an array. One of them starts from the
// beginning and the other from the end. The problem is arriving to the middle, so
// left thread will be in charge to do the central swaps (one for even
// arrays, 2 for odd ​array)

package ProcessAndThreads;

class ArrayToSort
{
	public int[] arr;
	int len;
	
	// Threading vars
	static boolean finish_middle_forward = false;
	static boolean finish_middle_reverse = false;
	static boolean finish_forward = false;
	static boolean finish_reverse = false;

	public void resetMiddleForward()
	{
		finish_middle_forward = false;
	}
	
	public void resetMiddleReverse()
	{
		finish_middle_reverse = false;
	}
	
	public void resetFinalForward()
	{
		finish_forward = false;
	}
	
	public void resetFinalReverse()
	{
		finish_reverse = false;
	}
	
	public synchronized void endForward()
	{
		notify();	// Necessary when reverse thread needs more iterations than forward one
		finish_forward = true;
		finish_middle_forward = true;
	}
	
	public synchronized void endReverse()
	{
		notify();	// Necessary when forward thread needs more iterations than reverse one
		finish_reverse = true;
		finish_middle_reverse = true;
	}
	
	public ArrayToSort(int[] arr)
	{
		this.arr = arr;
		len = this.arr.length;
	}
	
	public int getValue(int index)
	{
		return arr[index];
	}
	
	public int getLength()
	{
		return len;
	}
	
	public synchronized void enterCriticalMiddleZone(boolean forward)throws InterruptedException
	{
		if(forward)
		{
			if (!finish_middle_reverse)
				wait();	
		}
		else
		{
			finish_middle_reverse = true;
			notify();
			if (!finish_middle_forward)		// Enter here if forward thread finished totally.
				wait();	// Always enter...
		}
	}
	
	public synchronized void enterCriticalFinalZone(boolean forward)throws InterruptedException
	{
		if(forward)
		{
			finish_forward = true;
			if (!finish_reverse)	wait();
			else					notify();
		}
		else
		{
			finish_reverse = true;
			if (!finish_forward)	wait();	
			else					notify();
		}
	}
	
	public synchronized void exitCriticalZone() throws InterruptedException	// only forward
	{
		finish_middle_forward = true;
		notify();
	}
	
	public void swapPair(int i, int j)	// Swap element 'i' and 'j'
	{
		// swap
		int temp = arr[i];
		arr[i] = arr[j];
		arr[j] = temp;
	}
	
	public void print()
	{
		System.out.print("\n[");
		for (int i = 0; i < arr.length - 1; ++i)
		{
			System.out.print(arr[i] + ",");
		}
		System.out.print(arr[arr.length - 1]);
		System.out.print("]\n");
	}
}

public class BubbleSort extends Thread
{
	enum directionType {FORWARD, REVERSE};	// FORWARD ->     <- REVERSE
	
	// INPUT
	ArrayToSort array = null;
	
	directionType dir;
	
	// TEMP VARS
	int iterations = 0;
	int swapes = 0;
	
		
	void setArray(ArrayToSort array)
	{
		this.array = array;
	}
	
	// Constructor
	BubbleSort(ArrayToSort array, directionType dir)
	{
		super(dir.toString());
		this.array = array;
		this.dir = dir;
	}
	
	void forwardSort() throws InterruptedException
    {
		iterations = 0; swapes = 0;
		
		// Sorting ...
		int len = array.getLength();
		
		int init = 0;
		int end = len -1;
		boolean swap;
		do
		{
			swap = false;
			
			for(int i = init; i < end; ++i)
			{
				// CASO PAR
				// Stop if get to the half
				if(i == len/2 -1)
					array.enterCriticalMiddleZone(true);		// on enter, wait()
				
				if( array.getValue(i) > array.getValue(i+1) )
				{
					// swap
					array.swapPair(i, i+1);
					swap = true;
					++swapes;
				}
				
				if(i == len/2 -1)
				{
					array.exitCriticalZone();		// on exit, notify()
					array.resetMiddleForward();
				}
//				sleep(1);
			}
			
			if(init < end)	array.enterCriticalFinalZone(true);
			array.resetFinalForward();
			
			--end;
			++init;		// for each pass, last number is sortered [,,max->,,]
			
			++iterations;
			
		}while(swap && init < end);
		
		// Thread ends
		array.endForward();
		
		System.out.println("Iter forward: " + (iterations));
		System.out.println("Swapes forward: " + (swapes ));
	}
	
	void reverseSort() throws InterruptedException
    {
		iterations = 0; swapes = 0;
		
		// Sorting ...
		int len = array.getLength();
		
		int init = len - 1;
		int end = 0;
		
		boolean swap;
		do
		{
			swap = false;
			
			for(int i = init; i > end; --i)
			{				// Stop if get to the half
				if(i == len/2)
				{
 					array.enterCriticalMiddleZone(false);		// on enter, wait()
					array.resetMiddleReverse();
				}
				
				else if( array.arr[i] < array.arr[i - 1] )
				{
					array.swapPair(i, i - 1);
					swap = true;
					++swapes;
				}
//				sleep(1);
			}
			array.enterCriticalFinalZone(false);
			array.resetFinalReverse();

			--init;
			++end;
			
			++iterations;
		}while(swap && init > end);
		
		array.endReverse();
		
		System.out.println("Iter reverse: " + (iterations ));
		System.out.println("Swapes reverse: " + (swapes ));
	}
	
	
	@Override
	public void run()
	{
		// Divide arr in two parts
		
//		if(array.getLength() %2 ==0) {	// Even. YOU CAN DELETE THIS CONDITION
										// THIS ALSO WORKS WITH ODD ARRAYS	
		try {
			if(dir == directionType.FORWARD)
				forwardSort();
			else if(dir == directionType.REVERSE)
				reverseSort();
		} catch (InterruptedException ex) {
			System.err.println("Thread " + dir + " interromput: " + ex);
		}
//	}
	}
	
	private static int [] createArray(int ARRAY_SIZE)
	{
        int[] ret = new int[ARRAY_SIZE];
        for(int i=0; i<ARRAY_SIZE; i++){
            ret[i] = (int) (100*(Math.random()*2 - 1));
//			  ret[i] = ARRAY_SIZE - i - 1;
        }
		
        return ret;
    }
	
	public static void test()
	{
//		ArrayToSort arrayObj = new ArrayToSort(new int[]{2,9,4,8,5,3});
		ArrayToSort arrayObj = new ArrayToSort(createArray(60000));
		
		System.out.println("Original Array:");
		arrayObj.print();
		
		BubbleSort forwardThread = new BubbleSort(arrayObj, directionType.FORWARD);
		BubbleSort reverseThread = new BubbleSort(arrayObj, directionType.REVERSE);

		forwardThread.setPriority(MAX_PRIORITY);
		reverseThread.setPriority(MAX_PRIORITY);
				
		forwardThread.start();
		reverseThread.start();
		
		try
		{
			forwardThread.join();
			reverseThread.join();
			arrayObj.print();
			
		} catch (InterruptedException ex) {
			System.err.println("Thread/s interromput/s: " + ex);
		}
	}
}
