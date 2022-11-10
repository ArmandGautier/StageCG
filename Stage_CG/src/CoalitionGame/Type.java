package CoalitionGame;

public class Type {
	
	private int num;
	private String name;
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
	
	public void addPlayer() {
		this.numberPlayerOfThisType++;
	}

}
