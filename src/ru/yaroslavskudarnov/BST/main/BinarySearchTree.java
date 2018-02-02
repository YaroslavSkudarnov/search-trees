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
        private BinarySearchTreeNode left, right;
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

        private int compareTo(E e) {
            return payload.compareTo(e);
        }

        private void replaceContent(BinarySearchTreeNode replacement) {
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

    boolean addToSubtree(E e, BinarySearchTreeNode node) {
        int compare = node.compareTo(e);

        if (compare == 0) {
            return false;
        } else if (compare < 0) {
            if (node.right == null) {
                node.right = new BinarySearchTreeNode(e);
                return true;
            } else {
                return addToSubtree(e, node.right);
            }
        } else {
            if (node.left == null) {
                node.left = new BinarySearchTreeNode(e);
                return true;
            } else {
                return addToSubtree(e, node.left);
            }
        }
    }

    boolean removeFromSubtree(E e, BinarySearchTreeNode node, BinarySearchTreeNode parent) {
        int compare = node.compareTo(e);

        if (compare == 0) {
            BinarySearchTreeNode replacement;

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
                    BinarySearchTreeNode next = node.getNext();

                    if (next == null) {
                        replacement = node.left;
                    } else {
                        replacement = next;
                        BinarySearchTree.this.remove(replacement.payload);
                        replacement = new BinarySearchTreeNode(node.left, node.right, replacement.payload);
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
            return node.right != null && removeFromSubtree(e, node.left, node);
        } else {
            return node.left != null && removeFromSubtree(e, node.left, node);
        }
    }
}
