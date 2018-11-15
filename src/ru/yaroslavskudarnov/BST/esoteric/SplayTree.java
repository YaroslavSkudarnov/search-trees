package ru.yaroslavskudarnov.BST.esoteric;

import ru.yaroslavskudarnov.BST.core.BinarySearchTree;

/**
 * User: Skudarnov Yaroslav
 * Date: 11/12/2018
 * Time: 5:24 PM
 */
public class SplayTree<E extends Comparable<? super E>> extends BinarySearchTree<E, SplayTree<E>.SplayTreeNode> {
    class SplayTreeNode extends BinarySearchTree<E, SplayTreeNode>.BinarySearchTreeNode {
        SplayTreeNode(E e, SplayTreeNode parent) {
            this(e);
            this.parent = parent;
        }

        SplayTreeNode(E e) {
            super(e);
        }
    }

    @Override
    protected boolean subtreeContains(E e, SplayTreeNode node) {
        int compare = node.compareTo(e);

        if (compare == 0) {
            splay(node);
            return true;
        } else if (compare < 0) {
            return node.right != null && subtreeContains(e, node.right);
        } else {
            return node.left != null && subtreeContains(e, node.left);
        }
    }

    private void splay(SplayTreeNode node) {
        if (node.parent == null) {
            root = node;
        } else if (node.parent == root) {
            zig(node);
        } else {
            if ((node.parent.parent.left == node.parent) == (node.parent.left == node)) {
                zigzig(node);
            } else {
                zigzag(node);
            }

            splay(node);
        }
    }

    private void zig(SplayTreeNode node) {
        if (node.parent.left == node) {
            minorRightRotationCommon(node.parent);
        } else {
            minorLeftRotationCommon(node.parent);
        }
    }

    private void zigzig(SplayTreeNode node) {
        if (node.parent.left == node) {
            minorRightRotationCommon(node.parent.parent); minorRightRotationCommon(node.parent);
        } else {
            minorLeftRotationCommon(node.parent.parent); minorLeftRotationCommon(node.parent);
        }
    }

    private void zigzag(SplayTreeNode node) {
        if (node.parent.left == node) {
            minorRightRotationCommon(node.parent); minorLeftRotationCommon(node.parent);
        } else {
            minorLeftRotationCommon(node.parent); minorRightRotationCommon(node.parent);
        }
    }

    @Override
    protected boolean addToSubtree(E e, SplayTreeNode node) {
        int compare = node.compareTo(e);

        if (compare == 0) {
            return false;
        } else if (compare < 0) {
            if (node.right == null) {
                node.right = new SplayTreeNode(e, node);
                splay(node.right);
                return true;
            } else {
                return addToSubtree(e, node.right);
            }
        } else {
            if (node.left == null) {
                node.left = new SplayTreeNode(e, node);
                splay(node.left);
                return true;
            } else {
                return addToSubtree(e, node.left);
            }
        }
    }

    @Override
    protected SplayTreeNode initFirstNode(E e) {
        return new SplayTreeNode(e);
    }

    @Override
    protected boolean removeFromSubtree(E e, SplayTreeNode node) {
        int compare = node.compareTo(e);

        if (compare == 0) {
            splay(node);
            merge(node.left, node.right);

            return true;
        } else if (compare < 0) {
            return node.right != null && removeFromSubtree(e, node.right);
        } else {
            return node.left != null && removeFromSubtree(e, node.left);
        }
    }

    private void merge(SplayTreeNode leftSubtree, SplayTreeNode rightSubtree) {
        SplayTreeNode newRoot = leftmostDescendant(rightSubtree);

        if (newRoot != null) {
            splay(newRoot);
            newRoot.left = leftSubtree;
            checkNullAndSetParent(leftSubtree, newRoot);
        } else {
            root = leftSubtree;
            checkNullAndSetParent(leftSubtree, null);
        }
    }
}
