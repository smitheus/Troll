package za.co.sb.Troll.gui.component.span.example;

import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class BooleanCellRenderer implements TableCellRenderer {
    private JCheckBox renderer = new JCheckBox();
	private JLabel label = new JLabel();

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
        boolean hasFocus, int row, int column) {
		if (value instanceof Boolean) {
			renderer.setSelected((Boolean)value);
			return renderer;
		} else {
			return label;
		}
    }
}
