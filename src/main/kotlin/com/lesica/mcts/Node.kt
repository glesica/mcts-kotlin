package com.lesica.mcts

/**
 * A particular node in the search tree. Used internally to track
 * the progress of the MCTS algorithm.
 */
class Node<TMove, TBoard, TPlayer> {

    /**
     * The board state at this node in the search tree.
     */
    val board: TBoard

    val children
        get() = moveToChild as Map<TMove, Node<TMove, TBoard, TPlayer>>

    /**
     * Nodes reachable from this node by a single move.
     */
    private val moveToChild = HashMap<TMove, Node<TMove, TBoard, TPlayer>>()

    /**
     * The node used to reach this node.
     */
    val parent: Node<TMove, TBoard, TPlayer>?

    /**
     * The player whose turn it is to move at this node. In
     * other words, the player whose move will determine which
     * child node we end up at next.
     */
    val player: TPlayer

    /**
     * The number of times this node has been visited by the search
     * algorithm.
     */
    var visitCount = 0
        private set

    /**
     * The number of visits to this node by the search algorithm
     * that have resulted in simulated victories for the player
     * associated with the node.
     */
    var winCount = 0
        private set

    constructor(board: TBoard, player: TPlayer) {
        this.board = board
        this.parent = null
        this.player = player

    }

    private constructor(board: TBoard, player: TPlayer, parent: Node<TMove, TBoard, TPlayer>) {
        this.board = board
        this.parent = parent
        this.player = player
    }

    /**
     * Add a new child node to this one that will represent the
     * result of the given player having chosen the given move
     * at the parent node, producing the given board state.
     */
    fun add(move: TMove, board: TBoard, player: TPlayer): Node<TMove, TBoard, TPlayer> {
        val node = Node(board, player, this)
        moveToChild[move] = node
        return node
    }

    /**
     * Whether or not this node is a leaf node.
     */
    fun isLeaf() = moveToChild.size == 0

    /**
     * Report a new visit to this node, mutating its visit
     * count and (possibly) win count in the process.
     */
    fun visited(winners: List<TPlayer>) {
        visitCount++

        if (parent == null || winners.contains(parent.player)) {
            // The root node always wins because play always
            // passes through it.
            winCount++
        }
    }
}