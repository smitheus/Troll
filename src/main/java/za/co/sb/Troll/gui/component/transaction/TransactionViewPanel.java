package za.co.sb.Troll.gui.component.transaction;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import za.co.sb.Troll.dao.TransactionViewDao;
import za.co.sb.Troll.dto.TransactionViewItemDto;
import za.co.sb.Troll.gui.component.TrollConsoleFrame;

@SuppressWarnings("serial")
public class TransactionViewPanel extends JPanel implements TableModelListener
{
	private TrollConsoleFrame trollConsoleFrame;
	
	private TransactionViewTable transactionViewTable;
	private TransactionViewTableModel transactionViewTableModel;
	
	public static String[] verticleSubColumNames = {"Country:", "Of Instr:" };
	public static String[] horizontalSubColumNames = {"Timestamp", "Feedback/ACK", "Elapsed time" };

	/**
	 * Create the panel.
	 */
	public TransactionViewPanel(TrollConsoleFrame trollConsoleFrame) 
	{
		super();
		
		this.trollConsoleFrame = trollConsoleFrame;
		
		setLayout(new BorderLayout(0, 0));
		setBorder(new MatteBorder(3, 0, 0, 0, (Color) new Color(0, 0, 0)));
		
		transactionViewTableModel = new TransactionViewTableModel();
		transactionViewTableModel.addTableModelListener(this);
		
		transactionViewTable = new TransactionViewTable(transactionViewTableModel);
	
		JScrollPane scrollPane = new JScrollPane(transactionViewTable);
		add(scrollPane, BorderLayout.CENTER);
	}
	
	public void reload(List<String> filterCriteriaList,  String successFailureFilter)
	{
		try 
		{
			transactionViewTableModel.setupTableData(filterCriteriaList, successFailureFilter);
		} 
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Map<Integer, TransactionViewItemDto> getSelectedTransactionViewItemDto()
	{
		return this.transactionViewTableModel.getSelectedTransactionViewItemDto();
	}
	
	
	@Override
	public void tableChanged(TableModelEvent e) 
	{
		trollConsoleFrame.getHeaderPanel().setBtnExportEnabled(transactionViewTableModel.isUnderInvestigationFlagSelected());
	}
}

class ShowTransactionHistoryMouseAdapter extends MouseAdapter
{
	public void mousePressed(MouseEvent event) 
	{
		if (event.getClickCount() == 2) 
		{
			TransactionViewTable transactionViewTable = (TransactionViewTable) event.getSource();
			int row = transactionViewTable.rowAtPoint(event.getPoint());
			TransactionViewItemDto transactionViewItemDto = transactionViewTable.getTransactionViewTableModel().getTransactionViewItemDtoForRow(row);
			
			TransactionHistoryViewDialog dialog = new TransactionHistoryViewDialog(transactionViewItemDto);
				
        	try 
        	{
        		dialog.setVisible(true);
        	}
			catch (Exception ex) 
    		{
				// TODO
				ex.printStackTrace();
    		}
		}
		
	}
}

@SuppressWarnings("serial")
class TransactionViewTableModel extends AbstractTableModel  
{
	private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private static String[] COLUMN_NAMES = new String[] { "ID", "Action", "Interchange ID", "Instruction ID", "Transaction ID", "Insert Timestamp", "Timestamp", "Country", "Failure", "SLA 1 Breach" , "SLA 2 Breach", "Comments" };
	
	private Map<Integer, TransactionViewItemDto> transactionViewItemDtoMap = new HashMap<Integer, TransactionViewItemDto>();
	private Object[][] tableData;
	
	public TransactionViewTableModel()
	{
		super();

		try 
		{
			List<String> sqlFilterCriteriaList = new ArrayList<String>();
			sqlFilterCriteriaList.add(TransactionToolsPanel.WHEN_DEFAULT.getSqlFilter());
			
			setupTableData(sqlFilterCriteriaList, TransactionToolsPanel.HOW_DEFAULT.getSqlFilter());
		} 
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public void setupTableData(List<String> filterCriteriaList, String successFailureFilter) throws SQLException
	{
		TransactionViewDao transactionViewDao = new TransactionViewDao();
		
		this.transactionViewItemDtoMap = transactionViewDao.selectTransactionViewItemDtos(filterCriteriaList, successFailureFilter);
		this.tableData = new Object[transactionViewItemDtoMap.values().size()][COLUMN_NAMES.length];
		
		int index = 0;
		for (TransactionViewItemDto transactionViewItemDto : new ArrayList<TransactionViewItemDto>(transactionViewItemDtoMap.values()))
		{
			tableData[index][0] = transactionViewItemDto.getId();
			tableData[index][1] = new Boolean(false);
			tableData[index][2] = transactionViewItemDto.getInterchangeId();
			tableData[index][3] = transactionViewItemDto.getInstructionId();
			tableData[index][4] = transactionViewItemDto.getTransactionId();
			tableData[index][5] = DATE_FORMAT.format(transactionViewItemDto.getInsertTimestamp());
			tableData[index][6] = DATE_FORMAT.format(transactionViewItemDto.getSourceTimestamp());
			tableData[index][7] = transactionViewItemDto.getCountry();
			tableData[index][8] = transactionViewItemDto.getNakCnt() > 0 ? "Y" : "N";
			tableData[index][9] = transactionViewItemDto.getSla1BreachCnt() > 0 ? "Y" : "N";
			tableData[index][10] = transactionViewItemDto.getSla1BreachCnt() > 0 ? "Y" : "N";
			tableData[index][11] = transactionViewItemDto.getComments();
			
			index ++;
		}
		
		fireTableDataChanged();
	}
	
	public TransactionViewItemDto getTransactionViewItemDtoForRow(int row)
	{
		return transactionViewItemDtoMap.get(tableData[row][0]);
	}
	
	public boolean isUnderInvestigationFlagSelected()
	{
		for (int index = 0; index < tableData.length; index ++)
		{
			if  ((Boolean) tableData[index][1])
			{
				return true;
			}
		}
			
		return false;
	}
	
	public Map<Integer, TransactionViewItemDto> getSelectedTransactionViewItemDto() 
	{
		Map<Integer, TransactionViewItemDto> map = new LinkedHashMap<Integer, TransactionViewItemDto>();
		
		for (int index = 0; index < tableData.length; index ++)
		{
			if  ((Boolean) tableData[index][1])
			{
				int key = (int) tableData[index][0];
				map.put(key, transactionViewItemDtoMap.get(key));
			}
		}
		
		return map;
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
class TransactionViewTable extends JTable
{
	private static final Color COLOR_RED = new Color(204, 0, 0);
	private static final Color COLOR_ORANGE = new Color(255, 153, 0);
	private static final Color COLOR_GREEN = new Color(106, 168, 79);
	//private static final Color COLOR_GREY = new Color(221, 221, 221);
	private static final Color COLOR_LIGHT_GREY = new Color(238, 238, 238);
	//private static final Color COLOR_DARK_GREY = new Color(153, 153, 153);
	
	
	private TransactionViewTableModel transactionViewTableModel;
	
	public TransactionViewTable(TransactionViewTableModel transactionViewTableModel)
	{
		super(transactionViewTableModel);
		
		this.transactionViewTableModel = transactionViewTableModel;
		
		this.addMouseListener(new ShowTransactionHistoryMouseAdapter());
		
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
	    
	    getColumnModel().getColumn(1).setMinWidth(50);
	    getColumnModel().getColumn(1).setMaxWidth(50);
	    
	    getColumnModel().getColumn(5).setMinWidth(0);
	    getColumnModel().getColumn(5).setMaxWidth(0);
	    
	    getColumnModel().getColumn(6).setMinWidth(130);
	    getColumnModel().getColumn(6).setMaxWidth(130);
	    
	    getColumnModel().getColumn(7).setMinWidth(70);
	    getColumnModel().getColumn(7).setMaxWidth(70);
	    
	    getColumnModel().getColumn(8).setMinWidth(100);
	    getColumnModel().getColumn(8).setMaxWidth(100);
	    
	    getColumnModel().getColumn(9).setMinWidth(100);
	    getColumnModel().getColumn(9).setMaxWidth(100);
	    
	    getColumnModel().getColumn(10).setMinWidth(100);
	    getColumnModel().getColumn(10).setMaxWidth(100);
	}
	
	public TransactionViewTableModel getTransactionViewTableModel()
	{
		return transactionViewTableModel;
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
			if (column == 8 || column == 9 || column == 10) 
			{
				return new CenterTableCellRenderer();
			}
			
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
	public Component prepareRenderer(TableCellRenderer renderer, int row, int column) 
	{
		TransactionViewItemDto transactionViewItemDto = transactionViewTableModel.getTransactionViewItemDtoForRow(row);
		
		Component component = super.prepareRenderer(renderer, row, column);    
		JComponent jComponent = (JComponent) component;
	
		jComponent.setForeground(Color.BLACK);
		
		if ((row % 2 > 0))
		{
			jComponent.setBackground(COLOR_LIGHT_GREY);
		}
		else
		{
			jComponent.setBackground(Color.WHITE);
		}
		
		if (column == 1) 
		{
			if (transactionViewItemDto.isUnderInvestigation()) 
			{
				jComponent.setBackground(Color.PINK);
			}
			
		}
		
		if (column == 8) 
		{
			if (transactionViewItemDto.getNakCnt() > 0)
			{
				jComponent.setBackground(COLOR_RED);
				jComponent.setForeground(Color.WHITE);
			}
			else
			{
				jComponent.setBackground(COLOR_GREEN);
			}
		}
		
		if (column == 9) 
		{
			if (transactionViewItemDto.getSla1BreachCnt() > 0)
			{
				jComponent.setBackground(COLOR_ORANGE);
			}
			else
			{
				jComponent.setBackground(COLOR_GREEN);
			}
		}
		
		if (column == 10) 
		{
			if (transactionViewItemDto.getSla2BreachCnt() > 0)
			{
				jComponent.setBackground(COLOR_RED);
				jComponent.setForeground(Color.WHITE);
			}
			else
			{
				jComponent.setBackground(COLOR_GREEN);
			}
		}

		return component;
	}
}

@SuppressWarnings("serial")
class CenterTableCellRenderer extends DefaultTableCellRenderer
{
	public CenterTableCellRenderer()
	{
		super();
		setHorizontalAlignment(SwingConstants.CENTER);
	}
}



