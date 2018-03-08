package mcts

/**
 * An interface that describes the mechanics of a particular
 * game. Consumers can access the MCTS engine simply by
 * implementing this interface.
 *
 * TMove is a type that represents a particular game move and
 * must implement `equal` and `hashCode` because the engine
 * may compare them or use them as map keys.
 *
 * TBoard is a type that represents a particular state of the
 * game board. There are no restrictions on the implementation
 * but methods should always return new instances instead of
 * mutating existing boards that are passed in.
 *
 * TPlayer is a type that represents a player (player 1, 2,
 * and so on). It should implement `equal` and `hashCode` in
 * one way or another (using it enum is probably a good idea).
 */
interface Game<TMove, TBoard, TPlayer> {
    /**
     * Return a copy of the given board with the given move
     * applied to it. The instance returned should be a copy.
     */
    fun apply(move: TMove, board: TBoard): TBoard

    /**
     * Return the integer identifier of the player that
     * should move first.
     */
    fun firstPlayer(): TPlayer

    /**
     * Return a "clean" board state suitable for the start of
     * play.
     */
    fun initialBoard(): TBoard

    /**
     * Whether or not the given board state represents a game
     * that has concluded (someone has won or the game has ended
     * in a draw).
     */
    fun isOver(board: TBoard): Boolean

    /**
     * Return a list of valid moves that could be applied to
     * the given board.
     */
    fun possibleMoves(board: TBoard): List<TMove>

    /**
     * Return the next player that should move assuming that
     * the given player just moved.
     *
     * TODO: Would board state ever influence this?
     */
    fun nextPlayer(current: TPlayer): TPlayer

    /**
     * Return the scores for each player as a map from player
     * identifiers to scores.
     *
     * If the game is not yet over at the given board state,
     * this method should return an empty map.
     *
     * If `isOver` returns `true` for a given board state then
     * this method must return a map with at least one key-value
     * pair.
     */
    fun scores(board: TBoard): Map<TPlayer, Int>
}