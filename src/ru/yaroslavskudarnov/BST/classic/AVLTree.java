package ru.yaroslavskudarnov.BST.classic;

import ru.yaroslavskudarnov.BST.core.BinarySearchTree;

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

        AVLTreeNode(E payload, AVLTreeNode parent) {
            super(payload);
            this.balance = 0;
            this.parent = parent;
        }

        @Override
        protected void replaceContent(AVLTreeNode replacement) {
            super.replaceContent(replacement);
            this.balance = replacement.balance;
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
            root = initFirstNode(e);
            return true;
        } else {
            indicatorsOfNecessityOfRebalancing.push(true);
            boolean result = addToSubtree(e, root);
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
            boolean result = root != null && removeFromSubtree(e, root);
            indicatorsOfNecessityOfRebalancing.pop();
            return result;
        }
    }

    @Override
    protected AVLTreeNode initFirstNode(E e) {
        return new AVLTreeNode(e, null);
    }

    @Override
    protected boolean removeFromSubtree(E e, AVLTreeNode node) {
        int compare = node.compareTo(e);

        boolean result;

        AVLTreeNode parent = node.parent;

        if (compare == 0) {
            AVLTreeNode replacement;

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
                    replacement.balance = node.balance;
                    parent = findParent(e); replacement.parent = parent;

                    indicatorsOfNecessityOfRebalancing.set(indicatorsOfNecessityOfRebalancing.size() - 1, false);
                }
            }

            updateLinks(node, parent, replacement);

            result = true;
        } else if (compare < 0) {
            result = node.right != null && removeFromSubtree(e, node.right);

            if (indicatorsOfNecessityOfRebalancing.peek() && result) {
                rebalanceAfterChangingSubtreeIfNecessary(node, -1, -1);
            }
        } else {
            result = node.left != null && removeFromSubtree(e, node.left);

            if (indicatorsOfNecessityOfRebalancing.peek() && result) {
                rebalanceAfterChangingSubtreeIfNecessary(node, -1, 1);
            }
        }

        if (parent != null) {
            compare = parent.compareTo(e);

            if (compare > 0) {
                if ((parent.left != null) && (Math.abs(parent.left.balance) == 1)) {
                    indicatorsOfNecessityOfRebalancing.set(indicatorsOfNecessityOfRebalancing.size() - 1, false);
                }
            } else if (compare < 0) {
                if ((parent.right != null) && (Math.abs(parent.right.balance) == 1)) {
                    indicatorsOfNecessityOfRebalancing.set(indicatorsOfNecessityOfRebalancing.size() - 1, false);
                }
            }
        }

        return result;
    }

    private void minorLeftRotationAVL(AVLTreeNode node) {
        AVLTreeNode newSubRoot = node.right;

        boolean wasNewSubRootEvenlyBalanced = newSubRoot.balance == 0;

        minorLeftRotationCommon(node);

        node.balance += 1;
        newSubRoot.balance += 1;

        if (!wasNewSubRootEvenlyBalanced) {
            node.balance += 1;
        }
    }

    private void minorRightRotationAVL(AVLTreeNode node) {
        AVLTreeNode newSubRoot = node.left;
        boolean wasNewSubRootEvenlyBalanced = newSubRoot.balance == 0;

        minorRightRotationCommon(node);

        node.balance -= 1;
        newSubRoot.balance -= 1;

        if (!wasNewSubRootEvenlyBalanced) {
            node.balance -= 1;
        }
    }

    private void majorLeftRotation(AVLTreeNode exSubRoot) {
        AVLTreeNode rightSubtreeRoot = exSubRoot.right, nextSubRoot = exSubRoot.right.left;
        int nextSubRootExBalance = nextSubRoot.balance;

        minorRightRotationCommon(rightSubtreeRoot);
        minorLeftRotationCommon(exSubRoot);

        exSubRoot.balance = nextSubRootExBalance == -1 ? 1 : 0;
        rightSubtreeRoot.balance = nextSubRootExBalance == 1 ? -1 : 0;
        nextSubRoot.balance = 0;
    }
    
    private void majorRightRotation(AVLTreeNode exSubRoot) {
        AVLTreeNode leftSubtreeRoot = exSubRoot.left, nextSubRoot = exSubRoot.left.right;
        int nextSubRootExBalance = nextSubRoot.balance;

        minorLeftRotationCommon(leftSubtreeRoot);
        minorRightRotationCommon(exSubRoot);

        exSubRoot.balance = nextSubRootExBalance == 1 ? -1 : 0;
        leftSubtreeRoot.balance = nextSubRootExBalance == -1 ? 1 : 0;
        nextSubRoot.balance = 0;
    }

    protected boolean addToSubtree(E e, AVLTreeNode node) {
        int compare = node.compareTo(e);

        if (compare == 0) {
            return false;
        }

        boolean result;

        AVLTreeNode parent = node.parent;

        if (compare < 0) {
            if (node.right == null) {
                node.right = new AVLTreeNode(e, node);
                result = true;
            } else {
                result = addToSubtree(e, node.right);
            }

            if (indicatorsOfNecessityOfRebalancing.peek() && result) {
                rebalanceAfterChangingSubtreeIfNecessary(node, 1, -1);
            }
        } else {
            if (node.left == null) {
                node.left = new AVLTreeNode(e, node);
                result = true;
            } else {
                result = addToSubtree(e, node.left);
            }

            if (indicatorsOfNecessityOfRebalancing.peek() && result) {
                rebalanceAfterChangingSubtreeIfNecessary(node, 1, 1);
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

        return result;
    }

    /**
     * Updates balance of current node. To be invoked after changing one of its subtrees.
     * @param node current node
     * @param changeInNumberOfNodes +1 if this is invoked after adding new node, -1 if after deleting existing
     * @param subtreeChanged +1 if left, -1 if right
     */
    private void rebalanceAfterChangingSubtreeIfNecessary(AVLTreeNode node, int changeInNumberOfNodes, int subtreeChanged) {
        node.balance += changeInNumberOfNodes * subtreeChanged;

        if (node.balance != 0) {
            if ((changeInNumberOfNodes * subtreeChanged) > 0) {
                rightRotationsIfNecessary(node); //if we added a node in the left subtree or deleted it from the right subtree
            } else {
                leftRotationsIfNecessary(node);  //if we added a node in the right subtree or deleted it from the left subtree
            }
        }
    }

    private void leftRotationsIfNecessary(AVLTreeNode node) {
        if (node.balance == -2) {
            if (node.right.balance == 1) {
                majorLeftRotation(node);
            } else {
                minorLeftRotationAVL(node);
            }
        }
    }

    private void rightRotationsIfNecessary(AVLTreeNode node) {
        if (node.balance == 2) {
            if (node.left.balance == -1) {
                majorRightRotation(node);
            } else {
                minorRightRotationAVL(node);
            }
        }
    }
}