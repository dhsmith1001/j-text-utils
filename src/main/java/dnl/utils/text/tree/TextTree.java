package dnl.utils.text.tree;

import javax.swing.tree.TreeModel;
import java.io.PrintStream;

/**
 * Renders a {@link TreeModel} as a plain-text tree diagram.
 *
 * <p>The output uses ASCII art to depict parent-child relationships, for example:
 * <pre>{@code
 * -- root
 *    |-- child1
 *    |   `-- grandchild1
 *    `-- child2
 * }</pre>
 *
 * <p>By default the root node is included in the output. Call
 * {@link #setShowRoot(boolean) setShowRoot(false)} to suppress it and start
 * rendering from the root's immediate children instead.
 *
 * <p>Example usage:
 * <pre>{@code
 * DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");
 * root.add(new DefaultMutableTreeNode("child1"));
 * root.add(new DefaultMutableTreeNode("child2"));
 * TextTree tree = new TextTree(new DefaultTreeModel(root));
 * tree.printTree();
 * }</pre>
 *
 * @author Daniel Orr
 * @see TreeModel
 */
public class TextTree {

	private TreeModel model;
	private boolean showRoot = true;
	private StringBuilder sb;

	/**
	 * Creates a new {@code TextTree} that will render the given {@link TreeModel}.
	 *
	 * @param model the tree model to render; must not be {@code null}
	 */
	public TextTree(TreeModel model) {
		this.model = model;
	}

	/**
	 * Returns whether the root node is included in the rendered output.
	 *
	 * @return {@code true} if the root node is shown; {@code false} otherwise
	 */
	public boolean isShowRoot() {
		return showRoot;
	}

	/**
	 * Controls whether the root node is included in the rendered output.
	 *
	 * <p>When set to {@code false}, rendering begins at the root's immediate
	 * children, which is useful when the root is merely a structural container
	 * with no meaningful display value.
	 *
	 * @param showRoot {@code true} to include the root node in the output;
	 *                 {@code false} to start from its children
	 */
	public void setShowRoot(boolean showRoot) {
		this.showRoot = showRoot;
	}

	/**
	 * Prints the tree diagram to {@link System#out}.
	 *
	 * <p>Equivalent to {@code System.out.println(tree.toString())}.
	 */
	public void printTree() {
		System.out.println(toString());
	}

	/**
	 * Prints the tree diagram to the given {@link PrintStream}.
	 *
	 * <p>Equivalent to {@code printStream.println(tree.toString())}.
	 *
	 * @param printStream the stream to write the tree diagram to; must not be {@code null}
	 */
	public void printTree(PrintStream printStream) {
		printStream.println(toString());
	}

	/**
	 * Returns a plain-text representation of the tree diagram.
	 *
	 * <p>Each call rebuilds the diagram from the current state of the underlying
	 * {@link TreeModel}, so changes to the model are reflected in subsequent calls.
	 *
	 * @return a non-{@code null} string containing the ASCII art tree diagram
	 */
	@Override
	public String toString() {
		sb = new StringBuilder();
		Object root = model.getRoot();
		printNode(root, "", "", showRoot);
		return sb.toString();
	}

	private void printNode(Object node, String pref, String bud, boolean render) {
		if (render) {
			sb.append(pref);
			sb.append(bud);
			sb.append("-- ");
			sb.append(node.toString());
			sb.append("\n");
		}
		String pref1 = pref + "   ";
		String pref2 = pref + "   |";
		String pref3 = pref + "";
		int childCount = model.getChildCount(node);
		String ind;
		String bud2;
		if (childCount > 0) {
			for (int i = 0; i < childCount; i++) {
				boolean lastElement = i == childCount - 1;
				if (lastElement) {
					ind = pref1;
					bud2 = "`";
				} else {
					ind = pref2;
					bud2 = "";
				}
				printNode(model.getChild(node, i), !render ? pref3 : ind, !render ? "" : bud2, true);
			}
		}
	}

}
