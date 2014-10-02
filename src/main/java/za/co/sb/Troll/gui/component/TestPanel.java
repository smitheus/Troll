package za.co.sb.Troll.gui.component;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLayer;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.plaf.LayerUI;
import javax.swing.table.DefaultTableModel;

public class TestPanel extends JPanel {
	

	/**
	 * Create the panel.
	 */
	public TestPanel() 
	{
		JTable table = new JTable(new TableModel2());
		//table.putClientProperty(RowSpanUI.COLUMN_TO_SPAN_KEY, 1);
		JLayer layer = new JLayer(table, new RowSpanUI());
		
		add(layer);

	}
	
	
	

}
class TableModel2 extends DefaultTableModel  
{
	public TableModel2()
	{
		super();
		
		this.setColumnIdentifiers(new String[] {"Action", "Transaction ID", "Of Instr:", "gfnbgffgbsf", "NBOL Sent:", "Into PAYEX:", "Out of PAYEX:", "Integration:", "CB RTN (To PES):", "CB RTN: (To NBOL)", "E2E Timings:" });
		this.setupTableData(false);
	}
	
	public void setupTableData(boolean reconciled)
	{
		getDataVector().removeAllElements();
		
		Object[] data = new Object[TransactionView.mainColumNames.length];
				
		data[0] = "";
		data[1] = "TRAN001";
		data[2] = "INST001";
		
		data[3] = "timestamp";
		data[4] = "timestamp";
		data[5] = "timestamp";
		data[6] = "timestamp";
		data[7] = "timestamp";
		data[8] = "timestamp";
		
		
		addRow(data);
		
		data = new Object[TransactionView.mainColumNames.length];
		
		data[0] = "";
		data[1] = "Country:";
		data[2] = "Of Instr:";
		
		data[3] = "Feedback/ACK:";
		data[4] = "ACK";
		data[5] = "ACK";
		data[6] = "ACK";
		data[7] = "ACK";
		data[8] = "ACK";
		
		
		addRow(data);
		
		data = new Object[TransactionView.mainColumNames.length];
		
		data[0] = "";
		data[1] = "KE";
		data[2] = "INST001";
		
		data[3] = "Elapsed Time:";
		data[4] = "blah";
		data[5] = "blah";
		data[6] = "blah";
		data[7] = "blah";
		data[8] = "blah";
		
		
		addRow(data);
		
		fireTableDataChanged();
	} 
}

 class RowSpanUI extends LayerUI<JTable> {
    public static String COLUMN_TO_SPAN_KEY = "Table.columnToSpan";
    private JLayer layer;
    private JTextArea area;

    @Override
    public void installUI(JComponent c) {
        super.installUI(c);
        this.layer = (JLayer) c;
        installTextArea();
        installListeners();
    }


    @Override
    public void doLayout(JLayer<? extends JTable> l) {
        super.doLayout(l);
        l.getGlassPane().doLayout();
    }

     private void installTextArea() {
        area = new JTextArea(10, 20);
        layer.getGlassPane().setBorder(BorderFactory.createLineBorder(Color.RED));
        layer.getGlassPane().setLayout(new ColumnLayoutManager(this));
        layer.getGlassPane().add(area);
        layer.getGlassPane().setVisible(true);
    }

    public JTable getView() {
        return (JTable) layer.getView();
    }

    public int getViewColumnToSpan() {
        Object clientProperty = getView().getClientProperty(COLUMN_TO_SPAN_KEY);
        if (clientProperty instanceof Integer) {
            return getView().convertColumnIndexToView((int) clientProperty);
        }   
        return -1;
    }

    /**
     * Install listeners to manually trigger a layout of the glassPane.
     * This is incomplete, just the minimum for demonstration!
     */
    protected void installListeners() {
        ComponentListener compL = new ComponentListener() {

            @Override
            public void componentShown(ComponentEvent e) {
                doLayout(layer);
            }

            @Override
            public void componentResized(ComponentEvent e) {
                doLayout(layer);
            }

            @Override
            public void componentMoved(ComponentEvent e) {
                doLayout(layer);
            }

            @Override
            public void componentHidden(ComponentEvent e) {
                // TODO Auto-generated method stub

            }
        };
        layer.addComponentListener(compL);

        TableColumnModelListener columnL = new TableColumnModelListener() {

            @Override
            public void columnRemoved(TableColumnModelEvent e) {
                doLayout(layer);
            }

            @Override
            public void columnMoved(TableColumnModelEvent e) {
                doLayout(layer);
            }

            @Override
            public void columnMarginChanged(ChangeEvent e) {
                doLayout(layer);
            }

            @Override
            public void columnAdded(TableColumnModelEvent e) {
                doLayout(layer);
            }

            @Override
            public void columnSelectionChanged(ListSelectionEvent e) {
            }

        };
        getView().getColumnModel().addColumnModelListener(columnL);
    }

}

 class ColumnLayoutManager implements LayoutManager {

    private RowSpanUI ui;

    public ColumnLayoutManager(RowSpanUI ui) {
        this.ui = ui;
    }

    @Override
    public void layoutContainer(Container parent) {
        Component child = parent.getComponent(0);
        child.setBounds(getColumnBounds());
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        return ui.getView().getSize();
    }

    protected Rectangle getColumnBounds() {
        int viewColumn = ui.getViewColumnToSpan();
        if (viewColumn < 0) {
            return new Rectangle();
        }
        Rectangle r = ui.getView().getCellRect(0, viewColumn, false);
        r.height = ui.getView().getHeight();
        return r;
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return preferredLayoutSize(parent);
    }
    @Override
    public void addLayoutComponent(String name, Component comp) {
    }

    @Override
    public void removeLayoutComponent(Component comp) {
    }
}
