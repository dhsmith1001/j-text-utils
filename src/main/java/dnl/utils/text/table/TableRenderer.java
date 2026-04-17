package dnl.utils.text.table;

import java.io.OutputStream;
import java.io.Writer;

/**
 * Defines the contract for rendering a table to an output destination.
 *
 * <p>Implementations are responsible for formatting and writing table content
 * to either an {@link OutputStream} or a {@link Writer}, with support for
 * indentation.</p>
 */
public interface TableRenderer {

	/**
	 * Renders the table to the given {@link OutputStream}.
	 *
	 * @param ps     the output stream to write the rendered table to; must not be {@code null}
	 * @param indent the number of spaces to use for indentation
	 */
	public void render(OutputStream ps, int indent);

	/**
	 * Renders the table to the given {@link Writer}.
	 *
	 * @param w      the writer to write the rendered table to; must not be {@code null}
	 * @param indent the number of spaces to use for indentation
	 */
	public void render(Writer w, int indent);
}
