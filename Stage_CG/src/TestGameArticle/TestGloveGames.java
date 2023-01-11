package TestGameArticle;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


import ADD.ADDSimple;
import ADD.GenerateAddGloveGames;
import DAG.DAGSimple;
import LinearProgram.EmptyCore;

public class TestGloveGames {
	
	// BUGUE
	
	@SuppressWarnings("rawtypes")
	public static void main(String[] args) throws CloneNotSupportedException, IOException {
		
		int nbLeftGlove = 3;
		int nbRightGlove = 5;
		
		File file = new File("testGloveGames.csv");
		
	    if (!file.exists()) {
	       file.createNewFile();
		}
	    
	    String content;
	    
	    content = "nbPlayer, nbType, creationAddTime, nbNodeADD, creationDagTime, nbNodeDAG, solvingTime, emptyCore, cos\n";
	    
	    FileWriter fw = new FileWriter(file.getAbsoluteFile());
	    BufferedWriter bw = new BufferedWriter(fw);
	    
	    bw.write(content);
	    
	    for (int i=2; i<= nbRightGlove; i++) {
	    	
	    	for (int j=2; j<= nbLeftGlove; j++) {
		
				double start1 = System.currentTimeMillis();
				ADDSimple add = GenerateAddGloveGames.generateAddGloveGames(j,i);
				double creationAddTime = System.currentTimeMillis() - start1;
				
				int nbNodeADD = add.getNbNodes();
				add.writeADDinDOT("AddGloveGames"+i+j+".dot");
				
				double start2 = System.currentTimeMillis();
				DAGSimple dag = add.createDAG();
				double creationDagTime = System.currentTimeMillis() - start2;
				
				int nbNodeDAG = dag.getNbNodes();
				dag.writeDAGinDOT("DagGloveGames"+i+j+".dot");
				
				EmptyCore lpADD = new EmptyCore();
				lpADD.solve(dag,j+i);
				double solvingTime = lpADD.getSolvingTime();
				
				boolean emptyCore = true;
				double cos = lpADD.getCos();
				if ( cos < 0.001) {
					emptyCore = false;
					cos = 0;
				}
				
				content = i+j+ ", " +2+ ", " +creationAddTime+ ", " +nbNodeADD+ ", " +creationDagTime+ ", " +nbNodeDAG+ ", " +solvingTime+ ", " +emptyCore+ ", " +cos+ "\n";
				bw.write(content);
	    	
	    	}
	    }
	    
	    bw.close();
	
	}

}
