package mcts

/**
 * Implementation of a game that isn't very much fun, but has
 * a pure strategy for each player and a deterministic outcome
 * when players play their pure strategies.
 *
 * Players take turns choosing a letter, either "a" or "b".
 * Each letter is recorded as it is chosen. At the end of N
 * rounds the score for player 0 is the number of times "a"
 * has been played, and the score for player 1 is the number
 * of times "b" has been played.
 */
class TestGame(val numberOfRounds: Int): Game<String, String, Int> {

    override fun apply(move: String, board: String) = board + move

    override fun firstPlayer() = 0

    override fun initialBoard() = ""

    override fun isOver(board: String) = board.length >= numberOfRounds

    override fun possibleMoves(board: String) = listOf("a", "b")

    override fun nextPlayer(current: Int) = (current + 1) % 2

    override fun scores(board: String): Map<Int, Int> {
        if (board.length < numberOfRounds) {
            return HashMap()
        }

        val aCount = board.count { it == 'a' }
        val bCount = board.count { it == 'b' }

        return mapOf(Pair(0, aCount), Pair(1, bCount))
    }
}