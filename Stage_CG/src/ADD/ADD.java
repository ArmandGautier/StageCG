package ADD;

import java.util.Iterator;
import java.util.List;

import DAG.DAG;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class ADD<V> {
	
	/**
	 * Représentation de l'ADD sous forme de double HashMap, une sur le joueur puis une sur le hash du noeud
	 */
	private HashMap<Integer,HashMap<Integer,Node>> nodes;
	/**
	 * Racine de l'ADD
	 */
	private Node root;
	/**
	 * Variable sur lesquelles des noeuds de l'ADD peuvent porter dans l'ordre sur lequel elles peuvent porter
	 */
	private ArrayList<V> varOrder;
	
	/**
	 * @param nodes
	 * @param root
	 * @param varOrder
	 */
	public ADD(HashMap<Integer,HashMap<Integer,Node>> nodes, Node root, ArrayList<V> varOrder) {
		super();
		this.nodes = nodes;
		this.root = root;
		this.varOrder = varOrder;
	}
	
	/**
	 * @return le nombre de noeuds de l'ADD
	 */
	public int getNbNodes() {
		int res=0;
		for (Integer key : nodes.keySet()) {
			res += nodes.get(key).size();
		}
		return res;
	}
	
	/**
	 * @return la racine de l'ADD
	 */
	public Node getRoot() {
		return root;
	}
	
	/**
	 * @return les variables dans l'ordre dans lequel elles apparaissent dans l'ADD
	 */
	public ArrayList<V> getVarOrder() {
		return varOrder;
	}

	/**
	 * @return un itérateur sur les noeuds de l'ADD
	 */
	public Iterator<Node> getIteratorOnNodes() {
		List<Node> c = new ArrayList<Node>();
		for (Integer key : this.nodes.keySet()) {
			c.addAll(nodes.get(key).values());
		}
		return c.iterator();
	}
	
	/**
	 * @return la représentation de l'ADD sous forme de Hash Map
	 */
	public HashMap<Integer,HashMap<Integer,Node>> getNodes() {
		return nodes;
	}
	
	/**
	 * @param filename
	 */
	public void writeADDinDOT(String filename) {
		try {
			  
			  File file = new File(filename);

			  if (!file.exists()) {
				  file.createNewFile();
			  }
			  
			  FileWriter fw = new FileWriter(file.getAbsoluteFile());
			  BufferedWriter bw = new BufferedWriter(fw);
			  
			  String content;
			  content = "digraph G {\n";
			  
			  Iterator<Node> it = this.getIteratorOnNodes();
			  
			  while (it.hasNext()) {
				  Node<?> node = it.next();
				  
				  if ( ! node.isLeaf() ) {
					  
					  if (node.getType() == null) {  
						  content += node.getId() + " [label=" + node.getIdVariable() + "];\n";
						  content += node.getId() + " -> " + node.getRightChild().getId() + " [label="+true+"];\n";
						  content += node.getId() + " -> " + node.getLeftChild().getId() + " [label="+false+"];\n";
					  }
					  
					  else {  
						  content += node.getId() + " [label=x" + node.getIdVariable() + "_de_type_"+ node.getType().getName() +"];\n";
						  content += node.getId() + " -> " + node.getRightChild().getId() + " [label="+true+"];\n";
						  content += node.getId() + " -> " + node.getLeftChild().getId() + " [label="+false+"];\n";
					  }
				  }
				  
				  else {
					  content += node.getId() + " [label=" + node.getValue() + "];\n";
				  }
			  }
			  content += "}\n";
			  bw.write(content);
			  bw.close();
			  
			}
		
		catch (IOException e) {e.printStackTrace();}
	}
	
	/**
	 * @return le DAG construit à partir de l'ADD ( rajout des noeuds inutiles )
	 * @throws CloneNotSupportedException
	 */
	public DAG createDAG() throws CloneNotSupportedException {
		
		// Création des tables de Hash qui serviront à stocker les noeuds du DAG
		
		HashMap<Integer,HashMap<Integer,Node>> dagNodes = new HashMap<Integer,HashMap<Integer,Node>>();
		for (int i=0; i<=this.varOrder.size(); i++) {
			HashMap<Integer,Node> hash = new HashMap<Integer,Node>();
			dagNodes.put(i, hash);
		}
		
		// récupération et copie de la racine
		
		Node rootDag = this.cloneNode(this.root);
		
		// création du DAG 
		
		fun(dagNodes,rootDag,0,this.getNbNodes()-1);
		
		// Mise à jour de la racine si besoin
		
		if (rootDag.isLeaf()) {
			rootDag = dagNodes.get(0).get(this.getNbNodes());
		}
		else {
			if (! rootDag.getIdVariable().equals(varOrder.get(0))) {
				rootDag = dagNodes.get(0).get(this.getNbNodes());
			}
		}
		
		DAG res = new DAG(dagNodes,rootDag,this.varOrder);
		
		return res;
			
	}

	/**
	 * @param node
	 * @return un clone du noeud passé en paramètre
	 */
	private Node cloneNode(Node node) {
		Node n;
		if (node.isLeaf()) {
			n = new Node(node.getId(),node.getValue());
		}
		else {
			n = new Node(node.getId(), node.getIdVariable(), node.getType(), node.getRightChild(), node.getLeftChild());
		}
		return n;
	}
	
	/**
	 * @param dagNodes
	 * @param node
	 * @param depth
	 * @param nbNode
	 * @return le nombre de noeud dans le DAG 
	 * @throws CloneNotSupportedException
	 */
	private int fun(HashMap<Integer,HashMap<Integer,Node>> dagNodes, Node node, int depth, int nbNode) throws CloneNotSupportedException {
		
		// si on est sur une feuille et qu'on a pas la bonne profondeur
		
		if (node.isLeaf() && depth<this.varOrder.size()) {
			Node newNode = new Node(nbNode+1,this.varOrder.get(depth),node,node);
			nbNode = fun(dagNodes,newNode,depth,nbNode+1);
			return nbNode;
		}
		
		// si la racine ne porte pas sur la première variable (peut arriver si la première variable est inutile dans l'ADD)
		
		if ( ! node.isLeaf()) {
			if ( depth==0 && ! node.getIdVariable().equals(varOrder.get(0))) {
				Node newNode = new Node(nbNode+1,this.varOrder.get(0),node,node);
				nbNode = fun(dagNodes,newNode,depth,nbNode+1);
				return nbNode;
			}
		}
		
		// si la profondeur n'est pas celle de la dernière variable et que le noeud courant n'est pas déjà dans la Hash du DAG (i.e qu'on a déjà traité son cas)
		
		if (depth < this.varOrder.size()-1 && ! dagNodes.get(varOrder.indexOf(node.getIdVariable())).containsKey(node.getId())) {
		
			Node leftChild = this.cloneNode(node.getLeftChild());
			Node rightChild = this.cloneNode(node.getRightChild());
			
			boolean leftOk = true;
			boolean rightOk = true;
			
			// on regarde si les fils droit et gauche porte sur les bonne variables (i.e les variables suivantes dans l'ordre des variables)
			
			if (leftChild.isLeaf()) {
				leftOk = false;
			}
			else {
				if (! leftChild.getIdVariable().equals(varOrder.get(depth+1))) {
					leftOk = false;
				}
			}
			
			if (rightChild.isLeaf()) {
				rightOk = false;
			}
			else {
				if (! rightChild.getIdVariable().equals(varOrder.get(depth+1))) {
					rightOk = false;
				}
			}
			
			if (! leftOk) {
				
				nbNode++;
				
				// les deux pas ok
				
				if (! rightOk) {
					
					if (! rightChild.equals(leftChild)) {
						Node newNode1 = new Node(nbNode,this.varOrder.get(depth+1),leftChild,leftChild);
						nbNode++;
						Node newNode2 = new Node(nbNode,this.varOrder.get(depth+1),rightChild,rightChild);
						node.setLeftChild(newNode1);
						node.setRightChild(newNode2);
						nbNode = fun(dagNodes,newNode1,depth+1,fun(dagNodes,newNode2,depth+1,nbNode));
					}
					else {
						Node newNode = new Node(nbNode,this.varOrder.get(depth+1),rightChild,leftChild);
						node.setLeftChild(newNode);
						node.setRightChild(newNode);
						nbNode = fun(dagNodes,newNode,depth+1,nbNode);
					}
				}
				
				// gauche pas ok, droit ok
				
				else {
					Node newNode = new Node(nbNode,this.varOrder.get(depth+1),leftChild,leftChild);
					node.setLeftChild(newNode);
					nbNode = fun(dagNodes,rightChild,depth+1,fun(dagNodes,newNode,depth+1,nbNode));
				}
			}
			else {
				
				// gauche ok, droit pas ok
				
				if (! rightOk) {			
					nbNode++; 
					Node newNode = new Node(nbNode,this.varOrder.get(depth+1),rightChild,rightChild);
					node.setRightChild(newNode);
					nbNode = fun(dagNodes,leftChild,depth+1,fun(dagNodes,newNode,depth+1,nbNode));
				}
				
				// les deux ok
				
				else {
					nbNode = fun(dagNodes,leftChild,depth+1,fun(dagNodes,rightChild,depth+1,nbNode));
				}
			}
			
			// on ajoute le noeud courant au noeud du DAG
			
			if ( ! dagNodes.get(this.varOrder.indexOf(node.getIdVariable())).containsKey(node.getId()))
				dagNodes.get(this.varOrder.indexOf(node.getIdVariable())).put(node.getId(),node);
		}
		
		// Quelques manips pour ajouter le noeud courant et ses fils si ils ne sont pas déjà dans le DAG
		
		int key = this.varOrder.indexOf(node.getIdVariable());
		
		if ( ! dagNodes.get(key).containsKey(node.getId()))
			dagNodes.get(key).put(node.getId(),node);
		
		if (node.getRightChild().isLeaf()) {
			if ( ! dagNodes.get(this.varOrder.size()).containsKey(node.getRightChild().getId()))
				dagNodes.get(this.varOrder.size()).put(node.getRightChild().getId(),node.getRightChild());
		}
		else {
			key = this.varOrder.indexOf(node.getRightChild().getIdVariable());
			if ( ! dagNodes.get(key).containsKey(node.getRightChild().getId()))
				dagNodes.get(key).put(node.getRightChild().getId(),node.getRightChild());
		}
		
		if (node.getLeftChild().isLeaf()) {
			if ( ! dagNodes.get(this.varOrder.size()).containsKey(node.getLeftChild().getId()))
				dagNodes.get(this.varOrder.size()).put(node.getLeftChild().getId(),node.getLeftChild());
		}
		else {
			key = this.varOrder.indexOf(node.getLeftChild().getIdVariable());
			if ( ! dagNodes.get(key).containsKey(node.getLeftChild().getId()))
				dagNodes.get(key).put(node.getLeftChild().getId(),node.getLeftChild());
		}
		
		return nbNode;
	}
	
}
