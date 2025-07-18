package nbradham.blackjack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Stack;

import nbradham.blackjack.Card.Rank;
import nbradham.blackjack.Card.Suit;

final class Game {

	private static final long SEED = System.currentTimeMillis();
	private static final Random RAND = new Random(SEED);

	static enum Action {
		DOUBLE, HIT, SPLIT, STAND, SURRENDER
	};

	private final Stack<Card> deck = new Stack<>();
	private final ArrayList<Hand> playHands = new ArrayList<>();
	private final Hand dealHand = new Hand();
	private final HashSet<Action> availActs = new HashSet<>();
	private final Player player;

	private short bet;

	Game(final Player setPlayer) {
		(player = setPlayer).setGame(this);
	}

	final void start() {
		System.out.printf("Seed: %d%n", SEED);
		final short bet = player.getBet();
		final Rank[] ranks = Rank.values();
		for (Suit s : Suit.values())
			for (Rank r : ranks)
				deck.add(new Card(s, r));
		Collections.shuffle(deck, RAND);
		dealDealer(draw().setHidden(true));
		final Hand first = new Hand(bet);
		playHands.add(first);
		dealCard(first);
		dealDealer();
		dealCard(first);
		boolean noBust = false;
		Action act;
		if (handSum(first) < 21) {
			availActs.add(Action.HIT);
			availActs.add(Action.STAND);
			availActs.add(Action.SURRENDER);
			final Rank firstRank = getRank(first, 0);
			if (checkDouble() && firstRank == getRank(first, 1))
				availActs.add(Action.SPLIT);
			act = getAction(first);
			boolean split = false;
			if (act == Action.SPLIT) {
				takeCoin();
				final Hand second = new Hand(bet);
				playHands.add(second);
				second.cards.add(first.cards.removeLast());
				split = true;
				availActs.remove(Action.SPLIT);
				availActs.remove(Action.SURRENDER);
			}
			for (final Hand hand : playHands) {
				if (split) {
					dealCard(hand);
					if (firstRank == Rank.ACE)
						noBust = true;
					if (is21orGrtr(hand))
						continue;
				}
				if (!split || firstRank != Rank.ACE) {
					checkDouble();
					loop: while (true) {
						switch (act) {
						case DOUBLE:
							dealCard(hand);
							takeCoin();
							hand.bet <<= 1;
							break loop;
						case HIT:
							dealCard(hand);
							if (is21orGrtr(hand))
								break loop;
							break;
						case STAND:
							break loop;
						case SURRENDER:
							player.gain(bet / 2);
							return;
						}
						act = getAction(hand);
						availActs.remove(Action.DOUBLE);
					}
					if (handSum(hand) < 22)
						noBust = true;
				}
			}
		} else
			noBust = true;
		int win = 0;
		if (noBust) {
			get(dealHand, 0).hidden = false;
			byte dealSum;
			while ((dealSum = handSum(dealHand)) < 17)
				dealDealer();
			boolean isDealBJ = isBJ(dealHand);
			for (final Hand hand : playHands) {
				boolean isbj = isBJ(hand);
				if (isDealBJ) {
					if (isbj)
						win += hand.bet;
				} else if (isbj)
					win += hand.bet + hand.bet * 3 / 2;
				else {
					byte sum = handSum(hand);
					if (sum < 22)
						if (dealSum > 21 || sum > dealSum)
							win += hand.bet << 1;
						else if (sum == dealSum)
							win += hand.bet;
				}
			}
		}
		player.gain(win);
	}

	final Card[] getDealerHand() {
		return dealHand.cards.toArray(new Card[dealHand.cards.size()]);
	}

	final Card[][] getPlayerHands() {
		return playHands.parallelStream().map(hand -> hand.cards.toArray(new Card[hand.cards.size()]))
				.toArray(size -> new Card[size][]);
	}

	private final void dealDealer() {
		dealDealer(draw());
	}

	private final void dealDealer(final Card card) {
		dealCard(dealHand, card);
	}

	private final void dealCard(final Hand hand) {
		dealCard(hand, draw());
	}

	private final void dealCard(final Hand hand, final Card card) {
		hand.cards.add(card);
	}

	private final Card draw() {
		return deck.pop();
	}

	private final Action getAction(final Hand hand) {
		return player.getAction(availActs, hand.cards.toArray(new Card[hand.cards.size()]));
	}

	private final boolean checkDouble() {
		if (player.coin >= bet) {
			availActs.add(Action.DOUBLE);
			return true;
		}
		return false;
	}

	private final void takeCoin() {
		player.coin -= bet;
	}

	private static final byte handSum(final Hand hand) {
		byte sum = 0, aces = 0;
		for (Card c : hand.cards) {
			sum += c.rank.value;
			if (c.rank == Rank.ACE)
				++aces;
		}
		while (--aces != -1 && sum > 21)
			sum -= 10;
		return sum;
	}

	private static final Rank getRank(final Hand hand, final int index) {
		return get(hand, index).rank;
	}

	private static final boolean is21orGrtr(final Hand hand) {
		return handSum(hand) > 20;
	}

	private static final Card get(final Hand hand, final int index) {
		return hand.cards.get(index);
	}

	private static final boolean isBJ(final Hand hand) {
		return handSum(hand) == 21 && hand.cards.size() == 2;
	}

	private static final class Hand {

		private final ArrayList<Card> cards = new ArrayList<>();
		private short bet;

		private Hand() {
		}

		private Hand(final short setBet) {
			bet = setBet;
		}
	}
}