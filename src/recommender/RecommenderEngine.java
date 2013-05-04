package recommender;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import persistence.MySQLPersistence;
import data.Movie;

public class RecommenderEngine {
	Movie selectedMovie;
	HashMap<Integer,Movie> clusterMovies;
	public RecommenderEngine(Movie m) {
		selectedMovie=m;
		MySQLPersistence p=new MySQLPersistence();
		selectedMovie=p.getMovieDetails(m);
		clusterMovies=p.getClusterMovies(selectedMovie);
		p.closeConnection();
	}
	public List<RecommendScore> recommend()
	{
		List<RecommendScore> scores=new ArrayList<RecommendScore>();		
		for(Movie m:clusterMovies.values())
		{
			int score=0;
			//Compare director
			if(m.getDirectorID()==selectedMovie.getDirectorID())
			{
				score+=2;
			}			
			for(int c:selectedMovie.getActorRanks().keySet())
			{
				if(m.getActorRanks().containsKey(c))
				{
					score+=((4/selectedMovie.getActorRanks().get(c))*(1/m.getActorRanks().get(c)));
				}				
			}
			for(int c:selectedMovie.getGenres())
			{
				if(m.getGenres().contains(c))
				{
					score+=1;
				}						
			}
			for(int c:selectedMovie.getTags().keySet())
			{
				if(m.getTags().containsKey(c))
				{
					score+=selectedMovie.getTags().get(c)*m.getTags().get(c);
				}	
			}
			
			score+=m.getCriticsRating();
			score+=m.getAudienceRating()/2;
			RecommendScore r=new RecommendScore();
			System.out.println(score);
			r.score=score;
			r.movie=m;
			scores.add(r);
		}
		
		Collections.sort(scores);		
		
		return scores;
		
	}
	
}
