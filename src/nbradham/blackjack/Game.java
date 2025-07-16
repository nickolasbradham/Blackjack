package nbradham.blackjack;

import java.util.ArrayList;
import java.util.Arrays;
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
	private final ArrayList<ArrayList<Card>> playHands = new ArrayList<>();
	private final ArrayList<Card> dealHand = new ArrayList<>();
	private final Player player;

	Game(final Player setPlayer) {
		(player = setPlayer).setGame(this);
	}

	final void start() {
		final short bet = player.getBet();
		final Rank[] ranks = Rank.values();
		for (Suit s : Suit.values())
			for (Rank r : ranks)
				deck.add(new Card(s, r));
		Collections.shuffle(deck);
		dealDealer(draw().setHidden(true));
		final ArrayList<Card> first = new ArrayList<>();
		playHands.add(first);
		dealCard(first, draw());
		dealDealer(draw());
		dealCard(first, draw());
		HashSet<Action> availActs = new HashSet<>();
		availActs.add(Action.HIT);
		availActs.add(Action.STAND);
		availActs.add(Action.SURRENDER);
		if (player.getCoin() >= bet) {
			availActs.add(Action.DOUBLE);
			if (first.get(0).rank == first.get(1).rank)
				availActs.add(Action.SPLIT);
		}
		final Action act = player.getAction(availActs);
		boolean split = false;
		if (act == Action.SPLIT) {
			final ArrayList<Card> second = new ArrayList<>();
			playHands.add(second);
			second.add(first.removeLast());
			split = true;
		}
		//TODO Continue
	}

	private final void dealDealer(final Card card) {
		dealCard(dealHand, card);
	}

	private final void dealCard(final ArrayList<Card> hand, final Card card) {
		hand.add(card);
	}

	private final Card draw() {
		return deck.pop();
	}

	final String getDealerHand() {
		return dealHand.toString();
	}

	final String getPlayerHands() {
		// TODO Auto-generated method stub
		return null;
	}
}