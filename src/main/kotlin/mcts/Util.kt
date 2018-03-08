package mcts

import java.util.*

// TODO: Allow injection of a custom Random instance

private val defaultRandom = Random()

/**
 * Extension method on any list that will return a random element
 * between index 0 and the last index, inclusive.
 *
 * Source: Rosetta Code FTW
 */
fun <T> List<T>.chooseRandom(random: Random = defaultRandom) =
        this[random.nextInt(this.size)]

/**
 * Extension method to make checking for even integers read
 * a little better.
 */
fun Int.isEven() = this.rem(2) == 0
