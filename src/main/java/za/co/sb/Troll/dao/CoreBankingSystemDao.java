package za.co.sb.Troll.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import za.co.sb.Troll.db.ConnectionFactory;
import za.co.sb.Troll.db.DbUtil;
import za.co.sb.Troll.dto.CoreBankingSystemDto;

public class CoreBankingSystemDao 
{
	private static String SELECT_ALL_STATEMENT = "SELECT * FROM coreBankingSystems;";
	private static String SELECT_ALL_DISTINCT_SYSTEM_TYPE_STATEMENT = "SELECT DISTINCT(systemType) FROM coreBankingSystems;";
	
	private Connection connection;
	
	public List<CoreBankingSystemDto> selectAllCoreBankingSystems() throws SQLException
    {
		List<CoreBankingSystemDto> coreBankingSystemDtoList = new ArrayList<CoreBankingSystemDto>();
		
    	try 
    	{
            connection = ConnectionFactory.getConnection();
            
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_STATEMENT);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next())
            {
            	CoreBankingSystemDto coreBankingSystemDto = new CoreBankingSystemDto();
            	
            	coreBankingSystemDto.setSystemCode(resultSet.getString(1));
            	coreBankingSystemDto.setSystemType(resultSet.getString(2));
            	coreBankingSystemDto.setCountry(resultSet.getString(3));
            	
            	coreBankingSystemDtoList.add(coreBankingSystemDto);
            }
        } 
    	catch (SQLException sqlex) 
        {
            throw sqlex;
        }
        finally 
        {
            DbUtil.close(connection);
        }
    	
    	return coreBankingSystemDtoList;
    }
	
	public List<String> selectDistinctSystemTypes() throws SQLException
    {
		List<String> distinctSystemTypeList = new ArrayList<String>();
		
    	try 
    	{
            connection = ConnectionFactory.getConnection();
            
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_DISTINCT_SYSTEM_TYPE_STATEMENT);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next())
            {
            	distinctSystemTypeList.add(resultSet.getString(1));
            }
        } 
    	catch (SQLException sqlex) 
        {
            throw sqlex;
        }
        finally 
        {
            DbUtil.close(connection);
        }
    	
    	return distinctSystemTypeList;
    }
}
