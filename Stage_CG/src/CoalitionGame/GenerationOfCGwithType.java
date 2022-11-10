package CoalitionGame;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.TreeMap;

import Tools.Tools;

public class GenerationOfCGwithType {
	
	/**
	 * @param nbPlayer
	 * @param nameOfSkills, array of skill's name
	 * @param method, function to compute the gain of a coalition
	 * @param weight, array of weight for every different skill, the weight define the ideal number of player in a coalition
	 * @return a coalition game represented by a function nu
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public static CoalitionGame createCGandPlayer(int nbPlayer, ArrayList<String> nameOfSkills, Method method, int[] weight) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		ArrayList<Player> listPlayer = new ArrayList<Player>(nbPlayer);
		ArrayList<Type> listType = new ArrayList<Type>(nameOfSkills.size());
		Tools.generatePlayerWithType(nbPlayer, nameOfSkills, listPlayer, listType);
		TreeMap<Coalition,Double> nu = new TreeMap<Coalition,Double>();
	
		for (int p=0; p<=nbPlayer; p++) {
			Tools.createCoalitionOfSize(p,listPlayer,nu,method,listType,weight);
		}
		
		CoalitionGame game = new CoalitionGame(listPlayer,nu);
		
		return game;
	}
	
	/**
	 * @param listType , the list of different type
	 * @param listPlayer, the list of player
	 * @param method, function to compute the gain of a coalition
	 * @param weight, array of weight for every different skill, the weight define the ideal number of player in a coalition
	 * @return a coalition game represented by a function nu
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public static CoalitionGame createCGwithPlayer(ArrayList<Type> listType, ArrayList<Player> listPlayer, Method method, int[] weight) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		TreeMap<Coalition,Double> nu = new TreeMap<Coalition,Double>();
	
		for (int p=0; p<=listPlayer.size(); p++) {
			Tools.createCoalitionOfSize(p,listPlayer,nu,method,listType,weight);
		}
		
		CoalitionGame game = new CoalitionGame(listPlayer,nu);
		
		return game;
	}

}
