package ingsw.group1.kademlia.pendingrequests;

import androidx.annotation.NonNull;

import ingsw.group1.kademlia.ActionPropagator;
import ingsw.group1.kademlia.BinarySet;
import ingsw.group1.kademlia.KadAction;
import ingsw.group1.kademlia.KadActionsBuilder;
import ingsw.group1.kademlia.NodeDataProvider;
import ingsw.group1.kademlia.NodeUtils;
import ingsw.group1.kademlia.PeerNode;
import ingsw.group1.kademlia.listeners.InviteResultListener;
import ingsw.group1.msglibrary.SMSPeer;

/**
 * Class defining an implementation of {@link PendingRequest} for an INVITE type Request, as defined
 * in the Kademlia protocol.
 * After completion the {@code PendingRequest} should not receive further calls to
 * {@link PendingRequest#nextStep(KadAction)}.
 *
 * @author Riccardo De Zen
 */
public class InvitePendingRequest implements PendingRequest {

    private static KadActionsBuilder actionBuilder = new KadActionsBuilder();

    private RequestState requestState = RequestState.IDLE;
    private int stepsTaken = 0;
    private int operationId;
    private KadAction inviteAction;

    private ActionPropagator actionPropagator;
    private NodeDataProvider<BinarySet, PeerNode> nodeProvider;
    private InviteResultListener resultListener;

    /**
     * Default constructor.
     *
     * @param operationId      the unique id for this PendingRequest operation.
     * @param peerToInvite     the {@code Peer} to ping.
     * @param actionPropagator a valid {@link ActionPropagator}.
     * @param nodeProvider     a valid {@link NodeDataProvider}.
     * @param resultListener   a valid listener to this {@code PendingRequest}'s Result.
     */
    public InvitePendingRequest(
            int operationId,
            @NonNull SMSPeer peerToInvite,
            @NonNull ActionPropagator actionPropagator,
            @NonNull NodeDataProvider<BinarySet, PeerNode> nodeProvider,
            @NonNull InviteResultListener resultListener
    ) {
        this.operationId = operationId;
        this.actionPropagator = actionPropagator;
        this.nodeProvider = nodeProvider;
        this.resultListener = resultListener;
        this.inviteAction = buildAction(peerToInvite);
    }

    /**
     * @return the number of steps performed by the operation.
     * An {@code InvitePendingRequest} should perform only one step (when the answer to the
     * Invite came back).
     * @see PendingRequest#getTotalStepsTaken()
     */
    @Override
    public int getTotalStepsTaken() {
        return stepsTaken;
    }

    /**
     * @see PendingRequest#getOperationId()
     */
    @Override
    public int getOperationId() {
        return inviteAction.getOperationId();
    }

    /**
     * @return the current {@link RequestState} for this {@code PendingRequest}.
     */
    @Override
    public RequestState getRequestState(){
        return requestState;
    }

    /**
     * @see PendingRequest#start()
     * The only propagated Action is a Request of type
     * {@link KadAction.ActionType#INVITE}.
     */
    @Override
    public void start() {
        actionPropagator.propagateAction(inviteAction);
        requestState = RequestState.PENDING_RESPONSES;
    }

    /**
     * @return true if the given action can be used to continue the operation, false otherwise.
     * The action is always ignored if the current state is not {@link RequestState#PENDING_RESPONSES}.
     * The action is "pertinent" if:
     * - The {@code ActionType} of {@code action} is
     * {@link KadAction.ActionType#INVITE_ANSWER}.
     * - The {@code operationId} matches.
     * - The {@code Peer} of {@code action} matches the {@code Peer} of {@code inviteAction}.
     * @see PendingRequest#isActionPertinent(KadAction)
     */
    @Override
    public boolean isActionPertinent(@NonNull KadAction action) {
        if(getRequestState() != RequestState.PENDING_RESPONSES) return false;
        return KadAction.ActionType.INVITE_ANSWER == action.getActionType() &&
                action.getOperationId() == inviteAction.getOperationId() &&
                action.getPeer().equals(inviteAction.getPeer());
    }

    /**
     * For a PING type Request, an incoming pertinent Action is already a valid completion criteria.
     *
     * @param action a pertinent Action attempting to continue the operation.
     */
    @Override
    public void nextStep(@NonNull KadAction action) {
        if (!isActionPertinent(action)) return;
        boolean inviteAccepted = Boolean.valueOf(action.getPayload());
        PeerNode invitedNode = NodeUtils.getNodeForPeer(action.getPeer(),
                NodeUtils.DEFAULT_KEY_LENGTH);
        nodeProvider.visitNode(invitedNode);
        resultListener.onInviteResult(getOperationId(), invitedNode, inviteAccepted);
        requestState = RequestState.COMPLETED;
        stepsTaken++;
    }

    /**
     * Method to return the correct Action for a given Peer
     *
     * @param peer the target Peer for the Action.
     * @return the build {@code KadAction}.
     */
    private KadAction buildAction(SMSPeer peer) {
        return actionBuilder.buildInvite(operationId, peer);
    }
}
