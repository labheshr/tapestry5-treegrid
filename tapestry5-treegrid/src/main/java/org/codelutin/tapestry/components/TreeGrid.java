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

@SupportsInformalParameters
@IncludeStylesheet("css/TreeGrid.css")
@IncludeJavaScriptLibrary("js/TreeGrid.js")
public class TreeGrid {

    private List<RenderableNode> list;

    /**
     * Iterator to iterate over all tree elements
     */
    private Iterator<RenderableNode> iterator;

    /**
     * Defines the source Tree to walk over.
     */
    @Parameter(required = true)
    private List<TreeNode> source;

    @Parameter(required = true)
    private List<String> columnHeaders;

    /**
     * Current node of the tree
     */
    private RenderableNode currentNode;

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
        writer.element("table", "width", "100%");

        // header row
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

        return (iterator.hasNext());
    }

    /** Begins a node. */
    void beginRender() {
        currentNode = iterator.next();
    }

    void beforeRenderBody(MarkupWriter writer) {
        String[] columns = currentNode.getInnerNode().getColumns();

        writer.element("tr", "id", currentNode.getDotId(), "class", "a");

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

        for (int i = 1; i < columns.length; i++) {
            writer.element("td");
            writer.writeRaw(columns[i]);
            // td
            writer.end();
        }

        // tr
        writer.end();
    }

    /** Ends the node. */
    boolean afterRender() {
        return (!iterator.hasNext());
    }

    void cleanupRender(MarkupWriter writer) {
        // end table
        writer.end();
    }

}
