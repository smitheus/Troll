package za.co.sb.Troll.gui.component.transaction;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import za.co.sb.Troll.dao.CoreBankingSystemDao;
import za.co.sb.Troll.dto.CoreBankingSystemDto;

@SuppressWarnings("serial")
public class TransactionToolsPanel extends JPanel implements ActionListener 
{
	private static String FILTER_ACTION_COMMAND = "FILTER";
	
	private TransactionViewPanel transactionViewPanel;
	
	private WhatComboBox whatComboBox;
	private JPanel filterPanel;
	private JPanel searchPanel;
	private JButton filterButton;

	public TransactionToolsPanel(TransactionViewPanel transactionViewPanel) throws SQLException 
	{
		this.transactionViewPanel = transactionViewPanel;
		
		setLayout(new BorderLayout(0, 0));
		
		filterPanel = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) filterPanel.getLayout();
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		add(filterPanel, BorderLayout.WEST);
		JLabel lblWhat = new JLabel("What : ");
		filterPanel.add(lblWhat);
		
		whatComboBox = new WhatComboBox();
		filterPanel.add(whatComboBox);
		
		filterButton = new JButton("Filter");
		filterButton.setActionCommand(FILTER_ACTION_COMMAND);
		filterPanel.add(filterButton);
		
		searchPanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) searchPanel.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		add(searchPanel, BorderLayout.EAST);
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		
		String actionCommand = e.getActionCommand();
		
		if (actionCommand.equals(FILTER_ACTION_COMMAND)) 
		{
			try 
			{
				transactionViewPanel.reload();
			}
			catch (Exception ex) 
			{
				//JOptionPane.showMessageDialog(this, new ErrorScrollPane(ex), "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
		
		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}
}

@SuppressWarnings("serial")
class WhatComboBox extends JComboBox<String>
{
	private CoreBankingSystemDao coreBankingSystemDao = new CoreBankingSystemDao();
	
	public WhatComboBox() throws SQLException
	{
		super(new String[] { "All Flows" });
		
		List<String> distinctSystemTypeList = coreBankingSystemDao.selectDistinctSystemTypes();
		List<CoreBankingSystemDto> coreBankingSystemList = coreBankingSystemDao.selectAllCoreBankingSystems();
		
		for (String distinctSystemType : distinctSystemTypeList) 
		{
			addItem("All " + distinctSystemType);
		}
		
		for (CoreBankingSystemDto coreBankingSystem : coreBankingSystemList) 
		{
			addItem(coreBankingSystem.getCountry());
		}
	}
}

@SuppressWarnings("serial")
class HowComboBox extends JComboBox<String>
{
	private CoreBankingSystemDao coreBankingSystemDao = new CoreBankingSystemDao();
	
	public HowComboBox() throws SQLException
	{
		super(new String[] { "All Flows" });
		
		List<String> distinctSystemTypeList = coreBankingSystemDao.selectDistinctSystemTypes();
		List<CoreBankingSystemDto> coreBankingSystemList = coreBankingSystemDao.selectAllCoreBankingSystems();
		
		for (String distinctSystemType : distinctSystemTypeList) 
		{
			addItem("All " + distinctSystemType);
		}
		
		for (CoreBankingSystemDto coreBankingSystem : coreBankingSystemList) 
		{
			addItem(coreBankingSystem.getCountry());
		}
	}
}
/*
@SuppressWarnings("serial")
class WhenComboBox extends JComboBox<String>
{
	private static String[] VALUES = { "Last"
										
	};

	
	public WhenComboBox()
	{
		super(new String[] { "All Flows" });
		
		List<String> distinctSystemTypeList = coreBankingSystemDao.selectDistinctSystemTypes();
		List<CoreBankingSystemDto> coreBankingSystemList = coreBankingSystemDao.selectAllCoreBankingSystems();
		
		for (String distinctSystemType : distinctSystemTypeList) 
		{
			addItem("All " + distinctSystemType);
		}
		
		for (CoreBankingSystemDto coreBankingSystem : coreBankingSystemList) 
		{
			addItem(coreBankingSystem.getCountry());
		}
	}
}*/


