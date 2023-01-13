package CoalitionGame;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.TreeMap;

import Tools.Tools;

public class GenerationOfCGwithType {
	
	/**
	 * @param nbPlayer
	 * @param nameOfSkills
	 * @param method
	 * @param patronIdeal
	 * @return a CG 
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public static CoalitionGame createCGandPlayer(int nbPlayer, ArrayList<String> nameOfSkills, Method method, int[] patronIdeal) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		ArrayList<Player> listPlayer = new ArrayList<Player>(nbPlayer);
		ArrayList<Type> listType = new ArrayList<Type>(nameOfSkills.size());
		Tools.generatePlayerWithType(nbPlayer, nameOfSkills, listPlayer, listType);
		
		TreeMap<Coalition,Double> nu = Tools.createNu(listPlayer,listType,method,patronIdeal);
	
		CoalitionGame game = new CoalitionGame(listPlayer,nu);
		
		return game;
	}
	
	/**
	 * @param listType
	 * @param listPlayer
	 * @param method
	 * @param patronIdeal
	 * @return a CG
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public static CoalitionGame createCGwithPlayer(ArrayList<Type> listType, ArrayList<Player> listPlayer, Method method, int[] patronIdeal) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		TreeMap<Coalition,Double> nu = Tools.createNu(listPlayer,listType,method,patronIdeal);
		
		CoalitionGame game = new CoalitionGame(listPlayer,nu);
		
		return game;
	}

}
