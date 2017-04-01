package cs4347.jdbcProject.ecomm.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import cs4347.jdbcProject.ecomm.dao.CreditCardDAO;
import cs4347.jdbcProject.ecomm.entity.CreditCard;
import cs4347.jdbcProject.ecomm.util.DAOException;


public class CreditCardDaoImpl implements CreditCardDAO
{
	final static String createCC = "INSERT INTO CREDITCARD (name, ccNumber, expDate, securityCode, Customer_id) VALUE (?, ?, ?, ?, ?);";
	@Override
	public CreditCard create(Connection connection, CreditCard creditCard, Long customerID) throws SQLException, DAOException{
		if(customerID == null){
			throw new DAOException("Trying to insert creditcard with NULL ID");
		}
		PreparedStatement ps = null;
		try{
			ps = connection.prepareStatement(createCC, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, creditCard.getName());
			ps.setString(2, creditCard.getCcNumber());
			ps.setString(3, creditCard.getExpDate());
			ps.setString(4, creditCard.getSecurityCode());
			ps.setLong(5, customerID);
			
			int res = ps.executeUpdate();
			if(res != 1) {
				throw new DAOException("Create Did Not Update Expected Number Of Rows");
			}
		}
		finally{
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
		}
		return creditCard;	
	}
	final static String retrieveCC = "SELECT name, ccNumber, expDate, securityCode FROM CREDITCARD where Customer_id = ?;";
	@Override
	public CreditCard retrieveForCustomerID(Connection connection, Long customerID) throws SQLException, DAOException {
		if (customerID == null) {
			throw new DAOException("Trying to retrieve address with NULL ID");
		}
		PreparedStatement ps = null;
		try{
			ps = connection.prepareStatement(retrieveCC);
			ps.setLong(1, customerID);
			ResultSet rs = ps.executeQuery();
			if (!rs.next()) {
				return null;
			}

			CreditCard cc = new CreditCard();
			cc.setName(rs.getString("name"));
			cc.setCcNumber(rs.getString("ccNumber"));
			cc.setExpDate(rs.getString("expDate"));
			cc.setSecurityCode(rs.getString("securityCode"));
			return cc;
		}
		finally{
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
		}
	}
	final static String deleteAddress = "DELETE FROM CREDITCARD WHERE Customer_id = ?;";
	@Override
	public void deleteForCustomerID(Connection connection, Long customerID) throws SQLException, DAOException {
		
		if(customerID == null){
			throw new DAOException("Trying to delete creditcard with NULL ID");
		}
		PreparedStatement ps = null;
		try{
			ps = connection.prepareStatement(deleteAddress);
			ps.setLong(1, customerID);
			ps.executeUpdate();
		}
		finally{
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
		}
	}
}
