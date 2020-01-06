package ingsw.group1.kademlia;

import ingsw.group1.msglibrary.Message;
import ingsw.group1.msglibrary.Peer;

/**
 * Interface defining standard behaviour for an Action travelling through a Distributed Network.
 * This interface tries to be unrelated to the algorithm used to implement the Distributed Network.
 *
 * @param <P> type of {@code Peer} the Network Action uses.
 * @param <D> type of data {@code M} carries.
 * @param <M> type of {@code Message} the Network Action uses.
 * @author Niccolò Turcato, Riccardo De Zen
 */
public interface DistributedNetworkAction<P extends Peer, D, M extends Message<P, D>> {
    /**
     * Method returning whether an Action is valid. Depending on the implementation this could
     * always be true for Actions whose constructor came to an end.
     * The concept of "valid" depends on the implementation; it may or may not include the Action
     * fitting into a single {@code Message}.
     *
     * @return true if the Action is valid.
     */
    boolean isValid();

    /**
     * @return the {@code Peer} associated to this Action.
     */
    P getPeer();

    /**
     * Method to turn an Action into an appropriate {@code Message}. Implementing a static method or
     * a constructor to build an Action from a Message of appropriate type is highly recommended.
     *
     * @return a {@code Message}, containing the formatted Action command, ready to be sent.
     */
    M toMessage();

    /**
     * @return the D type representation of this Action, as it would be written in the Message
     * {@code <D>} type data.
     */
    D getPayload();
}
