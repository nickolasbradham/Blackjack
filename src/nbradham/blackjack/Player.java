package nbradham.blackjack;

import java.util.HashSet;

import nbradham.blackjack.Game.Action;

sealed abstract class Player permits TerminalPlayer {

	Game game;
	int coin;

	protected Player(final int startCoin) {
		coin = startCoin;
	}

	void setGame(final Game setGame) {
		game = setGame;
	}

	void gain(final int money) {
		coin += money;
	}

	abstract short getBet();

	abstract Action getAction(final HashSet<Action> availableActions, final Card[] hand);
}