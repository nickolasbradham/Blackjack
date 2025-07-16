package nbradham.blackjack;

final class Main {

	public static final void main(final String[] args) {
		new Game(new TerminalPlayer(100)).start();
	}
}