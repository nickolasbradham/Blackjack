package nbradham.blackjack;

import java.util.HashSet;

import nbradham.blackjack.Game.Action;

sealed abstract class Player permits TerminalPlayer {

	protected Game game;
	protected int coin;

	protected Player(final int startCoin) {
		coin = startCoin;
	}

	final int getCoin() {
		return coin;
	}

	abstract short getBet();

	abstract Action getAction(final HashSet<Action> availableActions, final Card[] hand);

	void setGame(final Game setGame) {
		game = setGame;
	}
}