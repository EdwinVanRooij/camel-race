package io.github.edwinvanrooij;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by eddy
 * on 5/31/17.
 */
class CamelRaceEngineTest {

    private CamelRaceEngine engine;

    @BeforeEach
    void setUp() {
        engine = new CamelRaceEngine();
    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void testSideCards() {
        // Get all side cards
        List<SideCard> sideCards = engine.getSideCardList();

        // Verify init
        assertNotNull(sideCards);

        int expectedSize = 4;
        int actualSize = sideCards.size();

        // Check if there are only 4 cards
        assertEquals(expectedSize, actualSize);

        // Cards should be NOT an ace, anything else is acceptable
        for (SideCard card : sideCards) {
            assertNotEquals(card.getCardValue(), Card.CardValue.ACE);
        }
    }

    @Test
    void testCamels() {
        // Get all camels
        List<Camel> camels = engine.getCamelList();

        // Verify init
        assertNotNull(camels);

        int expectedSize = 4;
        int actualSize = camels.size();

        // Check if there are only 4 camels
        assertEquals(expectedSize, actualSize);

        // Cards should all be ace
        for (Camel camel : camels) {
            assertEquals(camel.getCardValue(), Card.CardValue.ACE);
        }
    }

    @Test
    void testDeck() {
        // Get deck
        List<Card> deck = engine.getDeck();

        // Verify init
        assertNotNull(deck);

        int expectedSize = 44;
        int actualSize = deck.size();

        // There should be 44 cards left
        assertEquals(expectedSize, actualSize);

        // Cards should be anything but an ace
        for (Card card : deck){
            assertNotEquals(card.getCardValue(), Card.CardValue.ACE);
        }
    }
}

