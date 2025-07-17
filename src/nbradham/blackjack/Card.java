package nbradham.blackjack;

final class Card {
	static enum Suit {
		CLUBS('C'), DIAMONDS('D'), HEARTS('H'), SPADES('S');

		private final char character;

		private Suit(final char setChar) {
			character = setChar;
		}
	}

	static enum Rank {
		ACE(11, "A"), N2(2, "2"), N3(3, "3"), N4(4, "4"), N5(5, "5"), N6(6, "6"), N7(7, "7"), N8(8, "8"), N9(9, "9"),
		N10(10, "10"), JACK(10, "J"), QUEEN(10, "Q"), KING(10, "K");

		private final String str;
		final int value;

		private Rank(final int setValue, final String setStr) {
			value = setValue;
			str = setStr;
		}
	}

	final Rank rank;

	private final String str;
	private boolean hidden = false;

	Card(final Suit setSuit, final Rank setRank) {
		str = (rank = setRank).str + setSuit.character;
	}

	Card setHidden(boolean setHidden) {
		hidden = setHidden;
		return this;
	}

	@Override
	public final String toString() {
		return hidden ? "Secret" : str;
	}
}