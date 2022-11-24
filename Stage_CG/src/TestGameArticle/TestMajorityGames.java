package TestGameArticle;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import ADD.ADD;
import ADD.GenerateAddMajorityGames;
import DAG.DAG;
import LinearProgram.EmptyCore;

public class TestMajorityGames {
	
	public static void main(String[] args) throws CloneNotSupportedException, IOException {
		
		int n = 25;
		
		File file = new File("testMajorityGames.csv");
		
	    if (!file.exists()) {
	       file.createNewFile();
		}
	    
	    String content;
	    
	    content = "nbPlayer, nbType, creationAddTime, nbNodeADD, creationDagTime, nbNodeDAG, solvingTime, emptyCore, cos\n";
	    
	    FileWriter fw = new FileWriter(file.getAbsoluteFile());
	    BufferedWriter bw = new BufferedWriter(fw);
	    
	    bw.write(content);
	    
	    for (int i=2; i<= n; i++) {
		
    		double start1 = System.currentTimeMillis();
    		ADD<?> add = GenerateAddMajorityGames.generateAddMajorityGames(i);
    		double creationAddTime = System.currentTimeMillis() - start1;
    		
    		int nbNodeADD = add.getNbNodes();
    		//add.writeADDinDOT("AddMajorityGames.dot");
    		
    		double start2 = System.currentTimeMillis();
    		DAG<?> dag = add.createDAG();
    		double creationDagTime = System.currentTimeMillis() - start2;
    		
    		int nbNodeDAG = dag.getNbNodes();
    		//dag.writeDAGinDOT("DagMajorityGames.dot");
    		
    		EmptyCore lpADD = new EmptyCore();
    		lpADD.solve(dag,(2*i+1));
    		double solvingTime = lpADD.getSolvingTime();
			
			boolean emptyCore = true;
			double cos = lpADD.getCos();
			if ( cos < 0.001) {
				emptyCore = false;
				cos = 0;
			}
			
			content = (2*i+1)+ ", " +1+ ", " +creationAddTime+ ", " +nbNodeADD+ ", " +creationDagTime+ ", " +nbNodeDAG+ ", " +solvingTime+ ", " +emptyCore+ ", " +cos+ "\n";
			bw.write(content);
	    }
	    bw.close();
	}

}
