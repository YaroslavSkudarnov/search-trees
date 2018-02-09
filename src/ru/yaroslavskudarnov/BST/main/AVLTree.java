package ru.yaroslavskudarnov.BST.main;

import java.util.Collection;

/**
 * User: Skudarnov Yaroslav
 * Date: 11/3/2017
 * Time: 1:18 PM
 */
public class AVLTree<E extends Comparable<? super E>> extends BinarySearchTree<E, AVLTree<E>.AVLTreeNode> {
    class AVLTreeNode extends BinarySearchTree<E, AVLTreeNode>.BinarySearchTreeNode {
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

    private boolean keepRebalancing;

    public AVLTree() {
    }

    public AVLTree(Collection<E> collection) {
        super(collection);
    }

    @Override
    public boolean add(E e) {
        if (root == null) {
            root = new AVLTreeNode(e);
            return true;
        } else {
            keepRebalancing = true;
            boolean result = addToSubtree(e, root, null);
            keepRebalancing = false;

            return result;
        }
    }

    @Override
    protected AVLTreeNode initNode(E e) {
        return new AVLTreeNode(e);
    }

    @Override
    protected boolean removeFromSubtree(E e, AVLTreeNode subtree, AVLTreeNode parent) {
        return false;
    }

    private void minorLeftRotation(AVLTreeNode node, AVLTreeNode parent) {
        AVLTreeNode newSubRoot = node.right;

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
        node.balance += 1;
        newSubRoot.balance += 1;
    }

    private void minorRightRotation(AVLTreeNode node, AVLTreeNode parent) {
        AVLTreeNode newSubRoot = node.left;

        if (parent == null) {
            root = newSubRoot;
        } else {
            if ((parent.left != null) && (parent.left.compareTo(node.payload) == 0)) {
                parent.left = newSubRoot;
            } else {
                parent.right = newSubRoot;
            }
        }

        node.left = newSubRoot.right;
        newSubRoot.right = node;
        node.balance -= 1;
        newSubRoot.balance -= 1;
    }

    private void majorLeftRotation(AVLTreeNode node, AVLTreeNode parent) {
        minorRightRotation(node.right, node);
        minorLeftRotation(node, parent);
        node.balance += 1;
    }
    
    private void majorRightRotation(AVLTreeNode node, AVLTreeNode parent) {
        minorLeftRotation(node.left, node);
        minorRightRotation(node, parent);
        node.balance -= 1;
    }

    protected boolean addToSubtree(E e, AVLTreeNode node, AVLTreeNode parent) {
        int compare = node.compareTo(e);

        if (compare == 0) {
            return false;
        } else if (compare < 0) {
            boolean result;

            if (node.right == null) {
                node.right = new AVLTreeNode(e);
                node.balance -= 1;

                result = true;
            } else {
                result = addToSubtree(e, node.right, node);

                if (keepRebalancing && result) {
                    updateBalanceAfterAddingToRightSubtree(node, parent);
                }
            }

            return result;
        } else {
            boolean result;

            if (node.left == null) {
                node.left = new AVLTreeNode(e);
                node.balance += 1;

                result = true;
            } else {
                result = addToSubtree(e, node.left, node);

                if (keepRebalancing && result) {
                    updateBalanceAfterAddingToLeftSubtree(node, parent);
                }
            }

            return result;
        }
    }

    private void updateBalanceAfterAddingToLeftSubtree(AVLTreeNode node, AVLTreeNode parent) {
        node.balance += 1;

        if (node.balance == 0) {
            keepRebalancing = false;
        }

        if (node.balance == 2) {
            if (node.left.balance == -1) {
                majorRightRotation(node, parent);
            } else {
                minorRightRotation(node, parent);
            }
        }
    }

    private void updateBalanceAfterAddingToRightSubtree(AVLTreeNode node, AVLTreeNode parent) {
        node.balance -= 1;

        if (node.balance == 0) {
            keepRebalancing = false;
        }

        if (node.balance == -2) {
            if (node.right.balance == 1) {
                majorLeftRotation(node, parent);
            } else {
                minorLeftRotation(node, parent);
            }
        }
    }
}