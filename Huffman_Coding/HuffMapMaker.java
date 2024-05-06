import java.util.HashMap;
import java.util.Map;

public class HuffMapMaker {
    public Map<Integer, String> createHuffMap(TreeNode n) {
        Map<Integer, String> huffMap = new HashMap<>();
        String path = "";
        recursiveAddAll(n, huffMap, path);
        return huffMap;
    }

    public void recursiveAddAll(TreeNode n, Map<Integer, String> map, String path) {
        // Base Case: We have reached a leaf
        if (n.isLeaf()) {
            map.put(n.getValue(), path);
        }
        else {
            if (n.getLeft() == null && n.getRight() != null) {
                recursiveAddAll(n.getRight(), map, path + "1");
            }
            else if (n.getLeft() != null && n.getRight() == null) {
                recursiveAddAll(n.getLeft(), map, path + "0");
            }
            else {
                recursiveAddAll(n.getLeft(), map, path + "0");
                recursiveAddAll(n.getRight(), map, path + "1");
            }
        }
    }
}
