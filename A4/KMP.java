

/* 
EDITED BY: Allan Liu
V00806981
03-23-2017
Rahnuma Islam Nishat - 08/02/2014
*/

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;


public class  KMP 
{
   private static String pattern;
   /*my constructors*/
   private static int[] next;
   private int pat_len; //length of pattern
   
   public KMP(String pattern)
   {
	   //constructing the DFA for KMP substring search
	   this.pattern = pattern;
	   this.pat_len = pattern.length();
	   next = new int[pat_len];
	   int j = -1;
	   for(int i = 0; i < pat_len; i++)
	   {
		   /*at index 0, set the first element of the pattern to -1.
		   Similarly, it will have -1 for the last element in the pattern.
		   If the last element is not -1, then it means it could not find the pattern
		   in the text file.
		   */
		   if(i == 0)
		   {
			   next[i] = -1;
		   }
		   else if (pattern.charAt(i) != pattern.charAt(j))
		   {
			   next[i] = -j;
		   }
		   else
		   {
			   next[i] = next[j];
		   }
		   while (j >= 0 && pattern.charAt(i) != pattern.charAt(j))
		   {
			   j = next[j];
		   }
		   j++;
	   }
   }
   public static int search(String txt)
   {  
		int pat_len = pattern.length();
		int tex_len = txt.length();
		int i;
		int j;
		//simulate the DFA to find a match
		for (i = 0, j = 0; i < tex_len && j < pat_len; i++)
		{
			while (j >= 0 && txt.charAt(i) != pattern.charAt(j))
			{
				j = next[j];
			}
			j++;
		}
		if (j == pat_len)
		{
			//returns the index of first matched pattern
			return i - pat_len;
		}
		else {
		//returns length of the pattern if not found
			//System.out.println(tex_len);
			return tex_len;
		}
   }

        
  	public static void main(String[] args) throws FileNotFoundException{
		Scanner s;
		if (args.length > 0){
			try{
				s = new Scanner(new File(args[0]));
			} catch(java.io.FileNotFoundException e){
				System.out.println("Unable to open "+args[0]+ ".");
				return;
			}
			System.out.println("Opened file "+args[0] + ".");
			String text = "";
			while(s.hasNext()){
				text+=s.next()+" ";
			}
			
			for(int i=1; i<args.length ;i++){
				KMP k = new KMP(args[i]);
				int index = search(text);
				if(index >= text.length())System.out.println(args[i]+ " was not found.");
				else System.out.println("The string \""+args[i]+ "\" was found at index "+index + ".");
			}
			
			//System.out.println(text);
			
		}else{
			System.out.println("usage: java SubstringSearch <filename> <pattern_1> <pattern_2> ... <pattern_n>.");
		}
		
		
	}
}




