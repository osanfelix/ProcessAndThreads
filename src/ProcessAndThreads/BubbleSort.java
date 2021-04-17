
// Bubble sort implememntation (int)
// Max complexity O(n^2).

// Activity: Two threads for ordering an array. One of them starts from the
// beginning and the other from the end. The problem is arriving to the middle, so
// left thread will be in charge to do the central swaps (one for even
// arrays, 2 for odd ​array)

package ProcessAndThreads;

import java.util.logging.Level;
import java.util.logging.Logger;

class ArrayToSort
{
	public int[] arr;
	int len;
	
	// Threading vars
	static volatile boolean finish_middle_forward = false;
	static volatile boolean finish_middle_reverse = false;
	static volatile boolean finish_forward = false;
	static volatile boolean finish_reverse = false;

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
	
	public void endForward()
	{
		finish_forward = true;
		finish_middle_forward = true;
	}
	
	public void endReverse()
	{
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
			System.out.println("FORWARD MIDDLE ENTERING...");
			if (!finish_middle_reverse)
			{
				System.out.println("DUERME FORWARD...");wait();
				System.out.println("DESPIERTA FORWARD...");
			}
			
		}
		else
		{
			System.out.println("RESERVE MIDDLE ENTERING...");
			finish_middle_reverse = true;
			notify();
			if (!finish_middle_forward)
			{
				System.out.println("DUERME REVERSE...");wait();	// Siempre entrará
				System.out.println("DESPIERTA REVERSE...");
			}
			else		// ENTRARA AQUI?? NO CREC
			{
				notify();
			}
		}
	}
	
	public synchronized void enterCriticalFinalZone(boolean forward)throws InterruptedException
	{
		if(forward)
		{
			System.out.println("FORWARD FINAL ENTERING...");
			finish_forward = true;
			if (!finish_reverse)
			{
				System.out.println("DUERME FORWARD FINAL...");wait();
			}
			else
			{
				notify();
			}
		}
		else
		{
			System.out.println("REVERSE FINAL ENTERING...");
			finish_reverse = true;
			if (!finish_forward)
			{
				System.out.println("DUERME REVERSE FINAL...");wait();	
			}
			else
			{
				notify();
			}
		}
	}
	
	public synchronized void exitCriticalZone() throws InterruptedException	// only forward
	{
		if (!finish_middle_reverse)
		{
			System.err.println("NEVER ENTERING HERE...");
		}
		finish_middle_forward = true;
		notify();
	}
	
	public void swapPair(int i, int j)	// Swap element 'i' and 'j'
	{
		// swap
		int temp = arr[i];
		arr[i] = arr[j];
		arr[j] = temp;
		
		// Half part of the array: critical place
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
//		System.out.println("Iterations: " + iterations + " (" + swapes + " swapes)");
	}
}

public class BubbleSort extends Thread
{
	enum directionType {FORWARD, REVERSE};	// FORWARD ->     <- REVERSE
	
	// INPUT
	ArrayToSort array = null;
	
	directionType dir;
	
	//  VARS
	int[] originalArray = null;
	int[] sortedArray = null;
	
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
		super();
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
		boolean swap = false;
		do
		{
			swap = false;
			
			for(int i = init; i < end; ++i)
			{
				// CASO PAR
				// Stop if get to the half
				if(i == len/2 -1)
				{
					array.enterCriticalMiddleZone(true);		// on enter, wait()
				}
				
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
				}
//				sleep(1);
			}
			array.resetMiddleForward();
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
			{
				// CASO PAR
				// Stop if get to the half
				if(i == len/2)
				{
 					array.enterCriticalMiddleZone(false);		// on enter, wait()
				}
				
				/*else*/ if( array.arr[i] < array.arr[i - 1] )
				{
					// swap
					array.swapPair(i, i - 1);
					swap = true;
					++swapes;
				}
//				sleep(1);
			}
			array.resetMiddleReverse();
			if(init > end)
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
		
		if(array.getLength() %2 ==0)	// Even
		{
			try {
				if(dir == directionType.FORWARD)
					forwardSort();
				else if(dir == directionType.REVERSE)
					reverseSort();
			} catch (InterruptedException ex) {
				Logger.getLogger(BubbleSort.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}
		
	public void printSortedArray()	// DELETE
	{
		System.out.print("\n[");
		for (int i = 0; i < sortedArray.length - 1; ++i)
		{
			System.out.print(sortedArray[i] + ",");
		}
		System.out.print(sortedArray[sortedArray.length - 1]);
		System.out.print("]\n");
		System.out.println("Iterations: " + iterations + " (" + swapes + " swapes)");
	}
	
	private static int [] createArray(int ARRAY_SIZE)
	{
        int[] ret = new int[ARRAY_SIZE];
        for(int i=0; i<ARRAY_SIZE; i++){
//            ret[i] = (int) (Math.random() + 1);
			  ret[i] = ARRAY_SIZE - i - 1;
        }
		
        return ret;
    }
	
	public static void test()
	{
//		ArrayToSort arrayObj = new ArrayToSort(new int[]{20,19,18,17,16,15,14,13,12,11,10,9,8,7,6,5,4,3,2,1,0});
		ArrayToSort arrayObj = new ArrayToSort(createArray(50000));
//		ArrayToSort arrayObj = new ArrayToSort(new int[]{6,5,4,3,2,1});
		BubbleSort forwardThread = new BubbleSort(arrayObj, directionType.FORWARD);
		BubbleSort reverseThread = new BubbleSort(arrayObj, directionType.REVERSE);
		
		
		
		
		reverseThread.start();
		forwardThread.start();
		
		
		try
		{
			forwardThread.join();
			reverseThread.join();
			arrayObj.print();
			
//		reverseThread.start();
		
//		bubble.printSortedArray();
		
//		bubble.bubbleSortRev(new int[]{45,88,2,5,12,5,3,7,8,9,2,22,34,8,2,5,20,56,5,5,5,5,5,2});
//		bubble.printSortedArray();
//		
//		bubble.bubbleSortRev(new int[]{5,3,1,9,8,2,4,7});
//		bubble.printSortedArray();
		} catch (InterruptedException ex) {
			Logger.getLogger(BubbleSort.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
