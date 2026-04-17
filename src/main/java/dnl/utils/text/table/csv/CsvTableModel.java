package dnl.utils.text.table.csv;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import javax.swing.table.AbstractTableModel;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;


/**
 * Swing {@link javax.swing.table.TableModel} backed by CSV data.
 * <p>
 * The first CSV record supplies the column names and every subsequent record is exposed
 * as a row in the model.
 * </p>
 *
 * @author Daniel Orr
 */
public class CsvTableModel extends AbstractTableModel {

	/**
	 * Column names parsed from the first CSV record.
	 */
	private String[] columnNames;
	/**
	 * Data rows parsed after the header record.
	 */
	private List<String[]> lines = new ArrayList<>();

	/**
	 * Creates a table model by reading CSV content from a file.
	 *
	 * @param csvFile the CSV file to read
	 * @throws IOException if the file cannot be read
	 * @throws CsvValidationException if the CSV input is malformed
	 */
	public CsvTableModel(File csvFile) throws IOException, CsvValidationException {
		this(new FileReader(csvFile));
	}

	/**
	 * Creates a table model by reading CSV content from an in-memory string.
	 *
	 * @param csvString the CSV content to parse
	 * @throws IOException if the string reader cannot be consumed
	 * @throws CsvValidationException if the CSV input is malformed
	 */
	public CsvTableModel(String csvString) throws IOException, CsvValidationException {
		this(new StringReader(csvString));
	}

	/**
	 * Creates a table model by reading CSV content from a character stream.
	 *
	 * @param reader the character stream containing CSV data
	 * @throws IOException if the stream cannot be read
	 * @throws CsvValidationException if the CSV input is malformed
	 */
	public CsvTableModel(Reader reader) throws IOException, CsvValidationException
    {
		try (CSVReader csvReader = new CSVReader(reader)) {
			String[] line = null;
			boolean first = true;
			while ((line = csvReader.readNext()) != null) {
				if (first) {
					columnNames = line;
					first = false;
				} else {
					lines.add(line);
				}
			}
		}
	}

	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}

	@Override
	public int getRowCount() {
		return lines.size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		String[] line = lines.get(rowIndex);
		return line[columnIndex];
	}

}
