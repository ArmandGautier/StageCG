package CoalitionGame;

import java.util.Collection;
import java.util.TreeSet;

public class StructureOfCoalition {

	/**
	 * ensemble de coalition
	 */
	TreeSet<Coalition> struct = new TreeSet<Coalition>();

	/**
	 * @param struct
	 */
	public StructureOfCoalition(Collection<Coalition> struct) {
		super();
		this.struct.addAll(struct);
	}
	
	/**
	 * @param struct
	 */
	public StructureOfCoalition(Coalition c) {
		super();
		this.struct.add(c);
	}

	/**
	 * @return the struct
	 */
	public TreeSet<Coalition> getStruct() {
		return struct;
	}
	
}
