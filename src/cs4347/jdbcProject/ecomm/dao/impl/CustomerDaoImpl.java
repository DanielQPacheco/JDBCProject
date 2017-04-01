package cs4347.jdbcProject.ecomm.dao.impl;

import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cs4347.jdbcProject.ecomm.dao.CustomerDAO;
import cs4347.jdbcProject.ecomm.entity.Customer;
import cs4347.jdbcProject.ecomm.util.DAOException;

public class CustomerDaoImpl implements CustomerDAO
{
	private static final String insertSQL =
			"INSERT INTO customer (firstName, lastName, gender, dob, email) "
			+ "VALUES (?, ?, ?, ?, ?);";
	
	public Customer create(Connection connection, Customer customer) throws SQLException, DAOException
	{
		if(customer.getId() != null)
		{
			throw new DAOException("Throw to insert Customer with NON-NULL ID");
		}
		
		PreparedStatement ps = null;
		try
		{
			ps = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, customer.getFirstName());
			ps.setString(2, customer.getLastName());
			ps.setString(3, Character.toString(customer.getGender()));
			java.sql.Date sqlDate = new java.sql.Date(customer.getDob().getTime());
			ps.setDate(4, sqlDate);
			ps.setString(5, customer.getEmail());
			
			int res = ps.executeUpdate();
			if(res != 1)
			{
				throw new DAOException("Create Did Not Update Expected Number Of Rows");
			}
			
			// Copies the generated auto-increment primary key to the customer ID
			ResultSet keyRS = ps.getGeneratedKeys();
			keyRS.next();
			int lastKey = keyRS.getInt(1);
			customer.setId((long) lastKey);
			
			return customer;
		}
		finally
		{
			if(ps != null && !ps.isClosed())
			{
				ps.close();
			}
		}
	}

	
	private static final String selectSQL =
			"SELECT id, firstName, lastName, gender, dob, email "
			+ "FROM customer WHERE id = ?;";

	public Customer retrieve(Connection connection, Long id) throws SQLException, DAOException
	{
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
			
			Customer cust = new Customer();
			cust.setId(rs.getLong("id"));
			cust.setFirstName(rs.getString("firstName"));
			cust.setLastName(rs.getString("lastName"));
			cust.setGender(rs.getString("gender").charAt(0));
			cust.setDob(rs.getDate("dob"));
			cust.setEmail(rs.getString("email"));
			
			return cust;
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


	private static String updateSQL = 
			"UPDATE customer SET firstName = ?, lastName = ?, gender = ?, dob = ?, email = ? "
			+ "WHERE id = ?;";
	
	public int update(Connection connection, Customer customer) throws SQLException, DAOException
	{
		if (customer.getId() == null)
		{
			throw new DAOException("Trying to update Customer with NULL ID");
		}
		
		PreparedStatement ps = null;
		try
		{
			ps = connection.prepareStatement(updateSQL);
			ps.setString(1, customer.getFirstName());
			ps.setString(2, customer.getLastName());
			ps.setString(3, Character.toString(customer.getGender()));
			java.sql.Date sqlDate = new java.sql.Date(customer.getDob().getTime());
			ps.setDate(4, sqlDate);
			ps.setString(5, customer.getEmail());
			ps.setLong(6, customer.getId());

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

	
	final static String deleteSQL =
			"DELETE FROM customer WHERE id = ?;";

	public int delete(Connection connection, Long id) throws SQLException, DAOException
	{
		if (id == null)
		{
			throw new DAOException("Trying to delete Customer with NULL ID");
		}
		
		PreparedStatement ps = null;
		try
		{
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
	
	
	final static String selectZipcodeSQL = 
			"SELECT id, firstName, lastName, gender, dob, email "
			+ "FROM customer WHERE zipcode = ?;";

	public List<Customer> retrieveByZipCode(Connection connection, String zipCode) throws SQLException, DAOException
	{
		PreparedStatement ps = null;
		try
		{
			ps = connection.prepareStatement(selectZipcodeSQL);
			ps.setString(1, zipCode);
			ResultSet rs = ps.executeQuery();

			List<Customer> result = new ArrayList<Customer>();
			while (rs.next())
			{
				Customer cust = new Customer();
				cust.setId(rs.getLong("id"));
				cust.setFirstName(rs.getString("firstName"));
				cust.setLastName(rs.getString("lastName"));
				cust.setGender(rs.getString("gender").charAt(0));
				cust.setDob(rs.getDate("dob"));
				cust.setEmail(rs.getString("email"));
				result.add(cust);
			}
			return result;
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
	
	
	final static String selectDobSQL = 
			"SELECT id, firstName, lastName, gender, dob, email  "
			+ "FROM customer WHERE dob BETWEEN ? AND ?;";

	public List<Customer> retrieveByDOB(Connection connection, Date startDate, Date endDate) throws SQLException, DAOException
	{
		PreparedStatement ps = null;
		try
		{
			ps = connection.prepareStatement(selectDobSQL);
			java.sql.Date sqlStartDate = new java.sql.Date(startDate.getTime());
			ps.setDate(1, sqlStartDate);
			java.sql.Date sqlEndDate = new java.sql.Date(endDate.getTime());
			ps.setDate(2, sqlEndDate);
			ResultSet rs = ps.executeQuery();
			
			List<Customer> result = new ArrayList<Customer>();
			while (rs.next())
			{
				Customer cust = new Customer();
				cust.setId(rs.getLong("id"));
				cust.setFirstName(rs.getString("firstName"));
				cust.setLastName(rs.getString("lastName"));
				cust.setGender(rs.getString("gender").charAt(0));
				cust.setDob(rs.getDate("dob"));
				cust.setEmail(rs.getString("email"));
				result.add(cust);
			}
			return result;
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
}
