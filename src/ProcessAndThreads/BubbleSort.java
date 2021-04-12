
// Bubble sort implememntation (int)
// Max complexity O(n^2).

// Activity: Two threads for ordering an array. One of them starts from the
// beginning and the other from the end. The problem is arriving to the middle, so
// left thread will be in charge to do the central swaps (one for even
// arrays, 2 for odd ​array)

package ProcessAndThreads;

public class BubbleSort extends Thread
{
	enum directionType {FORWARD, REVERSE};	// FORWARD ->     <- REVERSE
	
	int[] arr = null;
	int[] originalArray = null;
	int[] sortedArray = null;
	directionType dir;
	int iterations = 0;
	int swapes = 0;
	
	// Threading vars
	static volatile boolean finish_A = false;
	static volatile boolean finish_B = false;
	
	void setArray()
	{
		
	}
	
	BubbleSort(int arr[], directionType dir)
	{
		super();
		this.arr = arr;
		this.dir = dir;
	}
	
	void bubbleSort()
    {
		iterations = 0; swapes = 0;
		// Save unsorted array
		originalArray = new int[arr.length];
		System.arraycopy(arr, 0, originalArray, 0, arr.length);
		
		// Sorting ...
		int len = arr.length;
		boolean swap = false;
		do
		{
			swap = false;
			for(int i = 0; i < len - 1; ++i)
			{
				if( arr[i] > arr[i+1] )
				{
					// swap
					int temp = arr[i];
					arr[i] = arr[i + 1];
					arr[i + 1] = temp;
					swap = true;
					++swapes;
				}
			}
			--len;		// for each pass, last number is sortered [,,max->,,]
			++iterations;
		}while(swap == true);
		
		// Save sorted array
		sortedArray = new int[arr.length];
		System.arraycopy(arr, 0, sortedArray, 0, arr.length);
	}
	
	void bubbleSortRev(int arr[])
    {
		iterations = 0; swapes = 0;
		// Save unsorted array
		originalArray = new int[arr.length];
		System.arraycopy(arr, 0, originalArray, 0, arr.length);
		
		// Sorting ...
		int len = arr.length;
		boolean swap = false;
		do
		{
			int init = 0;
			swap = false;
			for(int i = init; i < len - 1; ++i)
			{
				if( arr[len - 1 - i] < arr[len - 2 - i] )
				{
					// swap
					int temp = arr[len - 1 - i];
					arr[len - 1 - i] = arr[len - 2 - i];
					arr[len - 2 - i] = temp;
					swap = true;
					++swapes;
				}
			}
			++init;		// for each pass, last number is sortered [,,max->,,] AQUI SE JODE
			++iterations;
		}while(swap == true);
		
		// Save sorted array
		sortedArray = new int[arr.length];
		System.arraycopy(arr, 0, sortedArray, 0, arr.length);
	}
	
	
	@Override
	public void run()
	{
		
	}
	
	void bubbleSort2()		// variant without checking sorting
    {
		iterations = 0; swapes = 0;
		// Save unsorted array
		originalArray = new int[arr.length];
		System.arraycopy(arr, 0, originalArray, 0, arr.length);
		
        int n = arr.length;
        for (int i = 0; i < n-1; i++)
		{
            for (int j = 0; j < n-i-1; j++)
                if (arr[j] > arr[j+1])
                {
                    // swap arr[j+1] and arr[j]
                    int temp = arr[j];
                    arr[j] = arr[j+1];
                    arr[j+1] = temp;
					++swapes;
                }
			++iterations;
		}
		
		// Save sorted array
		sortedArray = new int[arr.length];
		System.arraycopy(arr, 0, sortedArray, 0, arr.length);
    }
	
	public void printSortedArray()
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
	
	public static void test()
	{
		BubbleSort bubble = new BubbleSort(new int[]{3,1,2},directionType.FORWARD);
		
		bubble.bubbleSort();
		bubble.printSortedArray();
		
//		bubble.bubbleSortRev(new int[]{45,88,2,5,12,5,3,7,8,9,2,22,34,8,2,5,20,56,5,5,5,5,5,2});
//		bubble.printSortedArray();
//		
//		bubble.bubbleSortRev(new int[]{5,3,1,9,8,2,4,7});
//		bubble.printSortedArray();
	}
	
}
