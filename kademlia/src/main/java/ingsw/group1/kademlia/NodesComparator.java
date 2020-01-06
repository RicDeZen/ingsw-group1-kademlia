package ingsw.group1.kademlia;

import java.util.Comparator;

/**
 * Compares Nodes based on the distance to a target node
 */
public class NodesComparator implements Comparator<Node<BinarySet>> {
    private Node<BinarySet> targetNode;
    /**
     * Builds this comparator with a target node
     */
    public NodesComparator(Node<BinarySet> target){
        targetNode = target;
    }

    /**
     * @param o1 First node to compare
     * @param o2 Second node to compare
     * @return 1, -1, 0 if 01 closer to target than o2, o1 further to target than o2, o1 same distance as o2
     */
    @Override
    public int compare(Node<BinarySet> o1, Node<BinarySet> o2) {
        BinarySet distance1 = targetNode.getDistance(o1);
        BinarySet distance2 = targetNode.getDistance(o2);
        return distance2.compareTo(distance1);
    }
}
