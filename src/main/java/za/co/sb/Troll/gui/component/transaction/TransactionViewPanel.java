package za.co.sb.Troll.gui.component.transaction;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

import za.co.sb.Troll.dao.ChannelTransactionDao;
import za.co.sb.Troll.dao.TransactionViewDao;
import za.co.sb.Troll.dto.ChannelTransactionHistoryDto;
import za.co.sb.Troll.dto.TransactionViewItemDto;


public class TransactionViewPanel extends JPanel 
{
	private JTable table1;
	private TableModel tableModel;
	
	public static String[] mainColumNames = new String[] {"Action", "Transaction ID", "Of Instr:", " fdvbvsdfsf", "NBOL Sent:", "Into PAYEX:", "Out of PAYEX:", "Integration:", "CB RTN (To PES):", "CB RTN: (To NBOL)", "E2E Timings:" };
	public static String[] verticleSubColumNames = {"Country:", "Of Instr:" };
	public static String[] horizontalSubColumNames = {"Timestamp", "Feedback/ACK", "Elapsed time" };

	
	
	/**
	 * Create the panel.
	 */
	public TransactionViewPanel() 
	{
		setLayout(new BorderLayout(0, 0));
		
		setBorder(new MatteBorder(3, 0, 0, 0, (Color) new Color(0, 0, 0)));
		
		tableModel = new TableModel();
		
		table1 = new TransactionTable(tableModel);
		
		
		
		//table1.prepareRenderer(new TableCellRenderer(), 1, 1);
		
		JScrollPane scrollPane = new JScrollPane(table1);
		
		
		add(scrollPane, BorderLayout.CENTER);
		
		

	}
	
	public void reload(List<String> filterCriteriaList)
	{
		try 
		{
			tableModel.setupTableData(filterCriteriaList);
		} 
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}





@SuppressWarnings("serial")
class TransactionTable extends JTable
{
	public TransactionTable(TableModel tableModel)
	{
		super(tableModel);
		
		setPreferredScrollableViewportSize(new Dimension(500, 70));
		setFillsViewportHeight(true);
		setShowGrid(false);
		
		//setGridColor(gridColor);
		
		getColumnModel().setColumnMargin(0);
		setRowMargin(0);
		
		JTableHeader header = getTableHeader();
	    header.setBackground(new Color(153, 153, 153));
	    header.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(153, 153, 153)));
	      
	}
	
	

	@Override
	public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {

		Component c = super.prepareRenderer(renderer, row, column);    
		JComponent jc = (JComponent)c;
	
		int ggg = getModel().getRowCount();
		
		
		
		if ((row == 0 || row % 3 > 0) && row != ggg)
		{
			setRowHeight(row, 20);
		}
		else
		{
			setRowHeight(row, 22);
			jc.setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, Color.BLACK)); 
		}
		
		
		
		if (column == 3)
		{
			
		}
		
		
		
		if ((column >= 0 && column <= 3) || column == 10) 
		{
			jc.setBackground(new Color(238, 238, 238));
		}
		else
		{
			jc.setBackground(new Color(221, 221, 221));
			
		}
		
		if ((column == 1 || column ==2 ) && ((row - 1) % 3 == 0))
		{
			jc.setBackground(new Color(153, 153, 153));
			
		}
		
		return c;
	}
	
	
	
}





@SuppressWarnings("serial")
class TableModel extends DefaultTableModel  
{
	
	
	public TableModel()
	{
		super();
		
		this.setColumnIdentifiers(new String[] {"Action", "Transaction ID", "Of Instr:", "", "NBOL Sent:", "Into PAYEX:", "Out of PAYEX:", "Integration:", "CB RTN (To PES):", "CB RTN: (To NBOL)", "E2E Timings:" });
		
		
		try {
			this.setupTableData(new ArrayList<String>());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	
	public void setupTableData(List<String> filterCriteriaList) throws SQLException
	{
		getDataVector().removeAllElements();
		
		Object[][] data;
		
		
		TransactionViewDao transactionViewDao = new TransactionViewDao();
		
		
		List<TransactionViewItemDto> list = transactionViewDao.selectTransactionViewItemDtos(filterCriteriaList);
		
		for (TransactionViewItemDto transactionViewItemDto : list) 
		{
			
			data = new Object[3][TransactionViewPanel.mainColumNames.length];
			
			// Add TRANSACTION data to table
			// Row 1
			data[0][0] = "";
			data[0][1] = transactionViewItemDto.getTransactionId();
			data[0][2] = transactionViewItemDto.getInstructionId();
			data[0][3] = "<html><i>Timestamp:</i><html>";
			
			//Row 2
			data[1][0] = "";
			data[1][1] = "Country: " + transactionViewItemDto.getCountry();
			data[1][2] = "Of InterCh:";
			data[1][3] = "<html><i>Feedback/ACK:</i><html>";
			
			//Row 3
			data[2][0] = "";
			data[2][1] = "";
			data[2][2] = transactionViewItemDto.getInterchangeId();
			data[2][3] = "<html><i>Elapsed Time:</i><html>";
			
			/*for (ChannelTransactionHistoryDto channelTransactionHistoryDto : channelTransactionDto.getChannelTransactionHistoryDtoList())
			{
				switch (channelTransactionHistoryDto.getEvent()) 
				{
		            case SENT :
		            	if ("NBOL".equals(channelTransactionHistoryDto.getSourceSystem()))
		            	{
		            		data[0][4] = channelTransactionHistoryDto.getSourceTimestamp();
		        			//data[1][4] = "";
		        			//data[2][4] = "";
		            	}
		            	else if ("PAYEX".equals(channelTransactionHistoryDto.getSourceSystem()))
		            	{ 
		            		data[0][6] = channelTransactionHistoryDto.getSourceTimestamp();
		        			data[1][6] = channelTransactionHistoryDto.getPesTransactionId();
		        			//data[2][6] = "";
		            	}
		            	
		            	break;
		            case RECD :
		            	if ("PAYEX".equals(channelTransactionHistoryDto.getSourceSystem()))
		            	{
		            		data[0][5] = channelTransactionHistoryDto.getSourceTimestamp();
		        			//data[1][4] = "";
		        			//data[2][4] = "";
		            	}
		            	else
		            	{
		            		
		            	}
		            	
		            	break;
		            case CREATE :
		            	if ("PAYEX".equals(channelTransactionHistoryDto.getSourceSystem()))
		            	{
		            		//data[0][4] = channelTransactionDto.getSourceTimestamp();
		        			data[1][6] = channelTransactionHistoryDto.getPesInstructionId();
		        			//data[2][4] = channelTransactionHistoryDto.getSourceTimestamp();
		            	}
		            	else
		            	{
		            		
		            	}
		            	
		            	break;
		            	
		            case MAX :
		            	if ("PAYEX".equals(channelTransactionHistoryDto.getSourceSystem()))
		            	{
		            		data[0][7] = channelTransactionHistoryDto.getSourceTimestamp();
		        			data[1][7] = channelTransactionHistoryDto.getAckNak();
		        			//data[2][4] = channelTransactionHistoryDto.getSourceTimestamp();
		            	}
		            	else
		            	{
		            		
		            	}
		            	
		            	break;		  
		            	
		            case CORE :
		            	if ("PAYEX".equals(channelTransactionHistoryDto.getSourceSystem()))
		            	{
		            		data[0][8] = channelTransactionHistoryDto.getSourceTimestamp();
		        			data[1][8] = channelTransactionHistoryDto.getAckNak() + " " + channelTransactionHistoryDto.getText();
		        			//data[2][4] = channelTransactionHistoryDto.getSourceTimestamp();
		            	}
		            	else
		            	{
		            		
		            	}
		            	
		            	break;		  		            	
		            	
		            default : 
		            	
		        }
				
				
				
			}*/
			
			addRow(data[0]);
			addRow(data[1]);
			addRow(data[2]);
		}
		
		
		
		fireTableDataChanged();
	} 
	
	
	
}

@SuppressWarnings("serial")
class CTableCellRenderer extends DefaultTableCellRenderer
{
	private static final int STATUS_COL = 4;
	
	
	
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) 
    {
    	
    	
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);

        
        return this;
    }   
}



