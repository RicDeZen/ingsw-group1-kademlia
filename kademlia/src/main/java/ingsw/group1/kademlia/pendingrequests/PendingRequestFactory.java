package ingsw.group1.kademlia.pendingrequests;

import androidx.annotation.NonNull;

import ingsw.group1.kademlia.ActionPropagator;
import ingsw.group1.kademlia.KadAction;
import ingsw.group1.kademlia.NodeDataProvider;
import ingsw.group1.kademlia.listeners.FindNodeResultListener;
import ingsw.group1.kademlia.listeners.FindValueResultListener;
import ingsw.group1.kademlia.listeners.InviteResultListener;
import ingsw.group1.kademlia.listeners.PingResultListener;
import ingsw.group1.kademlia.listeners.StoreResultListener;

/**
 * Class defining the various PendingRequest behaviours
 */
public class PendingRequestFactory{
    private static final String INVALID_ACTION_ERR = "The provided Action is invalid, only Request " +
            "type Actions can be used.";

    public static PendingRequest getPingPendingRequest(
            @NonNull KadAction action,
            @NonNull ActionPropagator actionPropagator,
            @NonNull NodeDataProvider nodeProvider,
            @NonNull PingResultListener resultListener
    ){
        return null;
    }

    public static PendingRequest getInvitePendingRequest(
            @NonNull KadAction action,
            @NonNull ActionPropagator actionPropagator,
            @NonNull NodeDataProvider nodeProvider,
            @NonNull InviteResultListener resultListener
    ){
        return null;
    }

    public static PendingRequest getFindNodePendingRequest(
            @NonNull KadAction action,
            @NonNull ActionPropagator actionPropagator,
            @NonNull NodeDataProvider nodeProvider,
            @NonNull FindNodeResultListener resultListener
    ){
        return null;
    }

    public static PendingRequest getFindValuePendingRequest(
            @NonNull KadAction action,
            @NonNull ActionPropagator actionPropagator,
            @NonNull NodeDataProvider nodeProvider,
            @NonNull FindValueResultListener resultListener
    ){
        return null;
    }

    public static PendingRequest getStorePendingRequest(
            @NonNull KadAction action,
            @NonNull ActionPropagator actionPropagator,
            @NonNull NodeDataProvider nodeProvider,
            @NonNull StoreResultListener resultListener
    ){
        return null;
    }
}
