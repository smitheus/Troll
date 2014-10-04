package za.co.sb.Troll.gui.component.export;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

import za.co.sb.Troll.dao.TransactionViewDao;
import za.co.sb.Troll.dto.TransactionViewItemDto;

@SuppressWarnings("serial")
public class ExportDialog extends JDialog implements ActionListener
{
	private static final String EXPORT_ACTION_COMMAND = "Export";
	private static final String CANCEL_ACTION_COMMAND = "Cancel";
	
	private final JPanel contentPanel = new JPanel();
	
	private ExportTableModel exportTableModel;
	private ExportTable exportTable;

	public ExportDialog(Map<Integer, TransactionViewItemDto> exportTransactionViewItemDtoMap) 
	{
		super();
		
		setTitle("Export Transactions");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setModal(true);
    	setBounds(100, 100, 800, 300);
    	
		getContentPane().setLayout(new BorderLayout());
		
		contentPanel.setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		exportTableModel = new ExportTableModel(exportTransactionViewItemDtoMap);
		exportTable = new ExportTable(exportTableModel);
		
		JScrollPane scrollPane = new JScrollPane(exportTable);
		contentPanel.add(scrollPane, BorderLayout.CENTER);
		
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		
		// OK button
		JButton btnExport = new JButton("Export");
		btnExport.setActionCommand(EXPORT_ACTION_COMMAND);
		btnExport.addActionListener(this);
		buttonPane.add(btnExport);
		getRootPane().setDefaultButton(btnExport);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setActionCommand(CANCEL_ACTION_COMMAND);
		btnCancel.addActionListener(this);
		buttonPane.add(btnCancel);
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		String actionCommand = e.getActionCommand();
		
		if (actionCommand.equals(EXPORT_ACTION_COMMAND))
		{
			TransactionViewDao transactionViewDao = new TransactionViewDao();
			
			for (TransactionViewItemDto transactionViewItemDto : exportTableModel.getUpdatedTransactionViewItemDtoList())
			{
				try 
				{
					transactionViewDao.updateTransactionViewItemDto(transactionViewItemDto);
				} 
				catch (SQLException sqlex) 
				{
					// TODO Auto-generated catch block
					sqlex.printStackTrace();
				}
			}
		}
		else if (actionCommand.equals(CANCEL_ACTION_COMMAND))
		{
		}
		
		dispose();
	}
}

@SuppressWarnings("serial")
class ExportTable extends JTable
{
	public ExportTable(ExportTableModel exportTableModel)
	{
		super(exportTableModel);
		
		setPreferredScrollableViewportSize(new Dimension(500, 70));
		setFillsViewportHeight(true);
		setShowGrid(false);
		getColumnModel().setColumnMargin(0);
		setRowMargin(0);
		
		JTableHeader header = getTableHeader();
	    header.setBackground(new Color(153, 153, 153));
	    header.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(153, 153, 153)));
	    
	    getColumnModel().getColumn(0).setMinWidth(0);
	    getColumnModel().getColumn(0).setMaxWidth(0);
	    
	    getColumnModel().getColumn(1).setMinWidth(100);
	    getColumnModel().getColumn(1).setMaxWidth(150);
	}
	
	@Override
	public Component prepareRenderer(TableCellRenderer renderer, int row, int column) 
	{
		Component component = super.prepareRenderer(renderer, row, column);    
		JComponent jComponent = (JComponent) component;
	
		if (row % 2 > 0) 
		{
			jComponent.setBackground(new Color(238, 238, 238));
		}
		else
		{
			jComponent.setBackground(new Color(255, 255, 255));
		}
		
		return component;
	}
}

@SuppressWarnings("serial")
class ExportTableModel extends AbstractTableModel  
{
	private static final String[] COLUMN_NAMES = new String[] { "ID", "Transaction ID", "Feedback" };
	
	private Map<Integer, TransactionViewItemDto> exportTransactionViewItemDtoMap;
	private Object[][] data;
	
	public ExportTableModel(Map<Integer, TransactionViewItemDto> exportTransactionViewItemDtoMap)
	{
		super();
		
		this.exportTransactionViewItemDtoMap = exportTransactionViewItemDtoMap;
		this.setupTableData();
	}
	
	public void setupTableData()
	{
		data = new Object[exportTransactionViewItemDtoMap.size()][COLUMN_NAMES.length];
		
		int index = 0;
		for (TransactionViewItemDto transactionViewItemDto : new ArrayList<TransactionViewItemDto>(exportTransactionViewItemDtoMap.values()))
		{
			data[index][0] = transactionViewItemDto.getId();
			data[index][1] = transactionViewItemDto.getTransactionId();
			data[index][2] = transactionViewItemDto.getComments() == null ? "" : transactionViewItemDto.getComments();
			
			index ++;
		}
		
		fireTableDataChanged();
	}
	
	public List<TransactionViewItemDto> getUpdatedTransactionViewItemDtoList()
	{
		for (int index = 0; index < data.length; index ++)
		{
			exportTransactionViewItemDtoMap.get(data[index][0]).setComments(String.valueOf(data[index][2]));
			exportTransactionViewItemDtoMap.get(data[index][0]).setUnderInvestigation(true);
		}
		
		return new ArrayList<TransactionViewItemDto>(exportTransactionViewItemDtoMap.values());
	}
	
	@Override
	public int getColumnCount() 
	{
        return COLUMN_NAMES.length;
    }

	@Override
    public int getRowCount() 
    {
        return data.length;
    }

	@Override
    public String getColumnName(int col) 
    {
        return COLUMN_NAMES[col];
    }

	@Override
    public Object getValueAt(int row, int col) 
	{
        return data[row][col];
    }

    @Override
	public Class<?> getColumnClass(int c) 
    {
    	return getValueAt(0, c).getClass();
    }

    @Override
    public boolean isCellEditable(int row, int col) 
    {
    	if (col == 2) 
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
    	data[row][col] = value;
    	fireTableCellUpdated(row, col);
    }
}


