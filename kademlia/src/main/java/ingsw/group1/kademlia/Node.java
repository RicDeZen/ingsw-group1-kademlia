package ingsw.group1.kademlia;


/**
 * Interface that describes an abstract Node for the distributed Network
 *
 * @param <T> type of key
 */
public interface Node<T> extends Cloneable{
    int keyLength();

    T getKey();

    T getDistance(Node<T> node);
}
