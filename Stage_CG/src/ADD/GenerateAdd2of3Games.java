package ADD;

import java.util.ArrayList;
import java.util.HashMap;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class GenerateAdd2of3Games {
	
	public static ADDSimple generateAdd2of3Games(int n) {
		
		ArrayList<String> listVar = new ArrayList<String>();
		
		for (int i=0; i<n; i++) {
			listVar.add("x"+(n-i));
			listVar.add("y"+(n-i));
			listVar.add("z"+(n-i));
		}
		
		HashMap<Integer,Node> nodes = new HashMap<Integer,Node>();
		Node zero = new Node(0,0);
		nodes.put(0, zero);
		Node root = createADD(n,nodes,zero);
		
		ADDSimple res = new ADDSimple(nodes,root,listVar);
		
		return res;
		
	}

	private static Node createADD(int n, HashMap<Integer,Node> nodes, Node zero) {
		
		if (n==1) {
			
			Node un = new Node(nodes.size(),1);
			nodes.put(nodes.size(), un);
			
			Node<String> Z = new Node(nodes.size(),"z"+n,un,zero);
			nodes.put(Z.getId(), Z);
			Node<String> Y1 = new Node(nodes.size(),"y"+n,Z,zero);
			nodes.put(Y1.getId(), Y1);
			Node<String> Y2 = new Node(nodes.size(),"y"+n,un,Z);
			nodes.put(Y2.getId(), Y2);
			Node<String> X = new Node(nodes.size(),"x"+n,Y2,Y1);
			nodes.put(X.getId(), X);
			
			return X;
			
		}
		
		Node<String> prev = createADD(n-1,nodes,zero);
		
		Node<String> Z = new Node(nodes.size(),"z"+n,prev,zero);
		nodes.put(Z.getId(), Z);
		Node<String> Y1 = new Node(nodes.size(),"y"+n,Z,zero);
		nodes.put(Y1.getId(), Y1);
		Node<String> Y2 = new Node(nodes.size(),"y"+n,prev,Z);
		nodes.put(Y2.getId(), Y2);
		Node<String> X = new Node(nodes.size(),"x"+n,Y2,Y1);
		nodes.put(X.getId(), X);
		
		return X;

	}

}
