package dnl.utils.text.table.csv;

import com.opencsv.CSVWriter;
import dnl.utils.text.table.TableRenderer;
import dnl.utils.text.table.TextTable;

import javax.swing.table.TableModel;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * Renders a {@link TextTable} as CSV.
 * <p>
 * The renderer writes a header row followed by every model row in model order using
 * OpenCSV.
 * </p>
 */
public class CsvTableRenderer implements TableRenderer {

	/**
	 * Reserved per-column format metadata for compatibility with the text renderer API.
	 */
	protected String[] formats;
	/**
	 * Reserved per-column length metadata for compatibility with the text renderer API.
	 */
	protected int[] lengths;

	/**
	 * Table wrapper that supplies the model to render.
	 */
	protected TextTable textTable;

	/**
	 * Cached model reference obtained from {@link #textTable}.
	 */
	protected TableModel tableModel;

	/**
	 * Creates a CSV renderer for the supplied text table.
	 *
	 * @param textTable the table whose model should be rendered as CSV
	 */
	public CsvTableRenderer(TextTable textTable) {
		this.textTable = textTable;
		this.tableModel = textTable.getTableModel();
	}

	@Override
	public void render(OutputStream ps, int indent) {
		this.render(new OutputStreamWriter(ps), indent);
	}

	@Override
	public void render(Writer w, int indent) {
		TableModel tableModel = textTable.getTableModel();
		try (CSVWriter csvWriter = new CSVWriter(w)) {
			String[] headers = new String[tableModel.getColumnCount()];
			for (int i = 0; i < headers.length; i++) {
				headers[i] = tableModel.getColumnName(i);
			}
			csvWriter.writeNext(headers);
			for (int i = 0; i < tableModel.getRowCount(); i++) {
				String[] line = new String[tableModel.getColumnCount()];
				for (int j = 0; j < tableModel.getColumnCount(); j++) {
					Object valueAt = tableModel.getValueAt(i, j);
					line[j] = String.valueOf(valueAt);
				}
				csvWriter.writeNext(line);
			}
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

}
