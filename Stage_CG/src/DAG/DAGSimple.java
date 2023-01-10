package DAG;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import ADD.Node;

@SuppressWarnings("rawtypes")
public class DAGSimple<V> {
	
	/**
	 * représentation du DAG
	 */
	HashMap<Integer,Node>  listNodes = new HashMap<Integer,Node> ();
	/**
	 * racine du DAG
	 */
	Node root;
	/**
	 * ordre des variables dans le DAG
	 */
	private ArrayList<V> varOrder;

	/**
	 * @param copyNodes
	 * @param root
	 * @param varOrder
	 */
	public DAGSimple(HashMap<Integer, Node> Nodes, Node<?> root, ArrayList<V> varOrder) {
		super();
		this.listNodes = Nodes;
		this.root = root;
		this.varOrder = varOrder;
	}

	/**
	 * @return the listNodes
	 */
	public HashMap<Integer,Node>  getListNodes() {
		return listNodes;
	}

	/**
	 * @return the root
	 */
	public Node<?> getRoot() {
		return root;
	}
	
	/**
	 * @return an iterator on nodes of the DAG
	 */
	public Iterator<Node> getIteratorOnNodes() {
		return listNodes.values().iterator();
	}

	/**
	 * @return order of variable of the DAG
	 */
	public ArrayList<V> getVarOrder() {
		return varOrder;
	}

	/**
	 * @return the number of nodes of the DAG
	 */
	public int getNbNodes() {
		return listNodes.size();
	}

	/**
	 * @param filename
	 */
	public void writeDAGinDOT(String filename) {
		
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
				  Node node = it.next();
				  
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

}
