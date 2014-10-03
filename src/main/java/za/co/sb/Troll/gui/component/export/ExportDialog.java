package za.co.sb.Troll.gui.component.export;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;

public class ExportDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			ExportDialog dialog = new ExportDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public ExportDialog() 
	{
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
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
	    
	    getColumnModel().getColumn(0).setMaxWidth(50);
        //getColumnModel().getColumn(0).setCellRenderer(new ConditionalCheckBoxRenderer());
        //getColumnModel().getColumn(1).setMaxWidth(100);
        //getColumnModel().getColumn(2).setMaxWidth(120);
	}
}

@SuppressWarnings("serial")
class ExportTableModel extends AbstractTableModel  
{
	public static String[] COLUMN_NAMES = new String[] { "Transaction ID", "Feedback" };
	
	
	Object[][] data;
	
	public ExportTableModel()
	{
		super();
		
		try {
			this.setupTableData(new ArrayList<String>());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public void setupTableData(List<String> filterCriteriaList) throws SQLException
	{
		
		
		
		fireTableDataChanged();
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
    	data[row][col] = value;

    	fireTableCellUpdated(row, col);
    }
}


