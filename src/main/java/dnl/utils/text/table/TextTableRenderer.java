package dnl.utils.text.table;

import org.apache.commons.lang3.StringUtils;

import javax.swing.table.TableModel;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Writer;

/**
 * Renders a {@link TextTable} as plain-text ASCII art to an {@link OutputStream} or {@link Writer}.
 *
 * <p>The rendered output includes a bordered header row, optional row-numbering, optional
 * row separators driven by {@link TextTable}'s separator policies, and properly aligned
 * columns whose widths are computed dynamically from the data.</p>
 *
 * <p>Rendering to a {@link java.io.Writer} is not yet supported and will throw an
 * {@link UnsupportedOperationException}.</p>
 */
public class TextTableRenderer implements TableRenderer {

	/** Format strings (one per column) used by {@link java.io.PrintStream#printf} to align cell values. */
	protected String[] formats;

	/** Maximum content width (in characters) for each column. */
	protected int[] lengths;

	/** The {@link TextTable} whose data this renderer displays. */
	protected TextTable textTable;

	/** The underlying data model obtained from {@link #textTable}. */
	protected TableModel tableModel;

	/** When {@code true}, {@code null} cell values are rendered as the string {@code "null"}; otherwise as an empty string. */
	private boolean showNulls = false;

	/**
	 * Creates a renderer for the given {@link TextTable}, rendering {@code null} values as empty strings.
	 *
	 * @param textTable the table to render; must not be {@code null}
	 */
	public TextTableRenderer(TextTable textTable) {
		this.textTable = textTable;
		this.tableModel = textTable.getTableModel();
	}

	/**
	 * Creates a renderer for the given {@link TextTable}.
	 *
	 * @param textTable the table to render; must not be {@code null}
	 * @param showNulls when {@code true}, {@code null} cell values are displayed as {@code "null"};
	 *                  when {@code false} they are displayed as an empty string
	 */
	public TextTableRenderer(TextTable textTable, boolean showNulls) {
		this.textTable = textTable;
		this.tableModel = textTable.getTableModel();
		this.showNulls = showNulls;
	}

	/**
	 * Renders the table to the given {@link OutputStream}.
	 *
	 * <p>The stream is wrapped in a {@link PrintStream} if it is not already one.
	 * Column widths are computed from the data before rendering begins.</p>
	 *
	 * @param os     the output stream to write to; must not be {@code null}
	 * @param indent the number of leading spaces to prepend to every line
	 */
	@Override
	public void render(OutputStream os, int indent) {
		PrintStream ps;
		if (os instanceof PrintStream) {
			ps = (PrintStream) os;
		} else {
			ps = new PrintStream(os);
		}
		TableModel tableModel = textTable.getTableModel();
		String indentStr = StringUtils.repeat(" ", indent);

		// Find the maximum length of a string in each column
		resolveColumnLengths();
		String separator = resolveSeparator(lengths);

		int rowCount = tableModel.getRowCount();
		int rowCountStrSize = Integer.toString(rowCount).length();
		String indexFormat1 = "%1$-" + rowCountStrSize + "s  ";
		String indexFormat2 = "%1$" + rowCountStrSize + "s. ";

		// Generate a format string for each column and calc totalLength
		int totLength = resolveFormats();

		String headerStartSep = StringUtils.repeat("_", totLength + tableModel.getColumnCount() * 2);
		ps.print(indentStr);
		indentAccordingToNumbering(ps, indexFormat1);
		ps.println(headerStartSep);

		ps.print(indentStr);
		indentAccordingToNumbering(ps, indexFormat1);
		for (int j = 0; j < tableModel.getColumnCount(); j++) {
			ps.printf(formats[j], tableModel.getColumnName(j));
		}

		indentAccordingToNumbering(ps, indexFormat1);
		String headerSep = StringUtils.repeat("=", totLength + tableModel.getColumnCount() * 2 - 1);
		ps.print(indentStr);
		ps.print("|");
		ps.print(headerSep);
		ps.println("|");

		// Print 'em out
		for (int i = 0; i < tableModel.getRowCount(); i++) {
			addSeparatorIfNeeded(ps, separator, indexFormat1, i, indentStr);
			ps.print(indentStr);
			if (textTable.addRowNumbering) {
				if (!modelAllowsNumberingAt(i)) {
					indentAccordingToNumbering(ps, indexFormat1);
				} else {
					ps.printf(indexFormat2, i + 1);
				}
			}
			for (int j = 0; j < tableModel.getColumnCount(); j++) {
				printValue(ps, i, j, false);
			}
		}
	}

	/**
	 * Computes the maximum content width for each column by scanning every cell value,
	 * storing the results in {@link #lengths}.
	 */
	private void resolveColumnLengths() {
		lengths = new int[tableModel.getColumnCount()];

		for (int col = 0; col < tableModel.getColumnCount(); col++) {
			for (int row = 0; row < tableModel.getRowCount(); row++) {
				Object val = tableModel.getValueAt(row, col);
				String valStr = String.valueOf(val);
				if (!showNulls && val == null) {
					valStr = "";
				}
				lengths[col] = Math.max(valStr.length(), lengths[col]);
			}
		}
	}

	/**
	 * Builds the row-separator string used between data rows, and also ensures that
	 * {@link #lengths} entries are at least as wide as the corresponding column header.
	 *
	 * @param lengths the column-width array to update in place; must not be {@code null}
	 * @return a separator string of the form {@code |------|------|...|}
	 */
	private String resolveSeparator(int[] lengths) {
		StringBuilder sepSb = new StringBuilder();

		for (int j = 0; j < tableModel.getColumnCount(); j++) {
			if (j == 0) {
				sepSb.append("|");
			}
			lengths[j] = Math.max(tableModel.getColumnName(j).length(), lengths[j]);
			// add 1 because of the leading space in each column
			sepSb.append(StringUtils.repeat("-", lengths[j] + 1));
			sepSb.append("|");
		}
		String separator = sepSb.toString();
		return separator;
	}

	/**
	 * Prints the row separator line for row {@code i} if the table's separator policies
	 * or the underlying {@link TextTableModel} request one at that position.
	 *
	 * @param ps         the print stream to write to; must not be {@code null}
	 * @param separator  the pre-built separator string returned by {@link #resolveSeparator}
	 * @param indexFormat1 the printf format string used to indent numbering columns
	 * @param i          the current (zero-based) row index
	 * @param indentStr  the leading whitespace string for the current indent level
	 */
	private void addSeparatorIfNeeded(PrintStream ps, String separator, String indexFormat1, int i, String indentStr) {
		if (!textTable.separatorPolicies.isEmpty() && textTable.hasSeparatorAt(i)
				|| ((tableModel instanceof TextTableModel) && ((TextTableModel) tableModel).addSeparatorAt(i))) {
			indentAccordingToNumbering(ps, indexFormat1);
			ps.print(indentStr);
			ps.println(separator);
		}
	}

	/**
	 * Returns whether the underlying model permits row numbering to be displayed for the given row.
	 *
	 * <p>If the model is a {@link TextTableModel}, its {@code allowNumberingAt} method is consulted;
	 * otherwise the value of {@link TextTable#addRowNumbering} is returned.</p>
	 *
	 * @param row the zero-based row index to check
	 * @return {@code true} if a row number should be printed for this row
	 */
	protected boolean modelAllowsNumberingAt(int row) {
		if (row == 8) {
			System.out.print("");
		}
		if (tableModel instanceof TextTableModel) {
			TextTableModel ttm = (TextTableModel) tableModel;
			return ttm.allowNumberingAt(row);
		}
		return textTable.addRowNumbering;
	}

	/**
	 * Prints a single cell value to the print stream using the pre-computed column format.
	 *
	 * <p>If a {@link javax.swing.RowSorter} is attached to the table, the row index is
	 * converted from the view to the model before the value is looked up.</p>
	 *
	 * @param ps    the print stream to write to; must not be {@code null}
	 * @param row   the zero-based view row index
	 * @param col   the zero-based column index
	 * @param empty when {@code true}, an empty string is printed regardless of the cell value
	 */
	protected void printValue(PrintStream ps, int row, int col, boolean empty) {
		int rowIndex = row;
		if (textTable.rowSorter != null) {
			rowIndex = textTable.rowSorter.convertRowIndexToModel(row);
		}
		Object val = tableModel.getValueAt(rowIndex, col);
		if (val == null && !showNulls) {
			val = "";
		}
		Object value = empty ? "" : val;
		ps.printf(formats[col], value);
	}

	/**
	 * Builds the printf format strings for every column and returns the total content width
	 * (sum of all column widths, excluding border characters).
	 *
	 * @return the total content width across all columns
	 */
	private int resolveFormats() {
		int totLength = 0;
		formats = new String[lengths.length];
		for (int i = 0; i < lengths.length; i++) {
			StringBuilder sb = new StringBuilder();
			if (i == 0) {
				sb.append("|");
			}
			sb.append(" %1$-");
			sb.append(lengths[i]);
			sb.append("s|");
			sb.append(i + 1 == lengths.length ? "\n" : "");
			formats[i] = sb.toString();
			totLength += lengths[i];
		}
		return totLength;
	}

	/**
	 * Prints the indentation placeholder used to align content with numbered rows when
	 * {@link TextTable#addRowNumbering} is enabled.
	 *
	 * @param ps           the print stream to write to; must not be {@code null}
	 * @param indexFormat1 the printf format string used for the row-number placeholder
	 */
	private void indentAccordingToNumbering(PrintStream ps, String indexFormat1) {
		if (textTable.addRowNumbering) {
			ps.printf(indexFormat1, "");
		}
	}

	/**
	 * Not yet implemented.
	 *
	 * @throws UnsupportedOperationException always
	 */
	@Override
	public void render(Writer w, int indent) {
		throw new UnsupportedOperationException();
	}
}
