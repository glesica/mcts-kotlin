package com.lesica.mcts

/**
 * Implementation of the actual MCTS algorithm. Consumers may
 * sub-class and override methods to tweak its behavior.
 */
class Engine<TMove, TBoard, TPlayer>(val game: Game<TMove, TBoard, TPlayer>) {

    val board
        get() = currentNode.board

    var currentNode = Node<TMove, TBoard, TPlayer>(game.initialBoard(), game.firstPlayer())
        private set

    /**
     * Apply a move that was computed externally (possibly by
     * a human player or another AI) to the board state held
     * by the engine.
     *
     * TODO: Add option to re-use sub-tree for better performance
     */
    fun apply(move: TMove) {
        val nextBoard = game.apply(move, currentNode.board)
        val nextPlayer = game.nextPlayer(currentNode.player)
        currentNode = Node(nextBoard, nextPlayer)
    }

    /**
     * The back propagation phase of the algorithm. This is where
     * we record the results of our simulation in nodes of the
     * search tree that we passed through during the selection
     * and expansion phases.
     */
    fun backpropagate(leaf: Node<TMove, TBoard, TPlayer>, winners: List<TPlayer>) {
        var current: Node<TMove, TBoard, TPlayer>? = leaf
        while (current != null) {
            current.visited(winners)
            current = current.parent
        }
    }

    /**
     * The expansion phase of the algorithm. This is where the
     * actual search tree is expanded downward and we pick the
     * node that will be used as the actual starting point for
     * the simulation phase.
     *
     * If the given leaf node has no possible moves (meaning
     * it is an end-game node) then it will be returned.
     */
    fun expand(leaf: Node<TMove, TBoard, TPlayer>): Node<TMove, TBoard, TPlayer> {
        val possibleMoves = game.possibleMoves(leaf.board)
        if (possibleMoves.isEmpty()) {
            return leaf
        }

        val nextPlayer = game.nextPlayer(leaf.player)

        // We fully hydrate the leaf node even though we will only
        // choose one of the new leaves. This simplifies the select
        // part of the algorithm at the cost of some memory.
        val possibleNodes = possibleMoves.map { move ->
            val board = game.apply(move, leaf.board)
            leaf.add(move, board, nextPlayer)
        }

        return expandChoose(possibleNodes)
    }

    /**
     * Method used to choose a node during the expansion phase.
     */
    fun expandChoose(nodes: List<Node<TMove, TBoard, TPlayer>>): Node<TMove, TBoard, TPlayer> {
        return nodes.chooseRandom()
    }

    fun run(iterations: Int = 1): Map<TMove, Double> {
        var iteration = 0
        return runWhile({ iteration++ < iterations })
    }

    fun runBest(iterations: Int): TMove? {
        return run(iterations).maxBy { it.value }?.key
    }

    fun runBestWhile(callback: () -> Boolean): TMove? {
        return runWhile(callback).maxBy { it.value }?.key
    }

    /**
     * Run the MCTS algorithm for as long as the provided callback
     * returns true.
     */
    fun runWhile(callback: () -> Boolean): Map<TMove, Double> {
        while (callback()) {
            val leaf = expand(select(currentNode))
            val winners = simulate(leaf)
            backpropagate(leaf, winners)
        }

        return currentNode.children.map {
            val node = it.value
            val score = node.winCount * 1.0 / node.visitCount
            Pair(it.key, score)
        }.toMap()
    }

    /**
     * The selection phase of the algorithm. This is where we
     * decide where to expand the tree so that we can run a
     * simulation.
     */
    fun select(root: Node<TMove, TBoard, TPlayer>): Node<TMove, TBoard, TPlayer> {
        var current = root
        while (!current.isLeaf()) {
            current = selectChoose(current.children.values.toList())
        }
        return current
    }

    /**
     * Method used to choose a node at each step of the selection
     * phase. Probably some kind of UCT.
     */
    fun selectChoose(nodes: List<Node<TMove, TBoard, TPlayer>>): Node<TMove, TBoard, TPlayer> {
        // TODO: UCT algorithm goes here
        return nodes.chooseRandom()
    }

    /**
     * The simulation phase of the algorithm. During this step we
     * play the game to the end and see who won, this step does
     * not expand or otherwise mutate the search tree itself.
     *
     * The return value is a list of identifiers of the players
     * that "won" (because they had the highest score). Usually
     * there will only be one identifier in the list, but there
     * can be more than one in the case of a draw.
     *
     * The list returned will never be empty.
     *
     * TODO: This would be more efficient if the board was optionally mutable
     */
    fun simulate(leaf: Node<TMove, TBoard, TPlayer>): List<TPlayer> {
        var current = leaf.board
        while (!game.isOver(current)) {
            val move = simulateChoose(game.possibleMoves(current))
            current = game.apply(move, current)
        }

        // We now have a board state for which `isOver` returns
        // `true`, therefore we know that there will be at least
        // one key-value pair in the map, we just need to find
        // the maximum score and return a list of the keys that
        // have that score.
        val scores = game.scores(current)
        val maxScore = scores.values.max()!!


        return scores.filter { it.value == maxScore }
                .keys.toList()
    }

    /**
     * Method used to choose a move during each step of the simulation
     * phase. Note that this phase does not expand the search tree.
     */
    fun simulateChoose(moves: List<TMove>): TMove {
        return moves.chooseRandom()
    }
}
