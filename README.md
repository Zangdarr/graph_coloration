# AEA TP3 Coloration de Graphes
Ce projet consiste à présenter les performances de différents algorithmes de coloration de graphe. L'implémentation influera grandement sur les résultats. 


## Algorithmes programmés
Pour ce projet, trois algorithmes ont été implémentés et testés : 

* [Greedy Coloration] qui est une heuristique gloutonne de coloration de graphe faite maison.
* [Welsh-Powell Coloration] qui implémente la stratégie fournie par ses deux concepteurs d'où provient son nom.
* [DSAT Coloration] qui applique l'algorithme de coloration séquentiel de Daniel Brélaz.

## Remarques sur l'implémentation
Tout au long du projet la question de l'implémentation à été un point crucial pour obtenir des performances viables tout en atteignant les résultats voulu. Chaque algorithme à ses spécificités et surtout ses "points critiques", des points sur lesquels, bien penser chaque ligne de code est déterminant sur les performances finales.
* Pour [Greedy Coloration], la recherche des couleurs des voisins d'un sommet est un point critique, car il nécessite une recherche pour chaque sommet et dépend du nombre d'arêtes du graphe. Mal considérer cette aspect de l'algorithme peut coûter cher.
* Pour [Welsh-Powell Coloration], de nombreuses zones stratégiques sont à prendre en compte : la recherche des degrés de chaque sommet, le tri par ordre décroissant et surtout la recherche de la plus grande stable qui maximise les degrés de ses sommets. Chacun de ses points influe sur les performances. L'emploie par cette algorithme d'un tri fusion ainsi que d'une fonction récursive terminal pour la recherche de stable joue en la faveur de ses performances.
* Pour [DSAT Coloration], même chose qu'avec WP-Coloration pour les degrés de chaque sommet trié, mais ensuite il est purement question de quelles informations conservées sous la main pour optimiser les calculs. Le choix de garder les voisins de chaque sommet dans une Map, ainsi que de conservé la couleur de chacun des sommets dans une autre map permet de rapidement accéder aux informations pertinentes et de les mettres à jours. De plus un petit "trick" mathématique pour déterminer quelle est la plus petite couleur disponible pour un sommet donné permet d'améliorer les performance.

## Performances
Ci dessous des triplets d'executions sur différents graphes :

* Pour un graphe de 1000 sommets avec 0.7 pour les arêtes : 

        [[  PERFORMANCES  ]] 
        
        ---  EXECUTION TIME  ---
         - Greedy       coloration time : 2.017330907
         - Welsh-Powell coloration time : 15.696715586
         - DSAT         coloration time : 2.815089443
        
        ---  Coloration  ---
         - Greedy       colors quantity : 192
         - Welsh-Powell colors quantity : 343
         - DSAT         colors quantity : 18

* Pour un graphe de 1000 sommets avec 1.0 pour les arêtes donc complet :
        [[  PERFORMANCES  ]] 
        
        ---  EXECUTION TIME  ---
         - Greedy       coloration time : 2.581101473
         - Welsh-Powell coloration time : 527.697868899
         - DSAT         coloration time : 7.807521359
        
        ---  Coloration  ---
         - Greedy       colors quantity : 1000
         - Welsh-Powell colors quantity : 1000
         - DSAT         colors quantity : 1000

* Pour un graphe de 100 sommets avec 0.7 pour les arêtes :
        [[  PERFORMANCES  ]] 
        
        ---  EXECUTION TIME  ---
         - Greedy       coloration time : 0.022718327
         - Welsh-Powell coloration time : 0.02811011
         - DSAT         coloration time : 0.02680374
        
        ---  Coloration  ---
         - Greedy       colors quantity : 31
         - Welsh-Powell colors quantity : 41
         - DSAT         colors quantity : 10
* Pour un graphe de 100 sommets avec 1.0 pour les arêtes donc complet :
        [[  PERFORMANCES  ]] 
        
        ---  EXECUTION TIME  ---
         - Greedy       coloration time : 0.034545133
         - Welsh-Powell coloration time : 0.074670529
         - DSAT         coloration time : 0.039627471
        
        ---  Coloration  ---
         - Greedy       colors quantity : 100
         - Welsh-Powell colors quantity : 100
         - DSAT         colors quantity : 100

* Pour un graphe de 10 sommets avec 0.7 pour les arêtes :
        [[  PERFORMANCES  ]] 
        
        ---  EXECUTION TIME  ---
         - Greedy       coloration time : 9.67886E-4
         - Welsh-Powell coloration time : 0.001055647
         - DSAT         coloration time : 0.001550782
        
        ---  Coloration  ---
         - Greedy       colors quantity : 6
         - Welsh-Powell colors quantity : 5
         - DSAT         colors quantity : 5

* Pour un graphe de 10 sommets avec 1.0 pour les arêtes donc complet :
        [[  PERFORMANCES  ]] 
        
        ---  EXECUTION TIME  ---
         - Greedy       coloration time : 0.001154779
         - Welsh-Powell coloration time : 0.001895747
         - DSAT         coloration time : 0.001993369
        
        ---  Coloration  ---
         - Greedy       colors quantity : 10
         - Welsh-Powell colors quantity : 10
         - DSAT         colors quantity : 10

## Remarques sur les performances
* Après analyse des résultats, on constate que les algorithme [Greedy] et [DSAT] passe aisément à l'échelle contrairement à [Welsh-Powell]. Comme dit précédemment l'implémentation influe beaucoup sur les performances donc il reste sans doute des optimisations en attente notamment au niveau de la recherche de stable.
* Les temps d'execution sont assez proche entre [DSAT] et [Greedy] grâce à leur optimisations respectives.
En terme de coloration [DSAT] est bien au dessus des deux autres ce qui en fait un algorithme de choix pour cette implémentation.



