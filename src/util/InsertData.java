package util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import persistence.MySQLPersistence;

import data.Movie;


public class InsertData {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MySQLPersistence persist=new MySQLPersistence();
		//HashMap<Integer,String> tags=fetchTags();
		//persist.insertTags(tags);
		//HashSet<String>genres=fetchGenres();
		//persist.insertGenre(genres);
		//HashMap<String,String> directors=fetchDirectors();
		//persist.insertDirector(directors);
		//HashMap<String,String> actors=fetchActors();
		//persist.insertActor(actors);
		//persist.insertMovies(prepareMovies(persist.getAllDirectors()));
		//HashMap<String,Integer>genres=persist.getAllGenres();
		//HashMap<Integer,List<Integer>> movieGenres=prepareMovieGenre(genres);
		//persist.insertMovieGenre(movieGenres);
		
		//HashMap<String,Integer> actors=persist.getAllActors();
		//HashMap<Integer,HashMap<Integer,Integer>> actorData=prepareMovieActor(actors);
		//persist.insertMovieActor(actorData);
		
		HashMap<Integer,HashMap<Integer,Integer>> movieTags=prepareMovieTags();		
		persist.insertMovieTags(movieTags);		
		persist.closeConnection();
	}
	private static HashMap<Integer, HashMap<Integer, Integer>> prepareMovieTags() {
		
		HashMap<Integer, HashMap<Integer, Integer>> movieTags=new HashMap<Integer, HashMap<Integer, Integer>>();
		try {
			BufferedReader br=new BufferedReader(new FileReader(Constants.MOVIE_TAGS));
			String currentLine="";			
			br.readLine();
			
			HashMap<Integer, Integer> tagWeight=new HashMap<Integer, Integer>();
			
			int last=1;
			
			while((currentLine=br.readLine())!=null)
			{
				String[] data=currentLine.split("\t");
				int movieId=Integer.parseInt(data[0]);				
				int tagId=Integer.parseInt(data[1]);;
				int weight=Integer.parseInt(data[2]);				
				if(last==movieId)
				{
					tagWeight.put(tagId,weight);					
				}
				else
				{
					movieTags.put(last,tagWeight);
					last=movieId;
					tagWeight=new HashMap<Integer, Integer>();
				}
			}
			br.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return movieTags;
	}
	private static HashMap<Integer, HashMap<Integer, Integer>> prepareMovieActor(
			HashMap<String, Integer> actors) {
		HashMap<Integer, HashMap<Integer, Integer>> actorData=new HashMap<Integer, HashMap<Integer, Integer>>();
		try {
			BufferedReader br=new BufferedReader(new FileReader(Constants.MOVIE_ACTORS));
			String currentLine="";			
			br.readLine();
			HashMap<Integer, Integer> actorRank=new HashMap<Integer, Integer>();
			
			int last=1;
			
			while((currentLine=br.readLine())!=null)
			{
				String[] data=currentLine.split("\t");
				int movieId=Integer.parseInt(data[0]);
				String actor=data[1];
				int actorId=actors.get(actor);
				int rank=Integer.parseInt(data[3]);				
				if(last==movieId)
				{
					actorRank.put(actorId,rank);					
				}
				else
				{
					actorData.put(last,actorRank);
					last=movieId;
					actorRank=new HashMap<Integer, Integer>();
				}
			}
			br.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return actorData;
	}
	private static HashMap<Integer,List<Integer>> prepareMovieGenre(HashMap<String,Integer> genres)
	{
		HashMap<Integer,List<Integer>>movieGenres=new HashMap<Integer,List<Integer>>();
		try {
			BufferedReader br=new BufferedReader(new FileReader(Constants.MOVIE_GENRES));
			String currentLine="";			
			br.readLine();
			List<Integer>genreList=new ArrayList<Integer>();
			
			int last=1;
			
			while((currentLine=br.readLine())!=null)
			{
				String[] data=currentLine.split("\t");
				int movieId=Integer.parseInt(data[0]);
				int genreId=genres.get(data[1]);
				if(last==movieId)
				{
					genreList.add(genreId);					
				}
				else
				{
					movieGenres.put(last,genreList);
					last=movieId;
					genreList=new ArrayList<Integer>();
				}
			}
			br.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return movieGenres;
	}
	private static Collection<Movie> prepareMovies(HashMap<String,Integer> directors)
	{
		HashMap<Integer,Movie> movies=new HashMap<Integer,Movie>();
		try {
			BufferedReader br=new BufferedReader(new FileReader(Constants.MOVIE_FILE));
			String currentLine="";			
			br.readLine();			
			while((currentLine=br.readLine())!=null)
			{
				Movie movie=new Movie();
				String[] data=currentLine.split("\t");
				movie.setId(Integer.parseInt(data[0]));
				movie.setMovieName(data[1]);
				//Audience score is out of 5
				if(data[6].equals(null)||data[6].equals("")||data[6].equals(" "))
				{
					System.out.println("Inside"+data[0]+" "+data[6]);
				}
				
				if(!(data[17].equals("\\N")||data[12].equals("\\N")||data[4].equals("\\N")||data[5].equals("\\N")))
				{
					movie.setAudienceRating(Float.parseFloat(data[17]));
					movie.setCriticsRating(Float.parseFloat(data[12]));
					movie.setPicURL(data[4]);	
					movie.setYear(Integer.parseInt(data[5]));
				}				
				movies.put(movie.getId(),movie);
			}
			br.close();
			br=new BufferedReader(new FileReader(Constants.MOVIE_DIRECTORS));
			while((currentLine=br.readLine())!=null)
			{	
				String[] data=currentLine.split("\t");				
				Movie m=movies.get(Integer.parseInt(data[0]));
				m.setDirectorID(directors.get(data[1]));
				movies.put(Integer.parseInt(data[0]),m);				
			}			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return movies.values();
	}
	
	private static HashMap<String, String> fetchActors() {
		HashMap<String,String> actors=new HashMap<String,String>();
		try {
			BufferedReader br=new BufferedReader(new FileReader(Constants.MOVIE_ACTORS));
			String currentLine="";			
			br.readLine();
			while((currentLine=br.readLine())!=null)
			{
				String[] data=currentLine.split("\t");
				actors.put(data[1],data[2]);				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return actors;
	}
	private static HashMap<String,String> fetchDirectors()
	{
		HashMap<String,String> directors=new HashMap<String,String>();
		try {
			BufferedReader br=new BufferedReader(new FileReader(Constants.MOVIE_DIRECTORS));
			String currentLine="";			
			br.readLine();
			while((currentLine=br.readLine())!=null)
			{
				String[] data=currentLine.split("\t");
				directors.put(data[1],data[2]);				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return directors;
	}
	private static HashSet<String> fetchGenres()
	{
		HashSet<String> genres=new HashSet<String>();
		
		try {
			BufferedReader br=new BufferedReader(new FileReader(Constants.MOVIE_GENRES));
			String currentLine="";
			br.readLine();
			while((currentLine=br.readLine())!=null)
			{
				String[] data=currentLine.split("\t");
				genres.add(data[1]);				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return genres;
		
	}
	private static HashMap<Integer,String> fetchTags() {
		// TODO Auto-generated method stub
		HashMap<Integer,String> tags=new HashMap<Integer,String>();
		try {
			BufferedReader br=new BufferedReader(new FileReader(Constants.TAGS));
			String currentLine="";
			br.readLine();
			while((currentLine=br.readLine())!=null)
			{
				String[] data=currentLine.split("\t");
				tags.put(Integer.parseInt(data[0]), data[1]);				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return tags;
	}
	

}
