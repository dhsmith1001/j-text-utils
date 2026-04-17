package dnl.utils.text.table;

import dnl.utils.text.table.csv.CsvTableRenderer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Renders a {@link TableModel} as a text table or CSV output.
 * <p>
 * A {@code TextTable} wraps any Swing {@link TableModel} and delegates rendering to
 * {@link TextTableRenderer} for aligned console-style output and to
 * {@link CsvTableRenderer} for CSV output. Optional row numbering, row sorting, and
 * separator policies can be configured before rendering.
 * </p>
 * <p>
 * When the wrapped model extends {@link TextTableModel}, the model can further control
 * whether row numbering or separators are shown for individual rows.
 * </p>
 *
 * @author Daniel Orr
 */
public class TextTable {

	/**
	 * Source model whose data and metadata are rendered.
	 */
	protected TableModel tableModel;
	/**
	 * Policies consulted to decide whether separator lines should be rendered before rows.
	 */
	protected List<SeparatorPolicy> separatorPolicies = new ArrayList<SeparatorPolicy>();

	/**
	 * Whether rendered text output should include row numbers.
	 */
	protected boolean addRowNumbering;

	/**
	 * Optional sorter used to translate rendered row positions to model row indexes.
	 */
	protected RowSorter<?> rowSorter;

	/**
	 * Indicates whether rendering should avoid UI-dependent behavior.
	 */
	protected boolean headless;

	/**
	 * Creates a text table backed by an existing table model.
	 *
	 * @param tableModel the model that provides the column metadata and row values to render
	 */
	public TextTable(TableModel tableModel) {
		this.tableModel = tableModel;
	}

	/**
	 * Creates a text table backed by an existing table model and optionally enables row
	 * numbering.
	 *
	 * @param tableModel the model that provides the column metadata and row values to render
	 * @param addNumbering {@code true} to prepend row numbers during text rendering,
	 *        {@code false} otherwise
	 */
	public TextTable(TableModel tableModel, boolean addNumbering) {
		this.tableModel = tableModel;
		this.addRowNumbering = addNumbering;
	}

	/**
	 * Creates a text table using an in-memory {@link DefaultTableModel} built from the
	 * supplied headers and data.
	 *
	 * @param columnNames the column headers in display order
	 * @param data the row data to expose through the generated table model
	 */
	public TextTable(String[] columnNames, Object[][] data) {
		this.tableModel = new DefaultTableModel(data, columnNames);
	}

	/**
	 * Returns the wrapped table model.
	 *
	 * @return the table model used as the source for all rendered output
	 */
	public TableModel getTableModel() {
		return tableModel;
	}

	/**
	 * Enables or disables row numbering for text rendering.
	 * <p>
	 * Numbering is only used by the text renderer. Models that extend
	 * {@link TextTableModel} may still suppress numbering for individual rows via
	 * {@link TextTableModel#allowNumberingAt(int)}.
	 * </p>
	 *
	 * @param addNumbering {@code true} to display row numbers, {@code false} to omit them
	 */
	public void setAddRowNumbering(boolean addNumbering) {
		this.addRowNumbering = addNumbering;
	}

	/**
	 * Adds a policy that can request a separator line before specific rendered rows.
	 * <p>
	 * The supplied policy is associated with this table's current model before being stored.
	 * Multiple policies may be registered; a separator is rendered when any policy reports
	 * that one should be shown for a row.
	 * </p>
	 *
	 * @param separatorPolicy the separator policy to register
	 */
	public void addSeparatorPolicy(SeparatorPolicy separatorPolicy) {
		separatorPolicies.add(separatorPolicy);
		separatorPolicy.setTableModel(tableModel);
	}

	/**
	 * Sorts the text output by the specified column in ascending order.
	 *
	 * @param column the zero-based model column index to sort on
	 */
	public void setSort(int column) {
		setSort(column, SortOrder.ASCENDING);
	}

	/**
	 * Configures sorting for subsequent text rendering.
	 * <p>
	 * Sorting is performed through a {@link TableRowSorter}. The configured sorter affects
	 * the order used when the table is rendered as aligned text. CSV export continues to use
	 * the underlying model order.
	 * </p>
	 *
	 * @param column the zero-based model column index to sort on
	 * @param sortOrder the direction to apply to the sort
	 */
	public void setSort(int column, SortOrder sortOrder) {
		rowSorter = new TableRowSorter<TableModel>(this.tableModel);
		List<RowSorter.SortKey> sortKeys = new ArrayList<RowSorter.SortKey>();
		// sortKeys.add(new RowSorter.SortKey(1, SortOrder.ASCENDING));
		sortKeys.add(new RowSorter.SortKey(column, sortOrder));
		rowSorter.setSortKeys(sortKeys);
	}

	/**
	 * Prints the table to {@link System#out} with no indentation.
	 */
	public void printTable() {
		printTable(System.out, 0);
	}

	/**
	 * Prints the table as aligned plain text.
	 *
	 * @param ps the stream that receives the rendered table
	 * @param indent the number of leading spaces to write before each rendered line
	 */
	public void printTable(PrintStream ps, int indent) {
		TextTableRenderer renderer = new TextTableRenderer(this);
		renderer.render(ps, indent);
	}

	/**
	 * Writes the table as CSV.
	 * <p>
	 * CSV output always writes the model's column names followed by each model row in model
	 * order. Text-specific features such as row numbering, separator policies, indentation,
	 * and configured row sorting are not applied.
	 * </p>
	 *
	 * @param os the target stream that receives the CSV data
	 */
	public void toCsv(OutputStream os) {
		CsvTableRenderer csvTableRenderer = new CsvTableRenderer(this);
		csvTableRenderer.render(os, 0);
	}

	/**
	 * Returns the value for a rendered row and column, translating the row index through the
	 * configured sorter when text sorting is enabled.
	 *
	 * @param row the zero-based rendered row index
	 * @param column the zero-based column index
	 * @return the value from the underlying model for the resolved row and column
	 */
	protected Object getValueAt(int row, int column) {
		int rowIndex = row;
		if (rowSorter != null) {
			rowIndex = rowSorter.convertRowIndexToModel(row);
		}
		return tableModel.getValueAt(rowIndex, column);
	}

	/**
	 * Determines whether any registered separator policy requests a separator before the
	 * specified rendered row.
	 *
	 * @param row the zero-based rendered row index
	 * @return {@code true} when a separator should be shown before the row,
	 *         {@code false} otherwise
	 */
	protected boolean hasSeparatorAt(int row) {
		for (SeparatorPolicy separatorPolicy : separatorPolicies) {
			if (separatorPolicy.hasSeparatorAt(row)) {
				return true;
			}
		}
		return false;
	}

}
