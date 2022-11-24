package TestGameArticle;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import ADD.ADDV1;
import ADD.GenerateAdd2of3Games;
import DAG.DAG;
import LinearProgram.EmptyCore;

public class Test2of3games {
	
	public static void main(String[] args) throws CloneNotSupportedException, IOException {
		
		int nbType = 30;
		
		File file = new File("test2of3games.csv");
		
	    if (!file.exists()) {
	       file.createNewFile();
		}
	    
	    String content;
	    
	    content = "nbPlayer, nbType, creationAddTime, nbNodeADD, creationDagTime, nbNodeDAG, solvingTime, emptyCore, cos\n";
	    
	    FileWriter fw = new FileWriter(file.getAbsoluteFile());
	    BufferedWriter bw = new BufferedWriter(fw);
	    
	    bw.write(content);
	    
	    for (int i=2; i<= nbType; i++) {
		
			double start1 = System.currentTimeMillis();
			ADDV1<?> add = GenerateAdd2of3Games.generateAdd2of3Games(i);
			double creationAddTime = System.currentTimeMillis() - start1;
			
			int nbNodeADD = add.getNbNodes();
			//add.writeADDinDOT("Add2of3games_"+i+".dot");
			
			double start2 = System.currentTimeMillis();
			DAG<?> dag = add.createDAG();
			double creationDagTime = System.currentTimeMillis() - start2;
			
			int nbNodeDAG = dag.getNbNodes();
			//dag.writeDAGinDOT("Dag2of3games_"+i+".dot");
			
			EmptyCore lpADD = new EmptyCore();
			lpADD.solve(dag,nbType*3);
			double solvingTime = lpADD.getSolvingTime();
			
			boolean emptyCore = true;
			double cos = lpADD.getCos();
			if ( cos < 0.001) {
				emptyCore = false;
				cos = 0;
			}
			
			content = i*3+ ", " +i+ ", " +creationAddTime+ ", " +nbNodeADD+ ", " +creationDagTime+ ", " +nbNodeDAG+ ", " +solvingTime+ ", " +emptyCore+ ", " +cos+ "\n";
			bw.write(content);
	    }
	    bw.close();
	}

}
