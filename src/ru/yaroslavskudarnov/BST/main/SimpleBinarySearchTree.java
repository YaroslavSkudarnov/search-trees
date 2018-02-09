package ru.yaroslavskudarnov.BST.main;

import java.util.Collection;

/**
 * User: Skudarnov Yaroslav
 * Date: 10/2/2017
 * Time: 12:34 PM
 */
public class SimpleBinarySearchTree<E extends Comparable<? super E>> extends BinarySearchTree<E, SimpleBinarySearchTree<E>.SimpleBinarySearchTreeNode> {
    class SimpleBinarySearchTreeNode extends BinarySearchTree<E, SimpleBinarySearchTreeNode>.BinarySearchTreeNode {
        SimpleBinarySearchTreeNode(E payload) {
            super(payload);
        }

        SimpleBinarySearchTreeNode(SimpleBinarySearchTreeNode node) {
            super(node);
        }
    }

    public SimpleBinarySearchTree() { super(); }

    public SimpleBinarySearchTree(Collection<E> collection) {
        super(collection);
    }

    protected boolean addToSubtree(E e, SimpleBinarySearchTreeNode node) {
        int compare = node.compareTo(e);

        if (compare == 0) {
            return false;
        } else if (compare < 0) {
            if (node.right == null) {
                node.right = new SimpleBinarySearchTreeNode(e);
                return true;
            } else {
                return addToSubtree(e, node.right);
            }
        } else {
            if (node.left == null) {
                node.left = new SimpleBinarySearchTreeNode(e);
                return true;
            } else {
                return addToSubtree(e, node.left);
            }
        }
    }

    @Override
    protected SimpleBinarySearchTreeNode initNode(E e) {
        return new SimpleBinarySearchTreeNode(e);
    }

    protected boolean removeFromSubtree(E e, SimpleBinarySearchTreeNode node, SimpleBinarySearchTreeNode parent) {
        int compare = node.compareTo(e);

        if (compare == 0) {
            SimpleBinarySearchTreeNode replacement;

            if (node.left == null) {
                if (node.right == null) {
                    replacement = null;
                } else {
                    replacement = node.right;
                }
            } else {
                if (node.right == null) {
                    replacement = node.left;
                } else {
                    SimpleBinarySearchTreeNode next = getNext(node);

                    if (next == null) {
                        replacement = node.left;
                    } else {
                        replacement = next;
                        SimpleBinarySearchTree.this.remove(replacement.payload);
                        replacement = new SimpleBinarySearchTreeNode(node);
                    }
                }
            }

            if (parent == null) {
                if (replacement == null) {
                    node.payload = null;
                } else {
                    node.replaceContent(replacement);
                }
            } else {
                if ((parent.left != null) && (parent.left.compareTo(e) == 0)) {
                    parent.left = replacement;
                } else {
                    parent.right = replacement;
                }
            }

            return true;
        } else if (compare < 0) {
            return node.right != null && removeFromSubtree(e, node.right, node);
        } else {
            return node.left != null && removeFromSubtree(e, node.left, node);
        }
    }
}
