# Introduction

Le code présent dans ce dépôt github a pour objectif de comparer des méthodes permettant de trouver le coeur d'un jeu de coalition, dans ce cadre on s'est intéressé ici aux 
travaux ménés dans un papier trouvable à cette adresse : https://www.researchgate.net/publication/221455616_Representation_of_coalitional_games_with_algebraic_decision_diagrams.

Dans tous le code, la notion de type ou compétence est fondamentale, tout le code a été dévéloppé dans un contexte où nos joueurs possèdent un type.

Nous avons implémentés :
- Une classe représentant les jeux de coalitions.
- Une classe représentant un ADD (algebric decision diagramm).
- Des fonctions calculant le gain d'une coalition.
- Deux programmes linéaire permettant de savoir si le coeur d'un jeu de coalition est vide. L'un pour la représentation classique d'un CG (coalition game) par sa fonction caractéristique et l'autre pour sa représentation en ADD présentée dans le papier précedemment mentionné.
- Un algo de PLNE permettant de savoir si il existe une structure de coalition stable.

# User guide

On peut :

## Instancier un jeu de coalition par sa fonction caractéristique et son ensemble de joueur

`
CoalitionGame game = new CoalitionGame(listPlayer,nu);
`

Pour ce faire on a besoin des deux attributs listPlayer et nu (nom de la fonction caractéristique).

Pour créer listPlayer, une liste de nbPlayer joueurs :

```
ArrayList<Player> listPlayer = new ArrayList<Player>();
int nbPlayer = 3;
for (int i=0; i<nbPlayer; i++) {  
   listPlayer.add(new Player(i));  
}  
```

Pour créer nu deux options :

### Option1 
On peut, sur de petit exemple, rentré à la main le gain des coalitions. On doit d'abord créer l'ensemble des coalitions puis une boucle sur celles-ci nous permet de leur donner chacune un gain.
```		
TreeMap<Coalition,Double> nu = new TreeMap<Coalition,Double>();
TreeSet<Coalition> listCoalition = new TreeSet<Coalition>();
for (int i=0; i<=nbPlayer; i++) {
    listCoalition.addAll(Tools.createCoalitionOfSize(i,listPlayer));
}
Scanner scan = new Scanner(System.in);
for (Coalition c : listCoalition) {
			System.out.println(c.toString());
      System.out.println("What is the gain for this coalition ? Please enter a number: ");
		  double number = scan.nextInt();
			nu.put(c,number);
}
scan.close();
```
### Option2
On peut aussi créer une *Méthode* qui calculera le gain d'une coalition à sa création.
```
TreeMap<Coalition,Double> nu = new TreeMap<Coalition,Double>();
for (int p=0; p<=listPlayer.size(); p++) {
    Tools.createCoalitionOfSize(p,listPlayer,nu,method,listType,patronIdeal);
}
```

Ici on procède un peu différement, on ajoute, pour chaque taille de coalition possible directement les coalitions créées à *nu*.

De nouveaux paramètres sont également nécessaire :
####
Le paramètres *patronIdeal* nous a servi ici a évité des jeux dont le coeur n'est jamais vide. Son but est de décrire une _équipe_ _type_. 
```
int[] patronIdeal = new int[3];
patronIdeal[0] = 2 // Au sein d'une coalition, il ne sert à rien d'avoir plus de deux joueur du type 0
patronIdeal[1] = 3 // Au sein d'une coalition, il ne sert à rien d'avoir plus de trois joueur du type 1
patronIdeal[2] = 1 // Au sein d'une coalition, il ne sert à rien d'avoir plus d'un joueur du type 2
```
On peut passer outre ce paramètres en mettant comme nombre idéal le nombre de joueur pour chaque type.
####
Le paramètre *method* renvoi à une fonction qui calculera le gain d'une coalition en fonction de ses membres, de leur type et du patronIdeal.
```
// spécification d'un méthode
public static int method(int[] membresParType, ArrayList<Type> listType, int[] patronIdeal) {
    nbMembreDeTypei= membreParType[i] // pour retrouver le nombre de joueur de type i au sein de la coalition
    ...
    return gain
}
```
_ATTENTION_ : la liste des types n'est pas forcément trié par leur numéro.
####
Enfin le paramètres listType est une liste des types possibles pour nos joueurs, un type est défini par un numéro et un nom.
```
// création des types à partir d'une liste de compétences

ArrayList<Type> listType = new ArrayList<Type>();
ArrayList<String> nameOfSkills = new ArrayList<String>();
nameOfSkills.add("Tireur");
nameOfSkills.add("Batteur");

int i=0;
for ( String name : nameOfSkills) {
    listType.add(new Type(i,name));
    i++;
}
```

Il est possible de réutiliser des *méthodes* déjà existante (dans le Package *GainFunction*). Mais elle ne marcheront que pour les types pour lesquelles elles ont été créées.

Voici comment les utiliser pour i joueur et j types (il en existe pour de 2 à 6 types)
```
ArrayList<Player> listPlayer = new ArrayList<Player>();
ArrayList<Type> listType = new ArrayList<Type>();
Tools.generatePlayerWithType(i, GetSkill.getSkill(j), listPlayer, listType);
int[] patronIdeal = Tools.generatePatronIdeal(listType,i);
CoalitionGame g = GenerationOfCGwithType.createCGwithPlayer(listType, listPlayer, GetMethod.getMethod(j), patronIdeal);
```

Vous pouvez aussi définir de nouvelles méthodes, qui devront simplement respecter la spécification présenter au dessus.
```
ArrayList<Player> listPlayer = ...;
ArrayList<Type> listType = ...;
int[] patronIdeal = ...;
// creation de la méthode
Class[] parameterTypes = new Class[3];
parameterTypes[0] = int[].class;
parameterTypes[1] = ArrayList.class;
parameterTypes[2] = int[].class;
Method method = *nomClasse*.getMethod("*nomMéthode*",parameterTypes);
CoalitionGame g = GenerationOfCGwithType.createCGwithPlayer(listType, listPlayer, method, patronIdeal);
```
où *nomClasse* désigne la classe dans laquelle vous définissez la méthode et *nomMéthode* son nom.
