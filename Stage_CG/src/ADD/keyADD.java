package ADD;

public class keyADD {
	
	private int indicePlayer;
	private int hashConfig;
	private int hashFils;
	
	/**
	 * @param indicePlayer
	 * @param hashConfig
	 * @param hashFils
	 */
	public keyADD(int indicePlayer, int hashConfig, int hashFils) {
		super();
		this.indicePlayer = indicePlayer;
		this.hashConfig = hashConfig;
		this.hashFils = hashFils;
	}

	/**
	 * @return the indicePlayer
	 */
	public int getIndicePlayer() {
		return indicePlayer;
	}

	/**
	 * @return the hashConfig
	 */
	public int getHashConfig() {
		return hashConfig;
	}

	/**
	 * @return the hashFils
	 */
	public int getHashFils() {
		return hashFils;
	}

	@Override
	public int hashCode() {
		return hashFils;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof keyADD) {
			keyADD key = (keyADD) obj;
			if (this.indicePlayer==key.getIndicePlayer()) {
				if (this.hashConfig==key.getHashConfig()) {
					return true;
				}
				if (this.hashFils==key.getHashFils()) {
					return true;
				}
			}
			return false;
			
		}
		return false;
	}
	
}
