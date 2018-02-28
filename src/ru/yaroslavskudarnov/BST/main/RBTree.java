package ru.yaroslavskudarnov.BST.main;

/**
 * User: Skudarnov Yaroslav
 * Date: 2/24/2018
 * Time: 12:27 PM
 */
public class RBTree<E extends Comparable<? super E>> extends BinarySearchTree<E, RBTree<E>.RBTreeNode> {
    enum Color {
        RED, BLACK
    }

    class RBTreeNode extends BinarySearchTree<E, RBTreeNode>.BinarySearchTreeNode {
        Color c;
        RBTreeNode parent;

        RBTreeNode(E payload) {
            super(payload);
            this.c = Color.RED;
        }

        RBTreeNode(RBTreeNode node, Color c) {
            super(node);
            this.c = c;
        }
    }

    @Override
    protected boolean addToSubtree(E e, RBTreeNode node) {
        return false;
    }

    @Override
    protected RBTreeNode initNode(E e) {
        return new RBTreeNode(e);
    }

    @Override
    protected boolean removeFromSubtree(E e, RBTreeNode node) {
        return false;
    }
}
