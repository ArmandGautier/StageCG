package Example;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.jgrapht.*;
import org.jgrapht.graph.*;
import org.jgrapht.nio.Attribute;
import org.jgrapht.nio.DefaultAttribute;
import org.jgrapht.nio.dot.DOTExporter;

import java.io.*;

import CoalitionGame.Player;
import Graph.Edge;

public class TestGraphe {

	public static void main(String[] args) throws IOException {
		
		Graph<Player, Edge> g = new DefaultUndirectedWeightedGraph<>(Edge.class);
		
		LinkedList<Player> listPlayer = new LinkedList<Player>();
		
		int nbPlayer = 3;
		
		// add the vertices
		
		for (int i=0; i<nbPlayer; i++) {
			listPlayer.add(new Player(i));
			g.addVertex(listPlayer.get(i));
		}

        // add edges to create linking structure
		g.addEdge(listPlayer.get(0), listPlayer.get(1),new Edge(listPlayer.get(0), listPlayer.get(1), 4));
		g.addEdge(listPlayer.get(0), listPlayer.get(2),new Edge(listPlayer.get(0), listPlayer.get(2), 3));
		g.addEdge(listPlayer.get(1), listPlayer.get(2),new Edge(listPlayer.get(1), listPlayer.get(2), 2));

        DOTExporter<Player, Edge> exporter = new DOTExporter<Player, Edge>();
        
        
        exporter.setVertexAttributeProvider(v -> {
            Map<String, Attribute> m = new HashMap<>();
            m.put("label", DefaultAttribute.createAttribute(v.toString()));
            return m;
        });
     
        exporter.setEdgeAttributeProvider(e -> {
            Map<String, Attribute> m = new HashMap<>();
            m.put("label", DefaultAttribute.createAttribute(e.getWeight()));
            return m;
        });
        
        Writer writer = new StringWriter();
        exporter.exportGraph(g,writer);
        System.out.println(writer.toString());

	}

}
