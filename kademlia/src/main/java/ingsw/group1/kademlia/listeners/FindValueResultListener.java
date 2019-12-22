package ingsw.group1.kademlia.listeners;

import androidx.annotation.Nullable;

import ingsw.group1.kademlia.PeerNode;
import ingsw.group1.repnetwork.Resource;

/**
 * Interface defining the default behaviour for a Class wanting to work as a listener for events
 * related to one or more
 * {@link ingsw.group1.kademlia.pendingrequests.FindValuePendingRequest}.
 *
 * @author Riccardo De Zen
 */
public interface FindValueResultListener {
    /**
     * Method called when a {@link Resource} has been found, or it has been deemed as
     * non-existing in the Network.
     *
     * @param operationId the id for the {@code PendingRequest} that reached a conclusion.
     * @param owner       the {@code Peer} that had the Resource, if the Resource was found,
     *                    or {@code null} otherwise.
     * @param resource    the {@code Resource} that was found, or {@code null} if no Resource was
     *                    found.
     */
    void onFindValueResult(int operationId, @Nullable PeerNode owner, @Nullable Resource resource);
}
