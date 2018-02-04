package ru.yaroslavskudarnov.BST.main;

import ru.yaroslavskudarnov.BST.core.SearchTree;
import ru.yaroslavskudarnov.BST.core.TreeNode;

import java.util.Collection;

/**
 * User: Skudarnov Yaroslav
 * Date: 12/17/2017
 * Time: 11:41 PM
 */
public abstract class BinarySearchTree<E extends Comparable<? super E>> extends SearchTree<E> {
    protected class BinarySearchTreeNode extends TreeNode {
        protected BinarySearchTreeNode left, right;
        protected E payload;

        BinarySearchTreeNode(E payload) {
            this.payload = payload;
        }
        BinarySearchTreeNode(BinarySearchTreeNode left, BinarySearchTreeNode right, E payload) {
            this.left = left;
            this.right = right;
            this.payload = payload;
        }

        BinarySearchTreeNode leftmostDescendant() {
            BinarySearchTreeNode tmp = this;

            while (tmp.left != null) {
                tmp = tmp.left;
            }

            return tmp;
        }

        BinarySearchTreeNode getPrevious() {
            BinarySearchTreeNode tmp = root, tmpResult = null;

            while (tmp != null) {
                if (tmp.payload.compareTo(payload) < 0) {
                    tmpResult = tmp;

                    tmp = tmp.right;
                } else {
                    if (tmp.left != null) {
                        tmp = tmp.left;
                    } else {
                        return tmpResult;
                    }
                }
            }

            return tmpResult;
        }

        BinarySearchTreeNode getNext() {
            BinarySearchTreeNode tmp = root, tmpResult = null;

            while (tmp != null) {
                if (tmp.payload.compareTo(payload) > 0) {
                    tmpResult = tmp;

                    tmp = tmp.left;
                } else {
                    if (tmp.right != null) {
                        tmp = tmp.right;
                    } else {
                        return tmpResult;
                    }
                }
            }

            return tmpResult;
        }

        protected int compareTo(E e) {
            return payload.compareTo(e);
        }

        protected void replaceContent(BinarySearchTreeNode replacement) {
            this.payload = replacement.payload;
            this.left = replacement.left;
            this.right = replacement.right;
        }
    }

    protected BinarySearchTreeNode root;

    protected BinarySearchTree() {}

    public BinarySearchTree(Collection<E> collection) {
        super(collection);
    }

    private int subtreeSize(BinarySearchTreeNode node) {
        int size = 1;

        size += node.left == null ? 0 : subtreeSize(node.left);
        size += node.right == null ? 0 : subtreeSize(node.right);

        return size;
    }

    @Override
    public int size() {
        if (isEmpty()) {
            return 0;
        } else {
            return subtreeSize(root);
        }
    }

    public boolean isEmpty() {
        return root == null;
    }
    
    @Override
    public boolean contains(Object o) {
        if ((o == null) || (root == null)) {
            return false;
        } else {
            @SuppressWarnings("unchecked")
                E e = (E) o;

            return subtreeContains(e, root);
        }
    }

    private boolean subtreeContains(E e, BinarySearchTreeNode node) {
        int compare = node.compareTo(e);

        if (compare == 0) {
            return true;
        } else if (compare < 0) {
            return node.right != null && subtreeContains(e, node.right);
        } else {
            return node.left != null && subtreeContains(e, node.left);
        }
    }
}
