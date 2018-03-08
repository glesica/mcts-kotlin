package mcts

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class NodeTest {

    val rootBoard = "root"

    val player0 = 0

    val player1 = 1

    fun getRoot() = Node<String, String, Int>(rootBoard, player0)

    @Nested
    inner class IsLeaf {

        @Test
        fun `should return true if children is empty`() {
            val root = getRoot()
            assertEquals(0, root.children.size)
            assertTrue(root.isLeaf())
        }

        @Test
        fun `should return false if children is not empty`() {
            val root = getRoot()
            root.add("move", "child", 1)
            assertEquals(1, root.children.size)
            assertFalse(root.isLeaf())
        }
    }

    @Nested
    inner class Add {

        @Test
        fun `should add and return new child`() {
            val root = getRoot()
            val child = root.add("move", "child", 1)
            assertSame(child, root.children.values.first())
        }

        @Test
        fun `should assign board property of child`() {
            val root = getRoot()
            val child = root.add("move", "child", 1)
            assertEquals("child", child.board)
        }

        @Test
        fun `should assign player property of child`() {
            val root = getRoot()
            val child = root.add("move", "child", 1)
            assertEquals(1, child.player)
        }

        @Test
        fun `should assign parent property of child`() {
            val root = getRoot()
            val child = root.add("move", "child", 1)
            assertSame(root, child.parent)
        }

        @Test
        fun `should add and return a second child`() {
            val root = getRoot()
            val child0 = root.add("move0", "child0", 1)
            val child1 = root.add("move1", "child1", 2)
            assertIterableEquals(listOf(child0, child1), root.children.values)
            assertEquals("child1", child1.board)
            assertEquals(2, child1.player)
            assertEquals(root, child1.parent)
        }
    }

    @Nested
    inner class Visited {

        @Test
        fun `should increment root counts when player 0 won`() {
            val root = getRoot()
            root.visited(listOf(player0))
            assertEquals(1, root.visitCount)
            assertEquals(1, root.winCount)
        }

        @Test
        fun `should increment root counts when player 1 won`() {
            val root = getRoot()
            root.visited(listOf(player1))
            assertEquals(1, root.visitCount)
            assertEquals(1, root.winCount)
        }

        @Test
        fun `should increment child counts if root player won`() {
            val root = getRoot()
            val child = root.add("move", "child", player1)
            child.visited(listOf(player0))
            assertEquals(1, child.visitCount)
            assertEquals(1, child.winCount)
        }

        @Test
        fun `should not increment child win count if root player lost`() {
            val root = getRoot()
            val child = root.add("move", "child", player1)
            child.visited(listOf(player1))
            assertEquals(1, child.visitCount)
            assertEquals(0, child.winCount)
        }
    }
}