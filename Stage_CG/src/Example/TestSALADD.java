package Example;


import compilateur.SALADD;

public class TestSALADD {

	public static void main(String[] args) {
		
		SALADD s=new SALADD();
		
		//nomFichier.xml; nature additive; heuristique numero 3; heuristique de contraintes numero 2; affichage de texte niveau 1 sur 3
		s.compilation("alarm.xml", false, 3, 2, 1);
		s.save("sauvegarde.dot");
		s.suppressionNoeudsBegayants();
		s.save("sauvegardeBis.dot");

	}

}
