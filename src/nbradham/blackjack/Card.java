package nbradham.blackjack;

final class Card {
	static enum Suit {
		CLUBS, DIAMONDS, HEARTS, SPADES
	}

	static enum Rank {
		ACE(1), N2(2), N3(3), N4(4), N5(5), N6(6), N7(7), N8(8), N9(9), N10(10), JACK(10), QUEEN(10), KING(10);

		private final int value;

		private Rank(int setValue) {
			value = setValue;
		}
	}

	private static final int N10 = Rank.N10.ordinal();

	final Rank rank;
	private final Suit suit;

	private boolean hidden = false;

	Card(final Suit setSuit, final Rank setRank) {
		suit = setSuit;
		rank = setRank;
	}

	Card setHidden(boolean setHidden) {
		hidden = setHidden;
		return this;
	}

	@Override
	public final String toString() {
		if (hidden)
			return "Secret";
		String r;
		if (rank == Rank.ACE || rank.ordinal() > N10)
			r = String.valueOf(rank.toString().charAt(0));
		else
			r = String.valueOf(rank.value);
		return r + suit.toString().charAt(0);
	}
}