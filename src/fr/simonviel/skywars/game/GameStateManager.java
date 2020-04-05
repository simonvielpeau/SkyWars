package fr.simonviel.skywars.game;

public class GameStateManager {

	private GameState currentState;
	
	public void setState(GameState gameState) {
		currentState = gameState;
		System.out.println("Passage du jeu en "+gameState.toString());
	}
	
	public boolean isState(GameState gameState) {
		return currentState == gameState;
	}
	
	public GameState getState() {
		return currentState;
	}
	
	public boolean canJoin() {
		return currentState == GameState.LOBBY;
	}
	
	
}
