package za.co.sb.Troll.gui.component.transaction;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.google.common.base.Strings;

import za.co.sb.Troll.dao.CoreBankingSystemDao;
import za.co.sb.Troll.dao.TransactionViewDao;
import za.co.sb.Troll.dto.CoreBankingSystemDto;
import za.co.sb.Troll.gui.component.ConsoleHeaderPanel;

import javax.swing.JTextField;

@SuppressWarnings("serial")
public class TransactionToolsPanel extends JPanel implements ActionListener 
{
	private static String FILTER_ACTION_COMMAND = "FILTER";
	private static String FIND_ACTION_COMMAND = "ENTER";
	
	private TransactionViewPanel transactionViewPanel;
	private ConsoleHeaderPanel consoleHeaderPanel;
	
	// Filter components
	private WhatComboBox whatComboBox;
	private HowComboBox howComboBox;
	private WhenComboBox whenComboBox;
	private JButton filterButton;
	private JPanel filterPanel;
	
	// Find components
	private JLabel lblFind;
	private FindComboBox findOptionComboBox;
	private JTextField findTxt;
	private JPanel searchPanel;
	
	public TransactionToolsPanel(TransactionViewPanel transactionViewPanel, ConsoleHeaderPanel consoleHeaderPanel) throws SQLException 
	{
		super();
		
		this.transactionViewPanel = transactionViewPanel;
		this.consoleHeaderPanel = consoleHeaderPanel;
		
		setLayout(new BorderLayout(0, 0));
		
		// Filter components
		filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		add(filterPanel, BorderLayout.WEST);
		
		JLabel lblWhat = new JLabel("What : ");
		filterPanel.add(lblWhat);
		
		whatComboBox = new WhatComboBox();
		filterPanel.add(whatComboBox);
		
		JLabel lblHow = new JLabel("How : ");
		filterPanel.add(lblHow);
		
		howComboBox = new HowComboBox();
		filterPanel.add(howComboBox);
		
		JLabel lblWhen = new JLabel("When : ");
		filterPanel.add(lblWhen);
		
		whenComboBox = new WhenComboBox();
		filterPanel.add(whenComboBox);
		
		filterButton = new JButton("Filter");
		filterButton.setActionCommand(FILTER_ACTION_COMMAND);
		filterButton.addActionListener(this);
		filterPanel.add(filterButton);
		
		// Search components
		searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		add(searchPanel, BorderLayout.EAST);
		
		lblFind = new JLabel("Find : ");
		searchPanel.add(lblFind);
		
		findOptionComboBox = new FindComboBox();
		searchPanel.add(findOptionComboBox);
		
		findTxt = new JTextField();
		findTxt.setColumns(10);
		findTxt.addActionListener(this);
		findTxt.setActionCommand(FIND_ACTION_COMMAND);
		searchPanel.add(findTxt);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		
		String actionCommand = e.getActionCommand();
		
		if (actionCommand.equals(FILTER_ACTION_COMMAND)) 
		{
			ComboBoxItem selectedWhatComboBoxItem = (ComboBoxItem) whatComboBox.getSelectedItem();
			consoleHeaderPanel.setTitleLabelText(selectedWhatComboBoxItem.getValue());
			
			List<String> filerCriteriaList = new ArrayList<String>();
			if (!Strings.isNullOrEmpty(selectedWhatComboBoxItem.getSqlFilter()))
			{
				filerCriteriaList.add(selectedWhatComboBoxItem.getSqlFilter());
			}
			
			try 
			{
				transactionViewPanel.reload(filerCriteriaList);
			}
			catch (Exception ex) 
			{
				ex.printStackTrace();
				//TODO
				//JOptionPane.showMessageDialog(this, new ErrorScrollPane(ex), "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
		else if (actionCommand.equals(FIND_ACTION_COMMAND))
		{
			ComboBoxItem selectedFindOptionComboBoxItem = (ComboBoxItem) findOptionComboBox.getSelectedItem();
			
			List<String> filerCriteriaList = new ArrayList<String>();
			filerCriteriaList.add(String.format(selectedFindOptionComboBoxItem.getSqlFilter(), findTxt.getText()));
			
			try 
			{
				transactionViewPanel.reload(filerCriteriaList);
			}
			catch (Exception ex) 
			{
				ex.printStackTrace();
				//TODO
				//JOptionPane.showMessageDialog(this, new ErrorScrollPane(ex), "Error", JOptionPane.ERROR_MESSAGE);
			}
			
			
		}
		
		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}
}

@SuppressWarnings("serial")
class WhatComboBox extends JComboBox<ComboBoxItem>
{
	private CoreBankingSystemDao coreBankingSystemDao = new CoreBankingSystemDao();
	
	public WhatComboBox() throws SQLException
	{
		super();
		
		addItem(new ComboBoxItem("All Flows" , ""));
		
		List<String> distinctSystemTypeList = coreBankingSystemDao.selectDistinctSystemTypes();
		List<CoreBankingSystemDto> coreBankingSystemList = coreBankingSystemDao.selectAllCoreBankingSystems();
		
		for (String distinctSystemType : distinctSystemTypeList) 
		{
			addItem(new ComboBoxItem("All " + distinctSystemType, String.format(TransactionViewDao.SYSTEM_TYPE_FILTER, distinctSystemType)));
		}
		
		for (CoreBankingSystemDto coreBankingSystem : coreBankingSystemList) 
		{
			addItem(new ComboBoxItem(coreBankingSystem.getCountry(), String.format(TransactionViewDao.COUNTRY_FILTER, coreBankingSystem.getCountry())));
		}
	}
}

@SuppressWarnings("serial")
class HowComboBox extends JComboBox<ComboBoxItem>
{
	public HowComboBox() throws SQLException
	{
		super();
		
		addItem(new ComboBoxItem("All" , ""));
		addItem(new ComboBoxItem("All Successes" , ""));
		addItem(new ComboBoxItem("All Failures" , ""));
		addItem(new ComboBoxItem("All System Failures" , ""));
		addItem(new ComboBoxItem("All Business Failures" , ""));
	}
}

@SuppressWarnings("serial")
class WhenComboBox extends JComboBox<ComboBoxItem>
{
	public WhenComboBox() throws SQLException
	{
		super();
		
		addItem(new ComboBoxItem("Last 10 Seconds" , ""));
		addItem(new ComboBoxItem("Last 15 Seconds" , ""));
		addItem(new ComboBoxItem("Last 30 Seconds" , ""));
		addItem(new ComboBoxItem("Last Minute" , ""));
		addItem(new ComboBoxItem("Last 5 Minutes" , ""));
		addItem(new ComboBoxItem("Last 5 Minutes" , ""));
		addItem(new ComboBoxItem("Last 10 Minutes" , ""));
		addItem(new ComboBoxItem("Last 15 Minutes" , ""));
		addItem(new ComboBoxItem("Last 30 Minutes" , ""));
		addItem(new ComboBoxItem("Last Hour" , ""));
		addItem(new ComboBoxItem("Last 2 Hours" , ""));
		addItem(new ComboBoxItem("Last 6 Hours" , ""));
		addItem(new ComboBoxItem("Custom" , ""));
	}
}

@SuppressWarnings("serial")
class FindComboBox extends JComboBox<ComboBoxItem>
{
	public FindComboBox() throws SQLException
	{
		super();
		
		addItem(new ComboBoxItem("nBol Transaction Id" , TransactionViewDao.NBOL_TRANSACTION_ID_FILTER));
		addItem(new ComboBoxItem("nBol Instruction Id" , TransactionViewDao.NBOL_INSTRUCTION_ID_FILTER));
		addItem(new ComboBoxItem("nBol Interchange Id" , TransactionViewDao.NBOL_INTERCHANGE_ID_FILTER));
		addItem(new ComboBoxItem("PAYEX Transaction Id" , TransactionViewDao.PAYEX_TRANSACTION_ID_FILTER));
		addItem(new ComboBoxItem("PAYEX Instruction Id" , TransactionViewDao.PAYEX_INSTRUCTION_ID_FILTER));
	}
}

class ComboBoxItem
{
	private String value;
	private String sqlFilter;
	
	public ComboBoxItem(String value, String sqlFilter)
	{
		this.value = value;
		this.sqlFilter = sqlFilter;
	}

	@Override
	public String toString() 
	{
		return value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) 
	{
		this.value = value;
	}

	public String getSqlFilter() 
	{
		return sqlFilter;
	}

	public void setSqlFilter(String sqlFilter) 
	{
		this.sqlFilter = sqlFilter;
	}
}




