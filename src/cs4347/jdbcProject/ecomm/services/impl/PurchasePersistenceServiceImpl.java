package cs4347.jdbcProject.ecomm.services.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import cs4347.jdbcProject.ecomm.dao.AddressDAO;
import cs4347.jdbcProject.ecomm.dao.CreditCardDAO;
import cs4347.jdbcProject.ecomm.dao.CustomerDAO;
import cs4347.jdbcProject.ecomm.dao.PurchaseDAO;
import cs4347.jdbcProject.ecomm.dao.impl.AddressDaoImpl;
import cs4347.jdbcProject.ecomm.dao.impl.CreditCardDaoImpl;
import cs4347.jdbcProject.ecomm.dao.impl.CustomerDaoImpl;
import cs4347.jdbcProject.ecomm.dao.impl.PurchaseDaoImpl;
import cs4347.jdbcProject.ecomm.entity.Customer;
import cs4347.jdbcProject.ecomm.entity.Purchase;
import cs4347.jdbcProject.ecomm.services.PurchasePersistenceService;
import cs4347.jdbcProject.ecomm.services.PurchaseSummary;
import cs4347.jdbcProject.ecomm.util.DAOException;

public class PurchasePersistenceServiceImpl implements PurchasePersistenceService
{
	private DataSource dataSource;

	public PurchasePersistenceServiceImpl(DataSource dataSource)
	{
		this.dataSource = dataSource;
	}

	@Override
	public Purchase create(Purchase purchase) throws SQLException, DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Purchase retrieve(Long id) throws SQLException, DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(Purchase purchase) throws SQLException, DAOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(Long id) throws SQLException, DAOException
	{
		PurchaseDAO purchaseDAO = new PurchaseDaoImpl();
		Connection connection = dataSource.getConnection();
		
		try
		{
			connection.setAutoCommit(false);
			int rows = purchaseDAO.delete(connection, id);
			
			if (purchaseDAO.retrieveForCustomerID(connection, id) == null)
			{
				throw new DAOException("Customers must include an Address instance.");
			}

			connection.commit();
			return rows;
		}
		catch (Exception ex)
		{
			connection.rollback();
			throw ex;
		}
		finally {
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
	public List<Purchase> retrieveForCustomerID(Long customerID) throws SQLException, DAOException
	{
		PurchaseDAO purchaseDAO = new PurchaseDaoImpl();
		Connection connection = dataSource.getConnection();
		
		try
		{
			List<Purchase> purcList = purchaseDAO.retrieveForCustomerID(connection, customerID);
			return purcList;
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
	public PurchaseSummary retrievePurchaseSummary(Long customerID) throws SQLException, DAOException
	{
		PurchaseDAO purchaseDAO = new PurchaseDaoImpl();
		Connection connection = dataSource.getConnection();
		
		try
		{
			PurchaseSummary ps = purchaseDAO.retrievePurchaseSummary(connection, customerID);
			return ps;
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
	public List<Purchase> retrieveForProductID(Long productID) throws SQLException, DAOException
	{
		PurchaseDAO purchaseDAO = new PurchaseDaoImpl();
		Connection connection = dataSource.getConnection();
		
		try
		{
			List<Purchase> prodList = purchaseDAO.retrieveForProductID(connection, productID);

			if(prodList.size() == 0)
				return null;

			return prodList;
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
