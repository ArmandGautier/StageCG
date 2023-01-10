package LinearProgram;

import java.util.HashMap;
import java.util.Iterator;

import ADD.Node;
import DAG.DAG;
import Tools.Tools;

public class TestCore {
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static boolean isStable(DAG dag, double[] Xi) {
		
		double[] Du = new double[dag.getNbNodes()];
		
		for (int i=0; i<dag.getNbNodes(); i++) {
			Du[i] = Double.MAX_VALUE;
		}
		
		for (int i=0; i<=Xi.length; i++) {
			
			HashMap<Integer,Node> hash = (HashMap<Integer, Node>) dag.getListNodes().get(i);
			Iterator<Node> it = hash.values().iterator();
			
			while (it.hasNext()) {
				Node node = it.next();
				int idNode = node.getId();
				
				if (node.isLeaf()) {
					if ( Du[idNode] < node.getValue()) 
						return false;
				}
				else {
					int idLeftChild = node.getLeftChild().getId();
					int idRightChild = node.getRightChild().getId();
					Du[idLeftChild] = Tools.min(Du[idLeftChild], Du[idNode]);
					Du[idRightChild] = Tools.min(Du[idRightChild], Du[idNode]+Xi[i]);
				}
				
			}
		}
		
		return true;
	}

}
