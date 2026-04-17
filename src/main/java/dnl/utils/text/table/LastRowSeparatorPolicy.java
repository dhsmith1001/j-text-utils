package dnl.utils.text.table;

import javax.swing.table.TableModel;

/**
 * A separator policy that places a separator line after the last row of a table.
 * <p>
 * This implementation will insert a separator at the final row index,
 * making it useful for distinguishing the last row of data or totals row.
 * </p>
 */
public class LastRowSeparatorPolicy extends SeparatorPolicy {

	/**
	 * Constructs a new {@code LastRowSeparatorPolicy} without an associated table model.
	 * <p>
	 * The table model must be set later via an appropriate setter method.
	 * </p>
	 */
	public LastRowSeparatorPolicy() {
	}

	/**
	 * Constructs a new {@code LastRowSeparatorPolicy} with the specified table model.
	 *
	 * @param tableModel the {@link TableModel} to which this policy applies
	 */
	public LastRowSeparatorPolicy(TableModel tableModel) {
		super(tableModel);
	}

	/**
	 * Determines whether a separator should be placed after the specified row.
	 *
	 * @param row the row index to check
	 * @return {@code true} if the row is the last row in the table model; {@code false} otherwise
	 */
	@Override
	public boolean hasSeparatorAt(int row) {
		return row == tableModel.getRowCount() - 1;
	}

}
