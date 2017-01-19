package weltQcu;

import com.amazon.speech.speechlet.*;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import java.net.URL;

public class WeltQcuSpeechlet implements Speechlet {


    public static final String WELT_ACTION = "Frag mich nach Q C U";
    public static final String DIE_WELT = "Die Welt";

    @Override
    public void onSessionStarted(SessionStartedRequest request, Session session) throws SpeechletException {

    }

    @Override
    public SpeechletResponse onLaunch(LaunchRequest request, Session session) throws SpeechletException {
        return welcomeUser();
    }

    @Override
    public SpeechletResponse onIntent(IntentRequest request, Session session) throws SpeechletException {
        return getQcu();
    }

    @Override
    public void onSessionEnded(SessionEndedRequest request, Session session) throws SpeechletException {

    }


    private SpeechletResponse welcomeUser() {
        String speechText = String.format("Hallo hier ist %s. %s", DIE_WELT, WELT_ACTION);
        SimpleCard card = new SimpleCard();
        card.setTitle(DIE_WELT);
        card.setContent(speechText);

        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(speech);
        return SpeechletResponse.newAskResponse(speech, reprompt, card);
    }

    private SpeechletResponse getQcu() {
        try {
            StringBuilder texte = new StringBuilder();
            URL feedUrl = new URL("https://www.welt.de/feeds/qcu");
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(feedUrl));

            for (SyndEntry item : feed.getEntries()) {

                texte.append(item.getDescription().getValue());
                texte.append(System.getProperty("line.separator"));
            }

            SimpleCard card = new SimpleCard();
            card.setTitle(DIE_WELT);
            card.setContent(texte.toString());

            PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
            speech.setText(texte.toString());

            Reprompt reprompt = new Reprompt();
            reprompt.setOutputSpeech(speech);
            return SpeechletResponse.newAskResponse(speech, reprompt, card);

        } catch (Exception e) {
            return error();
        }


    }

    private SpeechletResponse error() {
        final String fehler = "Fehler";
        SimpleCard errorCard = new SimpleCard();
        errorCard.setTitle(DIE_WELT);
        errorCard.setContent(fehler);
        PlainTextOutputSpeech errorSpeech = new PlainTextOutputSpeech();
        errorSpeech.setText(fehler);
        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(errorSpeech);
        return SpeechletResponse.newAskResponse(errorSpeech, reprompt, errorCard);
    }

    private SpeechletResponse getHelp() {
        String speechText = WELT_ACTION;

        // Create the Simple card content.
        SimpleCard card = new SimpleCard();
        card.setTitle("Die Welt");
        card.setContent(speechText);

        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        // Create reprompt
        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(speech);

        return SpeechletResponse.newAskResponse(speech, reprompt, card);
    }
}
