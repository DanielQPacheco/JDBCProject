package cs4347.jdbcProject.ecomm.services.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.ListIterator;

import javax.sql.DataSource;

import java.sql.PreparedStatement;

import cs4347.jdbcProject.ecomm.dao.AddressDAO;
import cs4347.jdbcProject.ecomm.dao.CreditCardDAO;
import cs4347.jdbcProject.ecomm.dao.CustomerDAO;
import cs4347.jdbcProject.ecomm.dao.impl.AddressDaoImpl;
import cs4347.jdbcProject.ecomm.dao.impl.CreditCardDaoImpl;
import cs4347.jdbcProject.ecomm.dao.impl.CustomerDaoImpl;
import cs4347.jdbcProject.ecomm.entity.Address;
import cs4347.jdbcProject.ecomm.entity.CreditCard;
import cs4347.jdbcProject.ecomm.entity.Customer;
import cs4347.jdbcProject.ecomm.services.CustomerPersistenceService;
import cs4347.jdbcProject.ecomm.util.DAOException;

public class CustomerPersistenceServiceImpl implements CustomerPersistenceService
{
	private DataSource dataSource;

	public CustomerPersistenceServiceImpl(DataSource dataSource)
	{
		this.dataSource = dataSource;
	}
	
	/**
	 * This method provided as an example of transaction support across multiple inserts.
	 * 
	 * Persists a new Customer instance by inserting new Customer, Address, 
	 * and CreditCard instances. Notice the transactional nature of this 
	 * method which inludes turning off autocommit at the start of the 
	 * process, and rolling back the transaction if an exception 
	 * is caught. 
	 */
	@Override
	public Customer create(Customer customer) throws SQLException, DAOException
	{
		CustomerDAO customerDAO = new CustomerDaoImpl();
		AddressDAO addressDAO = new AddressDaoImpl();
		CreditCardDAO creditCardDAO = new CreditCardDaoImpl();

		Connection connection = dataSource.getConnection();
		try {
			connection.setAutoCommit(false);
			Customer cust = customerDAO.create(connection, customer);
			Long custID = cust.getId();

			if (cust.getAddress() == null) {
				throw new DAOException("Customers must include an Address instance.");
			}
			Address address = cust.getAddress();
			addressDAO.create(connection, address, custID);

			if (cust.getCreditCard() == null) {
				throw new DAOException("Customers must include an CreditCard instance.");
			}
			CreditCard creditCard = cust.getCreditCard();
			creditCardDAO.create(connection, creditCard, custID);

			connection.commit();
			return cust;
		}
		catch (Exception ex) {
			connection.rollback();
			throw ex;
		}
		finally {
			if (connection != null) {
				connection.setAutoCommit(true);
			}
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		}
	}

	@Override
	public Customer retrieve(Long id) throws SQLException, DAOException {
		CustomerDAO customerDAO = new CustomerDaoImpl();
		AddressDAO addressDAO = new AddressDaoImpl();
		CreditCardDAO creditCardDAO = new CreditCardDaoImpl();

		Connection connection = dataSource.getConnection();
		try {
			connection.setAutoCommit(false);
			Customer cust = customerDAO.retrieve(connection, id);
			Long custID = cust.getId();
			cust.setAddress(addressDAO.retrieveForCustomerID(connection, custID));	
			cust.setCreditCard(creditCardDAO.retrieveForCustomerID(connection, custID));
			if (cust.getCreditCard() == null) {
				throw new DAOException("Customers must include an CreditCard instance.");
			}
			if (cust.getAddress() == null) {
				throw new DAOException("Customers must include an Address instance.");
			}
			connection.commit();
			return cust;
		}
		catch (Exception ex) {
			connection.rollback();
			throw ex;
		}
		finally {
			if (connection != null) {
				connection.setAutoCommit(true);
			}
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		}
	}

	@Override
	public int update(Customer customer) throws SQLException, DAOException {
		CustomerDAO customerDAO = new CustomerDaoImpl();
		AddressDAO addressDAO = new AddressDaoImpl();
		CreditCardDAO creditCardDAO = new CreditCardDaoImpl();

		Connection connection = dataSource.getConnection();
		try {
			connection.setAutoCommit(false);
			int rows = customerDAO.update(connection, customer);
			if (customer.getAddress() == null) {
				throw new DAOException("Customers must include an Address instance.");
			}
			addressDAO.create(connection, customer.getAddress(), customer.getId());

			if (customer.getCreditCard() == null) {
				throw new DAOException("Customers must include an CreditCard instance.");
			}
			creditCardDAO.create(connection, customer.getCreditCard(), customer.getId());
			connection.commit();
			return rows;
		}
		catch (Exception ex) {
			connection.rollback();
			throw ex;
		}
		finally {
			if (connection != null) {
				connection.setAutoCommit(true);
			}
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		}
	}

	@Override
	public int delete(Long id) throws SQLException, DAOException {
		CustomerDAO customerDAO = new CustomerDaoImpl();
		AddressDAO addressDAO = new AddressDaoImpl();
		CreditCardDAO creditCardDAO = new CreditCardDaoImpl();

		Connection connection = dataSource.getConnection();
		try {
			connection.setAutoCommit(false);
			int rows = customerDAO.delete(connection, id);
//			if (addressDAO.retrieveForCustomerID(connection, id) == null) {
//				throw new DAOException("Customers must include an Address instance.");
//			}
			addressDAO.deleteForCustomerID(connection, id);

//			if (creditCardDAO.retrieveForCustomerID(connection, id) == null) {
//				throw new DAOException("Customers must include an CreditCard instance.");
//			}
			creditCardDAO.deleteForCustomerID(connection, id);
			connection.commit();
			return rows;
		}
		catch (Exception ex) {
			connection.rollback();
			throw ex;
		}
		finally {
			if (connection != null) {
				connection.setAutoCommit(true);
			}
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		}
	}

	@Override
	public List<Customer> retrieveByZipCode(String zipCode) throws SQLException, DAOException
	{
		CustomerDAO customerDAO = new CustomerDaoImpl();
		Connection connection = dataSource.getConnection();
		
		try
		{
			List<Customer> custList = customerDAO.retrieveByZipCode(connection, zipCode);

			if(custList.size() == 0)
				return null;

			return custList;
		}
		catch (Exception ex)
		{
			connection.rollback();
			throw ex;
		}
		finally
		{
			if (connection != null)
			{
				connection.setAutoCommit(true);
			}
			if (connection != null && !connection.isClosed())
			{
				connection.close();
			}
		}
	}

	@Override
	public List<Customer> retrieveByDOB(Date startDate, Date endDate) throws SQLException, DAOException
	{
		CustomerDAO customerDAO = new CustomerDaoImpl();
		Connection connection = dataSource.getConnection();
		
		try
		{
			List<Customer> custList = customerDAO.retrieveByDOB(connection, startDate, endDate);

			if(custList.size() == 0)
				return null;

			return custList;
		}
		catch (Exception ex)
		{
			connection.rollback();
			throw ex;
		}
		finally
		{
			if (connection != null)
			{
				connection.setAutoCommit(true);
			}
			if (connection != null && !connection.isClosed())
			{
				connection.close();
			}
		}
	}
}
