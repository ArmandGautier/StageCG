package DAG;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import ADD.Node;

@SuppressWarnings("rawtypes")
public class DAG<V> {
	
	/**
	 * représentation du DAG
	 */
	private HashMap<Integer,HashMap<Integer,Node>> listNodes;
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
	public DAG(HashMap<Integer,HashMap<Integer,Node>> nodes, Node root, ArrayList<V> varOrder) {
		super();
		this.listNodes = nodes;
		this.root = root;
		this.varOrder = varOrder;
	}

	/**
	 * @return the listNodes
	 */
	public HashMap<Integer,HashMap<Integer,Node>>  getListNodes() {
		return listNodes;
	}

	/**
	 * @return the root
	 */
	public Node getRoot() {
		return root;
	}
	
	/**
	 * @return an iterator on nodes
	 */
	public Iterator<Node> getIteratorOnNodes() {
		List<Node> c = new ArrayList<Node>();
		for (Integer key : this.listNodes.keySet()) {
			c.addAll(listNodes.get(key).values());
		}
		return c.iterator();
	}

	/**
	 * @return order of variable in tge DAG
	 */
	public ArrayList<V> getVarOrder() {
		return varOrder;
	}

	/**
	 * @return the number of nodes in the DAG
	 */
	public int getNbNodes() {
		int res=0;
		for (Integer key : listNodes.keySet()) {
			res += listNodes.get(key).size();
		}
		return res;
	}
	
	/**
	 * @return the number of nodes who are not leaf
	 */
	public int getNbInternalNodes() {
		int res=0;
		for (int i=0; i<this.varOrder.size(); i++) {
			res += this.listNodes.get(i).size();
		}
		return res;
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
