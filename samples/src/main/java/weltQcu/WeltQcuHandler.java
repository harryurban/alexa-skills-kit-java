package weltQcu;

import com.amazon.speech.speechlet.lambda.SpeechletRequestStreamHandler;

import java.util.HashSet;
import java.util.Set;

public class WeltQcuHandler extends SpeechletRequestStreamHandler {
    private static final Set<String> supportedApplicationIds = new HashSet<String>();

    static {
        /*
         * This Id can be found on https://developer.amazon.com/edw/home.html#/ "Edit" the relevant
         * Alexa Skill and put the relevant Application Ids in this Set.
         */
        supportedApplicationIds.add("amzn1.ask.skill.17c084eb-eb67-47cd-93e7-342dd0edb5c2");
    }

    public WeltQcuHandler() {
        super(new WeltQcuSpeechlet(), supportedApplicationIds);
    }
}
