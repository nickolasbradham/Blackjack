package nbradham.blackjack;

import java.util.HashSet;
import java.util.Scanner;

import nbradham.blackjack.Game.Action;

final class TerminalPlayer extends Player {

	private static final Scanner scan = new Scanner(System.in);

	TerminalPlayer(final int startCoin) {
		super(startCoin);
	}

	@Override
	final short getBet() {
		while (true) {
			System.out.print("Enter bet: ");
			try {
				final short bet = Short.parseShort(scan.nextLine());
				if (bet < 1)
					System.out.println("Bet must be greater than zero.");
				else if (bet > coin)
					System.out.println("You don't have enough coin.");
				else
					return bet;
			} catch (NumberFormatException e) {
				System.out.println("Unable to parse bet.");
			}
		}
	}

	@Override
	Action getAction(final HashSet<Action> availableActions) {
		StringBuilder sb = new StringBuilder(String.format("Dealer Hand: %s%nPlayer Hand: %s%nAvailable actions: ",
				game.getDealerHand(), game.getPlayerHands()));
		for (Action a : availableActions)
			switch (a) {
			case DOUBLE:
				sb.append("(D)ouble Down, ");
				break;
			case HIT:
				sb.append("(H)it, ");
				break;
			case SPLIT:
				sb.append("(S)plit, ");
				break;
			case STAND:
				sb.append("S(t)and, ");
				break;
			case SURRENDER:
				sb.append("S(u)rrender, ");
			}
		sb.setLength(sb.length() - 2);
		System.out.println(sb);
		while (true) {
			System.out.print("Select action: ");
			Action a = null;
			switch (Character.toLowerCase(scan.nextLine().charAt(0))) {
			case 'd':
				a = Action.DOUBLE;
				break;
			case 'h':
				a = Action.HIT;
				break;
			case 's':
				a = Action.SPLIT;
				break;
			case 't':
				a = Action.STAND;
				break;
			case 'u':
				a = Action.SURRENDER;
			}
			if (availableActions.contains(a))
				return a;
			else
				System.out.println("Invalid selection.");
		}
	}
}