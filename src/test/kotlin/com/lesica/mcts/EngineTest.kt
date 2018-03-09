package com.lesica.mcts

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EngineTest {

    val game = TestGame(10)

    @Nested
    inner class Apply {

        @Test
        fun `should update currentNode board`() {
            val board = game.initialBoard()

            for (move in game.possibleMoves(board)) {
                val engine = Engine(game)
                engine.apply(move)
                assertEquals(game.apply(move, board), engine.currentNode.board)
            }
        }

        @Test
        fun `should set currentNode to a new instance`() {
            val board = game.initialBoard()

            for (move in game.possibleMoves(board)) {
                val engine = Engine(game)
                val node = engine.currentNode
                engine.apply(move)
                assertNotSame(node, engine.currentNode)
            }
        }
    }

    @Nested
    inner class Backpropagate {

        @Test
        fun `should update counts on root node for first player`() {
            val engine = Engine(game)
            val root = Node<String, String, Int>("root", 0)

            assertEquals(0, root.visitCount)
            assertEquals(0, root.winCount)
            engine.backpropagate(root, listOf(0))
            assertEquals(1, root.visitCount)
            assertEquals(1, root.winCount)
        }

        @Test
        fun `should update counts on root node for second player`() {
            val engine = Engine(game)
            val root = Node<String, String, Int>("root", 0)

            assertEquals(0, root.visitCount)
            assertEquals(0, root.winCount)
            engine.backpropagate(root, listOf(1))
            assertEquals(1, root.visitCount)
            assertEquals(1, root.winCount)
        }

        @Test
        fun `should update counts on child for first player`() {
            val engine = Engine(game)
            val root = Node<String, String, Int>("root", 0)
            val child = root.add("move", "child", 1)

            assertEquals(0, child.visitCount)
            assertEquals(0, child.winCount)
            engine.backpropagate(child, listOf(0))
            assertEquals(1, child.visitCount)
            assertEquals(1, child.winCount)
        }

        @Test
        fun `should update counts on child for second player`() {
            val engine = Engine(game)
            val root = Node<String, String, Int>("root", 0)
            val child = root.add("move", "child", 1)

            assertEquals(0, child.visitCount)
            assertEquals(0, child.winCount)
            engine.backpropagate(child, listOf(1))
            assertEquals(1, child.visitCount)
            assertEquals(0, child.winCount)
        }
    }

    @Nested
    inner class Expand {

        @Test
        fun `should fully expand node by one layer`() {
            val engine = Engine(game)
            val root = Node<String, String, Int>("root", 0)

            assertEquals(0, root.children.size)
            val child = engine.expand(root)
            assertSame(root, child.parent)
            assertTrue(root.children.values.contains(child), "Child is not in root.children")
            assertEquals(2, root.children.size)
            assertEquals(child.player, game.nextPlayer(root.player))
        }
    }

    @Nested
    inner class Select {

        @Test
        fun `should return root if it is a leaf`() {
            val engine = Engine(game)
            val root = Node<String, String, Int>("root", 0)
            val selected = engine.select(root)

            assertSame(root, selected)
        }

        @Test
        fun `should return leaf when one exists`() {
            val engine = Engine(game)
            val root = Node<String, String, Int>("root", 0)
            val child = root.add("move", "child", 1)
            val selected = engine.select(root)

            assertSame(child, selected)
        }

        @Test
        fun `should return a leaf when there are multiple choices`() {
            val engine = Engine(game)
            val root = Node<String, String, Int>("root", 0)
            root.add("move0", "child0", 1)
            root.add("move1", "child1", 1)
            val selected = engine.select(root)

            assertTrue(root.children.values.contains(selected), "Selected is in root.children")
        }
    }

    @Nested
    inner class Simulate {

        // TODO
    }
}