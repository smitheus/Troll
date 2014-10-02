package za.co.sb.Troll.gui.component.span.example;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import za.co.sb.Troll.gui.component.span.DefaultSpanModel;
import za.co.sb.Troll.gui.component.span.Span;
import za.co.sb.Troll.gui.component.span.SpanModel;
import za.co.sb.Troll.gui.component.span.SpanTableModel;

public class MyTableModel extends AbstractTableModel implements SpanTableModel {
    private SpanModel spanModel = new DefaultSpanModel();
    private List<MyData> data = new ArrayList<MyData>();
	private List<Index> indices = new ArrayList<Index>();

    class MyData {
        String string;
        private boolean flag;

        MyData(String string, boolean flag) {
            this.string = string;
            this.flag = flag;
        }

		void setFlag(boolean b) {
			boolean update = flag != b;
			flag = b;
			if (update) {
				rebuildIndices();
			}
		}
    }

	static class Index {
		int index;
		boolean firstRow;

		Index(int index, boolean firstRow) {
			this.index = index;
			this.firstRow = firstRow;
		}
	}

    MyTableModel() {
		super();
    }

    public void addString(String string) {
        data.add(new MyData(string, false));
		rebuildIndices();
    }

    public SpanModel getSpanModel() {
        return spanModel;
    }

    public int getColumnCount() {
        return 4;
    }

    public int getRowCount() {
        return indices.size();
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        Object rval = null;

		Index ndx = indices.get(rowIndex);
        MyData item = data.get(ndx.index);

		if (ndx.firstRow) {
            switch (columnIndex) {
                case 0:
                    rval = item.flag;
                    break;
                case 1:
                    rval = item.string.length();
                    break;
                case 2:
                    rval = item.string.charAt(0);
                    break;
                case 3:
                    rval = item.string.charAt(item.string.length() - 1);
                    break;
			}
		} else {
            if (columnIndex == 1) {
                rval = item.string;
            }
		}
        return rval;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
		Index ndx = indices.get(rowIndex);
        return ndx.firstRow && columnIndex == 0;
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
		Index ndx = indices.get(rowIndex);
        MyData item = data.get(ndx.index);

		if (ndx.firstRow) {
			switch (columnIndex) {
				case 0:
					item.setFlag((Boolean)value);
					break;
			}
		}
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        Class<?> rval = Object.class;
        switch (columnIndex) {
            case 0:
                rval = Boolean.class;
                break;
            case 1:
                rval = Object.class;
                break;
            case 2:
            case 3:
                rval = Character.class;
                break;
        }
        return rval;
	}

	private void rebuildIndices() {
		indices.clear();
		for (int i = 0; i < data.size(); ++i) {
			indices.add(new Index(i, true));

			if (!data.get(i).flag) {
				indices.add(new Index(i, false));
			}
		}

		// Rebuild the span model:
		spanModel.clear();
		for (int row = 0; row < indices.size(); ++row) {
			Index ndx = indices.get(row);
			if (!ndx.firstRow) {
				spanModel.addSpan(new Span(row, 0, row, 1, 1, getColumnCount()));
			}
		}

		fireTableStructureChanged();
	}
}

