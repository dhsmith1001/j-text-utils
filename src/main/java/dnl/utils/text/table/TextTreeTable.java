package dnl.utils.text.table;

import javax.swing.table.TableModel;
import java.io.PrintStream;

/**
 * A text-based table renderer with support for hierarchical row grouping.
 * <p>
 * This table behaves like {@link TextTable}, but applies a separator policy
 * that inserts separators when the value in the configured hierarchical column
 * changes between consecutive rows.
 *
 * @author Daniel Orr
 */
public class TextTreeTable extends TextTable {

	/**
	 * Column index used to determine hierarchy boundaries between rows.
	 */
	protected int hierarchicalColumn;

	/**
	 * Creates a tree table from raw column names and row data.
	 *
	 * @param columnNames column headers
	 * @param data table row data
	 */
	public TextTreeTable(String[] columnNames, String[][] data) {
		super(columnNames, data);
		addSeparatorPolicy(new TreeTableSeparatorPolicy());
	}

	/**
	 * Creates a tree table from an existing model.
	 *
	 * @param tableModel source table model
	 * @param addNumbering whether row numbering should be included
	 */
	public TextTreeTable(TableModel tableModel, boolean addNumbering) {
		super(tableModel, addNumbering);
		addSeparatorPolicy(new TreeTableSeparatorPolicy());
	}

	/**
	 * Creates a tree table from an existing model.
	 *
	 * @param tableModel source table model
	 */
	public TextTreeTable(TableModel tableModel) {
		super(tableModel);
		addSeparatorPolicy(new TreeTableSeparatorPolicy());
	}

	/**
	 * Sets the column index used to detect hierarchy transitions between rows.
	 *
	 * @param hierarchicalColumn hierarchy column index
	 */
	protected void setHierarchicalColumn(int hierarchicalColumn) {
		this.hierarchicalColumn = hierarchicalColumn;
	}

	/**
	 * Prints the table using a renderer specialized for tree-table output.
	 *
	 * @param ps output stream
	 * @param indent number of spaces to prefix each line with
	 */
	@Override
	public void printTable(PrintStream ps, int indent) {
		TextTableRenderer renderer = new TextTreeTableRenderer(this);
		renderer.render(ps, indent);
	}

	/**
	 * Separator policy that inserts separators when the hierarchy value changes
	 * between two adjacent rows.
	 */
	private class TreeTableSeparatorPolicy extends SeparatorPolicy{

		/**
		 * Determines whether a separator should be rendered before the given row.
		 *
		 * @param row row index to evaluate
		 * @return {@code true} when the hierarchy column value differs from the
		 *         previous row; {@code false} otherwise
		 */
		@Override
		boolean hasSeparatorAt(int row) {
			if(row == 0)
				return false;
			Object rowAgo = getValueAt(row-1, hierarchicalColumn);
			Object hierarchicalColumnVal = getValueAt(row, hierarchicalColumn);
			//System.out.println(row+ "> "+rowAgo+"-"+hierarchicalColumnVal);
			if(!hierarchicalColumnVal.equals(rowAgo)){
				return true;
			}
			return false;
		}

	}

}
