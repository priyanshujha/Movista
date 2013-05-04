package recommender;

import java.util.ArrayList;
import java.util.List;

import data.Movie;

public class TestClass {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Movie m=new Movie();
		m.setId(63113);
		m.setDirectorID(0);
		m.setMovieName("Quantum of Solace");	
		
		RecommenderEngine r=new RecommenderEngine(m);
		List<RecommendScore> score=new ArrayList<RecommendScore>();
		score=r.recommend();
		for(int i=0;i<12;i++)
		{
			m=score.get(i).movie;
			System.out.println(m.getMovieName()+" "+score.get(i).score);
		}

	}

}
