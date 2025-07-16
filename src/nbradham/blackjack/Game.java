package nbradham.blackjack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Stack;

import nbradham.blackjack.Card.Rank;
import nbradham.blackjack.Card.Suit;

final class Game {

	static enum Action {
		DOUBLE, HIT, SPLIT, STAND, SURRENDER
	};

	private final Stack<Card> deck = new Stack<>();
	private final ArrayList<Card> dealHand = new ArrayList<>(), playHand = new ArrayList<>();
	private final Player player;

	Game(final Player setPlayer) {
		player = setPlayer;
	}

	final void start() {
		final short bet = player.getBet();
		final Rank[] ranks = Rank.values();
		for (Suit s : Suit.values())
			for (Rank r : ranks)
				deck.add(new Card(s, r));
		Collections.shuffle(deck);
		dealDealer(draw().setRevealed(false));
		dealPlayer();
		dealDealer(draw());
		dealPlayer();
		HashSet<Action> availActs = new HashSet<>();
		availActs.add(Action.HIT);
		availActs.add(Action.STAND);
		availActs.add(Action.SURRENDER);
		if (player.getCoin() >= bet) {
			availActs.add(Action.DOUBLE);
			if (playHand.get(0).rank == playHand.get(1).rank)
				availActs.add(Action.SPLIT);
		}
		final Action act = player.getAction(availActs);
	}

	private final void dealDealer(final Card card) {
		dealCard(dealHand, card);
	}

	private final void dealPlayer() {
		dealCard(playHand, draw());
	}

	private final void dealCard(final ArrayList<Card> hand, final Card card) {
		hand.add(card);
	}

	private final Card draw() {
		return deck.pop();
	}
}