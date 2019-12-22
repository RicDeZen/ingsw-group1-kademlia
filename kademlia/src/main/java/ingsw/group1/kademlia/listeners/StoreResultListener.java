package ingsw.group1.kademlia.listeners;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import ingsw.group1.kademlia.PeerNode;
import ingsw.group1.repnetwork.Resource;

/**
 * Interface defining the default behaviour for a Class wanting to work as a listener for events
 * related to one or more
 * {@link ingsw.group1.kademlia.pendingrequests.StorePendingRequest}.
 *
 * @author Riccardo De Zen
 */
public interface StoreResultListener {
    /**
     * Method called when a Store operation has been completed.
     *
     * @param operationId    the id for the {@code PendingRequest} that reached a conclusion.
     * @param storedResource the {@code Resource} involved in the operation.
     * @param newOwner       the {@code Peer} that stored the Resource, or {@code null} if a
     *                       "better" new host for the {@code Resource} was not available.
     */
    void onStoreResult(int operationId, @NonNull Resource storedResource, @Nullable PeerNode newOwner);
}
