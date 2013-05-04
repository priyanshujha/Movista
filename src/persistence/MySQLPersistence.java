package persistence;

import java.sql.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import clustering.Cluster;
import data.Movie;

public class MySQLPersistence {

	public Connection connection = null;

	public MySQLPersistence() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("Where is your MySQL JDBC Driver?");
			e.printStackTrace();
			return;
		}
		try {
			connection = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/moviedb", "root", "admin");

		} catch (SQLException e) {
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return;
		}

		if (connection != null) {
			System.out.println("You made it, take control your database now!");
		} else {
			System.out.println("Failed to make connection!");
		}
	}

	public void closeConnection() {
		try {
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public HashMap<String, Integer> getAllActors() {
		HashMap<String, Integer> actors = new HashMap<String, Integer>(94000);
		try {
			String query = "Select idActors,actorIdName from actors";
			Statement st = connection.createStatement();
			ResultSet rs = st.executeQuery(query);
			while (rs.next()) {
				String actor_id = rs.getString(2);
				int id = rs.getInt(1);
				actors.put(actor_id, id);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return actors;
	}

	public HashMap<String, Integer> getAllDirectors() {
		HashMap<String, Integer> directors = new HashMap<String, Integer>(10000);
		try {
			String query = "Select idDirectors,directorIdName from directors";
			Statement st = connection.createStatement();
			ResultSet rs = st.executeQuery(query);
			while (rs.next()) {
				String director_id = rs.getString(2);
				int id = rs.getInt(1);
				directors.put(director_id, id);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return directors;
	}

	public HashMap<String, Integer> getAllGenres() {
		HashMap<String, Integer> genres = new HashMap<String, Integer>(20);
		try {
			String query = "Select idGenre,Genre from genre";
			Statement st = connection.createStatement();
			ResultSet rs = st.executeQuery(query);
			while (rs.next()) {
				String genre = rs.getString(2);
				int id = rs.getInt(1);
				genres.put(genre, id);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return genres;
	}

	public void insertTags(HashMap<Integer, String> tags) {
		String sql = "insert into tags (idtags,tagName) values (?,?)";
		PreparedStatement ps;
		try {
			ps = connection.prepareStatement(sql);

			final int batchSize = 1000;
			int count = 0;
			Set<Integer> keys = tags.keySet();
			for (int key : keys) {
				ps.setInt(1, key);
				ps.setString(2, tags.get(key));
				ps.addBatch();
				if (++count % batchSize == 0) {
					ps.executeBatch();
				}
				System.out.println(key);
			}
			ps.executeBatch(); // insert remaining records
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void insertGenre(HashSet<String> genres) {
		String sql = "insert into genre (Genre) values (?)";
		PreparedStatement ps;
		try {
			ps = connection.prepareStatement(sql);

			final int batchSize = 1000;
			int count = 0;

			for (String genre : genres) {
				ps.setString(1, genre);
				ps.addBatch();
				if (++count % batchSize == 0) {
					ps.executeBatch();
				}
				System.out.println(count);
			}
			ps.executeBatch(); // insert remaining records
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void insertActor(HashMap<String, String> actors) {
		String sql = "insert into actors (actorIdName,actorName) values (?,?)";
		PreparedStatement ps;
		try {
			ps = connection.prepareStatement(sql);

			final int batchSize = 1000;
			int count = 0;
			Set<String> keys = actors.keySet();
			for (String id : keys) {
				ps.setString(1, id);
				ps.setString(2, actors.get(id));
				ps.addBatch();
				if (++count % batchSize == 0) {
					ps.executeBatch();
				}
				System.out.println(count);
			}
			ps.executeBatch(); // insert remaining records
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void insertDirector(HashMap<String, String> directors) {
		String sql = "insert into directors (directorIdName,directorName) values (?,?)";
		PreparedStatement ps;
		try {
			ps = connection.prepareStatement(sql);

			final int batchSize = 1000;
			int count = 0;
			Set<String> keys = directors.keySet();
			for (String id : keys) {
				ps.setString(1, id);
				ps.setString(2, directors.get(id));
				ps.addBatch();
				if (++count % batchSize == 0) {
					ps.executeBatch();
				}
				System.out.println(count);
			}
			ps.executeBatch(); // insert remaining records
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void insertMovieRating() {

	}

	public void insertMovies(Collection<Movie> movies) {
		String sql = "insert into movies (idMovies,year,MovieName,criticsRating,audienceRating,picURL,movieDirector) values (?,?,?,?,?,?,?)";
		PreparedStatement ps;
		try {
			ps = connection.prepareStatement(sql);

			final int batchSize = 2000;
			int count = 0;

			for (Movie movie : movies) {
				ps.setInt(1, movie.getId());
				ps.setInt(2, movie.getYear());
				ps.setString(3, movie.getMovieName());
				ps.setFloat(4, movie.getCriticsRating());
				ps.setFloat(5, movie.getAudienceRating());
				ps.setString(6, movie.getPicURL());
				ps.setInt(7, movie.getDirectorID());
				if (count >= 7999) {
					System.out.println(movie.getId() + " rating :"
							+ movie.getCriticsRating());
				}
				ps.addBatch();
				if (++count % batchSize == 0) {
					ps.executeBatch();
				}
				System.out.println(count);
			}
			ps.executeBatch(); // insert remaining records
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void insertMovieActor(
			HashMap<Integer, HashMap<Integer, Integer>> actorData) {

		String sql = "insert into movies_actors (Movie_ID,actor_id,Ranking) values (?,?,?)";
		PreparedStatement ps;
		try {
			ps = connection.prepareStatement(sql);

			final int batchSize = 1000;
			int count = 0;
			Set<Integer> movieKeys = actorData.keySet();
			for (int id : movieKeys) {

				Set<Integer> actorKeys = actorData.get(id).keySet();
				for (int actorId : actorKeys) {

					ps.setInt(1, id);
					ps.setInt(2, actorId);
					ps.setInt(3, actorData.get(id).get(actorId));
					ps.addBatch();
					if (++count % batchSize == 0) {
						ps.executeBatch();
					}
					System.out.println(count);
				}

			}
			ps.executeBatch(); // insert remaining records
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void insertMovieGenre(HashMap<Integer, List<Integer>> genres) {

		String sql = "insert into movie_genre (MovieID,GenreID) values (?,?)";
		PreparedStatement ps;
		try {
			ps = connection.prepareStatement(sql);

			final int batchSize = 1000;
			int count = 0;
			Set<Integer> keys = genres.keySet();
			for (int id : keys) {
				for (int genreId : genres.get(id)) {
					ps.setInt(1, id);
					ps.setInt(2, genreId);
					ps.addBatch();
					if (++count % batchSize == 0) {
						ps.executeBatch();
					}
					System.out.println(count);
				}

			}
			ps.executeBatch(); // insert remaining records
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void insertMovieTags(
			HashMap<Integer, HashMap<Integer, Integer>> tagData) {
		String sql = "insert into movie_tags (movie_id,tag_id,weight) values (?,?,?)";
		PreparedStatement ps;
		try {
			ps = connection.prepareStatement(sql);

			final int batchSize = 1000;
			int count = 0;
			Set<Integer> movieKeys = tagData.keySet();
			for (int id : movieKeys) {

				Set<Integer> tagKeys = tagData.get(id).keySet();
				for (int tagId : tagKeys) {

					ps.setInt(1, id);
					ps.setInt(2, tagId);
					ps.setInt(3, tagData.get(id).get(tagId));
					ps.addBatch();
					if (++count % batchSize == 0) {
						ps.executeBatch();
					}
					System.out.println(count);
				}

			}
			ps.executeBatch(); // insert remaining records
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void insertCluster(HashMap<Integer, Cluster> clusters) {
		String sql = "insert into cluster (Cluster_ID,Movie_ID) values (?,?)";
		PreparedStatement ps;
		try {
			ps = connection.prepareStatement(sql);

			final int batchSize = 1000;
			int count = 0;
			Collection<Cluster> cluster = clusters.values();
			for (Cluster c : cluster) {

				for (int i : c.getMovies().keySet()) {
					ps.setInt(1, c.getId());
					ps.setInt(2, i);
					ps.addBatch();
					if (++count % batchSize == 0) {
						ps.executeBatch();
					}
					System.out.println(count);

				}
			}
			ps.executeBatch(); // insert remaining records
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}	
	public Movie getMovieDetails(Movie m) {
		try {
			System.out.println(m.getId());
			String query = "select actor_id,Ranking from movies_actors where movie_id="
					+ m.getId();
			Statement stmt;

			stmt = connection.createStatement();

			ResultSet ra = stmt.executeQuery(query);
			while (ra.next()) {
				m.getActorRanks().put(ra.getInt(1), ra.getInt(2));
			}
			query = "select genreID from movie_genre where movieID="
					+ m.getId();
			ra = stmt.executeQuery(query);
			while (ra.next()) {
				m.getGenres().add(ra.getInt(1));
			}
			query = "select tag_id,weight from movie_tags where movie_id="
					+ m.getId();
			while (ra.next()) {
				m.getTags().put(ra.getInt(1), ra.getInt(2));
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return m;
	}

	public HashMap<Integer, Movie> getClusterMovies(Movie selectedMovie) {
		HashMap<Integer, Movie> clusterMovies = new HashMap<Integer, Movie>();
		try {
			String query = "select * from movies m where idMovies in (SELECT Movie_ID FROM cluster where Cluster_ID=(Select Cluster_ID from cluster where Movie_ID="
					+ selectedMovie.getId() + "));";
			Statement st = connection.createStatement();
			ResultSet rs = st.executeQuery(query);
			while (rs.next()) {
				Movie m = new Movie();
				m.setId(rs.getInt(1));
				m.setMovieName(rs.getString(2));
				m.setCriticsRating(rs.getFloat(3));
				m.setAudienceRating(rs.getFloat(4));
				m.setPicURL(rs.getString(5));
				m.setDirectorID(rs.getInt(6));
				m.setYear(rs.getInt(7));
				m=getMovieDetails(m);
				clusterMovies.put(m.getId(), m);			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return clusterMovies;
	}

}
