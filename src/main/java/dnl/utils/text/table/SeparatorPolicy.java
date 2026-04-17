package dnl.utils.text.table;

import javax.swing.table.TableModel;

/**
 * Base class for policies that decide whether {@link TextTable} should render a
 * separator line before a specific row.
 * <p>
 * A policy is typically registered through {@link TextTable#addSeparatorPolicy(SeparatorPolicy)},
 * which associates the policy with the table's current {@link TableModel}. Subclasses
 * implement {@link #hasSeparatorAt(int)} to provide the row-specific decision logic.
 * </p>
 */
public abstract class SeparatorPolicy {

	/**
	 * The table model consulted by this policy.
	 * <p>
	 * This value may be {@code null} when the no-argument constructor is used until a
	 * model is assigned through {@link #setTableModel(TableModel)}.
	 * </p>
	 */
	protected TableModel tableModel;

	/**
	 * Constructs a separator policy without an associated table model.
	 */
	public SeparatorPolicy() {
	}

	/**
	 * Constructs a separator policy for the specified table model.
	 *
	 * @param tableModel the table model to evaluate when deciding where separators belong
	 */
	public SeparatorPolicy(TableModel tableModel) {
		this.tableModel = tableModel;
	}

	/**
	 * Returns the table model currently associated with this policy.
	 *
	 * @return the current table model, or {@code null} if no model has been assigned yet
	 */
	public TableModel getTableModel() {
		return tableModel;
	}

	/**
	 * Associates this policy with the specified table model.
	 *
	 * @param tableModel the table model to use when evaluating separator placement
	 */
	public void setTableModel(TableModel tableModel) {
		this.tableModel = tableModel;
	}

	/**
	 * Indicates whether a separator should be rendered before the specified row.
	 *
	 * @param row the zero-based row index to evaluate
	 * @return {@code true} if a separator should be rendered before the row; {@code false}
	 *         otherwise
	 */
	abstract boolean hasSeparatorAt(int row);
}
