package ingsw.group1.kademlia;

public class KBucket extends Bucket<Node<BinarySet>> {
    private int dimension;
    private int last;
    private Node[] elements;

    /**
     * @param dim capacity of the bucket
     */
    public KBucket(int dim) {
        dimension = dim;
        elements = new Node[dimension];
    }

    /**
     * @param toFind object of which verify presence in bucket
     * @return true if the element is contained in the bucket, false otherwise
     */
    public boolean contains(Node toFind) {
        for (int i = 0; i < last; i++)
            if (elements[i].equals(toFind))
                return true;
        return false;
    }

    /**
     * Added with stack logic, eventually replaces the one "on top"
     *
     * @param peerNode object to add
     * @return true if it has been added, false otherwise
     */
    public boolean add(Node peerNode) {
        if (!contains(peerNode)) {
            if (last == dimension)
                elements[dimension - 1] = peerNode;
            else
                elements[last++] = peerNode;
            return contains(peerNode);
        } else return false;
    }

    /**
     * @param peerNode object to remove from bucket
     * @return true if obj has been removed, false otherwise
     */
    public boolean remove(Node peerNode) {
        if (contains(peerNode)) {
            for (int i = 0; i < last; i++)
                if (elements[i].equals(peerNode)) {
                    for (int j = i; j < dimension - 1; j++)
                        elements[j] = elements[j + 1];
                    elements[--last] = null;
                }
            return !contains(peerNode);
        }
        return false;
    }

    /**
     * @return an array containing (a copy) the elements in the bucket, sorted by insertion time
     */
    public Node[] getElements() {
        Node[] copy = new Node[last];
        for (int i = 0; i < last; i++) copy[i] = elements[i];
        return copy;
    }

    /**
     * @return (a copy of) the oldest peer in the network, the "bottom" one, if the bucket is empty returns null
     */
    public Node getOldest() {
        if (dimension > 0)
            return elements[0];
        return null;
    }

    /**
     * @return number of nodes in the bucket
     */
    public int size(){
        return last;
    }
}
