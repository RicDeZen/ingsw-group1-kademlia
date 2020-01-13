package ingsw.group1.kademlia;

import androidx.annotation.NonNull;

import java.util.List;

/**
 * Interface defining the standard behaviour for a class able to propagate actions through a
 * Network.
 * Such a Class shall be passed to, and called by a
 * {@link ingsw.group1.kademlia.pendingrequests.PendingRequest}.
 *
 * @author Riccardo De Zen
 * CODE REVIEW
 */
public interface ActionPropagator {
    /**
     * Method to propagate an Action.
     *
     * @param action a valid, @NonNull Action.
     */
    void propagateAction(@NonNull KadAction action);

    /**
     * Method to propagate multiple Actions. When dealing with a conspicuous number of Actions,
     * to avoid stalling the main Thread, the implementation should judge whether to perform the
     * propagation in a separate Thread.
     *
     * @param actions a {@code List} containing the Actions to propagate.
     */
    void propagateActions(@NonNull List<KadAction> actions);
}
