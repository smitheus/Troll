package za.co.sb.Troll.gui.component.transaction;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.MatteBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import za.co.sb.Troll.dao.TransactionViewDao;
import za.co.sb.Troll.dto.TransactionHistoryViewItemDto;
import za.co.sb.Troll.dto.TransactionViewItemDto;
import za.co.sb.Troll.enums.AckNakEnum;

@SuppressWarnings("serial")
public class TransactionHistoryViewDialog extends JDialog implements ActionListener
{
	private static final String OK_ACTION_COMMAND = "Ok";
		
	private final JPanel contentPanel = new JPanel();
	
	private TransactionHistoryViewTableModel transactionHistoryViewTableModel;
	private TransactionHistoryTable transactionHistoryTable;

	public TransactionHistoryViewDialog(TransactionViewItemDto transactionViewItemDto) 
	{
		super();
		
		setTitle("Transaction History");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setModal(true);
    	setBounds(100, 200, 1700, 500);
    	
		getContentPane().setLayout(new BorderLayout());
		
		contentPanel.setLayout(new BorderLayout());
		contentPanel.setBorder(new MatteBorder(3, 3, 3, 3, Color.BLACK));
		
		transactionHistoryViewTableModel = new TransactionHistoryViewTableModel(transactionViewItemDto);
		
		JPanel headerPanel = new JPanel();
		getContentPane().add(headerPanel, BorderLayout.NORTH);
		
		JLabel titleLabel = new JLabel();
		titleLabel = new JLabel("Transaction History for " + transactionViewItemDto.getTransactionId());
		titleLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
		
		headerPanel.add(titleLabel);
		
		transactionHistoryTable = new TransactionHistoryTable(transactionHistoryViewTableModel);
		
		JScrollPane scrollPane = new JScrollPane(transactionHistoryTable);
		contentPanel.add(scrollPane, BorderLayout.CENTER);
		
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		JPanel buttonPane = new JPanel();
		buttonPane.setBorder(new MatteBorder(0, 3, 3, 3, Color.BLACK));
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		
		// OK button
		JButton btnExport = new JButton("Ok");
		btnExport.setActionCommand(OK_ACTION_COMMAND);
		btnExport.addActionListener(this);
		buttonPane.add(btnExport);
		getRootPane().setDefaultButton(btnExport);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		String actionCommand = e.getActionCommand();
		
		if (actionCommand.equals(OK_ACTION_COMMAND))
		{
		}
		
		dispose();
	}
}

@SuppressWarnings("serial")
class TransactionHistoryViewTableModel extends AbstractTableModel  
{
	private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("dd HH:mm:ss");
	
	private static String[] COLUMN_NAMES = new String[] { "Instruction ID", "Transaction ID", "PES Instruction ID", "PES Transaction ID", "Insert Timestamp", "Source Timestamp", "Source System", "Event", "AckNak", "Text", "ResponseRequired", "SLA 1 Due", "SLA Due", "Elapsed Time", "SLA 1 End", "SLA 2 End", "SLA 1 Breach", "SLA 2 Breach" };
	
	private TransactionViewItemDto transactionViewItemDto;
	private List<TransactionHistoryViewItemDto> transactionHistoryViewItemDtoList = new ArrayList<TransactionHistoryViewItemDto>();
	private Object[][] tableData;
	
	public TransactionHistoryViewTableModel(TransactionViewItemDto transactionViewItemDto)
	{
		super();
		
		this.transactionViewItemDto = transactionViewItemDto;

		try {
			setupTableData();
		} 
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public void setupTableData() throws SQLException
	{
		TransactionViewDao transactionViewDao = new TransactionViewDao();
		
		this.transactionHistoryViewItemDtoList = transactionViewDao.selectTransactionHistoryViewItemDtos(transactionViewItemDto.getInstructionId(), transactionViewItemDto.getTransactionId());
		this.tableData = new Object[transactionHistoryViewItemDtoList.size()][COLUMN_NAMES.length];
		
		int index = 0;
		for (TransactionHistoryViewItemDto transactionHistoryViewItemDto : transactionHistoryViewItemDtoList)
		{
			tableData[index][0] = transactionHistoryViewItemDto.getInstructionId();
			tableData[index][1] = transactionHistoryViewItemDto.getTransactionId(); 
			tableData[index][2] = transactionHistoryViewItemDto.getPesInstructionId(); 
			tableData[index][3] = transactionHistoryViewItemDto.getPesTransactionId(); 
			tableData[index][4] = DATE_FORMAT.format(transactionHistoryViewItemDto.getInsertTimestamp()); 
			tableData[index][5] = DATE_FORMAT.format(transactionHistoryViewItemDto.getSourceTimestamp()); 
			tableData[index][6] = transactionHistoryViewItemDto.getSourceSystem();
			tableData[index][7] = transactionHistoryViewItemDto.getEvent();
			tableData[index][8] = transactionHistoryViewItemDto.getAckNak() == AckNakEnum.UNKNOWN ? "" : transactionHistoryViewItemDto.getAckNak(); 
			tableData[index][9] = transactionHistoryViewItemDto.getText(); 
			tableData[index][10] = transactionHistoryViewItemDto.isResponseRequired();
			tableData[index][11] = transactionHistoryViewItemDto.getSla1Due() == null ? "" : DATE_FORMAT.format(transactionHistoryViewItemDto.getSla1Due()); 
			tableData[index][12] = transactionHistoryViewItemDto.getSla2Due() == null ? "" : DATE_FORMAT.format(transactionHistoryViewItemDto.getSla2Due()); 
			tableData[index][13] = transactionHistoryViewItemDto.getElapsedTime() == null ? "" : TIME_FORMAT.format(new Date(transactionHistoryViewItemDto.getElapsedTime() * 1000)); 
			tableData[index][14] = transactionHistoryViewItemDto.getSla1End();
			tableData[index][15] = transactionHistoryViewItemDto.getSla2End();
			tableData[index][16] = transactionHistoryViewItemDto.isSla1Breach();
			tableData[index][17] = transactionHistoryViewItemDto.isSla2Breach();
			System.out.println(transactionHistoryViewItemDto.getElapsedTime());
			
			index ++;
		}
		
		fireTableDataChanged();
	}

	@Override
	public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

	@Override
    public int getRowCount() {
        return tableData.length;
    }

	@Override
    public String getColumnName(int col) {
        return COLUMN_NAMES[col];
    }

	@Override
    public Object getValueAt(int row, int col) {
        return tableData[row][col];
    }

	@Override
    public Class<?> getColumnClass(int c) 
    {
    	if (getValueAt(0, c) == null) 
    	{
    		return String.class;
    	}
    	
        return getValueAt(0, c).getClass();
    }
	
	@Override
    public boolean isCellEditable(int row, int col) 
	{
    	if (col == 1) 
    	{
    		return true;
    	}
    	else
    	{
    		return false;
    	}
    }
    
	@Override
    public void setValueAt(Object value, int row, int col) 
	{
		tableData[row][col] = value;
        fireTableCellUpdated(row, col);
    }
}

@SuppressWarnings("serial")
class TransactionHistoryTable extends JTable
{
	private TransactionHistoryViewTableModel transactionHistoryViewTableModel;
	
	public TransactionHistoryTable(TransactionHistoryViewTableModel transactionHistoryViewTableModel)
	{
		super(transactionHistoryViewTableModel);
		
		this.transactionHistoryViewTableModel = transactionHistoryViewTableModel;
		
		setPreferredScrollableViewportSize(new Dimension(500, 70));
		setFillsViewportHeight(true);
		
		getColumnModel().setColumnMargin(5);
		setRowMargin(5);
		setRowHeight(20);
		
		JTableHeader header = getTableHeader();
	    header.setBackground(new Color(153, 153, 153));
	    header.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(153, 153, 153)));
	    
	    getColumnModel().getColumn(0).setMinWidth(0);
	    getColumnModel().getColumn(0).setMaxWidth(0);
	    
	    getColumnModel().getColumn(1).setMinWidth(0);
	    getColumnModel().getColumn(1).setMaxWidth(0);
	    
	    getColumnModel().getColumn(4).setMinWidth(0);
	    getColumnModel().getColumn(4).setMaxWidth(0);   
	}
	
	@Override
    public TableCellRenderer getCellRenderer(int row, int column)
    {
		if (getValueAt(row, column) instanceof Boolean)
		{
			return getDefaultRenderer(Boolean.class);
		}
		else
		{
			return getDefaultRenderer(String.class);
		}
    }
	
	@Override
    public TableCellEditor getCellEditor(int row, int column)
    {
		if (getValueAt(row, column) instanceof Boolean)
		{
			return getDefaultEditor(Boolean.class);	
		}
		else
		{
			return getDefaultEditor(String.class);
		}
    }
	
	@Override
	public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {

		Component c = super.prepareRenderer(renderer, row, column);    
		/*JComponent jc = (JComponent)c;
	
		int ggg = getModel().getRowCount();
		
		
		
		if ((row == 0 || row % 3 > 0) && row != ggg)
		{
			setRowHeight(row, 20);
		}
		else
		{
			setRowHeight(row, 22);
			jc.setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, Color.BLACK)); 
		}*/
		
		
		
		
		
		
		/*if ((column >= 1 && column <= 4) || column == 11) 
		{
			jc.setBackground(new Color(238, 238, 238));
		}
		else
		{
			jc.setBackground(new Color(221, 221, 221));
			
		}
		
		if ((column == 2 || column ==3 ) && ((row - 1) % 3 == 0))
		{
			jc.setBackground(new Color(153, 153, 153));
			
		}
		
		
		
		TransactionViewItemDto transactionViewItemDto = tableModel.getTransactionViewItemDto(row);
		
		
		if (column == 1)
		{
			if (transactionViewItemDto.isUnderInvestigation()) 
			{
				jc.setBackground(Color.PINK);
				
			}
			else
			{
				jc.setBackground(new Color(238, 238, 238));
			}
		}
		
		
		// nBol  sent column
		if (column == 5) 
		{
			if (transactionViewItemDto.getNbolSentAckNak() == AckNakEnum.NAK)
			{
				jc.setBackground(Color.PINK);
			}
			else if (transactionViewItemDto.isNbolSentResponseRequired())
			{
				jc.setBackground(Color.RED);
			}
			else
			{
				jc.setBackground(Color.GREEN);
				
			}
			
			
		}*/
		
		return c;
	}
}
