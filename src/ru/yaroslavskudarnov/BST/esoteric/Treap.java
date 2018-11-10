package ru.yaroslavskudarnov.BST.esoteric;

import ru.yaroslavskudarnov.BST.core.BinarySearchTree;

import java.util.Random;

/**
 * User: Skudarnov Yaroslav
 * Date: 9/30/2018
 * Time: 10:23 PM
 */
public class Treap<E extends Comparable<? super E>> extends BinarySearchTree<E, Treap<E>.TreapNode> {
    private Random randomizer;

    class SplitResults { //It's hard to overstate my hatred for lack of proper pairs in Java
        TreapNode rootOfAFirstSubtree, rootOfASecondSubtree;

        SplitResults(TreapNode rootOfAFirstSubtree, TreapNode rootOfASecondSubtree) {
            this.rootOfAFirstSubtree = rootOfAFirstSubtree;
            this.rootOfASecondSubtree = rootOfASecondSubtree;
        }

        SplitResults() {
            this(null, null);
        }
    }

    private SplitResults split(TreapNode node, E key) {
        if (node == null) {
            return new SplitResults();
        }

        if (node.payload.compareTo(key) < 0) {
            SplitResults splitRightSubtree = split(node.right, key);
            node.right = splitRightSubtree.rootOfAFirstSubtree;
            if (splitRightSubtree.rootOfAFirstSubtree != null) {
                splitRightSubtree.rootOfAFirstSubtree.parent = node;
            }
            return new SplitResults(node, splitRightSubtree.rootOfASecondSubtree);
        } else if (node.payload.compareTo(key) > 0) {
            SplitResults splitLeftSubtree = split(node.left, key);
            node.left = splitLeftSubtree.rootOfASecondSubtree;
            if (splitLeftSubtree.rootOfASecondSubtree != null) {
                splitLeftSubtree.rootOfASecondSubtree.parent = node;
            }
            return new SplitResults(splitLeftSubtree.rootOfAFirstSubtree, node);
        } else {
            return new SplitResults(node.left, node.right);
        }
    }

    private TreapNode merge(TreapNode leftSubtree, TreapNode rightSubtree) {
        if (leftSubtree == null) {
            return rightSubtree;
        }

        if (rightSubtree == null) {
            return leftSubtree;
        }

        if (leftSubtree.priority < rightSubtree.priority) {
            leftSubtree.right = merge(leftSubtree.right, rightSubtree);
            leftSubtree.right.parent = leftSubtree;
            return leftSubtree;
        } else {
            rightSubtree.left = merge(leftSubtree, rightSubtree.left);
            rightSubtree.left.parent = rightSubtree;
            return rightSubtree;
        }
    }

    class TreapNode extends BinarySearchTree<E, TreapNode>.BinarySearchTreeNode {
        private long priority;

        TreapNode(E e) {
            super(e);
            priority = randomizer.nextLong();
        }

        @Override
        protected void replaceContent(TreapNode replacement) {
            priority = replacement.priority;
            super.replaceContent(replacement);
        }
    }

    public Treap() {
        randomizer = new Random();
    }

    @Override
    protected boolean addToSubtree(E e, TreapNode node) {
        return addToSubtree(new TreapNode(e), node);
    }

    private boolean addToSubtree(TreapNode newNode, TreapNode currentNode) {
        if (newNode.priority > currentNode.priority) {
            int compare = currentNode.compareTo(newNode.payload);

            if (compare == 0) {
                return false;
            } else if (compare < 0) {
                if (currentNode.right == null) {
                    currentNode.right = newNode;
                    newNode.parent = currentNode;
                    return true;
                } else {
                    return addToSubtree(newNode, currentNode.right);
                }
            } else {
                if (currentNode.left == null) {
                    currentNode.left = newNode;
                    newNode.parent = currentNode;
                    return true;
                } else {
                    return addToSubtree(newNode, currentNode.left);
                }
            }
        } else if (newNode.priority == currentNode.priority) {
            return addToSubtree(newNode.payload, root);
        } else {
            if (subtreeContains(newNode.payload, currentNode)) {
                return false;
            }

            newNode.parent = currentNode.parent;

            SplitResults splitCurrentNode = split(currentNode, newNode.payload);
            newNode.left = splitCurrentNode.rootOfAFirstSubtree; newNode.right = splitCurrentNode.rootOfASecondSubtree;

            if (newNode.parent == null) {
                root = newNode;
            } else {
                if (newNode.parent.payload.compareTo(newNode.payload) > 0) {
                    newNode.parent.left = newNode;
                } else {
                    newNode.parent.right = newNode;
                }
            }
            checkNullAndSetParent(newNode.left, newNode);
            checkNullAndSetParent(newNode.right, newNode);
            return true;
        }
    }

    @Override
    protected TreapNode initFirstNode(E e) {
        return new TreapNode(e);
    }

    @Override
    protected boolean removeFromSubtree(E e, TreapNode node) {
        int compare = node.compareTo(e);

        TreapNode parent = node.parent;

        if (compare == 0) {
            TreapNode newRootOfSubtree = merge(node.left, node.right);
            checkNullAndSetParent(newRootOfSubtree, parent);

            if (parent == null) {
                root = newRootOfSubtree;
            } else {
                if (parent.payload.compareTo(e) > 0) {
                    parent.left = newRootOfSubtree;
                } else {
                    parent.right = newRootOfSubtree;
                }
            }

            return true;
        } else if (compare < 0) {
            return node.right != null && removeFromSubtree(e, node.right);
        } else {
            return node.left != null && removeFromSubtree(e, node.left);
        }
    }
}
