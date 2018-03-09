package com.lesica.mcts.example.tictactoe

enum class Player {
    X,
    O;

    fun next() = if (this == X) O else X
}