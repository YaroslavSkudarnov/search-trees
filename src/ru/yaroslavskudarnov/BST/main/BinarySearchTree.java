package ru.yaroslavskudarnov.BST.main;

import ru.yaroslavskudarnov.BST.core.SearchTree;
import ru.yaroslavskudarnov.BST.core.TreeNode;

import java.util.Collection;

/**
 * User: Skudarnov Yaroslav
 * Date: 12/17/2017
 * Time: 11:41 PM
 */
public abstract class BinarySearchTree<E extends Comparable<? super E>, N extends BinarySearchTree<E, N>.BinarySearchTreeNode> extends SearchTree<E> {
    abstract class BinarySearchTreeNode implements TreeNode {
        protected N left, right;
        protected E payload;

        BinarySearchTreeNode(E payload) {
            this.payload = payload;
        }
        BinarySearchTreeNode(N node) {
            replaceContent(node);
        }

        protected int compareTo(E e) {
            return payload.compareTo(e);
        }

        protected void replaceContent(N replacement) {
            this.payload = replacement.payload;
            this.left = replacement.left;
            this.right = replacement.right;
        }

        N getPrevious() {
            N tmp = root, tmpResult = null;

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

        N getNext() {
            N tmp = root, tmpResult = null;

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

        N leftmostDescendant() {
            @SuppressWarnings("unchecked")
            N tmp = (N) this;

            while (tmp.left != null) {
                tmp = tmp.left;
            }

            return (N) tmp;
        }
    }

    protected N root;

    protected BinarySearchTree() {}

    public BinarySearchTree(Collection<E> collection) {
        super(collection);
    }

    protected int subtreeSize(BinarySearchTreeNode node) {
        int size = 1;

        size += node.left == null ? 0 : subtreeSize(node.left);
        size += node.right == null ? 0 : subtreeSize(node.right);

        return size;
    }
}
