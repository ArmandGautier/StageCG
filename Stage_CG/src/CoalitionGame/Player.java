package CoalitionGame;

public class Player implements Comparable<Player> {

	Integer representation;
	Type type;
	
	/**
	 * @param representation
	 */
	public Player(Integer representation) {
		this.representation = representation;
		this.type = null;
	}
	
	/**
	 * @param representation
	 * @param type
	 */
	public Player(Integer representation, Type type) {
		super();
		this.representation = representation;
		this.type = type;
	}

	/**
	 * @return the representation
	 */
	public Integer getRepresentation() {
		return representation;
	}
    
	/**
	 * @return the type
	 */
	public Type getType() {
		return type;
	}

	@Override
    public String toString() {
    	return "Joueur "+this.representation;
    }
	
	@Override
	public int compareTo(Player o) {
		return this.representation.compareTo(o.getRepresentation());
	}
	
	@Override
	public boolean equals(Object p) {
		if ( p instanceof Player ) {
			return this.representation.equals(((Player) p).getRepresentation());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return this.toString().hashCode();
	}

}
