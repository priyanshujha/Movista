package clustering;

import java.util.HashMap;

public class MovieRating {

	/**
	 * @param args
	 */
	int ID;
	HashMap<Integer,Float>userRating=new HashMap<Integer,Float>();
	HashMap<Integer,Double>clusterDistance=new HashMap<Integer,Double>();
	int cluster;
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public HashMap<Integer,Float> getUserRating() {
		return userRating;
	}
	public void setUserRating(HashMap<Integer,Float> userRating) {
		this.userRating = userRating;
	}
	public HashMap<Integer, Double> getClusterDistance() {
		return clusterDistance;
	}
	public void setClusterDistance(HashMap<Integer, Double> clusterDistance) {
		this.clusterDistance = clusterDistance;
	}
	public int getCluster() {
		return cluster;
	}
	public void setCluster(int cluster) {
		this.cluster = cluster;
	}
	

}
