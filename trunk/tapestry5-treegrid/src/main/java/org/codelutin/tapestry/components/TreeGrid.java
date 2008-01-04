package org.codelutin.tapestry.components;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.tapestry.MarkupWriter;
import org.apache.tapestry.annotations.Environmental;
import org.apache.tapestry.annotations.IncludeJavaScriptLibrary;
import org.apache.tapestry.annotations.IncludeStylesheet;
import org.apache.tapestry.annotations.Parameter;
import org.apache.tapestry.annotations.SupportsInformalParameters;
import org.apache.tapestry.services.Heartbeat;
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

    @Environmental
    private Heartbeat heartbeat;

    private void buildSourceList(TreeNode treeNode, int depth, String dotId) {
        list.add(new RenderableNode(treeNode, depth, dotId));
        TreeNode[] children = (TreeNode[]) treeNode.getChildren();
        if (children != null) {
            for (int i = 0; i < children.length; i++) {
                if (!list.contains(children[i])) {
                    buildSourceList(children[i], depth + 1, dotId + "-" + i);
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

    /** Begins a new heartbeat. */
    void beginRender() {
        currentNode = iterator.next();
        heartbeat.begin();
    }

    void beforeRenderBody(MarkupWriter writer) {
        String[] columns = currentNode.getInnerNode().getColumns();

        writer.element("tr", "id", currentNode.getDotId(), "class", "a");

        writer.element("td");
        if (currentNode.getInnerNode().getType() == TreeNode.TYPE_FOLDER) {
            writer.element("div", "class", "tier" + currentNode.getDepth(),
                    "onclick", "toggleRowsDiv(this)");
            writer.element("a", "href", "#", "class", "folderopen", "onclick",
                    "toggleRows(this)");
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

    /** Ends the current heartbeat. */
    boolean afterRender() {
        heartbeat.end();
        return (!iterator.hasNext());
    }

    void cleanupRender(MarkupWriter writer) {
        // end table
        writer.end();
    }

}
