package ADD;

import CoalitionGame.Type;

@SuppressWarnings("rawtypes")
public class Node<V> implements Cloneable {
	
	/**
	 * l'identifiant du noeud
	 */
	private Integer id;
	/**
	 * la variable sur lequel porte le noeud
	 */
	private V idVariable = null;
	/**
	 * le type de la variable
	 */
	private Type type = null;
	/**
	 * la valeur de la feuille
	 */
	private int value = -1;
	/**
	 * le fils droit
	 */
	private Node rightChild = null;
	/**
	 * le fils gauche
	 */
	private Node leftChild = null;
	/**
	 * booléan disant si le noeud est une feuille ou pas
	 */
	private boolean isLeaf = false;
	/**
	 * valeur du hash, forcément mise à jour à la création du noeud
	 */
	private int hash = 0;
	
	// 3 manière de créer un noeud
	
	// si c'est une feuille 
	
	/**
	 * @param id
	 * @param value
	 */
	public Node(Integer id, int value) {
		super();
		this.id = id;
		this.value = value;
		this.isLeaf = true;
		this.hash = this.hashCode();
	}
	
	// si c'est un noeud interne (avec ou sans précision de type)
	
	/**
	 * @param id
	 * @param idVariable
	 * @param rightChild
	 * @param leftChild
	 */
	public Node(Integer id, V idVariable, Node rightChild, Node leftChild) {
		super();
		this.id = id;
		this.idVariable = idVariable;
		this.rightChild = rightChild;
		this.leftChild = leftChild;
		this.hash = this.hashCode();
	}

	/**
	 * @param id
	 * @param idVariable
	 * @param type
	 * @param rightChild
	 * @param leftChild
	 */
	public Node(Integer id, V idVariable, Type type, Node rightChild, Node leftChild) {
		super();
		this.id = id;
		this.idVariable = idVariable;
		this.type = type;
		this.rightChild = rightChild;
		this.leftChild = leftChild;
		this.hash = this.hashCode();
	}

	/**
	 * @return the rightChild
	 */
	public Node getRightChild() {
		return rightChild;
	}

	/**
	 * @return the leftChild
	 */
	public Node getLeftChild() {
		return leftChild;
	}
	
	/**
	 * @param rightChild the rightChild to set
	 */
	public void setRightChild(Node rightChild) {
		this.rightChild = rightChild;
	}

	/**
	 * @param leftChild the leftChild to set
	 */
	public void setLeftChild(Node leftChild) {
		this.leftChild = leftChild;
	}

	/**
	 * @return the hash of the node
	 */
	public int getHash() {
		return hash;
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
	
	/**
	 * @return true if the node is a leaf
	 */
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
			return ("Noeud de variable : " + idVariable.toString() + "\n\nSous arbe droit : " + idVariable.toString() + ": \n" + this.rightChild.toString()) + "\nSous arbre gauche : " + idVariable.toString() + ": \n" + this.leftChild.toString() ;
		}
	}

	@Override
	public int hashCode() {
		if (this.isLeaf()) {
			return this.value;
		}
		String res = "(" + leftChild.getHash() + "," + rightChild.getHash() + ")" + this.idVariable.toString();
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
		return this.getLeftChild().getHash()==n.getLeftChild().getHash() && this.getRightChild().getHash()==n.getRightChild().getHash() && this.getIdVariable().equals(n.getIdVariable());
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
}
