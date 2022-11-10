package Example;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import ADD.ADD;
import ADD.GenerationOfADDwithType;
import CoalitionGame.Coalition;
import CoalitionGame.CoalitionGame;
import CoalitionGame.GenerationOfCGwithType;
import CoalitionGame.Player;
import CoalitionGame.StructureOfCoalition;
import CoalitionGame.Type;
import LinearProgram.EmptyCore;
import LinearProgram.FindCore;
import Tools.GetMethod;
import Tools.GetSkill;
import Tools.Tools;

public class Test2 {

	public static void main(String[] args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, IOException {
		
		int nbPlayer = 26;
		int limNbPlayer = 41;
		int nbGame = 50;
		int nbType = 2;
		int limNbType = 7;
		ArrayList<Player> listPlayer = new ArrayList<Player>();
		ArrayList<Type> listType = new ArrayList<Type>();
		
		int indiceInfoGame = 0;
		
		File file = new File("testMoy.csv");
		
	    if (!file.exists()) {
	       file.createNewFile();
		}
	    
	    String content;
	    
	    content = "nbPlayer, nbType, MoytimeAddO, MoytimeConstructAdd, MoynbNodeAddO, nbEmptyCore\n";
		  
	    FileWriter fw = new FileWriter(file.getAbsoluteFile());
	    BufferedWriter bw = new BufferedWriter(fw);
	    
	    bw.write(content);
		
		for (int i=nbPlayer; i<limNbPlayer; i++) {
			System.out.println("nbjoueur : "+i);
			for ( int j=nbType; j<limNbType; j++) {
				System.out.println("nbtype : "+j);
			    
				File file2 = new File("testInfoGame"+indiceInfoGame+".csv");
				
			    if (!file2.exists()) {
			       file2.createNewFile();
				}
				  
			    FileWriter fw2 = new FileWriter(file2.getAbsoluteFile());
			    BufferedWriter bw2 = new BufferedWriter(fw2);
			    
			    content = "nbPlayer, nbType, timeAddO, timeConstructAdd, nbNodeAddO, isEmptyCore\n";
			    
			    //double MoytimeNu = 0;
			    double MoytimeAddO = 0;
			    //double MoytimeAddNO = 0;
			    double MoynbNodeAddO = 0;
			    //double MoynbNodeAddNO = 0;
			    int nbEmptyCore = 0;
			    double MoyConstructTime = 0;
			    
			    bw2.write(content);
			    
			    for (int k=0; k<nbGame; k++) {
			    	System.out.println("game : "+k);
			    	
					Tools.generatePlayerWithType(i, GetSkill.getSkill(j), listPlayer, listType);
					
					int[] weight = Tools.generateRandomWeight(listType,i);
					
					//CoalitionGame g = GenerationOfCGwithType.createCGwithPlayer(listType, listPlayer, GetMethod.getMethod(j), weight);
					//ADD<Player> addbig = GenerationOfADDwithType.createADDwithPlayer(listType, listPlayer, GetMethod.getMethod(j), weight, false);
					double time = System.currentTimeMillis();
					ADD<Player> add = GenerationOfADDwithType.createADDwithPlayer(listType, listPlayer, GetMethod.getMethod(j), weight, true);
					double constructTime = System.currentTimeMillis()-time;
					/*
					add.writeADDinDOT("dot.dot");
					addbig.writeADDinDOT("bigdot.dot");
					*/
					
				    //double timeNu;
				    double timeAddO;
				    //double timeAddNO;
				    double nbNodeAddO;
				    //double nbNodeAddNO;
				    boolean isEmpty = false;
				    
					/*FindCore lpCG = new FindCore(g);
					lpCG.linearProgramforCG(new StructureOfCoalition(new Coalition(listPlayer)));
					timeNu = lpCG.getSolvingTime();*/
					
					EmptyCore lpADD = new EmptyCore();
					lpADD.solve(add,i);
					timeAddO=lpADD.getSolvingTime();
					nbNodeAddO=add.getNbNodes();
					
					/*EmptyCore lpADDbig = new EmptyCore();
					lpADDbig.solve(addbig,i);
					timeAddNO=lpADDbig.getSolvingTime();
					nbNodeAddNO=addbig.getNbNodes();*/
					
				    //MoytimeNu += timeNu;
				    MoytimeAddO += timeAddO;
				    //MoytimeAddNO += timeAddNO;
				    MoynbNodeAddO += nbNodeAddO;
				    //MoynbNodeAddNO += nbNodeAddNO;
				    MoyConstructTime += constructTime;
				    
				    if ( lpADD.getCos() > 0.0000000001 ) {
				    	isEmpty = true;
			    	}
				    
				    if ( ! isEmpty ) {
				    	nbEmptyCore++;
				    }
					
					/*
					lpCG.print_results();
					lpADD.print_results();
					lpADDbig.print_results();
					*/
					
					listType.clear();
					listPlayer.clear();
					
					content = i+", "+j+", "+timeAddO+", "+constructTime+", "+nbNodeAddO+", "+isEmpty+"\n";
					bw2.write(content);
			    }
			    
			    bw2.close();
			    indiceInfoGame++;
			    content = i+", "+j+", "+MoytimeAddO/nbGame+", "+MoyConstructTime+", "+MoynbNodeAddO/nbGame+", "+nbEmptyCore+"\n";
			    bw.write(content);
			}
		}
		
		bw.close();

	}

}
