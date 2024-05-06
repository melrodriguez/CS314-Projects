import java.util.ArrayList;

public class PriorityQueue {
    private static final int EMPTY = -1;
    private ArrayList<TreeNode> container;

    /**
     * Constructor
     */
    public PriorityQueue() {
        container = new ArrayList<>();
    }

    /**
     * Add method for given value and frequency
     * @param value value of the character
     * @param freq frequency of the character
     */
    public void enqueue(int value, int freq) {
        enqueue(new TreeNode(value, freq));
    }
    /**
     * Adds a node to the container based on the frequency, it finds the first instance where the frequency is lower
     * than the frequency of the container and adds it to that index to make the queue fair
     * @param node the node that needs to be added
     */
    public void enqueue(TreeNode node) {
        if (container.isEmpty()) {
            container.add(node);
        }
        else {
            int index = 0;
            boolean foundGreaterNode = false;
            while (index < container.size() && !foundGreaterNode) {
                if (node.compareTo(container.get(index)) >= 0) {
                    index++;
                }
                else {
                    foundGreaterNode = true;
                }
            }
            if (foundGreaterNode) {
                // Adds in front of the first node with a higher frequency
                container.add(index, node);
            }
            else {
                // It has the highest frequency in the container
                container.add(node);
            }
        }
    }

    /**
     * Removes the element from the front of queue
     */
    public void dequeue() {
        container.remove(0);
    }

    /**
     * Combines the nodes from the container until the Huffman tree is created
     */
    public TreeNode combine() {
        if (container.isEmpty()) {
            throw new RuntimeException("Container cannot be empty.");
        }
        while (container.size() > 1) {
            TreeNode combinedNode = new TreeNode(container.get(0), EMPTY, container.get(1));
            enqueue(combinedNode);
            dequeue();
            dequeue();
        }

        return container.get(0);
    }
}

