package ru.yaroslavskudarnov.BST.classic;

import ru.yaroslavskudarnov.BST.core.BinarySearchTree;

import java.util.Collection;

/**
 * User: Skudarnov Yaroslav
 * Date: 10/2/2017
 * Time: 12:34 PM
 */
public class SimpleBinarySearchTree<E extends Comparable<? super E>> extends BinarySearchTree<E, SimpleBinarySearchTree<E>.SimpleBinarySearchTreeNode> {
    class SimpleBinarySearchTreeNode extends BinarySearchTree<E, SimpleBinarySearchTreeNode>.BinarySearchTreeNode {
        SimpleBinarySearchTreeNode(E payload, SimpleBinarySearchTreeNode parent) {
            super(payload); this.parent = parent;
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
                node.right = new SimpleBinarySearchTreeNode(e, node);
                return true;
            } else {
                return addToSubtree(e, node.right);
            }
        } else {
            if (node.left == null) {
                node.left = new SimpleBinarySearchTreeNode(e, node);
                return true;
            } else {
                return addToSubtree(e, node.left);
            }
        }
    }

    @Override
    protected SimpleBinarySearchTreeNode initFirstNode(E e) {
        return new SimpleBinarySearchTreeNode(e, null);
    }

    protected boolean removeFromSubtree(E e, SimpleBinarySearchTreeNode node) {
        int compare = node.compareTo(e);

        SimpleBinarySearchTreeNode parent = node.parent;

        if (compare == 0) {
            SimpleBinarySearchTreeNode replacement;

            if (node.left == null) {
                replacement = node.right;
            } else {
                if (node.right == null) {
                    replacement = node.left;
                } else {
                    replacement = getNext(node);

                    if (replacement == null) {
                        replacement = getPrevious(node);
                    }

                    remove(replacement.payload);
                    replacement.left = node.left; replacement.right = node.right;
                }
            }

            updateLinks(node, parent, replacement);

            return true;
        } else if (compare < 0) {
            return node.right != null && removeFromSubtree(e, node.right);
        } else {
            return node.left != null && removeFromSubtree(e, node.left);
        }
    }
}
