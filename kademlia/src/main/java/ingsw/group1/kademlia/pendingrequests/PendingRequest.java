package ingsw.group1.kademlia.pendingrequests;

import androidx.annotation.NonNull;

import ingsw.group1.kademlia.KadAction;

/**
 * Interface defining the standard behaviour of a {@code PendingRequest}. A single {@code
 * PendingRequest} is responsible of keeping track of the state of a Request made by the User.
 * <p>
 * A {@code PendingRequest} should call an {@link ingsw.group1.kademlia.ActionPropagator} in
 * order to propagate through the Network the Requests it's willing to send.
 * A {@code PendingRequest} should ignore attempts to perform steps with an impertinent Action,
 * where "pertinent" is based on the criteria defined in the implementation for
 * {@link PendingRequest#isActionPertinent(KadAction)}.
 * When a {@code PendingRequest} reaches the {@link RequestState#COMPLETED} state, it should not
 * perform further steps.
 *
 * @author Riccardo De Zen
 */
public interface PendingRequest {
    /**
     * Enum defining the current operative state of a {@code PendingRequest}.
     */
    enum RequestState {
        /**
         * The {@code PendingRequest} is in an idle state.
         */
        IDLE,
        /**
         * The {@code PendingRequest} is waiting for the result of another Request.
         */
        PENDING_SUBREQUEST,
        /**
         * The {@code PendingRequest} is waiting for Responses directed to itself. This should be
         * the only state where Actions CAN be pertinent.
         */
        PENDING_RESPONSES,
        /**
         * The {@code PendingRequest} has finished its execution and returned its result.
         */
        COMPLETED;
    }

    /**
     * @return the number of steps performed (number of times nextStep took a valid Action and acted
     * accordingly).
     */
    int getTotalStepsTaken();

    /**
     * @return the unique Code for this PendingRequest.
     */
    int getOperationId();

    /**
     * @return the current {@link RequestState} for this {@code PendingRequest}.
     */
    RequestState getRequestState();

    /**
     * Method used to start the PendingRequest, propagating its first Action.
     */
    void start();

    /**
     * @param action the Action whose pertinence must be checked.
     * @return true if {@code action} can be used by the Request (i.e. same type and code) false
     * otherwise.
     */
    boolean isActionPertinent(@NonNull KadAction action);

    /**
     * Method to perform the next step for this PendingRequest. The method should ignore Actions for
     * which {@link PendingRequest#isActionPertinent(KadAction action)} returns false. The User
     * should always check with the aforementioned method beforehand in order to know whether the
     * Action can be used.
     *
     * @param action an Action considered pertinent to this {@code PendingRequest}.
     */
    void nextStep(@NonNull KadAction action);
}
