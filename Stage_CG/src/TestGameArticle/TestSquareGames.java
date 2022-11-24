package TestGameArticle;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import ADD.ADDV1;
import ADD.GenerateAddSquareGames;
import DAG.DAG;
import LinearProgram.EmptyCore;

public class TestSquareGames {
	
	public static void main(String[] args) throws CloneNotSupportedException, IOException {
		
		int nbPlayer = 100;
		
		File file = new File("testSquareGames.csv");
		
	    if (!file.exists()) {
	       file.createNewFile();
		}
	    
	    String content;
	    
	    content = "nbPlayer, nbType, creationAddTime, nbNodeADD, creationDagTime, nbNodeDAG, solvingTime, emptyCore, cos\n";
	    
	    FileWriter fw = new FileWriter(file.getAbsoluteFile());
	    BufferedWriter bw = new BufferedWriter(fw);
	    
	    bw.write(content);
	    
	    for (int i=5; i<= nbPlayer; i++) {
		
			double start1 = System.currentTimeMillis();
			ADDV1<?> add = GenerateAddSquareGames.generateAddSquareGames(i);
			double creationAddTime = System.currentTimeMillis() - start1;
			
			int nbNodeADD = add.getNbNodes();
			//add.writeADDinDOT("AddSquareGames.dot");
			
			double start2 = System.currentTimeMillis();
			DAG<?> dag = add.createDAG();
			double creationDagTime = System.currentTimeMillis() - start2;
			
			int nbNodeDAG = dag.getNbNodes();
			//dag.writeDAGinDOT("DagSquareGames.dot");
			
			EmptyCore lpADD = new EmptyCore();
			lpADD.solve(dag,i);
			double solvingTime = lpADD.getSolvingTime();
			
			boolean emptyCore = true;
			double cos = lpADD.getCos();
			if ( cos < 0.001) {
				emptyCore = false;
				cos = 0;
			}
			
			content = i+ ", " +1+ ", " +creationAddTime+ ", " +nbNodeADD+ ", " +creationDagTime+ ", " +nbNodeDAG+ ", " +solvingTime+ ", " +emptyCore+ ", " +cos+ "\n";
			bw.write(content);
	    }
	    bw.close();
	}

}
