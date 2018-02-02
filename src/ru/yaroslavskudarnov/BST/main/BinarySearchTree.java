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

        BinarySearchTreeNode getPrevious(BinarySearchTreeNode root) {
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

        BinarySearchTreeNode getNext(BinarySearchTreeNode root) {
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

        private int subtreeSize() {
            int size = 1;

            size += left == null ? 0 : left.subtreeSize();
            size += right == null ? 0 : right.subtreeSize();

            return size;
        }

        private int compareTo(E e) {
            return payload.compareTo(e);
        }

        private boolean subtreeContains(E e) {
            int compare = compareTo(e);

            if (compare == 0) {
                return true;
            } else if (compare < 0) {
                return right != null && right.subtreeContains(e);
            } else {
                return left != null && left.subtreeContains(e);
            }
        }

        boolean addToSubtree(E e) {
            int compare = compareTo(e);

            if (compare == 0) {
                return false;
            } else if (compare < 0) {
                if (right == null) {
                    right = new BinarySearchTreeNode(e);
                    return true;
                } else {
                    return right.addToSubtree(e);
                }
            } else {
                if (left == null) {
                    left = new BinarySearchTreeNode(e);
                    return true;
                } else {
                    return left.addToSubtree(e);
                }
            }
        }

        boolean removeFromSubtree(E e, BinarySearchTreeNode parent) {
            int compare = compareTo(e);

            if (compare == 0) {
                BinarySearchTreeNode replacement;

                if (left == null) {
                    if (right == null) {
                        replacement = null;
                    } else {
                        replacement = right;
                    }
                } else {
                    if (right == null) {
                        replacement = left;
                    } else {
                        BinarySearchTreeNode next = getNext(root);

                        if (next == null) {
                            replacement = left;
                        } else {
                            replacement = next;
                            BinarySearchTree.this.remove(replacement.payload);
                            replacement = new BinarySearchTreeNode(left, right, replacement.payload);
                        }
                    }
                }

                if (parent == null) {
                    if (replacement == null) {
                        this.payload = null;
                    } else {
                        replaceContent(replacement);
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
                return right != null && right.removeFromSubtree(e, this);
            } else {
                return left != null && left.removeFromSubtree(e, this);
            }
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
    
    @Override
    public int size() {
        if (isEmpty()) {
            return 0;
        } else {
            return root.subtreeSize();
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

            return root.subtreeContains(e);
        }
    }
}
