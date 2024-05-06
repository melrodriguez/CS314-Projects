/* CS 314 STUDENTS: FILL IN THIS HEADER.
 *
 * Student information for assignment:
 *
 *  On my honor, <Melody Rodriguez>, this programming assignment is my own work
 *  and I have not provided this code to any other student.
 *
 *  UTEID: mar9688
 *  email address: mar9688@utexas.edu
 *  TA name: Bersam
 *  Number of slip days I am using: 0
 */

import java.util.List;
import java.util.ArrayList;

/**
 * Shell for a binary search tree class.
 * @author scottm
 * @param <E> The data type of the elements of this BinarySearchTree.
 * Must implement Comparable or inherit from a class that implements
 * Comparable.
 *
 */
public class BinarySearchTree<E extends Comparable<? super E>> {

    private BSTNode<E> root;
    private int size;

    // CS314 students. Add a default constructor here if you feel it is necessary.

    /**
     *  Add the specified item to this Binary Search Tree if it is not already present.
     *  <br>
     *  pre: <tt>value</tt> != null<br>
     *  post: Add value to this tree if not already present. Return true if this tree
     *  changed as a result of this method call, false otherwise.
     *  @param value the value to add to the tree
     *  @return false if an item equivalent to value is already present
     *  in the tree, return true if value is added to the tree and size() = old size() + 1
     */
    public boolean add(E value) {
        // Code came frome lecture
        int oldSize = size;
        root = addHelp(value, root);
        return oldSize != size;
    }

    /**
     * Helper method for the add method that returns the node with value
     */
    private BSTNode<E> addHelp (E val, BSTNode<E> n) {
        // Code came from lecture
        // If root is empty
        if (n == null) {
            size++;
            return new BSTNode<E>(val);
        }
        else {
            // Root is not empty
            int dir = val.compareTo(n.data);
            // val less than root go left
            if (dir < 0) {
                n.left = addHelp(val, n.left);
            }
            // val greater than root go right
            else if (dir > 0) {
                n.right = addHelp(val, n.right);
            }

            return n;
        }
    }

    /**
     *  Remove a specified item from this Binary Search Tree if it is present.
     *  <br>
     *  pre: <tt>value</tt> != null<br>
     *  post: Remove value from the tree if present, return true if this tree
     *  changed as a result of this method call, false otherwise.
     *  @param value the value to remove from the tree if present
     *  @return false if value was not present
     *  returns true if value was present and size() = old size() - 1
     */
    public boolean remove(E value) {
        // Code came from lecture
        int oldSize = size;
        root = removeHelp(value, root);
        return oldSize != size;
    }

    private BSTNode<E> removeHelp(E val, BSTNode<E> n) {
        // Code came from lecture
        if (n == null) {
            // val is not in this tree
            return null;
        }
        else {
            int dir = val.compareTo(n.data);
            if (dir < 0) {
                n.left = removeHelp(val, n.left);
            }
            else if (dir > 0) {
                n.right = removeHelp(val, n.right);
            }
            else {
                size--; // about to remove val from tree
                // Case one: n is leaf
                if (n.left == null && n.right == null) {
                    return null;
                }
                // Case two: n has single child to left
                else if (n.right == null) {
                    return n.left;
                }
                // Case three: n has single child to right
                else if (n.left == null) {
                    return n.right;
                }
                // n has two children
                else {
                    n.data = maxHelp(n.left);
                    n.left = removeHelp(n.data, n.left);
                    size++; // to add back extra size--
                }
            }

            return n;
        }
    }

    /**
     * Finds the maximum leaf from that node n
     * @param n is a BSTNode the method starts from
     * pre: n != null
     */
    private E maxHelp(BSTNode<E> n){
        // Code is from lecture
        while (n.right != null) {
            n = n.right;
        }

        return n.data;
    }


    /**
     *  Check to see if the specified element is in this Binary Search Tree.
     *  <br>
     *  pre: <tt>value</tt> != null<br>
     *  post: return true if value is present in tree, false otherwise
     *  @param value the value to look for in the tree
     *  @return true if value is present in this tree, false otherwise
     */
    public boolean isPresent(E value) {
        return isPresentHelp(value, root);
    }

    private boolean isPresentHelp(E val, BSTNode<E> n) {
        if (n == null) {
            // value is not in tree
            return false;
        }
        else {
            int dir = val.compareTo(n.data);

            if (dir < 0) {
               return isPresentHelp(val, n.left);
            }
            else if (dir > 0) {
                return isPresentHelp(val, n.right);
            }
            else {
                // n is the value
                return true;
            }
        }
    }


    /**
     *  Return how many elements are in this Binary Search Tree.
     *  <br>
     *  pre: none<br>
     *  post: return the number of items in this tree
     *  @return the number of items in this Binary Search Tree
     */
    public int size() {
        return size;
    }

    /**
     *  return the height of this Binary Search Tree.
     *  <br>
     *  pre: none<br>
     *  post: return the height of this tree.
     *  If the tree is empty return -1, otherwise return the
     *  height of the tree
     *  @return the height of this tree or -1 if the tree is empty
     */
    public int height() {
        // Code came from lecture
        return htHelp(root);
    }

    private int htHelp(BSTNode<E> n) {
        // Code came fromt lecture
        if (n == null) {
            return -1;
        }

        return 1 + Math.max(htHelp(n.left), htHelp(n.right));
    }

    /**
     *  Return a list of all the elements in this Binary Search Tree.
     *  <br>
     *  pre: none<br>
     *  post: return a List object with all data from the tree in ascending order.
     *  If the tree is empty return an empty List
     *  @return a List object with all data from the tree in sorted order
     *  if the tree is empty return an empty List
     */
    public List<E> getAll() {
        ArrayList<E> ascendingNodes = new ArrayList<>();
        getAllHelp(root, ascendingNodes);

        return ascendingNodes;
    }

    /**
     * Helper Method for the getAll method, recursively finds all the nodes in
     * ascending order and stores it in the list
     * @param n is the current node
     * @param nodes, a list of data from nodes in ascending order
     */
    private void getAllHelp(BSTNode<E> n, ArrayList<E> nodes) {
        // Base Case: We have reached a leaf
        if (n.left == null && n.right == null) {
            nodes.add(n.data);
        }
        // Recursive Case
        else {
            // If it has a right child
            if (n.left == null && n.right != null) {
                nodes.add(n.data);
                getAllHelp(n.right, nodes);
            }
            // If it has a left child
            else if (n.left != null && n.right == null) {
                getAllHelp(n.left, nodes);
                nodes.add(n.data);
            }
            // It has two children
            else {
                getAllHelp(n.left, nodes);
                nodes.add(n.data);
                getAllHelp(n.right, nodes);
            }
        }
    }




    /**
     * return the maximum value in this binary search tree.
     * <br>
     * pre: <tt>size()</tt> > 0<br>
     * post: return the largest value in this Binary Search Tree
     * @return the maximum value in this tree
     */
    public E max() {
        BSTNode<E> n = root;

        while (n.right != null) {
            n = n.right;
        }

        return n.data;
    }

    /**
     * return the minimum value in this binary search tree.
     * <br>
     * pre: <tt>size()</tt> > 0<br>
     * post: return the smallest value in this Binary Search Tree
     * @return the minimum value in this tree
     */
    public E min() {
        BSTNode<E> n = root;

        while (n.left != null) {
            n = n.left;
        }

        return n.data;
    }

    /**
     * An add method that implements the add algorithm iteratively 
     * instead of recursively.
     * <br>pre: data != null
     * <br>post: if data is not present add it to the tree, 
     * otherwise do nothing.
     * @param data the item to be added to this tree
     * @return true if data was not present before this call to add, 
     * false otherwise.
     */
    public boolean iterativeAdd(E data) {
        // Empty tree
        if (root == null) {
            root = new BSTNode<E>(data);
            size++;
            return true;
        }
        else {
            BSTNode<E> n = root;

            while (n != null) {
                int dir = data.compareTo(n.data);

                if (dir > 0) {
                    // If empty we add it
                    if (n.right == null) {
                        n.right = new BSTNode<E>(data);
                        size++;

                        return true;
                    }
                    else {
                        n = n.right;
                    }
                }
                else if (dir < 0) {
                    // If empty we add it
                    if (n.left == null) {
                        n.left = new BSTNode<E>(data);
                        size++;

                        return true;
                    }
                    else {
                        n = n.left;
                    }
                }
                else {
                    return false;
                }
            }

            return false; // Return statement will never get reached
        }
    }


    /**
     * Return the "kth" element in this Binary Search Tree. If kth = 0 the
     * smallest value (minimum) is returned.
     * If kth = 1 the second smallest value is returned, and so forth.
     * <br>pre: 0 <= kth < size()
     * @param kth indicates the rank of the element to get
     * @return the kth value in this Binary Search Tree
     */
    public E get(int kth) {
        if (kth < 0 || kth > size()) {
            throw new IllegalArgumentException("Kth is out of bounds");
        }

        ArrayList<E> nodes = new ArrayList<>();
        getHelperMethod(root, kth, nodes);
        return nodes.get(nodes.size() - 1);
    }

    /**
     * A helper method for the get method to recursively find all the nodes till
     * it reaches the kth element, then it returns the kth element
     * @param n, the current node
     * @param kth, the kth node we are trying to find
     * @param nodes, a list of nodes in ascending order
     */
    private void getHelperMethod(BSTNode<E> n, int kth, ArrayList<E> nodes) {
        // Base Case: We have reached the kth element then do nothing
        // Recursive Case
        if (nodes.size() - 1 < kth) {
            // We have reached a leaf
            if (n.left == null && n.right == null) {
                nodes.add(n.data);
            }
            // If it has a right child
            else if (n.left == null && n.right != null) {
                nodes.add(n.data);
                getHelperMethod(n.right, kth, nodes);
            }
            // If it has a left child
            else if (n.left != null && n.right == null) {
                getHelperMethod(n.left, kth, nodes);
                if (nodes.size() - 1 < kth) {
                    nodes.add(n.data);
                }
            }
            // It has two children
            else {
                getHelperMethod(n.left, kth, nodes);
                if (nodes.size() - 1 < kth) {
                    nodes.add(n.data);
                }
                getHelperMethod(n.right, kth, nodes);
            }
        }
    }

    /**
     * Return a List with all values in this Binary Search Tree 
     * that are less than the parameter <tt>value</tt>.
     * <tt>value</tt> != null<br>
     * @param value the cutoff value
     * @return a List with all values in this tree that are less than 
     * the parameter value. If there are no values in this tree less 
     * than value return an empty list. The elements of the list are 
     * in ascending order.
     */
    public List<E> getAllLessThan(E value) {
        ArrayList<E> nodesLessThan = new ArrayList<>();
        getAllLessThanHelper(root, value, nodesLessThan);
        return nodesLessThan;
    }


    /**
     * A helper method to find all the nodes that are less than a certain value
     * @param n, the current node
     * @param value, the value we are comparing our nodes to
     * @param nodesLessThan, a list of node datas that are less than the value
     */
    private void getAllLessThanHelper(BSTNode<E> n, E value, ArrayList<E> nodesLessThan) {
        int dir = value.compareTo(n.data);

        // Base Case: do nothing
        // Recursive Case
        if (dir <= 0 && n.left != null) {
            getAllLessThanHelper(n.left, value, nodesLessThan);
        }
        else if (dir > 0) {
            // We have reached a leaf
            if (n.left == null && n.right == null) {
                nodesLessThan.add(n.data);
            }
            // If it has a right child
            else if (n.left == null && n.right != null) {
                nodesLessThan.add(n.data);
                getAllLessThanHelper(n.right, value, nodesLessThan);
            }
            // If it has a left child
            else if (n.left != null && n.right == null) {
                getAllLessThanHelper(n.left, value, nodesLessThan);
                nodesLessThan.add(n.data);
            }
            // It has two children
            else {
                getAllLessThanHelper(n.left, value, nodesLessThan);
                nodesLessThan.add(n.data);
                getAllLessThanHelper(n.right, value, nodesLessThan);
            }
        }
    }


    /**
     * Return a List with all values in this Binary Search Tree 
     * that are greater than the parameter <tt>value</tt>.
     * <tt>value</tt> != null<br>
     * @param value the cutoff value
     * @return a List with all values in this tree that are greater
     *  than the parameter value. If there are no values in this tree
     * greater than value return an empty list. 
     * The elements of the list are in ascending order.
     */
    public List<E> getAllGreaterThan(E value) {
        ArrayList<E> nodesGreaterThan = new ArrayList<>();
        getAllGreaterThanHelper(root, value, nodesGreaterThan);
        return nodesGreaterThan;
    }

    private void getAllGreaterThanHelper(BSTNode<E> n, E value, ArrayList<E> nodesGreaterThan) {
        int dir = value.compareTo(n.data);

        // Base Case: do nothing
        // Recursive Case
        if (dir >= 0 && n.right != null) {
            getAllGreaterThanHelper(n.right, value, nodesGreaterThan);
        }
        else if (dir < 0) {
            // We have reached a leaf
            if (n.left == null && n.right == null) {
                nodesGreaterThan.add(n.data);
            }
            // If it has a right child
            else if (n.left == null && n.right != null) {
                nodesGreaterThan.add(n.data);
                getAllGreaterThanHelper(n.right, value, nodesGreaterThan);
            }
            // If it has a left child
            else if (n.left != null && n.right == null) {
                getAllGreaterThanHelper(n.left, value, nodesGreaterThan);
                nodesGreaterThan.add(n.data);
            }
            // It has two children
            else {
                getAllGreaterThanHelper(n.left, value, nodesGreaterThan);
                nodesGreaterThan.add(n.data);
                getAllGreaterThanHelper(n.right, value, nodesGreaterThan);
            }
        }
    }


    /**
     * Find the number of nodes in this tree at the specified depth.
     * <br>pre: none
     * @param d The target depth.
     * @return The number of nodes in this tree at a depth equal to
     * the parameter d.
     */
    public int numNodesAtDepth(int d) {
        int count = 0;
        return numNodesAtDepthHelper(d, count, root);
    }

    /**
     * Helper method to recursively find the total number of nodes at a certain depth
     * @param depth, number towards the goal depth
     * @param count, the total number of nodes at goal depth
     * @param n, the current node
     */
    private int numNodesAtDepthHelper(int depth, int count, BSTNode<E> n) {
        int newCount = 0;
        // Base Case: We reached a node at this depth
        if (depth == 0) {
            newCount = count + 1;
        }
        else {
            if (n.left != null) {
                newCount = numNodesAtDepthHelper(depth - 1, count, n.left);
            }
            if (n.right != null) {
                newCount = numNodesAtDepthHelper(depth - 1, newCount, n.right);
            }
        }

        return newCount;
    }

    /**
     * Prints a vertical representation of this tree.
     * The tree has been rotated counter clockwise 90
     * degrees. The root is on the left. Each node is printed
     * out on its own row. A node's children will not necessarily
     * be at the rows directly above and below a row. They will
     * be indented three spaces from the parent. Nodes indented the
     * same amount are at the same depth.
     * <br>pre: none
     */
    public void printTree() {
        printTree(root, "");
    }

    private void printTree(BSTNode<E> n, String spaces) {
        if(n != null){
            printTree(n.right, spaces + "  ");
            System.out.println(spaces + n.data);
            printTree(n.left, spaces + "  ");
        }
    }

    private static class BSTNode<E extends Comparable<? super E>> {
        private E data;
        private BSTNode<E> left;
        private BSTNode<E> right;

        private BSTNode() {
            this(null);
        }

        private BSTNode(E initValue) {
            this(null, initValue, null);
        }

        private BSTNode(BSTNode<E> initLeft,
                E initValue,
                BSTNode<E> initRight) {
            data = initValue;
            left = initLeft;
            right = initRight;
        }

        private boolean isLeaf() {
            return left == null && right == null;
        }
    }
}
