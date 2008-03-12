package org.codelutin.tapestry.components;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.tapestry.MarkupWriter;
import org.apache.tapestry.annotations.IncludeJavaScriptLibrary;
import org.apache.tapestry.annotations.IncludeStylesheet;
import org.apache.tapestry.annotations.Parameter;
import org.apache.tapestry.annotations.SupportsInformalParameters;
import org.codelutin.tapestry.beans.RenderableNode;
import org.codelutin.tapestry.beans.TreeNode;

/**
 * The Class TreeGrid.
 */
@SupportsInformalParameters
@IncludeStylesheet("css/TreeGrid.css")
@IncludeJavaScriptLibrary("js/TreeGrid.js")
public class TreeGrid {

    /** The list. */
    private List<RenderableNode> list;

    /** Iterator to iterate over all tree elements. */
    private Iterator<RenderableNode> iterator;

    /** Defines the source Tree to walk over. */
    @Parameter(required = true)
    private List<TreeNode> source;

    /** The column headers. */
    @Parameter(required = true)
    private List<String> columnHeaders;

    /** Current node of the tree. */
    private RenderableNode currentNode;

    /**
     * Builds the source list.
     * 
     * @param treeNode
     *            the tree node
     * @param depth
     *            the depth
     * @param dotId
     *            the dot id
     */
    private void buildSourceList(TreeNode treeNode, int depth, String dotId) {
        list.add(new RenderableNode(treeNode, depth, dotId));
        List<TreeNode> children = treeNode.getChildren();
        if (children != null) {
            int i = 0;
            for (TreeNode node : children) {
                if (!list.contains(node)) {
                    buildSourceList(node, depth + 1, dotId + "-" + i);
                    i++;
                }
            }
        }
    }

    /**
     * Setup render.
     * 
     * @param writer
     *            the writer
     * 
     * @return true, if successful
     */
    boolean setupRender(MarkupWriter writer) {
        if (source == null)
            return false;

        list = new ArrayList<RenderableNode>();
        int i = 0;
        for (TreeNode node : source) {
            buildSourceList(node, 0, Integer.toString(i));
            i++;
        }
        iterator = list.iterator();

        // begin table
        writer.element("table", "width", "100%", "class", "t-data-grid");

        // header row
        writer.element("thead");
        writer.element("tr");
        if (columnHeaders != null) {
            int col = 0;
            for (String column : columnHeaders) {
                writer.element("th");
                writer.writeRaw(column);
                writer.end();
                col++;
            }
        }
        writer.end();
        writer.end();

        return (iterator.hasNext());
    }

    /**
     * Begins a node.
     * 
     * @param writer
     *            the writer
     */
    void beginRender(MarkupWriter writer) {
        currentNode = iterator.next();

        String[] columns = currentNode.getInnerNode().getColumns();

        writer.element("tr", "id", currentNode.getDotId());

        writer.element("td");
        if (currentNode.getInnerNode().getType() == TreeNode.TYPE_FOLDER) {
            writer.element("div", "class", "tier" + currentNode.getDepth(),
                    "onclick", "toggleRowsDiv(this)");
            writer.element("a", "href", "#", "class", "folderopen");
        } else {
            writer.element("div", "class", "tier" + currentNode.getDepth());
            writer.element("a", "href", "#", "class", "doc");
        }
        // a
        writer.end();

        if (columns.length > 0) {
            writer.writeRaw(columns[0]);
        }

        // div
        writer.end();
        // td
        writer.end();

        for (int i = 1; i < columnHeaders.size(); i++) {
            writer.element("td");
            if (i<columns.length) {
                writer.writeRaw(columns[i]);
            } else {
                writer.writeRaw("&nbsp;");
            }
            // td
            writer.end();
        }

        // tr
        writer.end();

    }

    /**
     * Ends the node.
     * 
     * @param writer
     *            the writer
     * 
     * @return true, if after render
     */
    boolean afterRender(MarkupWriter writer) {
        return (!iterator.hasNext());
    }

    /**
     * Cleanup render.
     * 
     * @param writer
     *            the writer
     */
    void cleanupRender(MarkupWriter writer) {
        // end table
        writer.end();
    }

}
