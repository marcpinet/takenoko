# Rapport / Knowledge Exchange - Takenoko, team A

* Point d'avancement

- Résumé des fonctionnalités :
L'ensemble des fonctionnalités du jeu ont été traitées, et ce en respesctant les règles. La seule attente de base qui n'a pas été satisfaite à 100% est celle des bots. Nous avons un bot fauible, ainsi qu'un PlotRushBot //explicaitons
- Fonctionnalité supplémentaire 1 : les logs  et la démo de 2000 parties
Pour cette fonctionnalité, nous avons crées une classe Simulator qui a pour responsabilité de
- Fonctionnalité supplémentaire 2 : le CSV
//TODO
- Fonctionnalité supplémentaire 3 : Bot spécifique
Puisque le travail demandé pour cette fonctionnalité était conséquent, nous avons choisi de découpé le travail sur ce bot en 5 issues

* Architecture et qualité

- Architecture et choix
Nous avons décidé de diviser nos classes java dans 4 gros packages : action, utils, player et game. Ces packages contiennent eux-mêmes des sous-packages (par exemple, on peut trouver les packages tile, objective et board dans le package game). Le découpage en package nous paraissait indispensable pour entretenir une architecture claire et logique. Concernant le choix de répartition des classes, il s'est fait naturellement en fonction des relations entre les classes, ainsi que de leur responsabilité. Pour appuyer ces propos, prenons un exemple clair : celui des deux classes Inventory. Il peut paraître surprenant de trouver les classes VisibleInventory et PrivateInventory dans deux packages totalement différents, puisque l'inventaire visible est dans le package board, tandis que l'inventaire privé est dans le package player. En effet, bien qu'un joueur possède les deux types d'inventaire, seul le privé se trouve dans son package, car il est plus logique de mettre l'inventaire visible avec le board puisque c'est le board qui a la responsabilité d'afficher l'inventaire visible d'un joueur aux autres joueurs (concrètement, dans une partie "physique", cela se traduirait par la disposition des irrigations, aménagements et objectifs dévoilés au bord du plateau).
//TODO : parler de nos choix de conception en utilisant des éléments plus techniques.
- Où trouver les infos?
A la racine de notre projet se trouvent deux autres fichiers au format Markdown donnant des informations sur le projet : le fichier README explique les règles du jeu, la récupération et l'utilisation du projet, ainsi que la licence utilisée, tandis que le fichier CONTRIBUTING présente la branching stratégy utilisée lors de notre projet, ainsi que la façon d'y contribuer.
- Etat de la base du code
//TODO : parler des parties de code bien faites, des parties de code pas bien faite et de la sortie SONAR (j'ai pas très bien compris ce point là)

* Processus

- Répartition des tâches
Régulièrement, l'équipe dressait ensemble une liste des objectifs à atteindre, puis chacun avait le choix de l'issue qui lui convenait le mieux. Cela aura eu comme résultat global de faire travailler l'ensemble du groupe
sur beaucoup de points différents. Par exemple, Edgar a tout d'abord mis en place maven au sein de notre projet, puis Loïc a commencé à travailler sur le code en implémentant un système de coordonnées, et les tuiles correspondantes. Edgar et Clément ont utilisé ce travail pour implémenter les premiers objectifs de pattern de tuile, tandis que Marc travaillait sur le bot facile. Bien entendu, il est arrivé que plusieurs membres du groupe travaillent sur le même problème ou la même issue en cas de besoin.
//TODO : completer/gt
- Notre utilisation de git
Lorsque l'un d'entre nous travaillait sur une issue, il effectuait d'abord un premier commit (représentant le début de son travail). Suite à cela, il créait une Pull Request correspondant à son issue, puis il continuait à faire des commits jusqu'à ce que le travail de l'issue soit complètement fini. Cependant, rien n'était définitif, car il fallait que l'un des autres membres du groupe passe en revue les changements et, si des problèmes étaient détectés alors ils étaient remontés au responsable de la PR, qui effectuait les modifications. Lorsqu'une telle situation se produisait, une autre revue était nécessaire avant de pouvoir merge.
- Notre branching strategy
Nous avons choisi d'adopter la stratégie "Github Flow" et ce depuis le début du projet, pour différentes raisons :
Tout d'abord, nous avons constaté que la stratégie "Git Flow" demandait de créait trop de branches, ce qui ne nous convenait pas
De plus, nous souhaitions que la version fonctionnelle de notre code soit sur la branche main, ce qui éliminait la stratégie "Gitlab Flow".
Nous en sommes donc arrivé à la conclusion que "Github Flow" était la stratégie qui nous convenait le plus.
