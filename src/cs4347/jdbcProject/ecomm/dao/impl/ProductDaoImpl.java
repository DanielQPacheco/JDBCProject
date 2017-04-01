package cs4347.jdbcProject.ecomm.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import cs4347.jdbcProject.ecomm.dao.ProductDAO;
import cs4347.jdbcProject.ecomm.entity.Product;
import cs4347.jdbcProject.ecomm.util.DAOException;

public class ProductDaoImpl implements ProductDAO
{

	private static final String insertSQL =
			"INSERT INTO category (prodName, prodDescription, prodCategory, prodUPC) "
			+ "VALUES (?, ?, ?, ?);";
	
	private static final String selectSQL =
			"SELECT id, prodName, prodDescription, prodCategory, prodUPC"
			+ "FROM product where id = ?;";
	
	private static String updateSQL = 
			"UPDATE product SET prodName = ?, prodDescription = ?, prodCategory = ?, prodUPC = ? "
			+ "WHERE id = ?;";
	
	final static String deleteSQL =
			"DELETE FROM product WHERE id = ?;";
	
	final static String selectCategorySQL = 
			"SELECT id, prodName, prodDescription, prodCategory, prodUPC"
			+ "FROM product WHERE Category = ?;";
	
	final static String selectUPCSQL = 
			"SELECT id, prodName, prodDescription, prodCategory, prodUPC"
			+ "FROM product WHERE UPC = ?;";
	
	@Override
	public Product create(Connection connection, Product product)
			throws SQLException, DAOException {
		if(product.getId() != null)
		{
			throw new DAOException("Throw to insert Product with NON-NULL ID");
		}
		
		PreparedStatement ps = null;
		try
		{
			ps = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, product.getProdName());
			ps.setString(2, product.getProdDescription());
			ps.setInt(3, product.getProdCategory());
			ps.setString(4, product.getProdUPC());
			
			int res = ps.executeUpdate();
			if(res != 1)
			{
				throw new DAOException("Create Did Not Update Expected Number Of Rows");
			}
			
			ResultSet keyRS = ps.getGeneratedKeys();
			keyRS.next();
			int lastKey = keyRS.getInt(1);
			product.setId((long) lastKey);
			
			return product;
		}
		finally
		{
			if(ps != null && !ps.isClosed())
			{
				ps.close();
			}
		}
	} 
	

	@Override
	public Product retrieve(Connection connection, Long id)
			throws SQLException, DAOException {
		
		if(id == null)
		{
			throw new DAOException("Trying to retrieve Customer with NULL ID");
		}
		
		PreparedStatement ps = null;
		try
		{
			ps = connection.prepareStatement(selectSQL);
			ps.setLong(1, id);
			ResultSet rs = ps.executeQuery();
			if(!rs. next())
			{
				return null;
			}
			
			Product prod = new Product();
			prod.setId(rs.getLong("id"));
			prod.setProdName(rs.getString("prodName"));
			prod.setProdDescription(rs.getString("prodDescription"));
			prod.setProdCategory(rs.getInt("prodCategory"));
			prod.setProdUPC(rs.getString("prodUPC"));
			return prod;
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
	
	
	@Override
	public int update(Connection connection, Product product)
			throws SQLException, DAOException {
		
		if (product.getId() == null)
		{
			throw new DAOException("Trying to update Customer with NULL ID");
		}
		
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(updateSQL);
			ps.setString(1, product.getProdName());
			ps.setLong(2, product.getId());
			ps.setString(3, product.getProdDescription());
			ps.setInt(4, product.getProdCategory());
			ps.setString(2, product.getProdUPC());
			
			
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

	
	
	@Override
	public int delete(Connection connection, Long id) throws SQLException,
			DAOException {
		if (id == null)
		{
			throw new DAOException("Trying to delete Product with NULL ID");
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

	
	
	@Override
	public List<Product> retrieveByCategory(Connection connection, int category)
			throws SQLException, DAOException {
		
		PreparedStatement ps = null;
		try
		{
			ps = connection.prepareStatement(selectCategorySQL);
			ps.setInt(1, category);
			ResultSet rs = ps.executeQuery();

			List<Product> result = new ArrayList<Product>();
			while (rs.next())
			{
				Product prod = new Product();
				prod.setId(rs.getLong("id"));
				prod.setProdName(rs.getString("prodName"));
				prod.setProdDescription(rs.getString("prodDescription"));
				prod.setProdCategory(rs.getInt("proCategory"));
				prod.setProdUPC(rs.getString("prodUPC"));
				result.add(prod);
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

	@Override
	public Product retrieveByUPC(Connection connection, String upc)
			throws SQLException, DAOException {
	
		PreparedStatement ps = null;
		try
		{
			ps = connection.prepareStatement(selectCategorySQL);
			ps.setString(1, upc);
			ResultSet rs = ps.executeQuery();

			List<Product> result = new ArrayList<Product>();
			while (rs.next())
			{
				Product prod = new Product();
				prod.setId(rs.getLong("id"));
				prod.setProdName(rs.getString("prodName"));
				prod.setProdDescription(rs.getString("prodDescription"));
				prod.setProdCategory(rs.getInt("proCategory"));
				prod.setProdUPC(rs.getString("prodUPC"));
				result.add(prod);
			}
			return null;
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


