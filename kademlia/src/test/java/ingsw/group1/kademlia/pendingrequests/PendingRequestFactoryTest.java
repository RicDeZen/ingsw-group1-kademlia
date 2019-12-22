package ingsw.group1.kademlia.pendingrequests;

import org.junit.Test;

import ingsw.group1.kademlia.exceptions.InvalidActionException;

/**
 * Tests for {@link PendingRequestFactory}
 * @author Riccardo De Zen
 */
public class PendingRequestFactoryTest {
    @Test
    public void factoryBuildsInviteRequest(){

    }

    @Test
    public void factoryBuildsPingRequest(){

    }

    @Test
    public void factoryBuildsFindNodeRequest(){

    }

    @Test
    public void factoryBuildsFindValueRequest(){

    }

    @Test
    public void factoryBuildsStoreRequest(){

    }

    @Test(expected = InvalidActionException.class)
    public void factoryWontBuildFromResponse(){

    }
}