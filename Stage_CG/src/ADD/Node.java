package ADD;

import CoalitionGame.Type;

public class Node {
	
	private Integer id;
	private int numVariable = -1;
	private Type type = null;
	private int value = -1;
	private Node rightChild = null;
	private Node leftChild = null;
	private boolean isLeaf = false;
	
	/**
	 * @param id
	 * @param value
	 */
	public Node(Integer id, int value, boolean isLeaf) {
		super();
		this.id = id;
		this.value = value;
		this.isLeaf = true;
	}

	/**
	 * @param id
	 * @param numVariable
	 * @param type
	 */
	public Node(Integer id, int numVariable, Type type) {
		super();
		this.id = id;
		this.numVariable = numVariable;
		this.type = type;
	}

	/**
	 * @return the rightChild
	 */
	public Node getRightChild() {
		return rightChild;
	}

	/**
	 * @param rightChild the rightChild to set
	 */
	public void setRightChild(Node rightChild) {
		this.rightChild = rightChild;
	}

	/**
	 * @return the leftChild
	 */
	public Node getLeftChild() {
		return leftChild;
	}

	/**
	 * @param leftChild the leftChild to set
	 */
	public void setLeftChild(Node leftChild) {
		this.leftChild = leftChild;
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @return the numVariable
	 */
	public int getNumVariable() {
		return numVariable;
	}

	/**
	 * @return the value
	 */
	public int getValue() {
		return value;
	}
	
	public boolean isLeaf() {
		return this.isLeaf;
	}

	/**
	 * @return the type
	 */
	public Type getType() {
		return type;
	}

	@Override
	public String toString() {
		if ( this.isLeaf()) {
			return ("Feuille de valeur : " + value + " id : " + id + "\n");
		}
		else {
			return ("Noeud de variable : " + numVariable + "\nSous arbe droit : \n" + this.rightChild.toString()) + "Sous arbre gauche : \n" + this.leftChild.toString() ;
		}
	}

	@Override
	public int hashCode() {
		if (this.isLeaf()) {
			return this.value;
		}
		String res = "(" + leftChild.hashCode() + "," + rightChild.hashCode() + ")";
		return res.hashCode();
	}
	
}
