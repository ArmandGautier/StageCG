package ADD;

import java.util.Objects;

import CoalitionGame.Type;

public class Node<V> implements Cloneable {
	
	private Integer id;
	private V idVariable = null;
	private Type type = null;
	private int value = -1;
	private Node<?> rightChild = null;
	private Node<?> leftChild = null;
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
	public Node(Integer id, V idVariable, Type type) {
		super();
		this.id = id;
		this.idVariable = idVariable;
		this.type = type;
	}
	
	/**
	 * @param id
	 * @param numVariable
	 */
	public Node(Integer id, V idVariable) {
		super();
		this.id = id;
		this.idVariable = idVariable;
	}

	/**
	 * @return the rightChild
	 */
	public Node<?> getRightChild() {
		return rightChild;
	}

	/**
	 * @param rightChild the rightChild to set
	 */
	public void setRightChild(Node<?> rightChild) {
		this.rightChild = rightChild;
	}

	/**
	 * @return the leftChild
	 */
	public Node<?> getLeftChild() {
		return leftChild;
	}

	/**
	 * @param leftChild the leftChild to set
	 */
	public void setLeftChild(Node<?> leftChild) {
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
	public V getIdVariable() {
		return idVariable;
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
			return ("Noeud de variable : " + idVariable.toString() + "\n\nSous arbe droit de " + idVariable.toString() + ": \n" + this.rightChild.toString()) + "\nSous arbre gauche de " + idVariable.toString() + ": \n" + this.leftChild.toString() ;
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

	@Override
	public boolean equals(Object obj) {
		Node n = (Node) obj;
		if ( this.isLeaf() != n.isLeaf()) {
			return false;
		}
		if ( this.isLeaf()) {
			return this.getValue() == n.getValue();
		}
		return this.getLeftChild().equals(n.getLeftChild()) && this.getRightChild().equals(n.getRightChild());
	}

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	

	
}
