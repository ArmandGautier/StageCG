# Introduction

Le code présent dans ce dépôt github a pour objectif de comparer des méthodes permettant de trouver le coeur d'un jeu de coalition, dans ce cadre on s'est intéressé ici aux 
travaux menés dans un papier trouvable à cette adresse : https://www.researchgate.net/publication/221455616_Representation_of_coalitional_games_with_algebraic_decision_diagrams.

Un résumé du travail et des notions nécessaires sont disponibles sur ce lien : AJOUTER LIEN SLIDES.

Dans tout le code, la notion de type ou compétence est fondamentale, tout le code a été dévéloppé dans un contexte où nos joueurs possèdent un type.
Pour écrire et résoudre les différents algorithmes, nous avons utilisé CPLEX, https://www.ibm.com/fr-fr/analytics/cplex-optimizer.

Nous avons implémenté :
- Une classe représentant les jeux de coalition.
- Une classe représentant un ADD (algebraic decision diagram).
- Une classe représentant un DAG.
- Des fonctions calculant le gain d'une coalition.
- Deux programmes linéaires permettant de savoir si le coeur d'un jeu de coalition est vide. L'un pour la représentation classique d'un CG (coalition game) par sa fonction caractéristique et l'autre pour sa représentation en ADD présentée dans le papier précedemment mentionné.
- Un algo de PLNE permettant de savoir si il existe une structure de coalition stable.

# User guide

On peut :

## Instancier un jeu de coalition par sa fonction caractéristique et son ensemble de joueurs

`
CoalitionGame game = new CoalitionGame(listPlayer,nu);
`

Pour ce faire on a besoin des deux attributs listPlayer et nu (nom de la fonction caractéristique).

### *listPlayer*

Pour créer *listPlayer*, une liste de nbPlayer joueurs :

```
ArrayList<Player> listPlayer = new ArrayList<Player>();
int nbPlayer = 3;
for (int i=0; i<nbPlayer; i++) {  
   listPlayer.add(new Player(i));  
}  
```
Pour des joueurs qui possèdent un type :
```
ArrayList<Type> listType = ...; // voir ci dessous pour la création de listType
ArrayList<Player> listPlayer = new ArrayList<Player>();
Tools.generateListPlayer(nbPlayer, listPlayer, listType); // on génére nbPlayer joueurs qui auront un des types de listType
```

Pour créer la fonction caractéristique nu, deux options :

### *nu* Option1 
On peut, sur de petits exemples, rentrer à la main le gain des coalitions. On crée l'ensemble des coalitions puis une boucle sur celles-ci nous permet de leur donner chacune un gain.
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
### *nu* Option2
On peut aussi créer une *Méthode* qui calculera le gain d'une coalition à sa création.
```
TreeMap<Coalition,Double> nu = new TreeMap<Coalition,Double>();
for (int p=0; p<=listPlayer.size(); p++) {
    Tools.createCoalitionOfSize(p,listPlayer,nu,method,listType,patronIdeal);
}
```

Ici on procède un peu différement, on ajoute, pour chaque taille de coalition possible, directement les coalitions créées à *nu*.

De nouveaux paramètres sont également nécessaires :
#### *patronIdeal*
Le paramètre *patronIdeal* nous a servi ici à éviter des jeux dont le coeur n'est jamais vide. Son but est de décrire une _équipe_ _type_. 
```
int[] patronIdeal = new int[3];
patronIdeal[0] = 2 // Au sein d'une coalition, il ne sert à rien d'avoir plus de deux joueur du type 0
patronIdeal[1] = 3 // Au sein d'une coalition, il ne sert à rien d'avoir plus de trois joueur du type 1
patronIdeal[2] = 1 // Au sein d'une coalition, il ne sert à rien d'avoir plus d'un joueur du type 2
\\ pour le générer aléatoirement
int[] patronIdeal = Tools.generatePatronIdeal(listType,nbPlayer); \\ renvoie pour chaque type une valeur entre 1 et nbPlayer
```
On peut passer outre ce paramètre en mettant comme nombre idéal le nombre de joueurs pour chaque type.
#### *listType*
Le paramètre *listType* est une liste des types possibles pour nos joueurs, un type est défini par un numéro et un nom.
```
// création des types à partir d'une liste de compétences

ArrayList<Type> listType = new ArrayList<Type>();
ArrayList<String> nameOfSkills = new ArrayList<String>();
nameOfSkills.add("Tireur");
nameOfSkills.add("Batteur");

Tools.generateListType(nameOfSkills, listType);
```
#### *method*
Le paramètre *method* renvoie à une fonction qui calculera le gain d'une coalition en fonction de ses membres, de leur type et du patronIdeal.
```
// spécification d'une méthode
public static int method(int[] membresParType, ArrayList<Type> listType, int[] patronIdeal) {
    nbMembreDeTypei= membreParType[i] // pour retrouver le nombre de joueur de type i au sein de la coalition
    ...
    return gain
}
```
_ATTENTION_ : la liste des types n'est pas forcément triée par leur numéro.

Il est possible de réutiliser des *méthodes* déjà existantes (dans le Package *GainFunction*). Mais elles ne marcheront que pour les types pour lesquelles elles ont été créées.

Voici comment les utiliser pour i joueurs et j types (il en existe pour de 2 à 6 types)
```
ArrayList<Player> listPlayer = new ArrayList<Player>();
ArrayList<Type> listType = new ArrayList<Type>();
Tools.generatePlayerWithType(i, GetSkill.getSkill(j), listPlayer, listType);
int[] patronIdeal = Tools.generatePatronIdeal(listType,i);
CoalitionGame g = GenerationOfCGwithType.createCGwithPlayer(listType, listPlayer, GetMethod.getMethod(j), patronIdeal);
```

Vous pouvez aussi définir de nouvelles méthodes, qui devront simplement respecter la spécification présentée au dessus.
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

## Instancier un ADD qui représentera un CG

```
ADD<Player> add = GenerationOfADDwithType.createADDwithPlayer(listType, listPlayer, method, patronIdeal, ordered)
```

où *listType*, *listPlayer*, *method* et *patronIdeal* sont les paramètres comme décrits précedemment et *ordered* est un booléen qui dira si on veut trier les variables dans notre ADD ou pas.

Si *ordered* == true : on trie les joueurs par type et les types du plus nombreux au moins nombreux.
Sinon, l'ordre des joueurs est celui de *listPlayer*

Exemple :   
- Deux joueurs de type 0 : j0 et j3  
- Trois joueurs de type 1 : j1, j2 et j4  

Tri obtenu avec ordered = true :   
- Joueurs : j1, j2, j4, j0, j3  
- Type : type 1 , type 0  

On propose aussi un autre ordre dans lequel les joueurs sont triés par type et les types du moins nombreux au plus nombreux.

_ATTENTION_ pour que les ordres soient bons, il faut impérativement mettre à jour l'attribut *numberPlayerOfThisType* de la classe Type. On peut faire :
```
for (Player p : listPlayer) {
    listType.get(p.getType().getNum()).addPlayer(); 
}
```
En effet à ce moment les types sont encore triés dans leur ordre de création, donc par leur numéro, ce qui ne sera plus le cas après la création de l'ADD si on utilise un ordre.

## Créer le DAG à partir de l'ADD

Pour créer le DAG correspondant à un ADD :
```
DAG<Player> dag = add.createDAG();
```

## Utiliser les programmes linéaires sur nos CG/DAG

Finalement une fois notre DAG ou notre CG instancié, on peut lancer la résolution des programmes linéaires.

### PL sur nu

Pour savoir si le coeur d'un CG est vide : 
```
FindCore lpCg = new FindCore(game); \\ où game est une instance de la classe CoalitionGame
lpCg.linearProgramforCG(new StructureOfCoalition(new Coalition(listPlayer)));
```
Le coeur n'est pas vide si :
```
lpCg.isSolved() == true;
```
On peut alors afficher un vecteur de pay-off qui appartient au coeur en faisant :
```
lpCg.print_results();
```

### PL sur DAG

Pour savoir si le coeur d'un CG représenté par un DAG est vide : 
```
EmptyCore lpDAG = new EmptyCore();
lpDAG.solve(dag,i); \\ i est le nombre de joueurs jouant dans le jeux
```
Le coeur est vide si le COS (cost of stability) n'est pas nul, le COS représente ce qu'on devrait rajouter au gain de la grande coalition pour que le coeur ne soit pas vide:
```
lpDAG.getCos() > 0.00001 \\ il arrive que le PL renvoie une valeur extrêmement petite au lieu de 0 c'est pour ça qu'on ne fait pas > 0 directement.
```
Si le coeur n'est pas vide on peut alors afficher un vecteur de pay-off qui appartient au coeur en faisant :
```
lpDAG.print_results();
```
## Algo de PLNE pour le C-core
On peut également lancer un algo de PLNE qui nous dira si le C-coeur est vide, on peut le faire aussi bien sur l'ADD que sur le DAG et on a besoin d'avoir lancé dans un premier temps le programme linéaire pour savoir si le coeur est vide.
```
findStableSC sCAdd = new findStableSC();
findStableSC sCDag = new findStableSC();
sCAdd.solve(add, i, sol); // sol est un vecteur de pay-off coalitionnelement rationnel calculé par le PL précédent
sCDag.solve(dag, i, sol); // sol est un vecteur de pay-off coalitionnelement rationnel calculé par le PL précédent
```
Ce programme de PLNE peut être très long à trouver une solution, nous avons donc choisi d'imposer un temps max d'éxécution à CPLEX de 8 minutes. Ce paramètre peut être changé dans le fichier *findStableSC* du package *LinearProgram* au début des fonctions *solve*.
```
cplex.setParam(IloCplex.Param.TimeLimit, time); \\ où time est le temps d'éxécution maximal autorisé
```

Une fois le programme terminé, si il existe une solution :
```
if (sCAdd.isSolved());
```
On peut reconstruire les coalitions formant notre structure de coalition grâce aux variables *Eud* et *Eug*.
Vous trouverez un exemple dans le fichier *Test8* du package *Example*

