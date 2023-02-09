<!-- PROJECT SHIELDS -->
<!--
*** I'm using markdown "reference style" links for readability.
*** Reference links are enclosed in brackets [ ] instead of parentheses ( ).
*** See the bottom of this document for the declaration of the reference variables
*** for contributors-url, forks-url, etc. This is an optional, concise syntax you may use.
*** https://www.markdownguide.org/basic-syntax/#reference-style-links
[//]: # ([![Contributors][contributors-shield]][contributors-url])
[//]: # ([![Forks][forks-shield]][forks-url])
[//]: # ([![Stargazers][stars-shield]][stars-url])
[//]: # ([![Issues][issues-shield]][issues-url])
[//]: # ([![MIT License][license-shield]][license-url])
[//]: # ([![LinkedIn][linkedin-shield]][linkedin-url]-->

[![Java CI with Maven](https://github.com/pns-si3-projects/projet2-ps5-22-23-takenoko-2023-a/actions/workflows/autotest.yml/badge.svg)](https://github.com/pns-si3-projects/projet2-ps5-22-23-takenoko-2023-a/actions)

<!-- PROJECT LOGO -->
<br />
<div align="center">
<a href="https://github.com/pns-si3-projects/projet2-ps5-22-23-takenoko-2023-a">
<img src="./readme-data/takenoko.png" alt="Logo" width="100" height="100"-->

</a>

<h3 align="center">Takenoko</h3>

  <p align="center">Java implementation of the board game  <a href="https://fr.wikipedia.org/wiki/Takenoko">Takenoko</a> where players try to grow and harvest bamboo while managing a garden and a panda.
    <br />
    <br />
    <br />
    <a href="https://github.com/pns-si3-projects/projet2-ps5-22-23-takenoko-2023-a">View Demo</a>
    ·
    <a href="https://github.com/pns-si3-projects/projet2-ps5-22-23-takenoko-2023-a/issues">Report Bug</a>
    ·
    <a href="https://github.com/pns-si3-projects/projet2-ps5-22-23-takenoko-2023-a/issues">Request Feature</a>
  </p>
</div>

<!-- ABOUT THE PROJECT -->

## Game Rules

Takenoko is a board game for 2-4 players, set in ancient China during the Han Dynasty. The objective of the game is to
earn the most points by cultivating the garden and completing objectives.

Here are the basic rules for playing Takenoko:

- Set up the game board, which consists a pond tile. Place the panda and gardener figurines on the pond.
- On his turn, a player must choose two different actions from among :
  - Draw three tiles and choose one to place according to the placement rules
  - Take an irrigation stick from the reserve and add it to your inventory
  - Move the gardener and grow the adjacent bamboos according to the rules
  - Move the panda and get a bamboo on the arrival tile
  - Draw an objective card of a certain type. (max 5 in your hand)
- If a player unveils one of his achieved objective cards, he gets the points indicated on the card.
- The game ends when a player has revealed 7 objective cards, the player with the most points wins.

<a href="./readme-data/takenoko.pdf">Detailed rules</a>

## Current features

*Updated on February 09, 2023*

* 3 types of bots (random, strategy, wrecker)
* Simulations of n games in a row
* Stats summary output in a `.csv` file
* Actions system
* Undo/Redo design
* Fully automated repo to prevent breaking changes and minimize merge conflicts
* Extensible, well-documented

### Built With

* [Java](https://www.java.com/fr/)
* [Maven](https://maven.apache.org/)
* [JUnit](https://junit.org/junit5/)
* [OpenCSV](https://mvnrepository.com/artifact/com.opencsv/opencsv/)

<!-- GETTING STARTED -->

## Getting Started

### Prerequisites

* Maven
* Java 17 with preview enabled (--enable-preview VM option)
* Git

### Installation

1. Clone the repo

   ```sh
   git clone https://github.com/pns-si3-projects/projet2-ps5-22-23-takenoko-2023-a.git
   ```
2. Install the required dependencies

   ```sh
   mvn clean install
   ```
3. Build and run the project

   ```sh
   mvn exec:java
   ```

### Tests and arguments

Run tests

```sh
mvn test
```

Generate reports of 300 games (can be tweaked in the [Main](src/main/java/takenoko/main/Main.java) file)

```sh
mvn -e exec:java '-Dexec.args="--csv"'
```

Generate simulation of 2000 games

```sh
mvn -e exec:java '-Dexec.args="--2thousands"'
```

Demo of a single game

```sh
mvn -e exec:java '-Dexec.args="--demo"'
```

<!-- USAGE EXAMPLES -->

## Usage

This version of takenoko is fully automated: bots of different levels play against each other, so there is no specific
action to perform during the game, logs informing about each action will be logged as well as the detailed result of the
game.

<!-- LICENSE -->

## License

Distributed under the Mozilla Public License Version 2.0. See `LICENSE` for more information.

<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links
[contributors-shield]: https://img.shields.io/github/contributors/pns-si3-projects/projet2-ps5-22-23-takenoko-2023-a.svg?style=for-the-badge
[contributors-url]: https://github.com/pns-si3-projects/projet2-ps5-22-23-takenoko-2023-a/graphs/contributors
[forks-shield]: https://img.shields.io/github/forks/pns-si3-projects/projet2-ps5-22-23-takenoko-2023-a.svg?style=for-the-badge
[forks-url]: https://github.com/pns-si3-projects/projet2-ps5-22-23-takenoko-2023-a/network/members
[stars-shield]: https://img.shields.io/github/stars/pns-si3-projects/projet2-ps5-22-23-takenoko-2023-a.svg?style=for-the-badge
[stars-url]: https://github.com/pns-si3-projects/projet2-ps5-22-23-takenoko-2023-a/stargazers
[issues-shield]: https://img.shields.io/github/issues/pns-si3-projects/projet2-ps5-22-23-takenoko-2023-a.svg?style=for-the-badge
[issues-url]: https://github.com/pns-si3-projects/projet2-ps5-22-23-takenoko-2023-a/issues
[license-shield]: https://img.shields.io/github/license/pns-si3-projects/projet2-ps5-22-23-takenoko-2023-a.svg?style=for-the-badge
[license-url]: https://github.com/pns-si3-projects/projet2-ps5-22-23-takenoko-2023-a/blob/master/LICENSE.txt
[linkedin-shield]: https://img.shields.io/badge/-LinkedIn-black.svg?style=for-the-badge&logo=linkedin&colorB=555
[linkedin-url]: https://linkedin.com/in/linkedin_usern-->
