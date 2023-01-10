package Example;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import ADD.ADD;
import ADD.GenerationOfADDwithType;
import CoalitionGame.Player;
import CoalitionGame.Type;
import DAG.DAG;
import LinearProgram.EmptyCore;
import Tools.GetMethod;
import Tools.GetSkill;
import Tools.Tools;

public class Test6 {
	
	// on pousse le nombre de joueur avec la méthode de construction d'ADD la plus efficace

	@SuppressWarnings({ "rawtypes" })
	public static void main(String[] args) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, CloneNotSupportedException, IOException {
		
		int nbPlayer = 30;
		ArrayList<Player> listPlayer = new ArrayList<Player>();
		ArrayList<Type> listType = new ArrayList<Type>();
		
		System.out.println("Go");
		
		int nbGame = 200;
		int nbType = 6;
		
		int indiceInfoGame = 0;
		
		File file = new File("testMoy.csv");
		
	    if (!file.exists()) {
	       file.createNewFile();
		}
	    
	    String content;
	    
	    content = "nbPlayer, nbType, timeCreateADD, timeCreateDAG, nbNodeADD, nbNodeDAG, timeSolveDAG\n";
		  
	    FileWriter fw = new FileWriter(file.getAbsoluteFile());
	    BufferedWriter bw = new BufferedWriter(fw);
	    
	    bw.write(content);
		
		for (int i=5; i<=nbPlayer; i++) {
			for ( int j=2; j<=nbType; j++) {
				
				System.out.println("nbPlayer = "+i+" et Nbtype = "+j);
			    
				File file2 = new File("testInfoGame"+indiceInfoGame+".csv");
				
			    if (!file2.exists()) {
			       file2.createNewFile();
				}
				  
			    FileWriter fw2 = new FileWriter(file2.getAbsoluteFile());
			    BufferedWriter bw2 = new BufferedWriter(fw2);
			    
			    content = "nbPlayer, nbType, timeCreateADD, timeCreateDAG, nbNodeADD, nbNodeDAG, timeSolveDAG, emptyCore, cosDAG\n";
			    
			    bw2.write(content);
			    
			    double moytimeAdd = 0;
			    double moytimeDAG = 0;
			    double moynbNodeAdd = 0;
			    double moynbNodeDAG = 0;
			    double moySolveTimeDAG = 0;
			    
			    for (int k=0; k<nbGame; k++) {
				
					Tools.generatePlayerWithType(i, GetSkill.getSkill(j), listPlayer, listType);
					
					int[] patronIdeal = Tools.generateRandomWeight(listType,i);
					
					Method m = GetMethod.getMethod(j);
					
				    double timeAdd;
				    double timeDAG;
				    double nbNodeAdd;
				    double nbNodeDAG;
				    double solveTimeDAG = 0;
					
					double start2 = System.currentTimeMillis();
					ADD add = GenerationOfADDwithType.createADDwithPlayer(listType, listPlayer, m, patronIdeal, true);
					timeAdd=System.currentTimeMillis()-start2;
					//add.writeADDinDOT("fileBis.DOT");
					
					double start3 = System.currentTimeMillis();
					DAG dag = add.createDAG();
					timeDAG=System.currentTimeMillis()-start3;
					//dag.writeDAGinDOT("dag.DOT");
					
				    nbNodeAdd=add.getNbNodes();
				    nbNodeDAG=dag.getNbNodes();
					
					EmptyCore lpDAG = new EmptyCore();
					lpDAG.solve(dag,i);
					
				    solveTimeDAG = lpDAG.getSolvingTime();
				    
					double cosDag = lpDAG.getCos();
					boolean emptyCore = true;
					
					if (cosDag < 0.0001) {
						emptyCore = false;
					}
					
					listType.clear();
					listPlayer.clear();
					
					content =  "" + i + ", " + j + ", " + timeAdd + ", " + timeDAG + ", " + nbNodeAdd + ", " + nbNodeDAG + ", " + solveTimeDAG + ", " + emptyCore + ", " + cosDag + "\n";
					bw2.write(content);
					
				    moytimeAdd += timeAdd;
				    moytimeDAG += timeDAG;
				    moynbNodeAdd += nbNodeAdd;
				    moynbNodeDAG += nbNodeDAG;
				    moySolveTimeDAG += solveTimeDAG;
					
			    }
			    
			    bw2.close();
			    indiceInfoGame++;
			    content =  "" + i + ", " + j + ", " + moytimeAdd/nbGame + ", " + moytimeDAG/nbGame + ", " + moynbNodeAdd/nbGame + ", " + moynbNodeDAG/nbGame + ", " + moySolveTimeDAG/nbGame + "\n";
			    bw.write(content);
			}
		}
		
		bw.close();
	}

}
