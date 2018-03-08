package example.tictactoe

import mcts.Game

class TicTacToe: Game<Move, Board, Player> {

    override fun apply(move: Move, board: Board) = board.apply(move)

    override fun firstPlayer() = Player.X

    override fun initialBoard() = Board()

    override fun isOver(board: Board) = possibleMoves(board).isEmpty() || board.winner() != null

    override fun possibleMoves(board: Board) = board.possibleMoves()

    override fun nextPlayer(current: Player) = current.next()

    override fun scores(board: Board): Map<Player, Int> {
        if (!isOver(board)) {
            return HashMap()
        }

        val winner = board.winner()

        if (winner == null) {
            return mapOf(Pair(Player.X, 0), Pair(Player.O, 0))
        } else {
            val loser = winner.next()
            return mapOf(Pair(winner, 1), Pair(loser, 0))
        }
    }
}