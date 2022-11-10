# StageCG

Le code présent dans ce dépôt github a pour objectif de comparer des méthodes permettant de trouver le coeur d'un jeu de coalition, dans ce cadre on s'est intéressé ici aux 
travaux ménés dans un papier trouvable à cette adresse : https://www.researchgate.net/publication/221455616_Representation_of_coalitional_games_with_algebraic_decision_diagrams.

Nous avons implémentés :
- Une classe représentant les jeux de coalitions
- Une classe représentant un ADD (algebric decision diagramm) très simple, dans laquelle on retrouve uniquement ce dont nous avons eu besoin
- Des fonctions calculant le gain d'une coalition
- Deux programmes linéaire permettant de savoir si le coeur d'un jeu de coalition est vide. L'un pour la représentation classique d'un CG (coalition game) par sa fonction caractéristique
et l'autre pour sa représentation en ADD présentée dans le papier mentionné précedemment.
