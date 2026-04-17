package dnl.utils.text.table;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * {@link TextTableModel} backed by a list of maps.
 * <p>
 * Each map represents one row. Column names are derived from the key set of the first
 * map and reused for every row, so all rows are expected to provide a compatible set of
 * keys. Cell values are obtained by looking up the current column name in the current
 * row map.
 * </p>
 *
 * @author Daniel Orr
 */
public class MapBasedTableModel extends TextTableModel {

	/**
	 * Column names derived from the first row map.
	 */
	private List<String> columnNames;
	/**
	 * Row data exposed by this model.
	 */
	private List<Map> maps;

	/**
	 * Creates a table model from a list of row maps.
	 * <p>
	 * The first map defines the available columns and therefore the list is expected to be
	 * non-empty.
	 * </p>
	 *
	 * @param maps row data where each map contains values keyed by column name
	 */
	public MapBasedTableModel(List<Map> maps) {
		this.columnNames = new ArrayList<>(maps.get(0).keySet());
		this.maps = maps;
	}

	@Override
	public int getRowCount() {
		return maps.size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.size();
	}

	@Override
	public String getColumnName(int column) {
		return columnNames.get(column);
	}

	/**
	 * Returns the value stored in the row map for the requested column name.
	 *
	 * @param rowIndex zero-based model row index
	 * @param columnIndex zero-based model column index
	 * @return the mapped cell value, or {@code null} when the row map has no value for the
	 *         column
	 */
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Map m = maps.get(rowIndex);
		String columnName = columnNames.get(columnIndex);
		return m.get(columnName);
	}

	/**
	 * Disables row numbering for every row in this model.
	 *
	 * @param row zero-based model row index
	 * @return always {@code false}
	 */
	@Override
	public boolean allowNumberingAt(int row) {
		return false;
	}

	/**
	 * Disables separator insertion for every row in this model.
	 *
	 * @param row zero-based model row index
	 * @return always {@code false}
	 */
	@Override
	public boolean addSeparatorAt(int row) {
		return false;
	}

}
