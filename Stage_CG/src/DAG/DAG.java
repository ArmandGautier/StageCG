package DAG;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import ADD.Node;

public class DAG<V> {
	
	HashMap<Integer,Node<?>>  listNodes = new HashMap<Integer,Node<?>> ();
	Node<?> root;
	private ArrayList<V> varOrder;

	/**
	 * @param copyNodes
	 * @param root
	 * @param varOrder
	 */
	public DAG(HashMap<Integer, Node<?>> copyNodes, Node<?> root, ArrayList<V> varOrder) {
		super();
		this.listNodes = copyNodes;
		this.root = root;
		this.varOrder = varOrder;
	}

	/**
	 * @return the listNodes
	 */
	public HashMap<Integer,Node<?>>  getListNodes() {
		return listNodes;
	}

	/**
	 * @return the root
	 */
	public Node<?> getRoot() {
		return root;
	}
	
	public Iterator<Node<?>> getIteratorOnNodes() {
		return listNodes.values().iterator();
	}

	public ArrayList<V> getVarOrder() {
		return varOrder;
	}

	public int getNbNodes() {
		return listNodes.size();
	}

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
			  
			  Iterator<Node<?>> it = this.getIteratorOnNodes();
			  
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

}
