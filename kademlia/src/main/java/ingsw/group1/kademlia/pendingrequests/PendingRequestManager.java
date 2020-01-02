package ingsw.group1.kademlia.pendingrequests;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import ingsw.group1.kademlia.ActionPropagator;
import ingsw.group1.kademlia.BinarySet;
import ingsw.group1.kademlia.KadAction;
import ingsw.group1.kademlia.NodeDataProvider;
import ingsw.group1.kademlia.PeerNode;
import ingsw.group1.kademlia.exceptions.InvalidActionException;
import ingsw.group1.kademlia.listeners.FindNodeResultListener;
import ingsw.group1.kademlia.listeners.FindValueResultListener;
import ingsw.group1.kademlia.listeners.InviteResultListener;
import ingsw.group1.kademlia.listeners.PingResultListener;
import ingsw.group1.kademlia.listeners.StoreResultListener;
import ingsw.group1.msglibrary.SMSPeer;
import ingsw.group1.repnetwork.StringResource;

/**
 * Class meant to handle running operations in the Network.
 * Does not answer to incoming requests, only handles response to sent requests.
 * A maximum number of Requests can be set during creation.
 *
 * @author Riccardo De Zen
 * CODE REVIEW
 */
public class PendingRequestManager {

    private static final int MAX_SIZE = KadAction.MAX_ID;
    private static final int SENTINEL_ID = -1;
    private static final String CONTINUE_ERR = "Provided Action is not a Response.";
    private static final String SIZE_ERR = "The capacity should not be higher than: " + MAX_SIZE;

    private final int capacity;

    private ActionPropagator defaultActionPropagator;
    private NodeDataProvider<BinarySet, PeerNode> defaultNodeDataProvider;
    private List<PendingRequest> currentRequests = new ArrayList<>();

    /**
     * Constructor.
     *
     * @param capacity                The maximum amount of PendingRequests that can be pending
     *                                at a time. Can't
     *                                be higher than {@link PendingRequestManager#MAX_SIZE}.
     * @param defaultActionPropagator The {@link ActionPropagator} that will be used by default
     *                                by enqueued {@code PendingRequests}.
     * @param defaultNodeDataProvider The {@link NodeDataProvider} that will be used by default
     *                                by enqueued {@code PendingRequests}.
     * @throws IllegalArgumentException If the given capacity is higher than {@link #MAX_SIZE}.
     */
    public PendingRequestManager(
            int capacity,
            @NonNull ActionPropagator defaultActionPropagator,
            @NonNull NodeDataProvider<BinarySet, PeerNode> defaultNodeDataProvider
    ) {
        if (capacity > MAX_SIZE)
            throw new IllegalArgumentException(SIZE_ERR);
        this.capacity = capacity;
        this.defaultActionPropagator = defaultActionPropagator;
        this.defaultNodeDataProvider = defaultNodeDataProvider;
    }

    /**
     * Setter for {@link PendingRequestManager#defaultActionPropagator}.
     *
     * @param newDefaultPropagator the new value for the default propagator.
     */
    public void setDefaultActionPropagator(@NonNull ActionPropagator newDefaultPropagator) {
        defaultActionPropagator = newDefaultPropagator;
    }

    /**
     * Setter for {@link PendingRequestManager#defaultNodeDataProvider}.
     *
     * @param newDefaultProvider the new value for the default node provider.
     */
    public void setDefaultNodeDataProvider(@NonNull NodeDataProvider<BinarySet, PeerNode> newDefaultProvider) {
        defaultNodeDataProvider = newDefaultProvider;
    }

    /**
     * Method to enqueue a {@link PingPendingRequest}.
     *
     * @param peerToPing the {@code Peer} to ping.
     * @param listener   a valid listener to the result of the enqueued Request.
     * @return true if the {@code PendingRequest} could be enqueued, false otherwise.
     */
    public boolean enqueuePing(@NonNull SMSPeer peerToPing, @NonNull PingResultListener listener) {
        int id = generateId();
        if (id == SENTINEL_ID)
            return false;
        PendingRequest request = new PingPendingRequest(
                id,
                peerToPing,
                defaultActionPropagator,
                defaultNodeDataProvider,
                listener
        );
        currentRequests.add(request);
        request.start();
        return true;
    }

    /**
     * Method to enqueue a {@link InvitePendingRequest}.
     *
     * @param peerToInvite The {@code Peer} to be invited.
     * @param listener     a valid listener to the result of the enqueued Request.
     * @return true if the {@code PendingRequest} could be enqueued, false otherwise.
     */
    public boolean enqueueInvite(@NonNull SMSPeer peerToInvite,
                                 @NonNull InviteResultListener listener) {
        int id = generateId();
        if (id == SENTINEL_ID)
            return false;
        PendingRequest request = new InvitePendingRequest(
                id,
                peerToInvite,
                defaultActionPropagator,
                defaultNodeDataProvider,
                listener
        );
        currentRequests.add(request);
        request.start();
        return true;
    }

    /**
     * Method to enqueue a {@link FindNodePendingRequest}.
     *
     * @param targetId The Node id we are looking for.
     * @param listener a valid listener to the result of the enqueued Request.
     * @return true if the {@code PendingRequest} could be enqueued, false otherwise.
     */
    public boolean enqueueFindNode(@NonNull BinarySet targetId,
                                   @NonNull FindNodeResultListener listener) {
        int id = generateId();
        if (id == SENTINEL_ID)
            return false;
        PendingRequest request = new FindNodePendingRequest(
                id,
                targetId,
                defaultActionPropagator,
                defaultNodeDataProvider,
                listener
        );
        currentRequests.add(request);
        request.start();
        return true;
    }

    /**
     * Method to enqueue a {@link FindValuePendingRequest}.
     *
     * @param targetId the Node id we are looking for.
     * @param listener a valid listener to the result of the enqueued Request.
     * @return true if the {@code PendingRequest} could be enqueued, false otherwise.
     */
    public boolean enqueueFindValue(@NonNull BinarySet targetId,
                                    @NonNull FindValueResultListener listener) {
        int id = generateId();
        if (id == SENTINEL_ID)
            return false;
        PendingRequest request = new FindValuePendingRequest(
                id,
                targetId,
                defaultActionPropagator,
                defaultNodeDataProvider,
                listener
        );
        currentRequests.add(request);
        request.start();
        return true;
    }

    /**
     * Method to enqueue a {@link StorePendingRequest}.
     *
     * @param resourceToStore The {@code Resource} we are trying to store.
     * @param listener        a valid listener to the result of the enqueued Request.
     * @return true if the {@code PendingRequest} could be enqueued, false otherwise.
     */
    public boolean enqueueStore(@NonNull StringResource resourceToStore,
                                @NonNull StoreResultListener listener) {
        int id = generateId();
        if (id == SENTINEL_ID)
            return false;
        PendingRequest request = new StorePendingRequest(
                id,
                resourceToStore,
                defaultActionPropagator,
                defaultNodeDataProvider,
                listener
        );
        currentRequests.add(request);
        request.start();
        return true;
    }

    /**
     * Method to continue the execution of a Pending Request. Only Response type Actions will be
     * considered.
     *
     * @param action The action continuing the corresponding Request
     * @return true if a matching Request was found and executed, false otherwise.
     * @throws InvalidActionException If {@code action} is not a Response type Action.
     */
    public boolean continueRequest(KadAction action) {
        if (!action.getActionType().isResponse())
            throw new InvalidActionException(CONTINUE_ERR);
        int operationId = action.getOperationId();
        for (PendingRequest request : currentRequests) {
            if (request.isActionPertinent(action)) {
                request.nextStep(action);
                return true;
            }
        }
        return false;
    }

    /**
     * Removes the PendingRequest with the matching id if present.
     *
     * @param operationId The id of the PendingRequest to remove
     * @return the removed PendingRequest or null if {@code operationId} did not match any of the
     * enqueued {@code PendingRequests}' ids.
     */
    public PendingRequest dequeueRequest(int operationId) {
        for (PendingRequest request : currentRequests) {
            if (request.getOperationId() == operationId) {
                currentRequests.remove(request);
                return request;
            }
        }
        return null;
    }

    /**
     * Method to detect whether a given id is currently assigned to an enqueued {@code
     * PendingRequest}.
     *
     * @param id The id whose assignment should be checked.
     * @return true if the id is available, false if it is already assigned to a {@code
     * PendingRequest}.
     */
    private boolean isIdAvailable(int id) {
        for (PendingRequest request : currentRequests) {
            if (request.getOperationId() == id)
                return false;
        }
        return true;
    }

    /**
     * @return An available id, or -1 if no further ids can be assigned.
     */
    private int generateId() {
        if (currentRequests.size() < capacity) {
            for (int id = 0; id < capacity; id++) {
                if (isIdAvailable(id))
                    return id;
            }
        }
        return SENTINEL_ID;
    }
}
