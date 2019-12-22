package ingsw.group1.kademlia;

import android.content.Context;

import ingsw.group1.msglibrary.SMSManager;

public class DistributedNetworkActionResponder {

    private Context context;


    public DistributedNetworkActionResponder(Context context){

        this.context = context;
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
                onFindValueReceived();
                break;
            case STORE:
                onStoreReceived();
                break;

                //TODO add a filter for the *_ANSWER action
        }
    }

    /**
     * Method that send the actual response through SMS to the previous sender
     * @param responseAction
     */
    private void sendAction (KadAction responseAction){

        SMSManager.getInstance(context).sendMessage(responseAction.toMessage());

    }

    /**
     * Elaborate the response for the PING action and send back the confirm
     *
     * @param action
     */
    private void onPingReceived(KadAction action) {
        //NodeDataProvider.iGotCalledByNode(action.getPeer());

        KadAction answerPing =new KadAction(action.getPeer(), KadAction.ActionType.PING_ANSWER,1,1,1, KadAction.PayloadType.BOOLEAN,"true");//TODO inserire id min e max part correti e decidere payload

        sendAction(answerPing);
    }

    private void onFindNodeReceived(KadAction action) {
        //NodeDataProvider.iGotCalledByNode(action.getPeer());

        //TODO: Richiamo al costruttore della risposta

        //Todo: Mandare la risposta
    }

    private void onFindValueReceived (){
        //NodeDataProvider.iGotCalledByNode(action.getPeer());

        //TODO: Richiamo al costruttore della risposta

        //Todo: Mandare la risposta
    }

    private void onStoreReceived () {
        //NodeDataProvider.iGotCalledByNode(action.getPeer());

        //TODO: Richiamo al costruttore della risposta

        //Todo: Mandare la risposta
    }
}
