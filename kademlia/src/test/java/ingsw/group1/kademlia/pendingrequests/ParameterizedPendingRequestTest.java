package ingsw.group1.kademlia.pendingrequests;

import androidx.annotation.NonNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.robolectric.ParameterizedRobolectricTestRunner;

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
import ingsw.group1.kademlia.NodeDataProvider;
import ingsw.group1.kademlia.PeerNode;
import ingsw.group1.kademlia.listeners.FindNodeResultListener;
import ingsw.group1.kademlia.listeners.FindValueResultListener;
import ingsw.group1.kademlia.listeners.InviteResultListener;
import ingsw.group1.kademlia.listeners.PingResultListener;
import ingsw.group1.kademlia.listeners.StoreResultListener;
import ingsw.group1.msglibrary.SMSPeer;
import ingsw.group1.repnetwork.Resource;
import ingsw.group1.repnetwork.StringResource;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

@RunWith(ParameterizedRobolectricTestRunner.class)
public class ParameterizedPendingRequestTest {

    private static final String testAddress = "+39892424";
    private static final SMSPeer testPeer = new SMSPeer(testAddress);
    private static final BinarySet testBinarySet = new BinarySet(BitSetUtils.hash(testAddress,128));
    private static final StringResource testResource = new StringResource("test","test");

    private PendingRequest testedRequest;
    private int operationId;
    private int propagatedActions;
    private int expectedActionsOnStart;

    private KadAction exampleValidResponse;
    private KadAction exampleInvalidIdResponse;
    private List<KadAction> exampleInvalidTypeResponses;

    private KadAction buildResponseAction(KadAction.ActionType type, int code){
        switch(type){
            case PING_ANSWER:
                return new KadAction(testPeer,type,code,1,1, KadAction.PayloadType.IGNORED," ");
            case INVITE_ANSWER:
                return new KadAction(testPeer,type,code,1,1, KadAction.PayloadType.BOOLEAN,"true");
            case FIND_NODE_ANSWER:
                return new KadAction(testPeer,type,code,1,1, KadAction.PayloadType.PEER_ADDRESS,testPeer.getAddress());
            case FIND_VALUE_ANSWER:
                return new KadAction(testPeer,type,code,1,1, KadAction.PayloadType.PEER_ADDRESS,testPeer.getAddress());
            case STORE_ANSWER:
                return new KadAction(testPeer,type,code,1,1, KadAction.PayloadType.BOOLEAN,"true");
        }
        return null;
    }

    //Stub node data provider
    private NodeDataProvider nodeDataProvider = new StubNodeDataProvider();
    private class StubNodeDataProvider implements NodeDataProvider<BinarySet,PeerNode>{
        @Override
        public PeerNode getRootNode() {
            return null;
        }
        @Override
        public void visitNode(@NonNull PeerNode visitedNode) {
        }

        @Override
        public PeerNode getClosest(BinarySet target) {
            return null;
        }

        @Override
        public List<PeerNode> getKClosest(int k, BinarySet target) {
            return new ArrayList<>();
        }

        @Override
        public List<PeerNode> filterKClosest(int k, BinarySet target, List<PeerNode> nodes) {
            return new ArrayList<>();
        }
    };
    //Stub action propagator
    private ActionPropagator actionPropagator = new ActionPropagator() {
        @Override
        public void propagateAction(KadAction action) {
            propagatedActions++;
        }
        @Override
        public void propagateActions(List<KadAction> actions) {
            propagatedActions += actions.size();
        }
    };
    //Stub result listener
    private StubResultListener resultListener = new StubResultListener();
    private class StubResultListener implements PingResultListener, InviteResultListener, FindNodeResultListener, FindValueResultListener, StoreResultListener {
        @Override
        public void onFindNodeResult(int operationId, BitSet target, PeerNode closest) {

        }

        @Override
        public void onFindValueResult(int operationId, PeerNode owner, Resource resource) {

        }

        @Override
        public void onInviteResult(int operationId, PeerNode invited, boolean accepted) {

        }

        @Override
        public void onPingResult(int operationId, PeerNode pinged, boolean isOnline) {

        }

        @Override
        public void onStoreResult(int operationId, Resource storedResource, PeerNode newOwner) {

        }
    }

    /**
     * Params for tests
     * @return array of parameters as follows:
     * [PendingRequest class],
     * [Expected response type]
     * [Specific parameter for each class],
     * [Expected number of actions on start],
     */
    @ParameterizedRobolectricTestRunner.Parameters(name = "{index}: {0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {PingPendingRequest.class.getSimpleName(), PingPendingRequest.class, KadAction.ActionType.PING_ANSWER, testPeer, 1},
                {InvitePendingRequest.class.getSimpleName(), InvitePendingRequest.class, KadAction.ActionType.INVITE_ANSWER, testPeer, 1},
                {FindNodePendingRequest.class.getSimpleName(), FindNodePendingRequest.class, KadAction.ActionType.FIND_NODE_ANSWER, testBinarySet, 0},
                {FindValuePendingRequest.class.getSimpleName(), FindValuePendingRequest.class, KadAction.ActionType.FIND_VALUE_ANSWER, testBinarySet, 0},
                {StorePendingRequest.class.getSimpleName(), StorePendingRequest.class, KadAction.ActionType.STORE_ANSWER, testResource, 0}
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
     * @param className name for the class, not necessary, only used for Test name.
     * @param pendingRequestClass the class to Test
     * @param responseType the type of Response the class expects
     * @param param the only distinctive parameter for the class
     * @param expectedActionsOnStart the expected number of actions when the Pending Request starts
     */
    public ParameterizedPendingRequestTest(
            @NonNull String className,
            @NonNull Class<PendingRequest> pendingRequestClass,
            @NonNull KadAction.ActionType responseType,
            @NonNull Object param,
            int expectedActionsOnStart
    ){
        try{
            this.expectedActionsOnStart = expectedActionsOnStart;
            propagatedActions = 0;
            operationId = new Random().nextInt();
            Constructor constructor = pendingRequestClass.getConstructors()[0];
            //Switch seems to only work with Strings that are defined through hardcoding. Final attribute was of little help
            if(className.equals(PING_NAME)){
                testedRequest = (PendingRequest) constructor.newInstance(
                        operationId,
                        param,
                        actionPropagator,
                        nodeDataProvider,
                        resultListener
                );
            }
            if(className.equals(INVITE_NAME)){
                testedRequest = (PendingRequest) constructor.newInstance(
                        operationId,
                        param,
                        actionPropagator,
                        nodeDataProvider,
                        resultListener
                );
            }
            if(className.equals(FIND_NODE_NAME)){
                testedRequest = (PendingRequest) constructor.newInstance(
                        operationId,
                        param,
                        actionPropagator,
                        nodeDataProvider,
                        resultListener
                );
            }
            if(className.equals(FIND_VALUE_NAME)){
                testedRequest = (PendingRequest) constructor.newInstance(
                        operationId,
                        param,
                        actionPropagator,
                        nodeDataProvider,
                        resultListener
                );
            }
            if(className.equals(STORE_NAME)){
                testedRequest = (PendingRequest) constructor.newInstance(
                        operationId,
                        param,
                        actionPropagator,
                        nodeDataProvider,
                        resultListener
                );
            }
            generateExampleResponses(responseType);
        }
        catch(IllegalAccessException | InstantiationException | InvocationTargetException e){
            throw new IllegalStateException(e.getMessage());
        }
    }

    /**
     * Method to generate the example Responses from the expected correct type
     * @param responseType the expected Response type
     */
    private void generateExampleResponses(KadAction.ActionType responseType){
        exampleInvalidTypeResponses = new ArrayList<>();
        exampleValidResponse = buildResponseAction(responseType,operationId);
        exampleInvalidIdResponse = buildResponseAction(responseType,operationId+1);
        for(KadAction.ActionType invalidType : KadAction.ActionType.values()){
            if(invalidType != responseType && invalidType.isResponse())
                exampleInvalidTypeResponses.add(buildResponseAction(invalidType, operationId));
        }
    }

    @Test
    public void getOperationId_returnsId(){
        assertEquals(operationId, testedRequest.getOperationId());
    }

    @Test
    public void start_propagatesTheAction(){
        testedRequest.start();
        assertEquals(expectedActionsOnStart, propagatedActions);
    }

    @Test
    public void isPertinent_validResponse(){
        assertTrue(testedRequest.isActionPertinent(exampleValidResponse));
    }

    @Test
    public void isPertinent_invalidResponses(){
        assertFalse(testedRequest.isActionPertinent(exampleInvalidIdResponse));
        for(KadAction exampleResponse : exampleInvalidTypeResponses){
            assertFalse(testedRequest.isActionPertinent(exampleResponse));
        }
    }

    /**
     * isPertinent is always executed during nextStep, and {@link PendingRequest#getTotalStepsTaken()}
     */
    @Test
    public void nextStep_acceptsPertinent(){
        final int expectedSteps = 1;
        testedRequest.nextStep(exampleValidResponse);
        assertEquals(expectedSteps, testedRequest.getTotalStepsTaken());
    }

    @Test
    public void nextStep_ignoresNonPertinent(){
        final int expectedSteps = 0;
        testedRequest.nextStep(exampleInvalidIdResponse);
        for(KadAction exampleResponse : exampleInvalidTypeResponses){
            testedRequest.nextStep(exampleResponse);
        }
        assertEquals(expectedSteps, testedRequest.getTotalStepsTaken());
    }
}
