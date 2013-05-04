package data;

import java.util.*;

public class Movie {
	int id;
	
	String MovieName;
	float criticsRating;
	float audienceRating;
	String picURL;
	int directorID;
	HashMap<Integer,Integer> tags=new HashMap<Integer,Integer>();
	List<Integer> genres=new ArrayList<Integer>();	
	int year;
	
	
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getMovieName() {
		return MovieName;
	}
	public void setMovieName(String movieName) {
		MovieName = movieName;
	}
	public float getCriticsRating() {
		return criticsRating;
	}
	public void setCriticsRating(float criticsRating) {
		this.criticsRating = criticsRating;
	}
	public float getAudienceRating() {
		return audienceRating;
	}
	public void setAudienceRating(float audienceRating) {
		this.audienceRating = audienceRating;
	}
	public String getPicURL() {
		return picURL;
	}
	public void setPicURL(String picURL) {
		this.picURL = picURL;
	}
	public int getDirectorID() {
		return directorID;
	}
	public void setDirectorID(int directorID) {
		this.directorID = directorID;
	}
	public HashMap<Integer,Integer> getTags() {
		return tags;
	}
	public void setTags(HashMap<Integer,Integer> tags) {
		this.tags = tags;
	}
	public List<Integer> getGenres() {
		return genres;
	}
	public void setGenres(List<Integer> genres) {
		this.genres = genres;
	}
	public HashMap<String, Integer> getActors() {
		return actors;
	}
	public void setActors(HashMap<String, Integer> actors) {
		this.actors = actors;
	}
	HashMap<String,Integer> actors=new HashMap<String,Integer>();
	HashMap<Integer,Integer>actorRanks=new HashMap<Integer,Integer>();


	public HashMap<Integer, Integer> getActorRanks() {
		return actorRanks;
	}
	public void setActorRanks(HashMap<Integer, Integer> actorRanks) {
		this.actorRanks = actorRanks;
	}

}
