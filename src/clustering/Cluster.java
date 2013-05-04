package clustering;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import util.Constants;


public class Cluster {

	/**
	 * @param args
	 */
	List<Double> components;
	HashMap<Integer, MovieRating> movies;

	public HashMap<Integer, MovieRating> getMovies() {
		return movies;
	}
	public void setMovies(HashMap<Integer, MovieRating> movies) {
		this.movies = movies;
	}

	int id;
	public Cluster()
	{
		components = new ArrayList(71567);
		movies = new HashMap<Integer, MovieRating>();
	}
	public int getId() {
		return id;
	}
	public void resetCluster()
	{
		movies=new HashMap<Integer, MovieRating>();
	}
	public void setId(int id) {
		this.id = id;
	}

	public double calculateDistance(HashMap<Integer, Float> ratings) {
		double distance;
		BigDecimal sum=new BigDecimal(0);
		int count=1;
		for(Double d:components)
		{
			float rating=0;
			if(ratings.containsKey(count))
			{
				rating=ratings.get(count);				
			}
			count++;
			
			double diff=d-rating;					
			double power=Math.pow(diff, 2);			
			sum=sum.add(new BigDecimal(power));
			
		}
		distance=Math.sqrt(sum.doubleValue());		
		return distance;

	}

	public void update() {
		// TODO Auto-generated method stub
		components = new ArrayList<Double>(71567);
		
		for (int i = 1; i <= Constants.USERS; i++) {
			long x=0;
			for (MovieRating m : movies.values()) {
				if(m.userRating.containsKey(i))
				{
					x+=m.userRating.get(i);
				}			
			}
			components.add((double)(x/movies.size()));			
		}

	}

}
