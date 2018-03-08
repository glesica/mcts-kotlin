package example.tictactoe

import mcts.isEven

class Board {

    val pieces = HashMap<Move, Player>()

    constructor()

    private constructor(initialPieces: Map<Move, Player>) {
        initialPieces.forEach {
            pieces[it.key] = it.value
        }
    }

    fun apply(move: Move): Board {
        val board = Board(pieces)
        board.pieces[move] = nextPlayer()
        return board
    }

    fun emptyCount() = 9 - pieces.size

    fun nextPlayer(): Player {
        val empty = emptyCount()
        return when {
            empty.isEven() -> Player.O
            else -> Player.X
        }
    }

    fun possibleMoves(): List<Move> {
        return Move.values().filter {
            pieces[it] == null
        }.toList()
    }

    override fun toString() =
            toString(Move.NW) + toString(Move.N) + toString(Move.NE, true) + "-----------\n" +
            toString(Move.W) + toString(Move.C) + toString(Move.E, true) + "-----------\n" +
            toString(Move.SW) + toString(Move.S) + toString(Move.SE, true)

    private fun toString(move: Move, lastInRow: Boolean = false) =
        when (lastInRow) {
            true -> " ${pieces[move] ?: " "} \n"
            false -> " ${pieces[move] ?: " "} |"
        }

    fun winner(): Player? {
        return winner(Move.NW, Move.N, Move.NE) ?:
                winner(Move.W, Move.C, Move.E) ?:
                winner(Move.SW, Move.S, Move.SE) ?:
                winner(Move.NW, Move.C, Move.SE) ?:
                winner(Move.SW, Move.C, Move.NE) ?:
                winner(Move.NW, Move.W, Move.SW) ?:
                winner(Move.N, Move.C, Move.S) ?:
                winner(Move.NE, Move.E, Move.SE)
    }

    fun winner(first: Move, second: Move, third: Move): Player? {
        return if (pieces[first] == pieces[second] && pieces[first] == pieces[third]) {
            pieces[first]
        } else {
            null
        }
    }
}