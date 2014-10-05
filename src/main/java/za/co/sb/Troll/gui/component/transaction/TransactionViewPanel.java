package za.co.sb.Troll.gui.component.transaction;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
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


public class TransactionViewPanel extends JPanel implements TableModelListener
{
	private TrollConsoleFrame trollConsoleFrame;
	
	private JTable table1;
	private TableModel tableModel;
	
	public static String[] verticleSubColumNames = {"Country:", "Of Instr:" };
	public static String[] horizontalSubColumNames = {"Timestamp", "Feedback/ACK", "Elapsed time" };

	public Map<Integer, TransactionViewItemDto> getSelectedTransactionViewItemDto()
	{
		return this.tableModel.getSelectedTransactionViewItemDto();
	}
	
	@Override
	public void tableChanged(TableModelEvent e) 
	{
		boolean ggg = tableModel.getAllColumnData();
		
		trollConsoleFrame.getHeaderPanel().setBtnExportEnabled(ggg);
	}
	
	
	
	
	
	
	
	/**
	 * Create the panel.
	 */
	public TransactionViewPanel(TrollConsoleFrame trollConsoleFrame) 
	{
		super();
		
		this.trollConsoleFrame = trollConsoleFrame;
		
		setLayout(new BorderLayout(0, 0));
		
		setBorder(new MatteBorder(3, 0, 0, 0, (Color) new Color(0, 0, 0)));
		
		tableModel = new TableModel();
		tableModel.addTableModelListener(this);
		
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
	private TableModel tableModel;
	
	public TransactionTable(TableModel tableModel)
	{
		super(tableModel);
		
		this.tableModel = tableModel;
		
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
	    getColumnModel().getColumn(1).setMaxWidth(50);
       
	}
	
	
	
	
	//class UnderInvestigation
	
	
	@Override
    public TableCellRenderer getCellRenderer(int row, int column)
    {
		if (column == 1)
		{
			if (getValueAt(row, column) instanceof Boolean)
			{
				return getDefaultRenderer(Boolean.class);
			}
			else
			{
				//return new CenterTableCellRenderer();
				return getDefaultRenderer(String.class);
			}
		}
		else
		{
			return getDefaultRenderer(String.class);
		}
    }
	
	@Override
    public TableCellEditor getCellEditor(int row, int column)
    {
		
         Object ggg = getValueAt(row, column);
		
		if (getValueAt(row, column) instanceof Boolean )
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
		
		
		
		
		
		
		if ((column >= 1 && column <= 4) || column == 11) 
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
		
		
		
		
		
		
		if (column == 1)
		{
			TransactionViewItemDto ddd = tableModel.getTransactionViewItemDto(row);
			
			if (ddd.isUnderInvestigation()) 
			{
				jc.setBackground(Color.PINK);
				
			}
			else
			{
				jc.setBackground(new Color(238, 238, 238));
			}
			
			
			
			
		}
		
		return c;
	}
	
	//@Override
	//public void tableChanged(TableModelEvent e) {
       // int row = e.getFirstRow();
       // int column = e.getColumn();
        
       // System.out.println("WTF");
        
        //TableModel model = (TableModel)e.getSource();
        //String columnName = model.getColumnName(column);
        //Object data = model.getValueAt(row, column);

        // Do something with the data...
    //}
	
	
}
















@SuppressWarnings("serial")
class TableModel extends AbstractTableModel  
{
	public static String[] mainColumNames = new String[] { "ID", "Action", "Transaction ID", "Of Instr:", " fdvbvsdfsf", "NBOL Sent:", "Into PAYEX:", "Out of PAYEX:", "Integration:", "CB RTN (To PES):", "CB RTN: (To NBOL)", "E2E Timings:" };
	
	Map<Integer, TransactionViewItemDto> transactionViewItemDtoMap = new HashMap<Integer, TransactionViewItemDto>();
	
	Object[][] data;
	
	public TableModel()
	{
		super();
		
		
		
		//this.setColumnIdentifiers(new String[] {"Action", "Transaction ID", "Of Instr:", "", "NBOL Sent:", "Into PAYEX:", "Out of PAYEX:", "Integration:", "CB RTN (To PES):", "CB RTN: (To NBOL)", "E2E Timings:" });
		
		
		try {
			List<String> sqlFilterCriteriaList = new ArrayList<String>();;
			sqlFilterCriteriaList.add(String.format(TransactionViewDao.DATE_FILTER, "10", "SECOND"));
			
			
			this.setupTableData(sqlFilterCriteriaList);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	
	public boolean  getAllColumnData()
	{
		boolean on = false;
		
		for (int index = 0; index < data.length; index ++)
		{
			Object b = data[index][1];
			
			if (b instanceof Boolean) 
			{
				if (((Boolean) b))
				{
					return true;
				}
				
			}
		}
			
		return false;
	
		
		
	}
	
	public TransactionViewItemDto getTransactionViewItemDto(int row)
	{
		return transactionViewItemDtoMap.get(data[row][0]);
	}
	
	public Map<Integer, TransactionViewItemDto> getSelectedTransactionViewItemDto() 
	{
		//List<TransactionViewItemDto> list = new ArrayList<TransactionViewItemDto>();
		
		Map<Integer, TransactionViewItemDto> map = new LinkedHashMap<Integer, TransactionViewItemDto>();
		
		
		for (int index = 0; index < data.length; index ++)
		{
			Object b = data[index][1];
			
			if (b instanceof Boolean) 
			{
				if (((Boolean) b))
				{
					Integer bb = (Integer) data[index][0];
					
					map.put(bb, transactionViewItemDtoMap.get(bb));
				}
				
			}
		}
		
		return map;
		
	}
	
	
	public void setupTableData(List<String> filterCriteriaList) throws SQLException
	{
		TransactionViewDao transactionViewDao = new TransactionViewDao();
		
		
		
		this.transactionViewItemDtoMap = transactionViewDao.selectTransactionViewItemDtos(filterCriteriaList);
		
		
		List<TransactionViewItemDto> list = new ArrayList<TransactionViewItemDto>(transactionViewItemDtoMap.values());
		
		
		data = new Object[list.size() * 3][mainColumNames.length];
		
		int index = 0;
		for (TransactionViewItemDto transactionViewItemDto : list) 
		{
			
			//data = new Object[3][mainColumNames.length];
			
			// Add TRANSACTION data to table
			// Row 1
			data[index][0] = transactionViewItemDto.getId();
			data[index][1] = transactionViewItemDto.isUnderInvestigation() ? "UNDER" : "";
			data[index][2] = transactionViewItemDto.getTransactionId();
			data[index][3] = transactionViewItemDto.getInstructionId();
			data[index][4] = "<html><i>Timestamp:</i><html>";
			
			//Row 2
			data[index + 1][0] = transactionViewItemDto.getId();
			data[index + 1][1] = new Boolean(false);
			data[index + 1][2] = "Country: " + transactionViewItemDto.getCountry();
			data[index + 1][3] = "Of InterCh:";
			data[index + 1][4] = "<html><i>Feedback/ACK:</i><html>";
			
			//Row 3
			data[index + 2][0] = transactionViewItemDto.getId();;
			data[index + 2][1] = transactionViewItemDto.isUnderInvestigation() ? "INV" : "";
			data[index + 2][2] = "";
			data[index + 2][3] = transactionViewItemDto.getInterchangeId();
			data[index + 2][4] = "<html><i>Elapsed Time:</i><html>";
			
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
			
			//addRow(data[0]);
			//addRow(data[1]);
			//addRow(data[2]);
			
			index = index + 3;
		}
		
		
		
		fireTableDataChanged();
	}



	public int getColumnCount() {
        return mainColumNames.length;
    }

    public int getRowCount() {
        return data.length;
    }

    public String getColumnName(int col) {
        return mainColumNames[col];
    }

    public Object getValueAt(int row, int col) {
        return data[row][col];
    }

    /*
     * JTable uses this method to determine the default renderer/
     * editor for each cell.  If we didn't implement this method,
     * then the last column would contain text ("true"/"false"),
     * rather than a check box.
     */
    public Class getColumnClass(int c) 
    {
    	if (getValueAt(0, c) == null) 
    	{
    		return String.class;
    	}
    	
        return getValueAt(0, c).getClass();
    }

    /*
     * Don't need to implement this method unless your table's
     * editable.
     */
    public boolean isCellEditable(int row, int col) {
        //Note that the data/cell address is constant,
        //no matter where the cell appears onscreen.
    	
    	if (col == 1) 
    	{
    		return true;
    	}
    	else
    	{
    		return false;
    	}
    	
    	
    }
    
    
   /* @Override
    public boolean isCellEditable(int row, int column) {
        boolean editable = false;
        if (column == 0) {
            Object value = getValueAt(row, column);
            if (value instanceof Integer) {
                editable = ((int)value) != 0;
            }
        }
        return editable;
    }*/
    
    
    

    /*
     * Don't need to implement this method unless your table's
     * data can change.
     */
    public void setValueAt(Object value, int row, int col) {
    	
    	data[row][col] = value;
        fireTableCellUpdated(row, col);
    	
        /*if (DEBUG) {
            System.out.println("Setting value at " + row + "," + col
                               + " to " + value
                               + " (an instance of "
                               + value.getClass() + ")");
        }

        data[row][col] = value;
        fireTableCellUpdated(row, col);

        if (DEBUG) {
            System.out.println("New value of data:");
            printDebugData();
        }*/
    }



	
	
	
    
	
	
}



// Cell Redenderes
class CenterTableCellRenderer extends DefaultTableCellRenderer
{
	public CenterTableCellRenderer()
	{
		super();
		
		setHorizontalAlignment( JLabel.CENTER );
	}

}

/* class ConditionalCheckBoxRenderer extends JPanel implements TableCellRenderer {

    private static final Border NO_FOCUS_BORDER = new EmptyBorder(1, 1, 1, 1);
    private JCheckBox cb;

    public ConditionalCheckBoxRenderer() {
        setLayout(new GridBagLayout());
        setOpaque(false);
        cb = new JCheckBox();
        cb.setOpaque(false);
        cb.setContentAreaFilled(false);
        cb.setMargin(new Insets(0, 0, 0, 0));
        add(cb);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        setOpaque(isSelected);
        if (isSelected) {
            setForeground(table.getSelectionForeground());
            setBackground(table.getSelectionBackground());
        } else {
            setForeground(table.getForeground());
        }
        if (value instanceof Integer) {
            int state = (int) value;
            cb.setVisible(state != 0);
            cb.setSelected(state == 2);
        }
        if (hasFocus) {
            setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
        } else {
            setBorder(NO_FOCUS_BORDER);
        }
        return this;
    }
}

/*
class TransactionViewDataData {
	  private Integer a;

	  private String b;

	  private String c;

	  private String d;

	  private String e;

	  public Data() {
	  }

	  public Data(Integer aa, String bb, String cc, String dd, String ee) {
	    a = aa;
	    b = bb;
	    c = cc;
	    d = dd;
	    e = ee;
	  }

	  public Integer getA() {
	    return a;
	  }

	  public String getB() {
	    return b;
	  }

	  public String getC() {
	    return c;
	  }

	  public String getD() {
	    return d;
	  }

	  public String getE() {
	    return e;
	  }

	  public void setA(Integer aa) {
	    a = aa;
	  }

	  public void setB(String macName) {
	    b = macName;
	  }

	  public void setC(String cc) {
	    c = cc;
	  }

	  public void setD(String dd) {
	    d = dd;
	  }

	  public void setE(String ee) {
	    e = ee;
	  }
	}

*/













/*
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
}*/



