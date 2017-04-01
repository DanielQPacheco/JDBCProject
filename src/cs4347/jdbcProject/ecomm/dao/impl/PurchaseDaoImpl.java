package cs4347.jdbcProject.ecomm.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import cs4347.jdbcProject.ecomm.dao.PurchaseDAO;
import cs4347.jdbcProject.ecomm.entity.Customer;
import cs4347.jdbcProject.ecomm.entity.Purchase;
import cs4347.jdbcProject.ecomm.services.PurchaseSummary;
import cs4347.jdbcProject.ecomm.util.DAOException;

public class PurchaseDaoImpl implements PurchaseDAO
{
	private static final String insertSQL =
			"INSERT INTO purchase (productID, customerID, purchaseDate, purchaseAmount) "
			+ "VALUES (?, ?, ?, ?);";
	
	private static final String selectSQL =
			"SELECT id, productID, customerID, purchaseDate, purchaseAmount "
			+ "FROM purchase WHERE id = ?;";
	
	private static String updateSQL = 
			"UPDATE purchase SET productID = ?, customerID = ?, purchaseDate = ?, purchaseAmount = ? "
			+ "WHERE id = ?;";
	
	final static String deleteSQL =
			"DELETE FROM purchase WHERE id = ?;";
	
	final static String selectCustomerIDSQL = 
			"SELECT id, productID, customerID, purchaseDate, purchaseAmount "
			+ "FROM purchase WHERE customerID = ?;";
	
	final static String selectProductIDSQL = 
			"SELECT id, productID, customerID, purchaseDate, purchaseAmount "
			+ "FROM purchase WHERE productID = ?;";
	
	final static String selectPurchaseSummarySQL = 
			"SELECT purchaseAmount "
			+ "FROM purchase WHERE customerID = ?;";

	public Purchase create(Connection connection, Purchase purchase) throws SQLException, DAOException
	{
		if(purchase.getId() != null)
		{
			throw new DAOException("Throw to insert Purchase with NON-NULL ID");
		}
		
		PreparedStatement ps = null;
		try
		{
			ps = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
			ps.setLong(1, purchase.getProductID());
			ps.setLong(2, purchase.getCustomerID());
			java.sql.Date sqlDate = new java.sql.Date(purchase.getPurchaseDate().getTime());
			ps.setDate(3, sqlDate);
			ps.setString(4, Double.toString(purchase.getPurchaseAmount()));
			
			int res = ps.executeUpdate();
			if(res != 1)
			{
				throw new DAOException("Create Did Not Update Expected Number Of Rows");
			}
			
			// Copies the generated auto-increment primary key to the customer ID
			ResultSet keyRS = ps.getGeneratedKeys();
			keyRS.next();
			int lastKey = keyRS.getInt(1);
			purchase.setId((long) lastKey);
			
			return purchase;
		}
		finally
		{
			if(ps != null && !ps.isClosed())
			{
				ps.close();
			}
		}
	}


	public Purchase retrieve(Connection connection, Long id) throws SQLException, DAOException
	{
		if(id == null)
		{
			throw new DAOException("Trying to retrieve Purchase with NULL ID");
		}
		
		PreparedStatement ps = null;
		try
		{
			ps = connection.prepareStatement(selectSQL);
			ps.setLong(1, id);
			ResultSet rs = ps.executeQuery();
			if(!rs.next())
			{
				return null;
			}
			
			Purchase purc = new Purchase();
			purc.setId(rs.getLong("id"));
			purc.setProductID(rs.getLong("productID"));
			purc.setCustomerID(rs.getLong("customerID"));
			purc.setPurchaseDate(rs.getDate("purchaseDate"));
			purc.setPurchaseAmount(rs.getDouble("purchaseAmount"));
			
			return purc;
		}
		finally
		{
			if(ps != null & !ps.isClosed())
			{
				ps.close();
			}
			if(connection != null && !connection.isClosed())
			{
				connection.close();
			}
		}
	}


	public int update(Connection connection, Purchase purchase) throws SQLException, DAOException
	{

		if (purchase.getId() == null)
		{
			throw new DAOException("Trying to update Purchase with NULL ID");
		}
		
		PreparedStatement ps = null;
		try
		{
			ps = connection.prepareStatement(updateSQL);
			ps.setLong(1, purchase.getProductID());
			ps.setLong(2, purchase.getCustomerID());
			java.sql.Date sqlDate = new java.sql.Date(purchase.getPurchaseDate().getTime());
			ps.setDate(3, sqlDate);
			ps.setDouble(4, purchase.getPurchaseAmount());
			ps.setLong(5, purchase.getId());

			int rows = ps.executeUpdate();
			return rows;
		}
		finally
		{
			if (ps != null && !ps.isClosed())
			{
				ps.close();
			}
			if (connection != null && !connection.isClosed())
			{
				connection.close();
			}
		}
	}


	public int delete(Connection connection, Long id) throws SQLException, DAOException
	{
		if (id == null)
		{
			throw new DAOException("Trying to delete Purchase with NULL ID");
		}
		
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(deleteSQL);
			ps.setLong(1, id);

			int rows = ps.executeUpdate();
			return rows;
		}
		finally
		{
			if (ps != null && !ps.isClosed())
			{
				ps.close();
			}
			if (connection != null && !connection.isClosed())
			{
				connection.close();
			}
		}
	}


	public List<Purchase> retrieveForCustomerID(Connection connection, Long customerID) throws SQLException, DAOException
	{
		PreparedStatement ps = null;
		try
		{
			ps = connection.prepareStatement(selectCustomerIDSQL);
			ps.setLong(1, customerID);
			ResultSet rs = ps.executeQuery();

			List<Purchase> result = new ArrayList<Purchase>();
			while (rs.next())
			{
				Purchase purc = new Purchase();
				purc.setId(rs.getLong("id"));
				purc.setProductID(rs.getLong("productID"));
				purc.setCustomerID(rs.getLong("customerID"));
				purc.setPurchaseDate(rs.getDate("purchaseDate"));
				purc.setPurchaseAmount(rs.getDouble("purchaseAmount"));
				result.add(purc);
			}
			return result;
		}
		finally {
			if (ps != null && !ps.isClosed())
			{
				ps.close();
			}
			if (connection != null && !connection.isClosed())
			{
				connection.close();
			}
		}
	}
	
	//final static String selectProductIDSQL = 
		//	"SELECT id, productID, customerID, purchaseDate, purchaseAmount "
		//	+ "FROM purchase WHERE productID = ?;";


	public List<Purchase> retrieveForProductID(Connection connection, Long productID) throws SQLException, DAOException
	{
		PreparedStatement ps = null;
		try
		{
			ps = connection.prepareStatement(selectCustomerIDSQL);
			ps.setLong(1, productID);
			ResultSet rs = ps.executeQuery();

			List<Purchase> result = new ArrayList<Purchase>();
			while (rs.next())
			{
				Purchase purc = new Purchase();
				purc.setId(rs.getLong("id"));
				purc.setProductID(rs.getLong("productID"));
				purc.setCustomerID(rs.getLong("customerID"));
				purc.setPurchaseDate(rs.getDate("purchaseDate"));
				purc.setPurchaseAmount(rs.getDouble("purchaseAmount"));
				result.add(purc);
			}
			return result;
		}
		finally {
			if (ps != null && !ps.isClosed())
			{
				ps.close();
			}
			if (connection != null && !connection.isClosed())
			{
				connection.close();
			}
		}
	}


	public PurchaseSummary retrievePurchaseSummary(Connection connection, Long customerID) throws SQLException, DAOException
	{
		PreparedStatement ps = null;
		try
		{
			ps = connection.prepareStatement(selectCustomerIDSQL);
			ps.setLong(1, customerID);
			ResultSet rs = ps.executeQuery();
			
			double minPurchase = Double.MAX_VALUE;
			double maxPurchase = Double.MIN_VALUE;
			double totalPurchase = 0;
			int numberPurchases = 0;

			while (rs.next())
			{
				double currentAmount = rs.getDouble("purchaseAmount");
				
				if(currentAmount < minPurchase)
					minPurchase = currentAmount;
				if(currentAmount > maxPurchase)
					maxPurchase = currentAmount;
				
				totalPurchase = totalPurchase + currentAmount;
				numberPurchases++;
			}
			
			if(numberPurchases != 0)
			{
				PurchaseSummary purcSum = new PurchaseSummary();
				purcSum.minPurchase = (float)minPurchase;
				purcSum.maxPurchase = (float)maxPurchase;
				purcSum.avgPurchase = (float)(totalPurchase / numberPurchases);
				return purcSum;
			}
			else
			{
				return null;
			}
		}
		finally {
			if (ps != null && !ps.isClosed())
			{
				ps.close();
			}
			if (connection != null && !connection.isClosed())
			{
				connection.close();
			}
		}
	}
	
}
