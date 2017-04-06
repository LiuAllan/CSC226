/* MWST.java
EDITED by: Allan Liu
V00806981
February 21, 2017

   CSC 225 - Spring 2012
   Assignment 5 - Template for a Minimum Weight Spanning Tree algorithm
   
   The assignment is to implement the mwst() method below, using any of the algorithms
   studied in the course (Kruskal, Prim-Jarnik or Baruvka). The mwst() method computes
   a minimum weight spanning tree of the provided graph and returns the total weight
   of the tree. To receive full marks, the implementation must run in O(mlog(n)) time
   on a graph with n vertices and m edges.

   This template includes some testing code to help verify the implementation.
   Input graphs can be provided with standard input or read from a file.
   
   To provide test inputs with standard input, run the program with
	java MWST
   To terminate the input, use Ctrl-D (which signals EOF).
   
   To read test inputs from a file (e.g. graphs.txt), run the program with
    java MWST graphs.txt
	
   The input format for both methods is the same. Input consists
   of a series of graphs in the following format:
   
    <number of vertices>
	<adjacency matrix row 1>
	...
	<adjacency matrix row n>
	
   For example, a path on 3 vertices where one edge has weight 1 and the other
   edge has weight 2 would be represented by the following
   
    3
	0 1 0
	1 0 2
	0 2 0
	
   An input file can contain an unlimited number of graphs; each will be processed separately.
   
   B. Bird - 03/11/2012
*/

import java.util.Scanner;
import java.io.File;
import java.util.Comparator;
import java.util.PriorityQueue;

public class MWST{

	/*
	Edge class that is used by the Priority Queue
	Modified code from Algorithms fourth edition pg 610
	*/
	public static class Edge
	{
		Edge(int w, int s, int d)
		{
			sourceVertex = s;
			destVertex = d;
			weight = w;
		}
		int sourceVertex;
		int destVertex;
		int weight;
	}
	
	/*
	Priority Queue uses comparator that compares the edge weights, using edge1, edge2
	*/
	public static class EdgeComparator implements Comparator<Edge>
	{
		public int compare(Edge edge1, Edge edge2)
		{
			if(edge1.weight < edge2.weight)
			{
				return -1;
			}
			if(edge1.weight > edge2.weight)
			{
				return 1;
			}
			return 0;
		}
	}

	/* mwst(G)
		Given an adjacency matrix for graph G, return the total weight
		of all edges in a minimum weight spanning tree.
		
		If G[i][j] == 0, there is no edge between vertex i and vertex j
		If G[i][j] > 0, there is an edge between vertices i and j, and the
		value of G[i][j] gives the weight of the edge.
		No entries of G will be negative.
	*/
	static int mwst(int[][] G){
		int numVerts = G.length;

		/* Find a minimum weight spanning tree by any method */
		/* (You may add extra functions if necessary) */

		/*
		Initialize comparator and PQ
		*/
		Comparator<Edge> compare = new EdgeComparator();
		PriorityQueue<Edge> PQ = new PriorityQueue<Edge>(numVerts, compare);
		
		for(int i=0; i < numVerts; i++)
		{
			for(int j = i; j < numVerts; j++)
			{
				if(G[i][j] > 0)
				{
					Edge edge = new Edge(G[i][j], i, j);
					PQ.add(edge);
				}
			}
			
		}
		
		/*
		Initializes spanning tree T
		Remove the smallest edge from the priority queue and add it to the tree. Perform a check to satisfy
		the spanning tree property that it must not contain a cycle. If a cycle exists, then remove the edge.
		*/
		
		int[][] T = new int[numVerts][numVerts];
		int size = 0;
		while(!PQ.isEmpty() && size < numVerts - 1)
		{
			Edge e = PQ.remove();
			T[e.sourceVertex][e.destVertex] = e.weight;
			
			if(hasCycle(T))
			{
				T[e.sourceVertex][e.destVertex] = 0;
			}
			size++;
		}
		
		/* Add the weight of each edge in the minimum weight spanning tree
		   to totalWeight, which will store the total weight of the tree.
		*/
		int totalWeight = 0;
		for(int i = 0; i < numVerts; i++)
		{
			for(int j = i; j < numVerts; j++)
			{
				totalWeight += T[i][j];
			}
		}
		
		
		return totalWeight;
		
	}


	public static void main(String[] args){
		/* Code to test your implementation */
		/* You may modify this, but nothing in this function will be marked */

		int graphNum = 0;
		Scanner s;

		if (args.length > 0){
			//If a file argument was provided on the command line, read from the file
			try{
				s = new Scanner(new File(args[0]));
			} catch(java.io.FileNotFoundException e){
				System.out.printf("Unable to open %s\n",args[0]);
				return;
			}
			System.out.printf("Reading input values from %s.\n",args[0]);
		}else{
			//Otherwise, read from standard input
			s = new Scanner(System.in);
			System.out.printf("Reading input values from stdin.\n");
		}
		
		//Read graphs until EOF is encountered (or an error occurs)
		while(true){
			graphNum++;
			if(!s.hasNextInt())
				break;
			System.out.printf("Reading graph %d\n",graphNum);
			int n = s.nextInt();
			int[][] G = new int[n][n];
			int valuesRead = 0;
			for (int i = 0; i < n && s.hasNextInt(); i++){
				G[i] = new int[n];
				for (int j = 0; j < n && s.hasNextInt(); j++){
					G[i][j] = s.nextInt();
					valuesRead++;
				}
			}
			if (valuesRead < n*n){
				System.out.printf("Adjacency matrix for graph %d contains too few values.\n",graphNum);
				break;
			}
			if (!isConnected(G)){
				System.out.printf("Graph %d is not connected (no spanning trees exist...)\n",graphNum);
				continue;
			}
			int totalWeight = mwst(G);
			System.out.printf("Graph %d: Total weight is %d\n",graphNum,totalWeight);
				
		}
	}

	/* isConnectedDFS(G, covered, v)
	   Used by the isConnected function below.
	   You may modify this, but nothing in this function will be marked.
	*/
	static void isConnectedDFS(int[][] G, boolean[] covered, int v){
		covered[v] = true;
		for (int i = 0; i < G.length; i++)
			if (G[v][i] > 0 && !covered[i])
				isConnectedDFS(G,covered,i);
	}
	   
	/* isConnected(G)
	   Test whether G is connected.
	   You may modify this, but nothing in this function will be marked.
	*/
	static boolean isConnected(int[][] G){
		boolean[] covered = new boolean[G.length];
		for (int i = 0; i < covered.length; i++)
			covered[i] = false;
		isConnectedDFS(G,covered,0);
		for (int i = 0; i < covered.length; i++)
			if (!covered[i])
				return false;
		return true;
	}
	
	/*
	Test if the tree has a cycle using Depth first search. Code snippet reused from CSC225 Assignment 4
	*/
	static void hasCycleDFS(int[][] T, int[] visited, int v)
	{
		visited[v]++;
		for(int i=0; i < T.length; i++)
		{
			if(T[v][i] > 0)
			{
				hasCycleDFS(T, visited, i);
			}
		}
	}
	
	static boolean hasCycle(int[][] T)
	{
		int[] visited = new int[T.length];
		hasCycleDFS(T, visited, 0);
		for(int i=0; i<visited.length;i++)
		{
			if(visited[i] > 1)
			{
				return true;
			}
		}
		return false;
	}

}