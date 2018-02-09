package ru.yaroslavskudarnov.BST.main;

import java.util.Collection;

/**
 * User: Skudarnov Yaroslav
 * Date: 11/3/2017
 * Time: 1:18 PM
 */
public class AVLTree<E extends Comparable<? super E>> extends BinarySearchTree<E, AVLTree<E>.AVLTreeNode> {
    private class AVLTreeNode extends BinarySearchTreeNode {
        private int balance;

        AVLTreeNode(E payload) {
            super(payload);
            this.balance = 0;
        }

        AVLTreeNode(AVLTreeNode node, int balance) {
            super(node);
            this.balance = balance;
        }
    }

    public AVLTree() {}

    public AVLTree(Collection<E> collection) {
        super(collection);
    }

    @Override
    public boolean add(E e) {
        if (root == null) {
            root = new AVLTreeNode(e);
            return true;
        } else {
            return addToSubtree(e, root, null);
        }
    }

    @Override
    public boolean remove(Object o) {
        if ((o == null) || (root == null)) {
            return false;
        } else {
            @SuppressWarnings("unchecked")
            E e = (E) o;

            int compare = root.payload.compareTo(e);

            if (compare == 0) {
                E tmpPayload = null;

                if (root.getNext() != null) {
                    tmpPayload = root.getNext().payload;
                } else if (root.getPrevious() != null) {
                    tmpPayload = root.getPrevious().payload;
                } else {
                    root = null;
                }

                if (root != null) {
                    remove(tmpPayload);
                    root.payload = tmpPayload;
                }

                return true;
            } else if (compare < 0) {
                return root.right != null && root.right.remove(e, root);
            } else {
                return root.left != null && root.left.remove(e, root);
            }
        }
    }

    private void minorLeftRotation(AVLTreeNode node, AVLTreeNode parent) {
        BinarySearchTreeNode newSubRoot = node.right;

        if (parent == null) {
            root = newSubRoot;
        } else {
            if ((parent.left != null) && (parent.left.compareTo(node.payload) == 0)) {
                parent.left = newSubRoot;
            } else {
                parent.right = newSubRoot;
            }
        }

        node.right = newSubRoot.left;
        newSubRoot.left = node;
    }

    private boolean addToSubtree(E e, AVLTreeNode node, AVLTreeNode parent) {
        int compare = node.compareTo(e);

        if (compare == 0) {
            return false;
        } else if (compare < 0) {
            if (node.right == null) {
                node.right = new BinarySearchTreeNode(e);
                node.
                return true;
            } else {
                boolean result = addToSubtree(e, node.right, node);

                if (result) {
                    if (subtreeHeight(node.right) > subtreeHeight(node.left) + 1) {
                        if (subtreeHeight(node.right.left) > subtreeHeight(node.right.right)) { //major left rotation
                            minorRightRotation();
                            minorLeftRotation();
                        } else {
                            minorLeftRotation(node, parent);
                        }
                    }
                }

                return result;
            }
        } else {
            if (node.left == null) {
                node.left = new BinarySearchTreeNode(e);
                return true;
            } else {
                boolean result = addToSubtree(e, node.left, node);

                if (result) {

                }

                return result;
            }
        }
    }
}