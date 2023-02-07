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

[![Java CI with Maven](https://github.com/pns-si3-projects/projet2-ps5-22-23-takenoko-2023-a/actions/workflows/github-actions-maven-test-build.yml/badge.svg)](https://github.com/pns-si3-projects/projet2-ps5-22-23-takenoko-2023-a/actions)

<!-- PROJECT LOGO -->
<br />
<div align="center">
<a href="https://github.com/pns-si3-projects/projet2-ps5-22-23-takenoko-2023-a">
<img src="./readme-data/takenoko.png" alt="Logo" width="100" height="100"-->

</a>

<h3 align="center">Takenoko</h3>

  <p align="center">Java implementation of the board game  <a href="https://fr.wikipedia.org/wiki/Takenoko">Takenoko</a> where players try to grow and harvest bamboo while managing a garden and a panda.
    <br />
    <a href="https://github.com/pns-si3-projects/projet2-ps5-22-23-takenoko-2023-a"><strong>Explore the docs »</strong></a>
    <br />
    <br />
    <a href="https://github.com/pns-si3-projects/projet2-ps5-22-23-takenoko-2023-a">View Demo</a>
    ·
    <a href="https://github.com/pns-si3-projects/projet2-ps5-22-23-takenoko-2023-a/issues">Report Bug</a>
    ·
    <a href="https://github.com/pns-si3-projects/projet2-ps5-22-23-takenoko-2023-a/issues">Request Feature</a>
  </p>
</div>

[//]: # (<!-- TABLE OF CONTENTS -->)
[//]: # (<details>)
[//]: # (  <summary>Table of Contents</summary>)
[//]: # (  <ol>)
[//]: # (    <li>)
[//]: # (      <a href="#about-the-project">About The Project</a>)
[//]: # (      <ul>)
[//]: # (        <li><a href="#built-with">Built With</a></li>)
[//]: # (      </ul>)
[//]: # (    </li>)
[//]: # (    <li>)
[//]: # (      <a href="#getting-started">Getting Started</a>)
[//]: # (      <ul>)
[//]: # (        <li><a href="#prerequisites">Prerequisites</a></li>)
[//]: # (        <li><a href="#installation">Installation</a></li>)
[//]: # (      </ul>)
[//]: # (    </li>)
[//]: # (    <li><a href="#usage">Usage</a></li>)
[//]: # (    <li><a href="#contributing">Contributing</a></li>)
[//]: # (    <li><a href="#license">License</a></li>)
[//]: # (  </ol>)
[//]: # (</details>)

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

<a href="./readme-data/takenoko.pdf">Entire rules</a>

<p align="right">(<a href="#readme-top">back to top</a>)</p>

## Current features

Updated on December 20, 2022

* //TODO

<p align="right">(<a href="#readme-top">back to top</a>)</p>

### Built With

* [Java](https://www.java.com/fr/)
* [Maven](https://maven.apache.org/)
* [JUnit](https://junit.org/junit5/)

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- GETTING STARTED -->

## Getting Started

### Prerequisites

* Maven
* Java 17
* Git

### Installation

1. Clone the repo

   ```sh
   git clone https://github.com/pns-si3-projects/projet2-ps5-22-23-takenoko-2023-a.git
   ```
2. Build the project

   ```sh
   mvn clean install
   ```
3. Run the project

   ```sh
   mvn exec:java
   ```

<p align="right">(<a href="#readme-top">back to top</a>)</p>

### Run tests

1. Run tests

   ```sh
   mvn test
   ```
2. Generate reports

   ```sh
   mvn site //TODO
   ```

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- USAGE EXAMPLES -->

## Usage

This version of takenoko is fully automated: bots of different levels play against each other, so there is no specific
action to perform during the game, logs informing about each action will be logged as well as the detailed result of the
game.
To configure the number of bots of their level :
//TODO

<p align="right">(<a href="#readme-top">back to top</a>)</p>

[//]: # ()
[//]: # (<!-- ROADMAP -->)
[//]: # ()
[//]: # (## Roadmap)
[//]: # ()
[//]: # (- [ ] Feature 1)
[//]: # (- [ ] Feature 2)
[//]: # (- [ ] Feature 3)
[//]: # (    - [ ] Nested Feature)
[//]: # ()
[//]: # (See the [open issues]&#40;https://github.com/pns-si3-projects/projet2-ps5-22-23-takenoko-2023-a/issues; for a full list of)
[//]: # (proposed features &#40;and)
[//]: # (known issues&#41;.)
[//]: # ()
[//]: # (<p align="right">&#40;<a href="#readme-top">back to top</a>&#41;</p>)
[//]: # ()
[//]: # (<!-- CONTRIBUTING -->)
[//]: # ()
[//]: # (## Contributing)
[//]: # ()
[//]: # (Contributions are what make the open source community such an amazing place to learn, inspire, and create. Any)
[//]: # (contributions you make are **greatly appreciated**.)
[//]: # ()
[//]: # (If you have a suggestion that would make this better, please fork the repo and create a pull request. You can also)
[//]: # (simply open an issue with the tag "enhancement".)
[//]: # (Don't forget to give the project a star! Thanks again!)
[//]: # ()
[//]: # (1. Fork the Project)
[//]: # (2. Create your Feature Branch &#40;`git checkout -b feature/AmazingFeature`&#41;)
[//]: # (3. Commit your Changes &#40;`git commit -m 'Add some AmazingFeature'`&#41;)
[//]: # (4. Push to the Branch &#40;`git push origin feature/AmazingFeature`&#41;)
[//]: # (5. Open a Pull Request)
[//]: # ()
[//]: # (<p align="right">&#40;<a href="#readme-top">back to top</a>&#41;</p>)
[//]: # ()

<!-- LICENSE -->

## License

Distributed under the Mozilla Public License Version 2.0. See `LICENSE` for more information.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- CONTACT -->
[//]: # (## Contact)
[//]: # ()
[//]: # (Your Name - [@twitter_handle]&#40;https://twitter.com/twitter_handle&#41; - email@email_client.com)
[//]: # ()
[//]: # (Project)
[//]: # (Link: [https://github.com/pns-si3-projects/projet2-ps5-22-23-takenoko-2023-a]&#40;https://github.com/pns-si3-projects/projet2-ps5-22-23-takenoko-2023-a&#41;)
[//]: # ()
[//]: # (<p align="right">&#40;<a href="#readme-top">back to top</a>&#41;</p>)
[//]: # ()
[//]: # ()
[//]: # ()
[//]: # (<!-- ACKNOWLEDGMENTS -->)
[//]: # ()
[//]: # (## Acknowledgments)
[//]: # ()
[//]: # (* []&#40;&#41;)
[//]: # (* []&#40;&#41;)
[//]: # (* []&#40;&#41;)
[//]: # ()
[//]: # (<p align="right">&#40;<a href="#readme-top">back to top</a>&#41;</p>)
[//]: #-->
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
