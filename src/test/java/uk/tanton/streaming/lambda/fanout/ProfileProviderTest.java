package uk.tanton.streaming.lambda.fanout;

import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;


public class ProfileProviderTest {


    private ProfileProvider underTest;

    @Before
    public void setup() {
        this.underTest = new ProfileProvider(new Gson());
    }


    @Test
    public void itGetsAProfileFromClassPath() throws ProfileNotFoundException {
        final Profile pv001 = underTest.getProfile("pv001");

        assertEquals("pv001", pv001.getProfileName());
        assertEquals("libx264", pv001.getVideoCodec());
        assertEquals("keyint=25:min-keyint=25:scenecut=-1", pv001.getX264Opts());
        assertEquals(1000000, pv001.getVideoBitrate());
        assertEquals("libfaac", pv001.getAudioCodec());
    }

    @Test
    public void itThrowsAnExceptionIfTheProfileDoesNotExist()  {
        try {
            underTest.getProfile("blah");
            fail("expected and exception");
        } catch (final FileNotFoundException e) {
            assertTrue(e instanceof ProfileNotFoundException);
        }

    }




}