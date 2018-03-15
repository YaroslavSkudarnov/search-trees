package ru.yaroslavskudarnov.BST.main;

import java.util.Objects;

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
        Color color;
        RBTreeNode parent;

        RBTreeNode(E payload, RBTreeNode parent) {
            super(payload);
            this.color = Color.RED;
            this.parent = parent;
        }

        private RBTreeNode uncle() {
            RBTreeNode grandparent;

            if ((parent == null) || (parent.parent == null)) {
                return null;
            }

            grandparent = parent.parent;

            return grandparent.left == parent ? grandparent.right : grandparent.left;
        }
    }

    private Color getColor(RBTreeNode node) {
        return node == null ? Color.BLACK : node.color;
    }

    private void rebalanceAfterInserting(RBTreeNode node) {
        if (node.parent == null) {
            node.color = Color.BLACK;
        } else if (node.parent.color != Color.BLACK) {
            if (getColor(node.uncle()) == Color.BLACK) {
                node.parent.color = Color.BLACK;
                node.parent.parent.color = Color.RED;

                if (node.parent.left == node) {
                    minorRightRotationCommon(node, node.parent);
                } else {
                    minorLeftRotationCommon(node, node.parent);
                }
            } else {
                RBTreeNode uncle = node.uncle();
                assert uncle != null; // uncle can't be null if his color is red
                uncle.color = Color.BLACK;
                node.parent.color = Color.BLACK;
                node.parent.parent.color = Color.RED;
            }
        }
    }

    @Override
    protected boolean addToSubtree(E e, RBTreeNode node) {
        int compare = node.compareTo(e);

        if (compare == 0) {
            return false;
        }

        boolean result, insertedNow = false;

        if (compare < 0) {
            if (node.right == null) {
                node.right = new RBTreeNode(e, node);
                result = true;
                insertedNow = true;
            } else {
                result = addToSubtree(e, node.right);
            }
        } else {
            if (node.left == null) {
                node.left = new RBTreeNode(e, node);
                result = true;
                insertedNow = true;
            } else {
                result = addToSubtree(e, node.left);
            }
        }

        if (insertedNow) {
            rebalanceAfterInserting(node);
        }

        return result;
    }

    @Override
    protected RBTreeNode initFirstNode(E e) {
        return new RBTreeNode(e, null);
    }

    @Override
    protected boolean removeFromSubtree(E e, RBTreeNode node) {
        return false;
    }
}
