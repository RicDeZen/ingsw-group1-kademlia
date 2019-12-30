package ingsw.group1.kademlia.listeners;

import androidx.annotation.NonNull;

import ingsw.group1.kademlia.PeerNode;

/**
 * Interface defining the default behaviour for a Class wanting to work as a listener for events
 * related to one or more
 * {@link ingsw.group1.kademlia.pendingrequests.PingPendingRequest}.
 *
 * @author Riccardo De Zen
 * CODE REVIEW
 */
public interface PingResultListener {
    /**
     * Method called when a Ping operation came to an end.
     *
     * @param operationId the id for the {@code PendingRequest} that reached a conclusion.
     * @param pinged      the {@code Peer} that got pinged.
     * @param isOnline    this parameter is true if {@code pinged} answered the Ping, false
     *                    otherwise.
     */
    void onPingResult(int operationId, @NonNull PeerNode pinged, boolean isOnline);
}
