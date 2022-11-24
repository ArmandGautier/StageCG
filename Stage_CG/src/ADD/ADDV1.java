package ADD;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import DAG.DAG;

@SuppressWarnings({"unchecked","rawtypes"})
public class ADDV1<V> {
	
	private HashMap<Integer,Node> nodes;
	private Node root;
	private ArrayList<V> varOrder;
	
	/**
	 * @param nodes
	 * @param root
	 */
	public ADDV1(HashMap<Integer,Node> nodes, Node root, ArrayList<V> varOrder) {
		super();
		this.nodes = nodes;
		this.root = root;
		this.varOrder = varOrder;
	}
	
	public int getNbNodes() {
		return nodes.size();
	}
	
	public Node getRoot() {
		return root;
	}
	
	public ArrayList<V> getVarOrder() {
		return varOrder;
	}

	public Iterator<Node> getIteratorOnNodes() {
		return nodes.values().iterator();
	}
	
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
						  content += node.getId() + " [label=x" + node.getIdVariable() + "_de_type_"+ node.getType().getName() + "];\n";
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
	
	public DAG createDAG() throws CloneNotSupportedException {
		
		HashMap<Integer,Node>  dagNodes = new HashMap<Integer,Node>();
		Node rootDag = (Node) this.root.clone();
		fun(dagNodes,rootDag,0,this.getNbNodes()-1);
		DAG res = new DAG(dagNodes,rootDag,this.varOrder);
		
		return res;
			
	}

	/**
	 * @return the nodes
	 */
	public HashMap<Integer,Node> getNodes() {
		return nodes;
	}

	private int fun(HashMap<Integer, Node> dagNodes, Node node, int depth, int nbNode) {
		// si mon noeud est une feuille
		
		if (node.isLeaf() && depth<this.varOrder.size()) {
			Node newNode = new Node(nbNode+1,this.varOrder.get(depth));
			newNode.setLeftChild(node);
			newNode.setRightChild(node);
			nbNode = fun(dagNodes,newNode,depth,nbNode+1);
			return nbNode;
		}
		
		// si mon noeud racine ne porte pas sur la première variable
		
		if ( ! node.isLeaf()) {
			if ( depth==0 && ! node.getIdVariable().equals(varOrder.get(0))) {
				Node newNode = new Node(nbNode+1,this.varOrder.get(0));
				newNode.setLeftChild(node);
				newNode.setRightChild(node);
				nbNode = fun(dagNodes,newNode,depth,nbNode+1);
				return nbNode;
			}
		}
		
		if (depth < this.varOrder.size()-1 && ! dagNodes.containsKey(node.getId())) {
			Node leftChild = node.getLeftChild();
			Node rightChild = node.getRightChild();
			
			boolean leftOk = true;
			boolean rightOk = true;
			
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
						Node newNode1 = new Node(nbNode,this.varOrder.get(depth+1));
						nbNode++;
						Node newNode2 = new Node(nbNode,this.varOrder.get(depth+1));
						node.setLeftChild(newNode1);
						node.setRightChild(newNode2);
						newNode1.setLeftChild(leftChild);
						newNode1.setRightChild(leftChild);
						newNode2.setLeftChild(rightChild);
						newNode2.setRightChild(rightChild);
						nbNode = fun(dagNodes,newNode1,depth+1,fun(dagNodes,newNode2,depth+1,nbNode));
					}
					else {
						Node newNode = new Node(nbNode,this.varOrder.get(depth+1));
						node.setLeftChild(newNode);
						node.setRightChild(newNode);
						newNode.setLeftChild(leftChild);
						newNode.setRightChild(rightChild);
						nbNode = fun(dagNodes,newNode,depth+1,nbNode);
					}
				}
				
				// gauche pas ok, droit ok
				
				else {
					Node newNode = new Node(nbNode,this.varOrder.get(depth+1));
					node.setLeftChild(newNode);
					newNode.setLeftChild(leftChild);
					newNode.setRightChild(leftChild);
					nbNode = fun(dagNodes,rightChild,depth+1,fun(dagNodes,newNode,depth+1,nbNode));
				}
			}
			else {
				
				// gauche ok, droit pas ok
				
				if (! rightOk) {
					
					nbNode++; 
					
					Node newNode = new Node(nbNode,this.varOrder.get(depth+1));
					node.setRightChild(newNode);
					newNode.setLeftChild(rightChild);
					newNode.setRightChild(rightChild);
					nbNode = fun(dagNodes,leftChild,depth+1,fun(dagNodes,newNode,depth+1,nbNode));
				}
				
				// les deux ok
				
				else {
					nbNode = fun(dagNodes,leftChild,depth+1,fun(dagNodes,rightChild,depth+1,nbNode));
					/*double start = System.currentTimeMillis();
					if (! rightChild.equals(leftChild)) {
						System.out.println(System.currentTimeMillis()-start+" depth = "+depth);
					}
					else {
						System.out.println(System.currentTimeMillis()-start+" depth = "+depth);
						nbNode = fun(dagNodes,leftChild,depth+1,nbNode);
					}*/
				}
			}
		}
		if ( ! dagNodes.containsKey(node.getId()))
			dagNodes.put(node.getId(),node);
		if ( ! dagNodes.containsKey(node.getRightChild().getId()))
			dagNodes.put(node.getRightChild().getId(),node.getRightChild());
		if ( ! dagNodes.containsKey(node.getLeftChild().getId()))
			dagNodes.put(node.getLeftChild().getId(),node.getLeftChild());
		return nbNode;
	}
}
