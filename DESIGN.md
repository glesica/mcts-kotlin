# Library Design

## Data structures

### Game (`Game`)

Consumers must implement a `Game` sub-class that encapsulates
all necessary game mechanics. An instance of this implementation
is then injected into the MCTS engine.

### Board (`TBoard`)

A board must encapsulate all relevant game state. It need not
implement equality and there are no other special constraints
on the implementation.

### Move (`TMove`)

A position only needs to contain enough information to allow the
transformation of one board state into another board state.

For example, in Chess a position might be expressed `A2 -> A3`. This
position could be applied to any board state, though it might or
might not result in a legal position.

The MCTS engine does not check to see if a given position is legal
or productive, so it is up to the consumer to embed these concepts
in the `Game` implementation.

Moves must also implement equality (`equal` and `hashCode`) such
that two moves that compare as equal are also equivalent in the
context of the game mechanics.

### Player (`TPlayer`)

A player type must implement `equal` and `hashCode` and just
represents a player within the context of the game, player 1,
player 2, and so on.

For games with a fixed number of players should should probably
be an enum. For games with a variable number of players it can
be an integer or even a data class.

## Transformations

A move can be applied to a board to yield a new board which might
or might not differ from the original (depending on the game
mechanics). If two board instances are the same in terms of the
game mechanics, then they can be represented by the same object,
however, board states should never be mutated in-place, updates
should always result in a copy.

```
F(M, B0) -> B1
```

If two move instances produce identical boards, then the move
instances should compare as equal to one another (in terms of
their `equal` and `hashCode` methods).
