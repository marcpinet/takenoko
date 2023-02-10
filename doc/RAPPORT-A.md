# Rapport / Knowledge Exchange - Takenoko, team A

Edgar BIZEL, Loïc PALAYER, Marc PINET, Clément REMY

## 1. Point d'avancement

### Résumé des fonctionnalités

L'ensemble des fonctionnalités du jeu ont été traitées, et ce, en respectant les règles.

La seule attente de base qui n'a pas été satisfaite à 100% est celle des bots.

Nous disposons d'un `RandomBot` qui, comme son nom l'indique, effectuera des actions aléatoires.

Ensuite, le `PlotRushBot` fait en sorte d'avoir toujours un maximum d'objectifs dans sa main (de type `TilePattern`) et choisira l'action qui lui sera la plus bénéfique pour l'avancement de ses objectifs.

Pour cela, il effectue des simulations : il teste toutes les actions de placement de tuile possibles, et sélectionne celle qui lui sera le plus bénéfique.
Dans le cas où aucune action n'est utile, il prendra une irrigation et, si cela est utile, la placera.
Si plus aucune irrigation n'est disponible, l'algorithme se rabattra sur une action aléatoire.

Enfin, le `SaboteurBot`, que nous détaillons plus loin.

### Fonctionnalité supplémentaire 1 : les logs et les statistiques sur 2000 parties

Nous utilisions depuis longtemps `java.util.logging`. Nous avons simplement eu à les rendre plus esthétiques pour la démonstration.

Pour le lancement de multiples parties, nous avons créé une classe `Simulator` qui a pour responsabilité de lancer un nombre défini de parties, et de renvoyer les résultats bruts.
Cette classe utilise d'ailleurs le *multithreading* afin de renvoyer les résultats bien plus rapidement : ce problème étant hautement parallélisable, le temps d'exécution est approximativement divisé par le nombre de cœurs du processeur.

Ces résultats sont ensuite traités et affichés.

Pour *parser* les arguments, nous avons comme demandé utilisé la bibliothèque JCommander.

Nous avons créé une classe `Args`, dans laquelle nous avons défini plusieurs paramètres, dont `--2thousands`, paramètre qui correspond donc au lancement du simulateur.

### Fonctionnalité supplémentaire 2 : le CSV

Nous disposions déjà des statistiques grâce à la première fonctionnalité. Nous n'avions plus qu'à les récupérer et les écrire via `OpenCSV`.

Nous avons donc écrit `CSVHandler`, qui se charge de l'écriture du CSV. Si jamais celui-ci existe déjà, elle fusionne les résultats existants avec les nouveaux.

### Fonctionnalité supplémentaire 3 : `SaboteurBot`

Puisque le travail demandé pour cette fonctionnalité était conséquent, nous avons choisi de découper le travail sur ce bot en 5 issues. Nous avons réussi à en faire 4 sur les 5. La fonctionnalité de surveiller les mouvements de ses adversaires n'a malheureusement pas pu être implémentée, par manque de temps. Mais nous avions déjà réfléchi à des façons de la découper et de l'implémenter.

Le `SaboteurBot` respecte les contraintes que nous avions :
- il récupère un maximum de bambous, même s’il n’a pas de cartes avec la couleur correspondante.
- Il essaie d’avoir 5 cartes d'objectifs en main tout le temps.
- Les deux premiers mouvements du bot sont donc de prendre une carte d'objectif et un canal d’irrigation
- Quand il tire la météo « ? » dans les premiers tours, il prend un aménagement d'irrigation
- Il essaie de se focaliser sur deux cartes à la fois, celles qui lui rapporteront le plus de points.

Quant à la partie sabotage, voici comment nous comptions procéder :
- Les joueurs ont accès à la liste de tous les objectifs existants et à leur progression relative à chaque joueur.
- Les joueurs ont accès à la liste des actions effectuées par les joueurs précédents.
- Ainsi, il est possible de savoir quels types d'objectifs les autres joueurs ont en main, et de suivre leur progression.
- Si l'action d'un joueur résulte en la progression d'un objectif du type de ceux qu'il a en main, alors il possède probablement cet objectif.
- Il n'y a plus qu'à jouer de façon à le contrecarrer, ce qui peut se faire par simulation des coups possibles.

Malgré l'absence de cette fonctionnalité, le Saboteur est de loin notre meilleur bot. Il sélectionne les meilleurs coups possibles de façon plus précise que notre `PlotRush`, et la progression sur 2 objectifs à la fois le rend plus rapide.

## 2. Architecture et qualité

### Architecture

#### Packages

Nous avons décidé de diviser nos classes java en 4 packages principaux : `action`, `utils`, `player` et `game`. Ces packages contiennent eux-mêmes des sous-packages. Par exemple, on peut trouver les packages `tile`, `objective` et `board` dans le package `game`.

Le découpage en package nous paraissait indispensable pour entretenir une architecture claire et logique. Concernant le choix de répartition des classes, il s'est fait naturellement en fonction des relations entre les classes, ainsi que de leur responsabilité.

Pour appuyer ces propos, prenons un exemple clair : celui des deux classes `Inventory`. Il peut paraître surprenant de trouver les classes `VisibleInventory` et `PrivateInventory` dans deux packages totalement différents, puisque l'inventaire visible est dans le package `board`, tandis que l'inventaire privé est dans le package `player`.

En effet, bien qu'un joueur possède les deux types d'inventaire, seul le privé se trouve dans son package, car il est plus logique de mettre l'inventaire visible avec le `board` : c'est le `board` qui a la responsabilité d'afficher l'inventaire visible d'un joueur aux autres joueurs. Concrètement, dans une partie "physique", cela se traduirait par la disposition des irrigations, aménagements et objectifs dévoilés au bord du plateau.

#### Communication entre jeu et joueurs - système d'actions

En simplifiant, voici comment une partie se déroule :
- Le jeu est initialisé avec une liste de joueurs
- À chaque tour, chaque joueur sélectionne une action jusqu'à ne plus avoir de *crédit d'action*.
- Les joueurs ne peuvent pas influencer directement sur le jeu. À la place, ils peuvent uniquement regarder le plateau, et choisir une action parmi celle qui leur est proposée
- Cette action sera appliquée par une classe dédiée

Ce système permet une certaine robustesse. Nous sommes certains que les actions respecteront les règles du jeu, et que chaque bot ne les implémentera pas à sa matière.

La duplication de code est ainsi réduite au maximum, tandis qu'ajouter une nouvelle action demande un minimum de modifications.

Nous avons également choisi de faire d'`Action` une interface scellée : seules les classes autorisées peuvent en hériter. Ainsi, nous pouvons `switch` sur une instance d'`Action` et gérer tous les cas. Si jamais nous en oublions un, le compilateur nous le fera savoir : pas de risque d'oubli.

Ce système d'action a un autre avantage : étant donné que tout changement d'état s'effectue suite à une action, nous pouvons stocker la liste de ces actions. Mieux encore, nous pouvons revenir en arrière en appliquant une `UndoAction` qui contient les données nécessaires. Les bots peuvent ainsi simuler des coups, de façon très performante et assez ergonomique.

### Où trouver les informations ?

À la racine de notre projet se trouvent deux autres fichiers au format Markdown donnant des informations sur le projet :
- le fichier README explique les règles du jeu, la récupération et l'utilisation du projet, ainsi que la licence utilisée
- le fichier CONTRIBUTING présente la branching strategy utilisée lors de notre projet, ainsi que la façon d'y contribuer.

Nous avons également brièvement documenté toutes les `interface`s en JavaDoc. Les plus importantes sont `Action`, `Player` et `Objective`.

### État de la base du code

Le code est globalement très propre. Les responsabilités sont bien définies et séparées, les principes SOLID semblent être respectés. Nous pensons notamment à la `Dependency Injection` : tous les `Random` que nous utilisons sont injectés, permettant des tests reproductibles.

Nous avons utilisé de nombreux outils automatisés pour vérifier la qualité de notre code, et pouvoir nous concentrer sur la réflexion :
- `git hooks` pour vérifier le *formatting*, la bonne utilisation des mails universitaires et le respect des *conventional commits*
- `github actions` pour ne jamais intégrer sur la branche principale du code qui ne passe pas les tests
- `pitest` pour le *mutation testing*, permettant d'identifier les zones mal testées
- `sonarqube` pour afficher les métriques de notre code

La sortie SonarQube de notre projet est d'ailleurs très positive : à l'exception d'une méthode trop complexe (dans un bot), tout est parfait.
Notre couverture de test est au dessus de 85%. Cette métrique n'est pas suffisante pour affirmer que nos tests sont bons, mais indique tout de même qu'une grande partie du code est testé.

Ainsi, nous pouvons dire que nous avons une confiance assez forte dans notre code.

Notre seul regret est la classe `Board` : elle est trop complexe et a trop de responsabilités. Nous aimerions la décomposer, mais n'avons pas eu le temps.

Nous avons donc une dette technique, faible mais existante. Sonar n'a pas su la détecter : nous voyons ici les limites de cet outil.

## 3. Processus

### Répartition des tâches

Régulièrement, l'équipe dressait ensemble une liste des objectifs à atteindre, puis chacun avait le choix de l'issue qui lui convenait le mieux. Cela aura eu comme résultat global de faire travailler l'ensemble du groupe sur beaucoup de points différents. Par exemple, Edgar a tout d'abord mis en place maven au sein de notre projet, puis Loïc a commencé à travailler sur le code en implémentant un système de coordonnées et les tuiles correspondantes.

Edgar et Clément ont utilisé ce travail pour implémenter les premiers objectifs de modèle de tuile, tandis que Marc travaillait sur le bot facile. Bien entendu, il est arrivé que plusieurs membres du groupe travaillent sur le même problème ou la même issue en cas de besoin.

### Notre utilisation de git

Lorsque l'un d'entre nous travaillait sur une issue, il effectuait d'abord un premier commit (représentant le début de son travail). Suite à cela, il créait une Pull Request correspondant à son issue, puis il continuait à faire des commits jusqu'à ce que le travail de l'issue soit complètement fini. Cependant, rien n'était définitif, car il fallait que l'un des autres membres du groupe passe en revue les changements et, si des problèmes étaient détectés alors ils étaient remontés au responsable de la PR, qui effectuait les modifications. Lorsqu'une telle situation se produisait, une autre revue était nécessaire avant de pouvoir merge.

### Notre branching strategy

Nous avons choisi d'adopter la stratégie "Github Flow" et ce depuis le début du projet, pour différentes raisons :
Tout d'abord, nous avons constaté que la stratégie "Git Flow" demandait de créer trop de branches, ce qui ne nous convenait pas
De plus, nous souhaitions que la version fonctionnelle de notre code soit sur la branche main, ce qui éliminait la stratégie "Gitlab Flow".
Nous en sommes donc arrivés à la conclusion que "Github Flow" était la stratégie qui nous convenait le plus.

## 4. Scénario Industriel

### Rétrospective globale

D'une manière générale, le projet s'est très bien déroulé pour chacun des membres du groupe. L'organisation a été correcte et globalement respectée et nous a permis d'être assez efficace. Certains ont appris plein de concepts assimilés directement à Java ou à Git, d'autres ont pu partager leur savoir et ajouter le takenoko à leur liste de projets réalisés. Ce projet nous a également rappelé l'importance de la communication pour réussir, mais aussi l'importance de fixer des deadlines progressives pour chaque étape (et, bien sûr, de les respecter !)

### Points à conserver pour nos futurs projets

Notre définition régulière des tâches et la communication autour du projet (avancement de chacun, difficultés, ...) ont été deux points très importants dans le bon déroulement de notre projet. De même, il est important de continuer à respecter au maximum les principes de bonne conception(GRASP et SOLID) afin de non seulement garder une architecture et une conception propre et cohérente, mais aussi de pouvoir implémenter de nouvelles fonctionnalités sans avoir à refactor le code actuel.

### Erreurs à éviter pour nos futurs projets

Notre plus grosse erreur a été probablement la gestion du bot intelligent, qui a été non seulement bien trop tardive, mais en plus assez mal supervisée. En effet, nous avons découvert un problème lié à ce bot (qui, jusqu'ici, jouait parfaitement son rôle de bot intelligent), et lorsque nous l'avons corrigé il s'est avéré que ce bot s'est mis à perdre toute sa capacité de réflexion, et perdait largement contre un bot aléatoire. Nous avons sûrement sous-estimé la complexité de cette tâche, ce qui fut une erreur importante.
