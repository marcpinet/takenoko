# Rapport / Knowledge Exchange - Takenoko, team A

## Edgar BIZEl, Loïc PALAYER, Marc PINET, Clément REMY

### 1. Point d'avancement

* Résumé des fonctionnalités :
  L'ensemble des fonctionnalités du jeu ont été traitées, et ce en respesctant les règles. La seule attente de base qui
  n'a pas été satisfaite à 100% est celle des bots. Nous avons un bot faible, ainsi qu'un PlotRushBot qui fait en sorte
  d'avoir toujours un maximum d'objectif dans sa main (de type TilePattern) et qui choisira l'action qui lui sera la
  plus bénéfique pour l'avancement de ses objectifs. Dans le cas où aucune action ne sera possible, il prendra une
  irrigation et la placera à un endroit qui lui sera bénéfique aussi. Enfin, s'il ne peut pas placer d'irrigation, il en
  prendra une. Si plus aucune irrigation n'est disponible, l'algorithme se rabattra sur une action aléatoire. Nous
  disposons aussi d'un SaboteurBot qui récupère un maximum de bambous, même s’il n’a pas de cartes avec la couleur
  correspondante. Il essaie d’avoir 5 cartes objectif en main tout le temps. Les deux premiers mouvements du bot
  devraient donc être de prendre une carte objectif et de prendre un canal d’irrigation. Quand il tire une la météo « ?
  » dans les premiers tours, il prend une irrigation. Il essaie de se focaliser sur plusieurs (pas toutes) cartes à la
  fois. S’il a deux cartes Panda, il va se focaliser sur les deux en même temps. Nous disposons aussi d'un RandomBot
  qui, comme son nom l'indique, effectuera des actiosn aléatoires. Nous avons aussi implémenté un système de simulations
  et de statistiques (voir [README.md](/README.md)).

* Fonctionnalité supplémentaire 1 : les logs et la démo de 2000 parties
  Pour cette fonctionnalité, nous avons crée une classe Simulator qui a pour responsabilité de simuler un nombre défini
  de parties. Nous nous servons de cette fonctionnalité avec le framework JCommander, qui permet de "parser" les
  arguments rentrés en ligne de commande. Nous avons implémenté une classe Args, dans laquelle nous avons défini
  plusieurs paramètres, dont "--2thousands", paramètre qui correspond donc au lancement du simulator.

* Fonctionnalité supplémentaire 2 : le CSV
  Concernant le csv, nous avons tout d'abord ajouté le moyen de récupérer des statistiques de parties via la classe
  Simulator. Puis, via le framework OpenCSV, nous avons crée une classe CSVHandler qui répertorie les statistiques de
  500 parties dans un fichier stats.csv. Si le fichier existe déjà, le programme va écrire à la suite des données
  contenue dans le fichier.

* Fonctionnalité supplémentaire 3 : Bot spécifique
  Puisque le travail demandé pour cette fonctionnalité était conséquent, nous avons choisi de découpé le travail sur ce
  bot en 5 issues. Nous avons réussi à en faire 4 sur les 5. La fonctionnalité de surveiller les mouvements de ses
  adversaires n'a malheureusement pas pu être implémentée de par sa difficulté et notre manque de temps.

### 2. Architecture et qualité

* Architecture et choix
  Nous avons décidé de diviser nos classes java dans 4 gros packages : action, utils, player et game. Ces packages
  contiennent eux-mêmes des sous-packages (par exemple, on peut trouver les packages tile, objective et board dans le
  package game). Le découpage en package nous paraissait indispensable pour entretenir une architecture claire et
  logique. Concernant le choix de répartition des classes, il s'est fait naturellement en fonction des relations entre
  les classes, ainsi que de leur responsabilité. Pour appuyer ces propos, prenons un exemple clair : celui des deux
  classes Inventory. Il peut paraître surprenant de trouver les classes VisibleInventory et PrivateInventory dans deux
  packages totalement différents, puisque l'inventaire visible est dans le package board, tandis que l'inventaire privé
  est dans le package player. En effet, bien qu'un joueur possède les deux types d'inventaire, seul le privé se trouve
  dans son package, car il est plus logique de mettre l'inventaire visible avec le board puisque c'est le board qui a la
  responsabilité d'afficher l'inventaire visible d'un joueur aux autres joueurs (concrètement, dans une partie "
  physique", cela se traduirait par la disposition des irrigations, aménagements et objectifs dévoilés au bord du
  plateau). Pour les tours, nous utilisons un système d'action qui permet à la fois de surveiller que chaque action
  dispose des informations auxquelles elle est censée avoir accès de manière contrôlée, tout en garantissant une
  extension des possibilités d'ajouts et de la robustesse de notre projet (nous nous reposons sur des switch plutôt que
  des instanceof qui sont plus appropriés). Nous avons aussi un design d'undo/redo qui facilite la recherche du meilleur
  coup possible à travers des simulations pour les bots, mais permet aussi la curabilité en cas d'échec.

* Où trouver les infos?
  A la racine de notre projet se trouvent deux autres fichiers au format Markdown donnant des informations sur le
  projet : le fichier README explique les règles du jeu, la récupération et l'utilisation du projet, ainsi que la
  licence utilisée, tandis que le fichier CONTRIBUTING présente la branching stratégy utilisée lors de notre projet,
  ainsi que la façon d'y contribuer.

* Etat de la base du code
  La sortie SonarQube de notre projet est très positive, puisque hormis une dizaine de minutes de dette technique et un
  unique soucis de “code smell”, tout est parfait. Nous n’avons ni de problème de bugs ni de problème de sécurité, et
  notre coverage global est au-dessus de 85%, ce qui est correct. Concernant le degré de confiance quant au code que
  nous avons produit, celui-ci est très positif même si nous sommes assez déçu de la classe Board qui détient trop de
  responsabilités. Nous aurions voulu déléguer ces responsabilités à d'autres classes nouvellement créées à l'occasion.

### 3. Processus

* Répartition des tâches
  Régulièrement, l'équipe dressait ensemble une liste des objectifs à atteindre, puis chacun avait le choix de l'issue
  qui lui convenait le mieux. Cela aura eu comme résultat global de faire travailler l'ensemble du groupe
  sur beaucoup de points différents. Par exemple, Edgar a tout d'abord mis en place maven au sein de notre projet, puis
  Loïc a commencé à travailler sur le code en implémentant un système de coordonnées, et les tuiles correspondantes.
  Edgar et Clément ont utilisé ce travail pour implémenter les premiers objectifs de pattern de tuile, tandis que Marc
  travaillait sur le bot facile. Bien entendu, il est arrivé que plusieurs membres du groupe travaillent sur le même
  problème ou la même issue en cas de besoin.

* Notre utilisation de git
  Lorsque l'un d'entre nous travaillait sur une issue, il effectuait d'abord un premier commit (représentant le début de
  son travail). Suite à cela, il créait une Pull Request correspondant à son issue, puis il continuait à faire des
  commits jusqu'à ce que le travail de l'issue soit complètement fini. Cependant, rien n'était définitif, car il fallait
  que l'un des autres membres du groupe passe en revue les changements et, si des problèmes étaient détectés alors ils
  étaient remontés au responsable de la PR, qui effectuait les modifications. Lorsqu'une telle situation se produisait,
  une autre revue était nécessaire avant de pouvoir merge.

* Notre branching strategy
  Nous avons choisi d'adopter la stratégie "Github Flow" et ce depuis le début du projet, pour différentes raisons :
  Tout d'abord, nous avons constaté que la stratégie "Git Flow" demandait de créait trop de branches, ce qui ne nous
  convenait pas
  De plus, nous souhaitions que la version fonctionnelle de notre code soit sur la branche main, ce qui éliminait la
  stratégie "Gitlab Flow".
  Nous en sommes donc arrivé à la conclusion que "Github Flow" était la stratégie qui nous convenait le plus.

### 4. Scénario Industriel

* Rétrospective globale
  D'une manière générale, le projet s'est très bien déroulé pour chacun des membres du groupe. L'organisation a été
  correcte et globalement respectée et nous a permis d'être assez efficace. Certains ont appris plein de concepts
  assimilés directement à Java ou à Git, d'autres ont pu partager leur savoir et ajouter le takenoko à leur liste de
  projets réalisés. Ce projet nous a également rappelé l'importance de la communication pour réussir, mais aussi
  l'importance de fixer des deadlines progressives pour chaque étape (et, bien sûr, de les respecter!)

* Points à conserver pour nos futurs projets
  Notre définition régulière des tâches, et la communication autour du projet (avancement de chacun, difficultés, ...)
  ont été deux points très importants dans le bon déroulement de notre projet. De même, il est important de continuer à
  respecter au maximum les principes de bonne conception(GRASP et SOLID) afin de non seulement garder une architecture
  et une conception propre et cohérente mais aussi de pouvoir implémenter de nouvelles fonctionnalités sans avoir à
  refactor le code actuel.

* Erreurs à éviter pour nos futurs projets
  Notre plus grosse erreur a été probablement la gestion du bot intelligent, qui a été non seulement bien trop tardive,
  mais en plus assez mal supervisée. En effet, nous avons découvert un problème lié à ce bot (qui, jusqu'ici, jouait
  parfaitement son rôle de bot intelligent), et lorsque nous l'avons corrigé il s'est avéré que ce bot s'est mis à
  perdre toute sa capacité de réflexion, et perdait largement contre un bot aléatoire. Nous avons probablement
  sous-estimé la complexité de cette tâche, ce qui fut une grosse erreur.
