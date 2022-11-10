package Graph;

import CoalitionGame.Player;

public class Edge {

	private Player start;
	private Player finish;
	private Integer weight;
	private boolean isOriented = false;
	/**
	 * @return the weight
	 */
	public Integer getWeight() {
		return weight;
	}
	/**
	 * @param weight the weight to set
	 */
	public void setWeight(Integer weight) {
		this.weight = weight;
	}
	/**
	 * @return the start
	 */
	public Player getStart() {
		return start;
	}
	/**
	 * @return the finish
	 */
	public Player getFinish() {
		return finish;
	}
	/**
	 * @return the isOriented
	 */
	public boolean isOriented() {
		return isOriented;
	}
	/**
	 * @param start
	 * @param finish
	 * @param weight
	 */
	public Edge(Player start, Player finish, Integer weight) {
		super();
		this.start = start;
		this.finish = finish;
		this.weight = weight;
	}
	/**
	 * @param start
	 * @param finish
	 * @param isOriented
	 * @param weight
	 */
	public Edge(Player start, Player finish, boolean isOriented, Integer weight) {
		super();
		this.start = start;
		this.finish = finish;
		this.isOriented = isOriented;
		this.weight = weight;
	}
}
