package za.co.sb.Troll.gui.component.span.example;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import za.co.sb.Troll.gui.component.span.Span;
import za.co.sb.Troll.gui.component.span.SpanTable;

public class Example extends JFrame {
    private JTextField rowField;
    private JTextField columnField;
    private JTextField heightField;
    private JTextField widthField;
    private MyTableModel tableModel = new MyTableModel();

    class AddSpanAction extends AbstractAction {
        AddSpanAction() {
            super("Add Span");
        }

        public void actionPerformed(ActionEvent e) {
            int row = Integer.valueOf(rowField.getText().trim());
            int column = Integer.valueOf(columnField.getText().trim());
            int height = Integer.valueOf(heightField.getText().trim());
            int width = Integer.valueOf(widthField.getText().trim());
            Span span = new Span(row, column, height, width);
            tableModel.getSpanModel().addSpan(span);
        }
    }

    class DeleteSpanAction extends AbstractAction {
        DeleteSpanAction() {
            super("Delete Span");
        }

        public void actionPerformed(ActionEvent e) {
            int row = Integer.valueOf(rowField.getText().trim());
            int column = Integer.valueOf(columnField.getText().trim());
            Span span = tableModel.getSpanModel().getDefinedSpan(row, column);
            tableModel.getSpanModel().removeSpan(span);
        }
    }

	public static void main(String[] args) {
		SwingUtilities.invokeLater(
            new Runnable() {
                public void run() {
                    Example example = new Example();
                    example.setVisible(true);
                }
            });
	}

    public Example() {
        super("Table with Spans");
        init();
    }

    private void init() {
        tableModel.addString("ABCDEF");
        tableModel.addString("GHIJKLM");
        tableModel.addString("NOPQ");
        tableModel.addString("RSTUVWXY");
        tableModel.addString("Z");
        SpanTable table = new SpanTable(tableModel);
        table.setDefaultRenderer(Boolean.class, new BooleanCellRenderer());
        table.setDefaultEditor(Boolean.class, new DefaultCellEditor(new JCheckBox()));
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(new JScrollPane(table), BorderLayout.CENTER);
        getContentPane().add(createBottomPanel(), BorderLayout.SOUTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 295);
    }

    private JComponent createBottomPanel() {
        JComponent panel = Box.createVerticalBox();
        panel.add(createEntryPanel());
        panel.add(createButtonPanel());
        return panel;
    }

    private JComponent createEntryPanel() {
        JComponent panel = Box.createHorizontalBox();
        panel.add(Box.createHorizontalStrut(10));
        panel.add(new JLabel("Row:"));
        panel.add(Box.createHorizontalStrut(3));
        rowField = new JTextField();
        panel.add(rowField);
        panel.add(Box.createHorizontalGlue());
        panel.add(new JLabel("Column:"));
        panel.add(Box.createHorizontalStrut(3));
        columnField = new JTextField();
        panel.add(columnField);
        panel.add(Box.createHorizontalGlue());
        panel.add(new JLabel("Height:"));
        panel.add(Box.createHorizontalStrut(3));
        heightField = new JTextField();
        panel.add(heightField);
        panel.add(Box.createHorizontalGlue());
        panel.add(new JLabel("Width:"));
        panel.add(Box.createHorizontalStrut(3));
        widthField = new JTextField();
        panel.add(widthField);
        panel.add(Box.createHorizontalStrut(10));
        return panel;
    }

    private JComponent createButtonPanel() {
        JComponent panel = Box.createHorizontalBox();
        panel.add(Box.createHorizontalStrut(10));
        panel.add(Box.createHorizontalGlue());
        panel.add(new JButton(new AddSpanAction()));
        panel.add(Box.createHorizontalGlue());
        panel.add(new JButton(new DeleteSpanAction()));
        panel.add(Box.createHorizontalGlue());
        panel.add(Box.createHorizontalStrut(10));
        return panel;
    }
}

