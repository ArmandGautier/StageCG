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

public class Test3 {

	public static void main(String[] args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, IOException {
		
		System.out.println("Go");
		
		int nbPlayer = 11;
		int limNbPlayer = 21;
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
	    
	    content = "nbPlayer, nbType, MoytimeNu, MoytimeAddO, MoytimeAddnO, MoytimeAddOO, MoynbNodeAddO, MoynbNodeAddnO, MoynbNodeAddOO, MoytimeConstructNu, MoytimeConstructAddO, MoytimeConstructAddNO, MoytimeConstructAddOO\n";
		  
	    FileWriter fw = new FileWriter(file.getAbsoluteFile());
	    BufferedWriter bw = new BufferedWriter(fw);
	    
	    bw.write(content);
		
		for (int i=nbPlayer; i<limNbPlayer; i++) {
			for ( int j=nbType; j<limNbType; j++) {
			    
				File file2 = new File("testInfoGame"+indiceInfoGame+".csv");
				
			    if (!file2.exists()) {
			       file2.createNewFile();
				}
				  
			    FileWriter fw2 = new FileWriter(file2.getAbsoluteFile());
			    BufferedWriter bw2 = new BufferedWriter(fw2);
			    
			    content = "nbPlayer, nbType, timeNu, timeAddO, timeAddNO, timeAddOO, nbNodeAddO, nbNodeAddNO, nbNodeAddOO, temsConstructNu, tempsConstructAddO, tempsConstructAddNO, tempsConstructAddOO\n";
			    
			    double MoytimeNu = 0;
			    double MoytimeAddO = 0;
			    double MoytimeAddNO = 0;
			    double MoytimeAddOO = 0;
			    double MoynbNodeAddO = 0;
			    double MoynbNodeAddNO = 0;
			    double MoynbNodeAddOO = 0;
			    double MoytimeConstructNu = 0;
			    double MoytimeConstructAddO = 0;
			    double MoytimeConstructAddNO = 0;
			    double MoytimeConstructAddOO = 0;
			    
			    bw2.write(content);
			    
			    for (int k=0; k<nbGame; k++) {
				
					Tools.generatePlayerWithType(i, GetSkill.getSkill(j), listPlayer, listType);
					
					int[] weight = Tools.generateRandomWeight(listType,i);
					
					double time1 = 0;
					double time2 = 0;
					double time3 = 0;
					double time4 = 0;
					double time;
					
					time = System.currentTimeMillis();
					CoalitionGame g = GenerationOfCGwithType.createCGwithPlayer(listType, listPlayer, GetMethod.getMethod(j), weight);
					time1 = System.currentTimeMillis() - time;
					time = System.currentTimeMillis();
					ADD<Player> addbig = GenerationOfADDwithType.createADDwithPlayer(listType, listPlayer, GetMethod.getMethod(j), weight, false);
					time2 = System.currentTimeMillis() - time;
					time = System.currentTimeMillis();
					ADD<Player> add = GenerationOfADDwithType.createADDwithPlayer(listType, listPlayer, GetMethod.getMethod(j), weight, true);
					time3 = System.currentTimeMillis() - time;
					
					time = System.currentTimeMillis();
					ADD<Player> addOO = GenerationOfADDwithType.createADDwithPlayerOtherOrder(listType, listPlayer, GetMethod.getMethod(j), weight, true);
					time4 = System.currentTimeMillis() - time;
					
					/*
					add.writeADDinDOT("dot.dot");
					addbig.writeADDinDOT("bigdot.dot");
					*/
					
				    double timeNu;
				    double timeAddO;
				    double timeAddNO;
				    double timeAddOO;
				    double nbNodeAddO;
				    double nbNodeAddNO;
				    double nbNodeAddOO;
				    
					FindCore lpCG = new FindCore(g);
					lpCG.linearProgramforCG(new StructureOfCoalition(new Coalition(listPlayer)));
					timeNu = lpCG.getSolvingTime();
					
					EmptyCore lpADD = new EmptyCore();
					lpADD.solve(add,i);
					timeAddO=lpADD.getSolvingTime();
					nbNodeAddO=add.getNbNodes();
					
					EmptyCore lpADDbig = new EmptyCore();
					lpADDbig.solve(addbig,i);
					timeAddNO=lpADDbig.getSolvingTime();
					nbNodeAddNO=addbig.getNbNodes();
					
					EmptyCore lpADDOO = new EmptyCore();
					lpADDOO.solve(addOO,i);
					timeAddOO=lpADDOO.getSolvingTime();
					nbNodeAddOO=addOO.getNbNodes();
					
				    MoytimeNu += timeNu;
				    MoytimeAddO += timeAddO;
				    MoytimeAddNO += timeAddNO;
				    MoytimeAddOO += timeAddOO;
				    MoynbNodeAddO += nbNodeAddO;
				    MoynbNodeAddNO += nbNodeAddNO;
				    MoynbNodeAddOO += nbNodeAddOO;
				    MoytimeConstructNu += time1;
				    MoytimeConstructAddO += time3;
				    MoytimeConstructAddNO += time2;
				    MoytimeConstructAddOO += time4;
					
					/*
					lpCG.print_results();
					lpADD.print_results();
					lpADDbig.print_results();
					*/
					
					listType.clear();
					listPlayer.clear();
					
					content = i+", "+j+", "+timeNu+", "+timeAddO+", "+timeAddNO+", "+timeAddOO+", "+nbNodeAddO+", "+nbNodeAddNO+", "+nbNodeAddOO+", "+time1+", "+time3+", "+time2+", "+time4+"\n";
					bw2.write(content);
			    }
			    
			    bw2.close();
			    indiceInfoGame++;
			    content = i+", "+j+", "+MoytimeNu/nbGame+", "+MoytimeAddO/nbGame+", "+MoytimeAddNO/nbGame+", "+MoytimeAddOO/nbGame+", "+MoynbNodeAddO/nbGame+", "+MoynbNodeAddNO/nbGame+", "+MoynbNodeAddOO/nbGame+", "+MoytimeConstructNu+", "+MoytimeConstructAddO+", "+MoytimeConstructAddNO+", "+MoytimeConstructAddOO+"\n";
			    bw.write(content);
			}
		}
		
		bw.close();

	}

}
