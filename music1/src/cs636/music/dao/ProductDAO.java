package cs636.music.dao;

import static cs636.music.dao.DBConstants.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import cs636.music.domain.Product;
import cs636.music.domain.Track;

public class ProductDAO {

	private Connection connection;

	public ProductDAO(DbDAO db) {
		connection = db.getConnection();
	}
	/**
	 * Search all Products 
	 * @return Set of Product Object
	 * @param 
	 * @throws SQLException
	 */
	public Set<Product> findAllProducts() throws SQLException
	{
		Set<Product> products = new HashSet<Product> ();
		Statement stmt = connection.createStatement();
		try
		{
			ResultSet set = stmt.executeQuery(" select * from " + PRODUCT_TABLE);			
				
				while (set.next())
				{
					Product product = new Product(set.getLong("product_id"), set.getString("product_code"),
							set.getString("product_description"), set.getBigDecimal("product_price"), null);
					products.add(product);
				}			
		}
		finally
		{
			stmt.close();
		}
		return products;
	}
	/**
	 * Search Product using given product id. 
	 * @return Product Object
	 * @param long product id
	 * @throws SQLException
	 */
	public Product findProductByPID(long productid) throws SQLException {
		Product product = null;
		Statement stmt = connection.createStatement();
		try {
			ResultSet set = stmt.executeQuery(" select * from " + PRODUCT_TABLE + " p, " + TRACK_TABLE + " t "
					+ " where p.product_id = " + productid + " and t.product_id = p.product_id ");
			if (set.next()) {
				product = new Product(set.getLong("product_id"), set.getString("product_code"),
						set.getString("product_description"), set.getBigDecimal("product_price"), null);
				
				product.setTracks(setTracksusingResultset (set,product));				
			}
			set.close();

		} finally {
			stmt.close();
		}
		return product;
	}
	/**
	 * Search Product using given product code. 
	 * @return Product Object
	 * @param String pcode
	 * @throws SQLException
	 */
	public Product findProductByPCode(String pcode) throws SQLException {
		Product product = null;
		Statement stmt = connection.createStatement();
		try {			
			ResultSet set = stmt.executeQuery(" select * from " + PRODUCT_TABLE + " p, " + TRACK_TABLE + " t "
					+ " where p.product_code = '" +pcode+ "'  and t.product_id = p.product_id ");			
			
			if (set.next()) {
				product = new Product(set.getLong("product_id"), set.getString("product_code"),
						set.getString("product_description"), set.getBigDecimal("product_price"), null);				
				
				product.setTracks(setTracksusingResultset (set,product));				
			}
			set.close();
		} 
			finally {
			stmt.close();
		}
		return product;
	}
	
	private Set<Track> setTracksusingResultset (ResultSet set, Product product) throws SQLException
	{
		
		Set<Track> tracks = new HashSet<Track>();
		do {
			
			Track track = new Track(set.getLong("track_id"), product, set.getInt("track_number"),
					set.getString("title"), set.getString("sample_filename"));
			tracks.add(track);
			
		} while (set.next());
		
		return tracks;
	}
	/**
	 * Search Track using given track id. 
	 * @return Track Object
	 * @param long track id
	 * @throws SQLException
	 */
	public Track findTrackByTID(long trackid) throws SQLException {
		Track track = new Track();

		Statement stmt = connection.createStatement();
		try {
			ResultSet set = stmt.executeQuery("select * from " + TRACK_TABLE + " where track_id = " + trackid);
			if (set.next()) {
				track.setId(set.getLong("track_id"));
				track.setProduct(findProductByPID(set.getInt("product_id")));
				track.setTrackNumber(set.getInt("track_number"));
				track.setTitle(set.getString("title"));
				track.setSampleFilename(set.getString("sample_filename"));
			}

		} finally {
			stmt.close();
		}
		return track;
	}
}
