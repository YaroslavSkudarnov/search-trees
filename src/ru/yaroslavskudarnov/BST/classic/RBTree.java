package ru.yaroslavskudarnov.BST.classic;

import ru.yaroslavskudarnov.BST.core.BinarySearchTree;

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

        RBTreeNode(E payload, RBTreeNode parent) {
            super(payload);
            this.color = Color.RED;
            this.parent = parent;
        }

        private RBTreeNode uncle() {
            if (parent == null) {
                return null;
            }

            return parent.sibling();
        }

        private RBTreeNode sibling() {
            if (parent == null) {
                return null;
            }

            return parent.left == this ? parent.right : parent.left;
        }
    }

    private Color getColor(RBTreeNode node) {
        return node == null ? Color.BLACK : node.color;
    }

    private void rebalanceAfterInserting(RBTreeNode node) {
        if (node.parent == null) {
            node.color = Color.BLACK;
        } else if (node.parent.color != Color.BLACK) {
            RBTreeNode uncle = node.uncle();

            if (getColor(uncle) == Color.BLACK) {
                if ((node.parent.parent.left != null) && (node.parent.parent.left.right == node)) {
                    minorLeftRotationCommon(node.parent); // place node in the left border of a subtree
                    node = node.left;
                } else if ((node.parent.parent.right != null)  && (node.parent.parent.right.left == node)) {
                    minorRightRotationCommon(node.parent); // place node in the right border of a subtree
                    node = node.right;
                }

                node.parent.color = Color.BLACK;
                node.parent.parent.color = Color.RED;

                if (node.parent.left == node) {
                    minorRightRotationCommon(node.parent.parent);
                } else {
                    minorLeftRotationCommon(node.parent.parent);
                }
            } else {
                assert uncle != null; // uncle can't be null if his color is red
                uncle.color = Color.BLACK;
                node.parent.color = Color.BLACK;
                node.parent.parent.color = Color.RED;

                rebalanceAfterInserting(node.parent.parent);
            }
        }
    }

    @Override
    protected boolean addToSubtree(E e, RBTreeNode node) {
        int compare = node.compareTo(e);

        if (compare == 0) {
            return false;
        }

        boolean result; RBTreeNode insertedNode = null;

        if (compare < 0) {
            if (node.right == null) {
                node.right = new RBTreeNode(e, node);
                result = true;
                insertedNode = node.right;
            } else {
                result = addToSubtree(e, node.right);
            }
        } else {
            if (node.left == null) {
                node.left = new RBTreeNode(e, node);
                result = true;
                insertedNode = node.left;
            } else {
                result = addToSubtree(e, node.left);
            }
        }

        if (insertedNode != null) {
            rebalanceAfterInserting(insertedNode);
        }

        return result;
    }

    @Override
    protected RBTreeNode initFirstNode(E e) {
        RBTreeNode node = new RBTreeNode(e, null); node.color = Color.BLACK;
        return node;
    }

    private void rebalanceAfterRemoval(RBTreeNode node, RBTreeNode replacement) {
        if (node.color == Color.BLACK) { // if node.color is Color.RED, all the properties of red-black tree still hold true after removal
            if (getColor(replacement) == Color.RED) {
                replacement.color = Color.BLACK;
                updateLinks(node, node.parent, replacement);
            } else {
                RBTreeNode parent = node.parent;
                updateLinks(node, parent, replacement);
                repaintTree(replacement, parent);
            }
        } else {
            if (node.parent.left == node) {
                node.parent.left = replacement;
            } else {
                node.parent.right = replacement;
            }

            checkNullAndSetParent(replacement, node.parent);
        }
    }

    private void repaintTree(RBTreeNode node, RBTreeNode parent) {
        if (parent == null) {
            if (node != null) {
                node.color = Color.BLACK;
            }
            return;
        }

        RBTreeNode sibling = getSiblingEvenForNullNode(node, parent);

        if (getColor(sibling) == Color.RED) {
            if (node == parent.left) {
                minorLeftRotationCommon(parent);
            } else {
                minorRightRotationCommon(parent);
            }

            parent.color = Color.RED;
            parent.parent.color = Color.BLACK;
            sibling = getSiblingEvenForNullNode(node, parent);
        }

        if ((getColor(sibling.left) == Color.BLACK) && (getColor(sibling.right) == Color.BLACK)) {
            sibling.color = Color.RED;

            if (getColor(parent) == Color.BLACK) {
                repaintTree(parent, parent.parent);
            } else {
                parent.color = Color.BLACK;
            }
        } else {
            if ((node == parent.left) && (getColor(sibling.right) == Color.BLACK) && (getColor(sibling.left) == Color.RED)) {
                sibling.color = Color.RED;
                sibling.left.color = Color.BLACK;
                minorRightRotationCommon(sibling);
            } else if ((node == parent.right) && (getColor(sibling.left) == Color.BLACK) && (getColor(sibling.right) == Color.RED)) {
                sibling.color = Color.RED;
                sibling.right.color = Color.BLACK;
                minorLeftRotationCommon(sibling);
            }

            sibling = getSiblingEvenForNullNode(node, parent);

            sibling.color = parent.color;
            parent.color = Color.BLACK;

            if (node == parent.left) {
                sibling.right.color = Color.BLACK;
                minorLeftRotationCommon(parent);
            } else {
                sibling.left.color = Color.BLACK;
                minorRightRotationCommon(parent);
            }
        }
    }

    private RBTreeNode getSiblingEvenForNullNode(RBTreeNode node, RBTreeNode parent) {
        return node == null ? (parent.left == null ? parent.right : parent.left) : node.sibling();
    }

    @Override
    protected boolean removeFromSubtree(E e, RBTreeNode node) {
        int compare = node.compareTo(e);

        if (compare == 0) {
            RBTreeNode replacement;

            if ((node.left != null) && (node.right != null)) {
                replacement = getNext(node);

                remove(replacement.payload);
                replacement.left = node.left; replacement.right = node.right;
                replacement.color = node.color;
                RBTreeNode parent = findParent(e); replacement.parent = parent;
                updateLinks(node, parent, replacement);
            } else {
                replacement = node.left == null ? node.right : node.left;
                rebalanceAfterRemoval(node, replacement);
            }

            return true;
        } else if (compare < 0) {
            return node.right != null && removeFromSubtree(e, node.right);
        } else {
            return node.left != null && removeFromSubtree(e, node.left);
        }
    }
}
