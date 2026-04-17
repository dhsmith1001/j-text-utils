package dnl.utils.text.table;

import java.io.PrintStream;

/**
 * Renderer for {@link TextTreeTable} that suppresses repeated values in the
 * configured hierarchical column until a separator boundary is reached.
 */
public class TextTreeTableRenderer extends TextTableRenderer{

	/**
	 * Creates a renderer for the provided tree table.
	 *
	 * @param textTable tree table instance to render
	 */
	public TextTreeTableRenderer(TextTreeTable textTable) {
		super(textTable);
	}

	/**
	 * Returns the table instance as a {@link TextTreeTable}.
	 *
	 * @return tree table bound to this renderer
	 */
	private TextTreeTable getTextTreeTable(){
		return (TextTreeTable) textTable;
	}

	/**
	 * Prints a single cell value, replacing repeated hierarchical values with an
	 * empty value when no separator is present at the current row.
	 *
	 * @param ps output stream
	 * @param row source row index
	 * @param col source column index
	 * @param c fallback empty-cell flag from parent renderer behavior
	 */
	@Override
	protected void printValue(PrintStream ps, int row, int col, boolean c){
		boolean empty = false;
		if( row != 0 && col == getTextTreeTable().hierarchicalColumn && !textTable.hasSeparatorAt(row)){
			empty = true;
		}
		super.printValue(ps, row, col, empty);
	}
}
