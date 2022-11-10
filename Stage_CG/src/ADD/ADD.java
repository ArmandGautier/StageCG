package ADD;

import java.util.Iterator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ADD<V> {
	
	private HashMap<Integer,Node> nodes;
	private Node root;
	private ArrayList<V> varOrder;
	
	/**
	 * @param nodes
	 * @param root
	 */
	public ADD(HashMap<Integer, Node> nodes, Node root, ArrayList<V> varOrder) {
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
				  Node node = it.next();
				  
				  if ( ! node.isLeaf() ) {
					  content += node.getId() + " [label=x" + node.getNumVariable() + "_de_type_"+ node.getType().getName() + "];\n";
					  content += node.getId() + " -> " + node.getRightChild().getId() + " [label="+true+"];\n";
					  content += node.getId() + " -> " + node.getLeftChild().getId() + " [label="+false+"];\n";
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
