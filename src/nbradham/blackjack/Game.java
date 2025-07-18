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
	private final ArrayList<ArrayList<Card>> playHands = new ArrayList<>();
	private final ArrayList<Card> dealHand = new ArrayList<>();
	private final HashSet<Action> availActs = new HashSet<>();
	private final Player player;

	private short bet;

	Game(final Player setPlayer) {
		(player = setPlayer).setGame(this);
	}

	@SuppressWarnings("incomplete-switch")
	final void start() {
		bet = player.getBet();
		final Rank[] ranks = Rank.values();
		for (Suit s : Suit.values())
			for (Rank r : ranks)
				deck.add(new Card(s, r));
		Collections.shuffle(deck);
		dealDealer(draw().setHidden(true));
		final ArrayList<Card> first = new ArrayList<>();
		playHands.add(first);
		dealCard(first);
		dealDealer(draw());
		dealCard(first);
		final Rank firstRank = first.get(0).rank;
		if (firstRank.value + first.get(1).rank.value != 21) {
			availActs.add(Action.HIT);
			availActs.add(Action.STAND);
			availActs.add(Action.SURRENDER);
			if (checkDouble() && first.get(0).rank == first.get(1).rank)
				availActs.add(Action.SPLIT);
		}
		Action act = getAction(first);
		boolean split = false;
		if (act == Action.SPLIT) {
			doubleBet();
			final ArrayList<Card> second = new ArrayList<>();
			playHands.add(second);
			second.add(first.removeLast());
			split = true;
			availActs.remove(Action.SPLIT);
			availActs.remove(Action.SURRENDER);
		}
		for (ArrayList<Card> hand : playHands) {
			if (split)
				dealCard(hand);
			if (!split || firstRank != Rank.ACE) {
				checkDouble();
				loop: while (true) {
					switch (act) {
					case DOUBLE:
						dealCard(hand);
						doubleBet();
						break loop;
					case HIT:
						dealCard(hand);
						byte sum = 0, aces = 0;
						for (Card c : hand) {
							sum += c.rank.value;
							if (c.rank == Rank.ACE)
								++aces;
						}
						while (--aces != -1 && (sum -= 10) > 21)
							;
						if (sum >= 21)
							break loop;
					case STAND:
						break loop;
					case SURRENDER:
						player.gain(bet / 2);
						return;
					}
					act = getAction(hand);
					availActs.remove(Action.DOUBLE);
				}
			}
		}
		// TODO Handle Dealer.
	}

	final Card[] getDealerHand() {
		return dealHand.toArray(new Card[dealHand.size()]);
	}

	final Card[][] getPlayerHands() {
		return playHands.parallelStream().map(hand -> hand.toArray(new Card[hand.size()]))
				.toArray(size -> new Card[size][]);
	}

	private final void dealDealer(final Card card) {
		dealCard(dealHand, card);
	}

	private final void dealCard(final ArrayList<Card> hand) {
		dealCard(hand, draw());
	}

	private final void dealCard(final ArrayList<Card> hand, final Card card) {
		hand.add(card);
	}

	private final Card draw() {
		return deck.pop();
	}

	private final Action getAction(ArrayList<Card> hand) {
		return player.getAction(availActs, hand.toArray(new Card[hand.size()]));
	}

	private final boolean checkDouble() {
		if (player.getCoin() >= bet) {
			availActs.add(Action.DOUBLE);
			return true;
		}
		return false;
	}

	private final void doubleBet() {
		player.coin -= bet;
		bet <<= 1;
	}
}