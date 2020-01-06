package ingsw.group1.kademlia;

import android.content.Context;

import androidx.annotation.NonNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import ingsw.group1.msglibrary.SMSManager;
import ingsw.group1.msglibrary.SMSPeer;
import ingsw.group1.repnetwork.StringResource;

import static ingsw.group1.kademlia.KadAction.ActionType.*;
import static ingsw.group1.kademlia.KadAction.INVALID_KAD_ACTION;
import static ingsw.group1.kademlia.KadAction.PayloadType;
import static ingsw.group1.kademlia.KademliaConstants.*;

/**
 * @author Martignago
 * @author Turcato
 */
public class DistributedNetworkActionResponder {

    private Context context;
    private KadActionsBuilder builder;
    private NodeDataProvider<BinarySet, Node<BinarySet>> nodeDataProvider;
    private Map<ResourceNode, StringResource> targetResourceMap;
    private static final String UNKNOWN_VALUE = "unknown_value";

    /**
     * Constructor that defines the actions builder and receives data structures
     * Context is used to call the SMSManager
     *
     * @param context     This instance of the context is used to call the SMSManager
     * @param provider    An instance of NodeDataProvider to access data structures of the network
     * @param resourceMap A map containing ResourceNodes
     */
    public DistributedNetworkActionResponder(Context context, NodeDataProvider<BinarySet, Node<BinarySet>> provider, Map<ResourceNode, StringResource> resourceMap) {
        this.context = context;
        builder = new KadActionsBuilder(KadAction.MAX_ID);
        nodeDataProvider = provider;
        targetResourceMap = resourceMap;
    }

    /**
     * This method is called when a message with an action is received
     *
     * @param action received action
     */
    public void onActionReceived(KadAction action) {
        switch (action.getActionType()) {
            case PING:
                onPingReceived(action);
                break;
            case FIND_NODE:
                onFindNodeReceived(action);
                break;
            case FIND_VALUE:
                onFindValueReceived(action);
                break;
            case STORE:
                onStoreReceived(action);
                break;
        }
    }

    /**
     * Method that send the actual response through SMS to the previous sender
     *
     * @param responseAction A response action
     */
    private void sendAction(KadAction responseAction) {
        SMSManager.getInstance(context).sendMessage(responseAction.toMessage());
    }

    /**
     * Elaborate the response for the PING action and send back the confirm
     * If the action is not a PING, the method won't do anything
     * This method will respond positively to a PING
     *
     * @param action The PING "request" action
     */
    private void onPingReceived(@NonNull KadAction action) {
        KadAction answerPing = builder.buildPingAnswer(action);
        if (answerPing != INVALID_KAD_ACTION)
            sendAction(answerPing);
    }

    /**
     * Elaborate the response for the FIND_NODE action and send back the answer
     * If the action is not a FIND_NODE, or the payload is not a Node address, the method won't do anything
     * This method will respond positively to the request
     *
     * @param action The FIND_NODE "request" action
     */
    private void onFindNodeReceived(@NonNull KadAction action) {
        if (action.getActionType() == FIND_NODE
                && action.getPayloadType() == PayloadType.NODE_ID) {
            BinarySet targetAddress = new BinarySet(action.getPayload());

            SMSPeer[] peersFound = findClosestPeers(nodeDataProvider, targetAddress);

            KadAction[] answerFindNode = builder.buildFindNodeAnswer(action, peersFound);
            for (KadAction answer : answerFindNode) {
                if (answer != INVALID_KAD_ACTION)
                    sendAction(answer);
            }
        }
    }

    /**
     * @param provider An instance of NodeDataProvider to access data structures of the network
     * @param target   The node of which find it's closest peers in the network
     * @return The closest Peers to a target Node in the network
     */
    private static SMSPeer[] findClosestPeers(NodeDataProvider<BinarySet, Node<BinarySet>> provider, BinarySet target) {
        ArrayList<Node<BinarySet>> nodes = new ArrayList<>(provider.getKClosest(KADEMLIA_DEFAULT_K, target));
        ArrayList<SMSPeer> peers = new ArrayList<>();
        for (Object n : nodes) {
            if (n instanceof PeerNode && ((PeerNode) n).getPhysicalPeer().isValid())
                peers.add(((PeerNode) n).getPhysicalPeer());
        }
        SMSPeer[] peersFound = new SMSPeer[peers.size()];
        System.arraycopy(peers, 0, peersFound, 0, peers.size());
        return peersFound;
    }

    /**
     * Elaborate the response for the FIND_VALUE action and send back the answer
     * If the action is not a FIND_VALUE, or the payload is not a Node address, the method won't do anything
     * This method will respond positively to the request
     *
     * @param action The FIND_VALUE "request" action
     */
    private void onFindValueReceived(@NonNull KadAction action) {
        if (action.getActionType() == FIND_VALUE
                && action.getPayloadType() == PayloadType.NODE_ID) {
            ArrayList<KadAction> answerFindValue = new ArrayList<>();
            ResourceNode searched = new ResourceNode(new BinarySet(action.getPayload()), UNKNOWN_VALUE);

            if (targetResourceMap.containsKey(searched))
                answerFindValue.add(builder.buildFindValueAnswer(action, targetResourceMap.get(searched)));
            else {
                SMSPeer[] foundPeers = findClosestPeers(nodeDataProvider, searched.getKey());
                answerFindValue = new ArrayList<KadAction>(Arrays.asList(builder.buildFindValueAnswer(action, foundPeers)));
            }

            for (KadAction answer : answerFindValue) {
                if (answer != INVALID_KAD_ACTION)
                    sendAction(answer);
            }
        }
    }

    /**
     * Elaborate the response for the STORE action and send back the answer
     * If the action is not a STORE, or the payload is not a StringResource formatted with KadAction.RESOURCE_SEPARATOR
     * the method won't do anything
     * This method will respond positively to the request
     *
     * @param action The STORE "request" action
     */
    private void onStoreReceived(@NonNull KadAction action) {
        if (action.getActionType() == STORE) {
            ArrayList<KadAction> answerStore = new ArrayList<>();

            if (action.getPayloadType() == PayloadType.RESOURCE) {
                String[] splittedResource = action.getPayload().split(KadAction.RESOURCE_SEPARATOR);
                StringResource resource = new StringResource(splittedResource[0], splittedResource[1]);
                ResourceNode resourceNode = new ResourceNode(new BinarySet(BitSetUtils.hash(resource.getValue(), KADEMLIA_DEFAULT_ID_LENGTH)), resource.getValue());
                targetResourceMap.put(resourceNode, resource);
                answerStore.add(builder.buildStoreAnswer(action, targetResourceMap.containsKey(resourceNode)));
            }
            else if (action.getPayloadType() == PayloadType.NODE_ID) {
                ResourceNode resourceNode = new ResourceNode(new BinarySet(action.getPayload()), UNKNOWN_VALUE);

                SMSPeer[] closestPeers = findClosestPeers(nodeDataProvider, resourceNode.getKey());

                answerStore = new ArrayList<KadAction>(Arrays.asList(builder.buildStoreAnswer(action, closestPeers)));

            }
            for (KadAction answer : answerStore) {
                if (answer != INVALID_KAD_ACTION)
                    sendAction(answer);
            }
        }

    }


}
