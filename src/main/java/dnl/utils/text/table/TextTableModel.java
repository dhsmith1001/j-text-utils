package dnl.utils.text.table;

import javax.swing.table.AbstractTableModel;

/**
 * Base class for table models that expose row-level rendering hints to
 * {@link TextTable}.
 * <p>
 * In addition to the standard Swing {@link javax.swing.table.TableModel} contract,
 * implementations can control whether individual rows should display row numbers and
 * whether a separator line should be rendered before a row when text output is
 * generated.
 * </p>
 *
 * @author Daniel Orr
 */
public abstract class TextTableModel extends AbstractTableModel {

	/**
	 * Serialization identifier for this abstract table model base class.
	 */
	private static final long serialVersionUID = -663028121137688537L;

	/**
	 * Creates a table model base that can supply row-level rendering hints.
	 */
	protected TextTableModel() {
	}

	/**
	 * Indicates whether {@link TextTable} may render a row number for the specified row.
	 *
	 * @param row zero-based model row index
	 * @return {@code true} when numbering may be shown for the row, {@code false}
	 *         otherwise
	 */
	public abstract boolean allowNumberingAt(int row);

	/**
	 * Indicates whether {@link TextTable} should render a separator line before the
	 * specified row.
	 *
	 * @param row zero-based model row index
	 * @return {@code true} when a separator should precede the row, {@code false}
	 *         otherwise
	 */
	public abstract boolean addSeparatorAt(int row);
}
