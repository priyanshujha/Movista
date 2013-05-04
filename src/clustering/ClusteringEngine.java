package clustering;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import persistence.MySQLPersistence;
import util.Constants;


public class ClusteringEngine {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			BufferedReader br = new BufferedReader(new FileReader(
					Constants.RATINGS));
			br.readLine();
			HashMap<Integer,MovieRating>movieRatings=new HashMap<Integer,MovieRating>(20000);
			String currentLine;
			MovieRating m;
			while ((currentLine = br.readLine()) != null) {
				String[] data = currentLine.split("::");
				int userId=Integer.parseInt(data[0]);
				int movieId=Integer.parseInt(data[1]);
				Float rating=Float.parseFloat(data[2]);				
				
				if (movieRatings.containsKey(movieId)) {
					m=movieRatings.get(movieId);
					m.getUserRating().put(userId,rating);					
				}
				else
				{
					m=new MovieRating();
					m.setID(movieId);
					HashMap<Integer,Float> user_rating=new HashMap<Integer,Float>();
					user_rating.put(userId,rating);
					m.setUserRating(user_rating);
					movieRatings.put(movieId,m);
				}
			}
			System.out.println("Fetched Movie Ratings:"+movieRatings.keySet().size());			
			HashMap<Integer,Cluster> clusters=clusterData(movieRatings,50);
			MySQLPersistence persist=new MySQLPersistence();
			persist.insertCluster(clusters);
			persist.closeConnection();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static HashMap<Integer,Cluster> clusterData(HashMap<Integer,MovieRating>movieRatings,int cluster) {
		// TODO Auto-generated method stub
		HashMap<Integer,Cluster>clusters=new HashMap<Integer,Cluster>();
		HashMap<Integer,Integer> initialSeed=new HashMap<Integer,Integer>();
		Random randomGenerator = new Random();
		
		int noOfMovies=0;
		int rand;
		//generate initial seed		
		System.out.println("Generating random seed");
		for(int i=0;i<cluster;i++)
		{
			do
			{
				rand=randomGenerator.nextInt(65000);
			}while(initialSeed.containsKey(rand) || !movieRatings.containsKey(rand));
			initialSeed.put(rand,rand);
			MovieRating m=movieRatings.get(rand);
			HashMap<Integer,Float> ratings=m.getUserRating();
			Cluster c=new Cluster();
			c.setId(i);
			for(int j=1;j<=Constants.USERS;j++)
			{
				if(ratings.containsKey(j))
					c.components.add((double)ratings.get(j));
				else
					c.components.add(0.0);
				
			}
			System.out.println(i+":"+rand);
			clusters.put(i,c);
		}
		
		//initial assign to clusters		
		Collection<MovieRating> movies=movieRatings.values();
		noOfMovies=movies.size();		
		int count=0;
		System.out.println("starting Initial Assignment");
		System.out.println("Initial Assignment Complete");
		//Keep moving while there is less change in clusters
		int noOfMoves=51;
		
		while(noOfMoves>50){
			
			noOfMoves=0;
			count=0;
			movies=movieRatings.values();
			noOfMovies=movies.size();
			for(Cluster c:clusters.values())
			{
				c.resetCluster();
			}
			
			for(MovieRating m:movies)
			{
				
				int temp=m.getCluster();
				double minDistance=Double.MAX_VALUE;
				for(Cluster c:clusters.values())
				{
					double distance=c.calculateDistance(m.getUserRating());					
					if(distance<minDistance)
					{
						m.setCluster(c.getId());					
						minDistance=distance;
					}
				}
				if(temp!=m.getCluster())
				{
					noOfMoves++;
				}
				if(((++count)%1000)==0)
					System.out.println(count);
				Cluster c=clusters.get(m.getCluster());
				c.movies.put(m.ID,m);				
			}			
			int temp=0;
			for(Cluster c:clusters.values())
			{
				c.update();				
				System.out.println("updated:"+temp++);
			}			
			
			System.out.println("Iteration Complete:"+noOfMoves);
		}
		return clusters;
	}
}
