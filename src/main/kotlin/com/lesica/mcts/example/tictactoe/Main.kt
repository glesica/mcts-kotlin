package com.lesica.mcts.example.tictactoe

import com.lesica.mcts.Engine
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    val game = TicTacToe()
    val engine = Engine(game)

    while (!game.isOver(engine.board)) {
        println(engine.board)

        val nextMove = if (engine.board.nextPlayer() == Player.X) {
            getMove()
        } else {
            engine.runBest(10)!!
        }

        engine.apply(nextMove)
    }

    println(engine.board)
    println("Winner: ${engine.board.winner() ?: "Draw"}")
}

fun getMove(): Move {
    println("Move (? for help):")

    while (true) {
        val input = readLine()?.trim()?.toUpperCase() ?: continue
        if (input == "?") {
            printHelp()
            continue
        }
        if (input == "Q") {
            println("Bye")
            exitProcess(0)
        }
        try {
            return Move.valueOf(input)
        } catch (e: IllegalArgumentException) {
            println("Invalid move, try again")
        }
    }
}

fun printHelp() {
    println("Possible moves:")
    println("")
    println("NW | N | NE")
    println("-----------")
    println(" W | C | E ")
    println("-----------")
    println("SW | S | SE")
    println("")
    println("Q to quit early")
}
