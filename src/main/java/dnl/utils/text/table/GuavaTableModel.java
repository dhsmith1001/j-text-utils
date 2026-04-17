package dnl.utils.text.table;

import com.google.common.collect.Lists;
import com.google.common.collect.Table;

import javax.swing.table.AbstractTableModel;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Swing {@link javax.swing.table.TableModel} backed by a Guava
 * {@code Table<Integer, String, V>}.
 * <p>
 * Column names are derived from the Guava table column keys and sorted in natural
 * string order. Row count is based on the maximum row index present in the table,
 * plus one, which means sparse row indexes are represented as virtual rows.
 * Reading a value from a missing cell returns {@code null}, matching
 * {@link Table#get(Object, Object)} semantics.
 *
 * @param <V> value type stored in each table cell
 *
 * @author Daniel Orr
 */
public class GuavaTableModel<V> extends AbstractTableModel {

	/**
	 * Backing Guava table keyed by row index and column name.
	 */
	private Table<Integer, String, V> guavaTable;
	/**
	 * Sorted list of column names derived from the Guava table.
	 */
	private List<String> columnNames;
	/**
	 * Row count exposed by the model, including sparse virtual rows.
	 */
	private int rowCount;

	/**
	 * Creates a table model from a Guava table.
	 * <p>
	 * The provided table is used directly and is expected to contain at least one row
	 * key. Row keys are interpreted as non-negative, zero-based indexes.
	 *
	 * @param guavaTable source table keyed by row index and column name
	 */
	public GuavaTableModel(Table<Integer, String, V> guavaTable) {
		this.guavaTable = guavaTable;
		this.columnNames = Lists.newArrayList(guavaTable.columnKeySet());
		Collections.sort(this.columnNames);
		Set<Integer> rowIndexes = this.guavaTable.rowKeySet();
		this.rowCount = Collections.max(rowIndexes)+1;
	}

	/**
	 * Returns the sorted column name at the given model index.
	 *
	 * @param column model column index
	 * @return column name for {@code column}
	 */
	@Override
	public String getColumnName(int column) {
		return columnNames.get(column);
	}

	/**
	 * Returns total row count, including virtual rows caused by sparse row indexes.
	 *
	 * @return number of rows exposed by this model
	 */
	@Override
	public int getRowCount() {
		return rowCount;
	}

	/**
	 * Returns number of distinct column keys present in the source table.
	 *
	 * @return number of columns in this model
	 */
	@Override
	public int getColumnCount() {
		return columnNames.size();
	}

	/**
	 * Returns the value at the given row and column model indexes.
	 *
	 * @param rowIndex model row index
	 * @param columnIndex model column index
	 * @return cell value, or {@code null} when the table has no value for the cell
	 */
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return guavaTable.get(rowIndex, columnNames.get(columnIndex));
	}
}
