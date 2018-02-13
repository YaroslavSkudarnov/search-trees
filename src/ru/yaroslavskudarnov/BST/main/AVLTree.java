package ru.yaroslavskudarnov.BST.main;

import java.util.Collection;
import java.util.Stack;

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

    private Stack<Boolean> indicatorsOfNecessityOfRebalancing;

    public AVLTree() {
        indicatorsOfNecessityOfRebalancing = new Stack<>();
    }

    public AVLTree(Collection<E> collection) {
        super(collection); indicatorsOfNecessityOfRebalancing = new Stack<>();
    }

    @Override
    public boolean add(E e) {
        if (root == null) {
            root = new AVLTreeNode(e);
            return true;
        } else {
            indicatorsOfNecessityOfRebalancing.push(true);
            boolean result = addToSubtree(e, root, null);
            indicatorsOfNecessityOfRebalancing.pop();

            return result;
        }
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) {
            return false;
        } else {
            @SuppressWarnings("unchecked")
            E e = (E) o;

            indicatorsOfNecessityOfRebalancing.push(true);
            boolean result = root != null && removeFromSubtree(e, root, null);
            indicatorsOfNecessityOfRebalancing.pop();
            return result;
        }
    }

    @Override
    protected AVLTreeNode initNode(E e) {
        return new AVLTreeNode(e);
    }

    @Override
    protected boolean removeFromSubtree(E e, AVLTreeNode node, AVLTreeNode parent) {
        int compare = node.compareTo(e);

        if (compare == 0) {
            AVLTreeNode replacement;

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
                    AVLTreeNode next = getNext(node);

                    if (next == null) {
                        replacement = node.left;
                    } else {
                        replacement = next;
                        AVLTree.this.remove(replacement.payload);
                        replacement.left = node.left; replacement.right = node.right;
                        replacement.balance = node.balance;
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
        }

        boolean result;

        if (compare < 0) {
            result = node.right != null && removeFromSubtree(e, node.right, node);
        } else {
            result = node.left != null && removeFromSubtree(e, node.left, node);
        }

        if (indicatorsOfNecessityOfRebalancing.peek() && result) {
            rebalanceAfterChangingSubtreeIfNecessary(node, parent, -1, -1);
        }

        return result;
    }

    private void minorLeftRotationAux(AVLTreeNode node, AVLTreeNode parent) {
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
    }

    private void minorLeftRotation(AVLTreeNode node, AVLTreeNode parent) {
        AVLTreeNode newSubRoot = node.right;

        boolean wasNewSubRootEvenlyBalanced = newSubRoot.balance == 0;

        minorLeftRotationAux(node, parent);

        node.balance += 1;
        newSubRoot.balance += 1;

        if (!wasNewSubRootEvenlyBalanced) {
            node.balance += 1;
        }
    }

    private void minorRightRotationAux(AVLTreeNode node, AVLTreeNode parent) {
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
    }

    private void minorRightRotation(AVLTreeNode node, AVLTreeNode parent) {
        AVLTreeNode newSubRoot = node.left;
        boolean wasNewSubRootEvenlyBalanced = newSubRoot.balance == 0;

        minorRightRotationAux(node, parent);

        node.balance -= 1;
        newSubRoot.balance -= 1;

        if (!wasNewSubRootEvenlyBalanced) {
            node.balance -= 1;
        }
    }

    private void majorLeftRotation(AVLTreeNode exSubRoot, AVLTreeNode parent) {
        AVLTreeNode rightSubtreeRoot = exSubRoot.right, nextSubRoot = exSubRoot.right.left;
        int nextSubRootExBalance = nextSubRoot.balance;

        minorRightRotationAux(rightSubtreeRoot, exSubRoot);
        minorLeftRotationAux(exSubRoot, parent);

        exSubRoot.balance = nextSubRootExBalance == -1 ? 1 : 0;
        rightSubtreeRoot.balance = nextSubRootExBalance == 1 ? -1 : 0;
        nextSubRoot.balance = 0;
    }
    
    private void majorRightRotation(AVLTreeNode exSubRoot, AVLTreeNode parent) {
        AVLTreeNode leftSubtreeRoot = exSubRoot.left, nextSubRoot = exSubRoot.left.right;
        int nextSubRootExBalance = nextSubRoot.balance;

        minorLeftRotationAux(leftSubtreeRoot, exSubRoot);
        minorRightRotationAux(exSubRoot, parent);

        exSubRoot.balance = nextSubRootExBalance == 1 ? -1 : 0;
        leftSubtreeRoot.balance = nextSubRootExBalance == -1 ? 1 : 0;
        nextSubRoot.balance = 0;
    }

    protected boolean addToSubtree(E e, AVLTreeNode node, AVLTreeNode parent) {
        int compare = node.compareTo(e);

        if (compare == 0) {
            return false;
        }

        boolean result;

        if (compare < 0) {
            if (node.right == null) {
                node.right = new AVLTreeNode(e);
                node.balance -= 1;

                result = true;
            } else {
                result = addToSubtree(e, node.right, node);

                if (indicatorsOfNecessityOfRebalancing.peek() && result) {
                    rebalanceAfterChangingSubtreeIfNecessary(node, parent, 1, -1);
                }
            }
        } else {
            if (node.left == null) {
                node.left = new AVLTreeNode(e);
                node.balance += 1;

                result = true;
            } else {
                result = addToSubtree(e, node.left, node);

                if (indicatorsOfNecessityOfRebalancing.peek() && result) {
                    rebalanceAfterChangingSubtreeIfNecessary(node, parent, 1, 1);
                }
            }
        }

        if (parent != null) {
            compare = parent.compareTo(e);

            if (compare > 0) {
                if (parent.left.balance == 0) {
                    indicatorsOfNecessityOfRebalancing.set(indicatorsOfNecessityOfRebalancing.size() - 1, false);
                }
            } else if (compare < 0) {
                if (parent.right.balance == 0) {
                    indicatorsOfNecessityOfRebalancing.set(indicatorsOfNecessityOfRebalancing.size() - 1, false);
                }
            }
        }

        if (node.balance == 0) {
            indicatorsOfNecessityOfRebalancing.set(indicatorsOfNecessityOfRebalancing.size() - 1, false);
        }

        return result;
    }

    /**
     * Updates balance of current node. To be invoked after changing one of its subtrees.
     * @param node current node
     * @param parent its parent
     * @param changeInNumberOfNodes +1 if this is invoked after adding new node, -1 if after deleting existing
     * @param subtreeChanged +1 if left, -1 if right
     */
    private void rebalanceAfterChangingSubtreeIfNecessary(AVLTreeNode node, AVLTreeNode parent, int changeInNumberOfNodes, int subtreeChanged) {
        node.balance += changeInNumberOfNodes * subtreeChanged;

        if (node.balance != 0) {
            if ((changeInNumberOfNodes * subtreeChanged) > 0) {
                rightRotationsIfNecessary(node, parent); //if we added a node in the left subtree or deleted it from the right subtree
            } else {
                leftRotationsIfNecessary(node, parent);  //if we added a node in the right subtree or deleted it from the left subtree
            }
        }
    }

    private void leftRotationsIfNecessary(AVLTreeNode node, AVLTreeNode parent) {
        if (node.balance == -2) {
            if (node.right.balance == 1) {
                majorLeftRotation(node, parent);
            } else {
                minorLeftRotation(node, parent);
            }
        }
    }

    private void rightRotationsIfNecessary(AVLTreeNode node, AVLTreeNode parent) {
        if (node.balance == 2) {
            if (node.left.balance == -1) {
                majorRightRotation(node, parent);
            } else {
                minorRightRotation(node, parent);
            }
        }
    }
}