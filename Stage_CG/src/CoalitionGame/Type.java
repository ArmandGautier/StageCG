package CoalitionGame;

public class Type {
	
	/**
	 * l'identifiant du type
	 */
	private int num;
	/**
	 * le nom du type
	 */
	private String name;
	/**
	 * le nombre de joueur de ce type 
	 */
	private int numberPlayerOfThisType = 0;
	
	/**
	 * @param num
	 * @param name
	 */
	public Type(int num, String name) {
		super();
		this.num = num;
		this.name = name;
	}

	/**
	 * @return the num
	 */
	public int getNum() {
		return num;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the numberPlayerOfThisType
	 */
	public int getNumberPlayerOfThisType() {
		return numberPlayerOfThisType;
	}
	
	/**
	 * 
	 */
	public void addPlayer() {
		this.numberPlayerOfThisType++;
	}

}
