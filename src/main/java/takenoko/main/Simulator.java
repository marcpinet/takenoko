package takenoko.main;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.IntStream;
import takenoko.game.Game;
import takenoko.game.WeatherDice;
import takenoko.game.tile.TileDeck;
import takenoko.player.Player;
import takenoko.player.PlayerFactory;
import takenoko.player.PlayerType;

public class Simulator {

    private final int nbGames;

    private List<GameStats> stats;

    private final List<PlayerType> botTypes;

    private final Logger logger;

    private final Random random;
    boolean parallel;

    private static final List<String> NAMES =
            List.of("Philippe", "Mireille", "Anne-Marie", "Nassim");

    enum Parallelism {
        YES
    }

    public Simulator(int nbGames, List<PlayerType> botTypes, Logger logger, Random random) {
        this(nbGames, botTypes, logger, random, false);
    }

    public Simulator(
            int nbGames, List<PlayerType> botTypes, Logger logger, Parallelism parallelism) {
        // if we have parallelism, we can't have a deterministic random
        this(nbGames, botTypes, logger, new Random(), parallelism == Parallelism.YES);
    }

    private Simulator(
            int nbGames,
            List<PlayerType> botTypes,
            Logger logger,
            Random random,
            boolean parallel) {
        this.nbGames = nbGames;
        this.botTypes = botTypes;
        this.logger = logger;
        this.random = random;
        this.parallel = parallel;
        stats = new ArrayList<>(nbGames);
    }

    public SimStats simulate() {

        if (botTypes.size() < 2 || botTypes.size() > 4) {
            throw new IllegalArgumentException("Number of players must be between 2 and 4");
        }

        var stream = IntStream.range(0, nbGames);
        if (parallel) stream = stream.unordered().parallel();

        this.stats = stream.mapToObj(ignored -> simulateOneGame()).toList();

        var botMap = new HashMap<String, PlayerType>();
        for (int i = 0; i < botTypes.size(); i++) {
            botMap.put(NAMES.get(i), botTypes.get(i));
        }

        var namesInGame = NAMES.subList(0, botTypes.size());

        return new SimStats(nbGames, stats, namesInGame, botMap);
    }

    private GameStats simulateOneGame() {

        var tileDeck = new TileDeck(random);

        var players = new ArrayList<Player>();
        for (int j = 0; j < botTypes.size(); j++) {
            players.add(PlayerFactory.makePlayer(botTypes.get(j), NAMES.get(j), random));
        }

        var game = new Game(players, logger, tileDeck, new WeatherDice(random), random);
        var result = game.play();
        Optional<String> winner = result.map(Player::getName);

        var playersStats = new ArrayList<PlayerStats>();

        for (int j = 0; j < players.size(); j++) {
            var objective = players.get(j).getVisibleInventory().getFinishedObjectives().size();
            var score = players.get(j).getScore();
            playersStats.add(new PlayerStats(NAMES.get(j), score, objective));
        }

        return new GameStats(game.getNumTurn(), winner, playersStats);
    }

    public record SimStats(
            int nbGames,
            List<GameStats> stats,
            List<String> players,
            Map<String, PlayerType> botTypes) {
        public Map<String, Integer> getNumWins() {
            var numWins = new HashMap<String, Integer>();
            for (var player : players) {
                numWins.put(player, 0);
            }

            for (var stat : stats) {
                stat.winnerName.ifPresent(name -> numWins.put(name, numWins.get(name) + 1));
            }

            return numWins;
        }

        public Map<String, Double> getPercentWins() {
            var percentWins = new HashMap<String, Double>();
            var numWins = getNumWins();
            for (var player : players) {
                percentWins.put(player, (double) numWins.get(player) / nbGames * 100);
            }

            return percentWins;
        }

        public Map<String, Double> getAvgScore() {
            var avgScore = new HashMap<String, Double>();
            for (var player : players) {
                avgScore.put(player, 0.0);
            }

            for (var stat : stats) {
                for (var playerStat : stat.playersStats) {
                    avgScore.put(
                            playerStat.playerName,
                            avgScore.get(playerStat.playerName) + playerStat.score);
                }
            }

            for (var player : players) {
                avgScore.put(player, avgScore.get(player) / nbGames);
            }

            return avgScore;
        }

        public Map<String, Double> getAvgObjective() {
            var avgObjective = new HashMap<String, Double>();
            for (var player : players) {
                avgObjective.put(player, 0.0);
            }

            for (var stat : stats) {
                for (var playerStat : stat.playersStats) {
                    avgObjective.put(
                            playerStat.playerName,
                            avgObjective.get(playerStat.playerName)
                                    + playerStat.completedObjectiveCount);
                }
            }

            for (var player : players) {
                avgObjective.put(player, avgObjective.get(player) / nbGames);
            }

            return avgObjective;
        }

        public double getAvgTurn() {
            var avgTurn = 0.0;
            for (var stat : stats) {
                avgTurn += stat.turns;
            }

            return avgTurn / nbGames;
        }

        @Override
        public String toString() {
            return "Results for "
                    + nbGames
                    + " games simulated:"
                    + "\n Type of bots: "
                    + botTypes
                    + "\n Number of wins: "
                    + getNumWins()
                    + "\n Win rate: "
                    + getPercentWins()
                    + "\n Average score: "
                    + getAvgScore()
                    + "\n Average objectives: "
                    + getAvgObjective()
                    + "\n Average turns: "
                    + getAvgTurn();
        }
    }

    public record PlayerStats(String playerName, int score, int completedObjectiveCount) {}

    public record GameStats(
            int turns, Optional<String> winnerName, List<PlayerStats> playersStats) {}
}
