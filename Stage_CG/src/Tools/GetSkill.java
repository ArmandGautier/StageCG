package Tools;

import java.util.ArrayList;

public class GetSkill {
	
	public static ArrayList<String> getSkill(int nbType) {
		switch (nbType) {
		case 2 : return getTwoSkillsTB();
		case 3 : return getThreeSkillsTBT();
		case 4 : return getFourSkillsTBTR();
		case 5 : return getFiveSkillsTBTRP();
		case 6 : return getSixSkillsTBRwithXP();
		default : System.out.println("Nbplayer non compatible");
		break;
		}
		return null;
	}

	public static ArrayList<String> getTwoSkillsTB() {
		ArrayList<String> res = new ArrayList<String>();
		res.add("Tireur");
		res.add("Batteur");
		return res;
	}
	
	public static ArrayList<String> getTwoSkillsCP() {
		ArrayList<String> res = new ArrayList<String>();
		res.add("Chasseur");
		res.add("Pecheur");
		return res;
	}
	
	public static ArrayList<String> getThreeSkillsTBR() {
		ArrayList<String> res = new ArrayList<String>();
		res.add("Tireur");
		res.add("Batteur");
		res.add("Revendeur");
		return res;
	}
	
	public static ArrayList<String> getThreeSkillsTBT() {
		ArrayList<String> res = new ArrayList<String>();
		res.add("Tireur");
		res.add("Batteur");
		res.add("Trappeur");
		return res;
	}
	
	public static ArrayList<String> getFourSkillsTBTR() {
		ArrayList<String> res = new ArrayList<String>();
		res.add("Tireur");
		res.add("Batteur");
		res.add("Revendeur");
		res.add("Trappeur");
		return res;
	}
	
	public static ArrayList<String> getFourSkillsTBPR() {
		ArrayList<String> res = new ArrayList<String>();
		res.add("Tireur");
		res.add("Batteur");
		res.add("Revendeur");
		res.add("Pisteur");
		return res;
	}
	
	public static ArrayList<String> getFiveSkillsTBTRP() {
		ArrayList<String> res = new ArrayList<String>();
		res.add("Tireur");
		res.add("Batteur");
		res.add("Revendeur");
		res.add("Trappeur");
		res.add("Pecheur");
		return res;
	}
	
	public static ArrayList<String> getSixSkillsTBRwithXP() {
		ArrayList<String> res = new ArrayList<String>();
		res.add("Tireur");
		res.add("Batteur");
		res.add("Revendeur");
		res.add("TireurNoXP");
		res.add("BatteurNoXP");
		res.add("RevendeurNoXP");
		return res;
	}
}
