package SelectMedian;
import java.util.Scanner;
import java.util.Vector;
import java.util.Arrays;
import java.io.File;
/*
Modified by Allan Liu
V00806981
January 30, 2017
CSC 226
*/
/**
 *
 * @author Rahnuma Islam Nishat
 * January 20, 2017
 * CSC 226 - Spring 2017
 */
public class SelectMedian {

    //if there is only 1 element, then return it, otherwise select the median
    public static int LinearSelect(int[] A, int k){
        if (A.length == 1)
		{
			return A[0];
		}
		else if (A.length == 0)
		{
			return -1;
		}
		else
		{
			return select(0,A.length-1,A,k);
		}
	}

	private static int select(int left,int right, int[] array, int k)
	{
		//pick the smart pivot and exec partition
		int p = pickSmartPivot(left, right, array);
		int index = partition(left, right, array, p);
		
		//recursive operations after partioning
		//recall from lecture slides and CSC225
		if (k == index+1)
		{
			return array[index];
		}
		else if (k <= index)
		{
			return select(left, index-1, array, k);
		}
		else
		{
			return select(index+1, right, array, k);
		}
	}
	
	private static int partition(int left, int right, int[] array, int indexOfpivot)
	{
		//swapping the pivot with the last element in array
		swap(array,indexOfpivot,right);
		int pivotValue = array[right];
		int l = left;
		
		//quicksort partition about the pivot
		for(int j = left; j < right; j++)
		{
			if (array[j] <= pivotValue)
			{
				swap(array, l, j);
				l++;
			}
		}
		swap(array, l, right);
		return l;
	}
	
	private static int pickSmartPivot(int left, int right, int[] startArray)
	{
		//new array for recursion
		int[] newArray = new int[startArray.length-1];
		for(int i = 0; i < newArray.length; i++) 
		{
			newArray[i] = startArray[i];
		}
		
		//base case: need to sort all the values so we can find the median of the sorted array
		if (right-left <5) 
		{
			return chooseMedian(left, right, newArray);
		}
		int indexCounter = left;
		for(int i = left; i < right; i=i+5) 
		{
			//code segment helped from stackoverflow community
			int subright = i+4;
			if (subright > right) 
			{
				subright = right;
			}
			int median = chooseMedian(i, subright, newArray);
			swap(newArray, median, indexCounter);
			indexCounter++;
		}
		return pickSmartPivot(left, (left + (int)Math.ceil((right-left))/5), newArray);
	}
		
	private static void swap(int[]array, int x, int y)
	{
		int temp = array[x];
		array[x] = array[y];
		array[y] = temp;
	}
	
	//choosing the median from the already partitioned array A, using trusty'ol insertionsort
	public static int chooseMedian(int left, int right, int[] array) 
	{
		for(int i = left+1; i < right; i++) 
		{
			int tempVal = array[i];
			int j = left;

			while((j >= left) && (array[j] > tempVal)) 
			{
				array[j+1] = array[j];
				j--;
			}
			array[j+1] = tempVal;
		}
		//find the median of the sorted array
		int median = (int)Math.ceil((right-left)/2);
		return left + median;
	}
	
    public static void main(String[] args) {
        //int[] A = {50, 54, 49, 49, 48, 49, 56, 52, 51, 52, 50, 59};
		//int[] A = {3, 75, 12, 20};
		//int[] A = {};

		Scanner s;
		if (args.length > 0){
			try{
				s = new Scanner(new File(args[0]));
			} catch(java.io.FileNotFoundException e){
				System.out.printf("Unable to open %s\n",args[0]);
				return;
			}
			System.out.printf("Reading input values from %s.\n",args[0]);
		}else{
			s = new Scanner(System.in);
			System.out.printf("Enter a list of non-negative integers. Enter a negative value to end the list.\n");
		}
		Vector<Integer> inputVector = new Vector<Integer>();

		int v;
		while(s.hasNextInt() && (v = s.nextInt()) >= 0)
			inputVector.add(v);

		int[] A = new int[inputVector.size()];

		for (int i = 0; i < A.length; i++)
			A[i] = inputVector.get(i);
		
		System.out.printf("Read %d values.\n",A.length);
		long startTime = System.nanoTime();
		long endTime = System.nanoTime();
		double totalTime = (endTime-startTime)/1e6;
		
		//***Uncertain here***
		/*
		Finding the value k using this will return value 3 in example {1 2 4 5 3}.
		However, if I leave it as is in the orginally, I will get value 2 because
		A.length/2 will round down if odd, but in the example output it rounds up if odd.
		*/
		int x;
		if(A.length%2 == 1)
		{
			x = (A.length/2)+1;
		}
		else
		{
			x = A.length/2;
		}
		System.out.println("The median weight is " + LinearSelect(A, x));
		
        //System.out.println("The median weight is " + LinearSelect(A, A.length/2));
		
		System.out.printf("Total Time (milliseconds): %.4f\n",totalTime);
    }
    
}
