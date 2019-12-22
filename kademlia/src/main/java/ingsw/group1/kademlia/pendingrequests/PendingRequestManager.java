package ingsw.group1.kademlia.pendingrequests;

import java.util.ArrayList;
import java.util.List;

import ingsw.group1.kademlia.KadAction;
import ingsw.group1.kademlia.exceptions.InvalidActionException;

/**
 * Class meant to handle running operations in the Network.
 * Does not answer to incoming requests, only handles response to sent requests.
 * @author Riccardo De Zen
 */
public class PendingRequestManager{

    private static final String CONTINUE_ERR = "Provided Action is not a Response.";

    private List<PendingRequest> currentRequests = new ArrayList<>();

    /**
     * Method to enqueue a PendingRequest from an Action
     * @param newRequest the Action starting this PendingRequest
     */
    public void enqueueRequest(PendingRequest newRequest) {
        currentRequests.add(newRequest);
    }

    /**
     * Method to continue the elaboration of a Pending Request. Only Response type Actions will be
     * attempted
     * @param action The action continuing the corresponding Request
     * @return true if a matching Request was found and executed, false otherwise.
     */
    public boolean continueRequest(KadAction action){
        if(!action.getActionType().isResponse())
            throw new InvalidActionException(CONTINUE_ERR);
        int operationId = action.getOperationId();
        for(PendingRequest request : currentRequests){
            if(request.isActionPertinent(action)){
                request.nextStep(action);
                return true;
            }
        }
        return false;
    }

    /**
     * Removes the PendingRequest with the matching id if present.
     * @param operationId The id of the PendingRequest to remove
     * @return the removed PendingRequest or null if the id wasn't found
     */
    public PendingRequest dequeueRequest(int operationId){
        for(PendingRequest request : currentRequests){
            if(request.getOperationId() == operationId){
                currentRequests.remove(request);
                return request;
            }
        }
        return null;
    }
}
