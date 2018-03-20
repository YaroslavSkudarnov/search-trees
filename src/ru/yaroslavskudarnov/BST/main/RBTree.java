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

    @Override
    protected boolean removeFromSubtree(E e, RBTreeNode node) {
        int compare = node.compareTo(e);

        boolean result;

        RBTreeNode parent = node.parent;

        if (compare == 0) {
            RBTreeNode replacement;

            if (node.left == null) {
                replacement = node.right;
            } else {
                if (node.right == null) {
                    replacement = node.left;
                } else {
                    RBTreeNode next = getNext(node);

                    if (next == null) {
                        replacement = node.left;
                    } else {
                        replacement = next;
                        remove(replacement.payload);
                        replacement.left = node.left; replacement.right = node.right;
                        replacement.color = node.color;
                        parent = findParent(e); replacement.parent = parent;
                    }
                }
            }

            updateLinks(e, node, parent, replacement);
            rebalanceAfterInserting(insertedNode);

            result = true;
        } else if (compare < 0) {
            result = node.right != null && removeFromSubtree(e, node.right);
        } else {
            result = node.left != null && removeFromSubtree(e, node.left);
        }

        return result;
    }
}
