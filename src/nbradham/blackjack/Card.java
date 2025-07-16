package nbradham.blackjack;

final class Card {
	static enum Suit {
		CLUBS, DIAMONDS, HEARTS, SPADES
	}

	static enum Rank {
		ACE, N2, N3, N4, N5, N6, N7, N8, N9, N10, JACK, QUEEN, KING
	}

	final Rank rank;
	private final Suit suit;

	private boolean revealed = true;

	Card(final Suit setSuit, final Rank setRank) {
		suit = setSuit;
		rank = setRank;
	}

	Card setRevealed(boolean setRevealed) {
		revealed = setRevealed;
		return this;
	}
}