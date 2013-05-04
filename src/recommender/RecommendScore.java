package recommender;

import data.Movie;

public class RecommendScore implements Comparable<RecommendScore>{
	Movie movie;
	int  score;
	@Override
	public int compareTo(RecommendScore o) {
		if((this.score-o.score)==0)
			return 0;
		else
			return (this.score>o.score?-1:1);		
	}	
}
