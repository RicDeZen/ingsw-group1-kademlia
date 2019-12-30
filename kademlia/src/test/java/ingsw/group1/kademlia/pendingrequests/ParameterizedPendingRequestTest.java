package ingsw.group1.kademlia.pendingrequests;

import androidx.annotation.NonNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import ingsw.group1.kademlia.ActionPropagator;
import ingsw.group1.kademlia.BinarySet;
import ingsw.group1.kademlia.BitSetUtils;
import ingsw.group1.kademlia.KadAction;
import ingsw.group1.kademlia.KadActionsBuilder;
import ingsw.group1.kademlia.NodeDataProvider;
import ingsw.group1.kademlia.PeerNode;
import ingsw.group1.kademlia.listeners.FindNodeResultListener;
import ingsw.group1.kademlia.listeners.FindValueResultListener;
import ingsw.group1.kademlia.listeners.InviteResultListener;
import ingsw.group1.kademlia.listeners.PingResultListener;
import ingsw.group1.kademlia.listeners.StoreResultListener;
import ingsw.group1.msglibrary.RandomSMSPeerGenerator;
import ingsw.group1.msglibrary.SMSPeer;
import ingsw.group1.repnetwork.Resource;
import ingsw.group1.repnetwork.StringResource;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertNotEquals;

@RunWith(Parameterized.class)
public class ParameterizedPendingRequestTest {

    private static final KadActionsBuilder ACTIONS_BUILDER = new KadActionsBuilder();
    private static final RandomSMSPeerGenerator PEER_GENERATOR = new RandomSMSPeerGenerator();

    private static final String testAddress = "+39892424";
    private static final SMSPeer testPeer = new SMSPeer(testAddress);
    private static final BinarySet testBinarySet = new BinarySet(BitSetUtils.hash(testAddress,
            128));
    private static final StringResource testResource = new StringResource("test", "test");

    private PendingRequest testedRequest;
    private int operationId;
    private int propagatedActions;

    private KadAction exampleValidResponse;
    private KadAction exampleInvalidIdResponse;
    private List<KadAction> exampleInvalidTypeResponses;

    //Stub node data provider
    private NodeDataProvider<BinarySet, PeerNode> nodeDataProvider = new StubNodeDataProvider();

    private class StubNodeDataProvider implements NodeDataProvider<BinarySet, PeerNode> {
        @Override
        public PeerNode getRootNode() {
            return null;
        }

        @Override
        public void visitNode(@NonNull PeerNode visitedNode) {
        }

        @Override
        public PeerNode getClosest(@NonNull BinarySet target) {
            return null;
        }

        @Override
        public List<PeerNode> getKClosest(int k, @NonNull BinarySet target) {
            return new ArrayList<>();
        }

        @Override
        public List<PeerNode> filterKClosest(int k, @NonNull BinarySet target,
                                             @NonNull List<PeerNode> nodes) {
            return new ArrayList<>();
        }
    }

    //Stub action propagator
    private ActionPropagator actionPropagator = new ActionPropagator() {
        @Override
        public void propagateAction(@NonNull KadAction action) {
            propagatedActions++;
        }

        @Override
        public void propagateActions(List<KadAction> actions) {
            propagatedActions += actions.size();
        }
    };

    //Stub result listener
    private StubResultListener resultListener = new StubResultListener();

    private class StubResultListener implements
            PingResultListener,
            InviteResultListener,
            FindNodeResultListener,
            FindValueResultListener,
            StoreResultListener {
        @Override
        public void onFindNodeResult(int operationId, @NonNull BitSet target,
                                     @NonNull PeerNode closest) {
        }

        @Override
        public void onFindValueResult(int operationId, PeerNode owner, Resource resource) {
        }

        @Override
        public void onInviteResult(int operationId, @NonNull PeerNode invited, boolean accepted) {
        }

        @Override
        public void onPingResult(int operationId, @NonNull PeerNode pinged, boolean isOnline) {
        }

        @Override
        public void onStoreResult(int operationId, @NonNull Resource storedResource,
                                  PeerNode newOwner) {
        }
    }

    /**
     * Params for tests
     *
     * @return array of parameters as follows:
     * [PendingRequest class],
     * [Expected response type]
     * [Specific parameter for each class]
     */
    @Parameterized.Parameters(name = "{index}: {0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {PingPendingRequest.class.getSimpleName(), PingPendingRequest.class,
                        KadAction.ActionType.PING_ANSWER, testPeer},
                {InvitePendingRequest.class.getSimpleName(), InvitePendingRequest.class,
                        KadAction.ActionType.INVITE_ANSWER, testPeer},
                {FindNodePendingRequest.class.getSimpleName(), FindNodePendingRequest.class,
                        KadAction.ActionType.FIND_NODE_ANSWER, testBinarySet},
                {FindValuePendingRequest.class.getSimpleName(), FindValuePendingRequest.class,
                        KadAction.ActionType.FIND_VALUE_ANSWER, testBinarySet},
                {StorePendingRequest.class.getSimpleName(), StorePendingRequest.class,
                        KadAction.ActionType.STORE_ANSWER, testResource}
        });
    }

    //Need to be declared separately for switch/series of if in constructor
    private static final String PING_NAME = PingPendingRequest.class.getSimpleName();
    private static final String INVITE_NAME = InvitePendingRequest.class.getSimpleName();
    private static final String FIND_NODE_NAME = FindNodePendingRequest.class.getSimpleName();
    private static final String FIND_VALUE_NAME = FindValuePendingRequest.class.getSimpleName();
    private static final String STORE_NAME = StorePendingRequest.class.getSimpleName();


    /**
     * Constructor for the Test class
     *
     * @param className              name for the class, not necessary, only used for Test name.
     * @param pendingRequestClass    the class to Test
     * @param responseType           the type of Response the class expects
     * @param param                  the only distinctive parameter for the class
     */
    public ParameterizedPendingRequestTest(
            @NonNull String className,
            @NonNull Class<PendingRequest> pendingRequestClass,
            @NonNull KadAction.ActionType responseType,
            @NonNull Object param
    ) {
        try {
            propagatedActions = 0;
            operationId = Math.abs(new Random().nextInt() % KadAction.MAX_ID);
            Constructor constructor = pendingRequestClass.getConstructors()[0];
            //Switch seems to only work with Strings that are defined through hardcoding. Final
            // attribute was of little help
            if (className.equals(PING_NAME)) {
                testedRequest = (PendingRequest) constructor.newInstance(
                        operationId,
                        param,
                        actionPropagator,
                        nodeDataProvider,
                        resultListener
                );
            }
            if (className.equals(INVITE_NAME)) {
                testedRequest = (PendingRequest) constructor.newInstance(
                        operationId,
                        param,
                        actionPropagator,
                        nodeDataProvider,
                        resultListener
                );
            }
            if (className.equals(FIND_NODE_NAME)) {
                testedRequest = (PendingRequest) constructor.newInstance(
                        operationId,
                        param,
                        actionPropagator,
                        nodeDataProvider,
                        resultListener
                );
            }
            if (className.equals(FIND_VALUE_NAME)) {
                testedRequest = (PendingRequest) constructor.newInstance(
                        operationId,
                        param,
                        actionPropagator,
                        nodeDataProvider,
                        resultListener
                );
            }
            if (className.equals(STORE_NAME)) {
                testedRequest = (PendingRequest) constructor.newInstance(
                        operationId,
                        param,
                        actionPropagator,
                        nodeDataProvider,
                        resultListener
                );
            }
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    @Test
    public void getOperationIdReturnsId() {
        assertEquals(operationId, testedRequest.getOperationId());
    }

    @Test
    public void startPropagatesSomeAction() {
        testedRequest.start();
        assertNotEquals(0, propagatedActions);
    }

    @Test
    public void isPertinentValidResponse() {
        assertTrue(testedRequest.isActionPertinent(exampleValidResponse));
    }

    @Test
    public void isPertinentInvalidResponses() {
        assertFalse(testedRequest.isActionPertinent(exampleInvalidIdResponse));
        for (KadAction exampleResponse : exampleInvalidTypeResponses) {
            assertFalse(testedRequest.isActionPertinent(exampleResponse));
        }
    }

    @Test
    public void nextStepAcceptsPertinent() {
        final int expectedSteps = 1;
        testedRequest.nextStep(exampleValidResponse);
        assertEquals(expectedSteps, testedRequest.getTotalStepsTaken());
    }

    @Test
    public void nextStepIgnoresNonPertinent() {
        final int expectedSteps = 0;
        testedRequest.nextStep(exampleInvalidIdResponse);
        for (KadAction exampleResponse : exampleInvalidTypeResponses) {
            testedRequest.nextStep(exampleResponse);
        }
        assertEquals(expectedSteps, testedRequest.getTotalStepsTaken());
    }
}
