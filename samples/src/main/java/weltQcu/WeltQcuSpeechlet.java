package weltQcu;

import com.amazon.speech.speechlet.*;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.jdom2.Element;

import java.net.URL;
import java.util.List;

public class WeltQcuSpeechlet implements Speechlet {


    public static final String WELT_ACTION = "Frag mich nach Nachrichten.";
    public static final String DIE_WELT = "Die Welt";

    @Override
    public void onSessionEnded(SessionEndedRequest request, Session session) throws SpeechletException {
    }

    @Override
    public void onSessionStarted(SessionStartedRequest request, Session session) throws SpeechletException {

    }

    @Override
    public SpeechletResponse onLaunch(LaunchRequest request, Session session) throws SpeechletException {
        return getForText("Willkommen im Nachrichten Check von Welt N24. " + WELT_ACTION);
    }

    @Override
    public SpeechletResponse onIntent(IntentRequest request, Session session) throws SpeechletException {
        final String intentName = request.getIntent().getName();
        if ("Rufen".equals(intentName)) {
            return getForText("Check die Welt auf welt.de");
        } else if ("AMAZON.StopIntent".equals(intentName)) {
            return handleStopIntent();
        } else if ("Nachrichten".equals(intentName)) {
            return getQcu();
        } else return getForText("Die Welt sagt Entschuldigung. Bitte wiederholen.");
    }

    private SpeechletResponse handleStopIntent() {
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText("Auf Wiedersehen und check die welt auf www.welt.de");
        return SpeechletResponse.newTellResponse(speech);
    }

    private SpeechletResponse getQcu() {
        try {
            StringBuilder texte = new StringBuilder();
            URL feedUrl = new URL("https://www.welt.de/feeds/qcu");
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(feedUrl));

            for (SyndEntry item : feed.getEntries()) {
                final List<Element> foreignMarkup = item.getForeignMarkup();
                for (final Element element : foreignMarkup) {
                    if ("qcu".equals(element.getName())) {
                        texte.append(item.getDescription().getValue());
                        texte.append(System.getProperty("line.separator"));
                    }
                }
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
            return getForText("Die Welt sagt sorry. Bitte wiederholen.");
        }
    }

    private SpeechletResponse getForText(String text) {
        SimpleCard errorCard = new SimpleCard();
        errorCard.setTitle(DIE_WELT);
        errorCard.setContent(text);
        PlainTextOutputSpeech errorSpeech = new PlainTextOutputSpeech();
        errorSpeech.setText(text);
        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(errorSpeech);
        return SpeechletResponse.newAskResponse(errorSpeech, reprompt, errorCard);
    }
}
