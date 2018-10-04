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

        if (node.payload.compareTo(key) > 0) {
            SplitResults splitRightSubtree = split(node.right, key);
            node.right = splitRightSubtree.rootOfAFirstSubtree;
            return new SplitResults(node, splitRightSubtree.rootOfASecondSubtree);
        } else if (node.payload.compareTo(key) < 0) {
            SplitResults splitLeftSubtree = split(node.left, key);
            node.left = splitLeftSubtree.rootOfASecondSubtree;
            return new SplitResults(splitLeftSubtree.rootOfAFirstSubtree, node);
        } else {
            return new SplitResults(node.left, node.right);
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
                    return true;
                } else {
                    return addToSubtree(newNode, currentNode.right);
                }
            } else {
                if (currentNode.left == null) {
                    currentNode.left = newNode;
                    return true;
                } else {
                    return addToSubtree(newNode, currentNode.left);
                }
            }
        } else if (newNode.priority == currentNode.priority) {
            return addToSubtree(newNode.payload, root);
        } else {
            SplitResults splitCurrentNode = split(currentNode, newNode.payload);
            newNode.left = splitCurrentNode.rootOfAFirstSubtree; newNode.right = splitCurrentNode.rootOfASecondSubtree;

            updateLinks(currentNode, currentNode.parent, newNode);
            return true;
        }
    }

    @Override
    protected TreapNode initFirstNode(E e) {
        return new TreapNode(e);
    }

    @Override
    protected boolean removeFromSubtree(E e, TreapNode node) {
        return false;
    }
}
