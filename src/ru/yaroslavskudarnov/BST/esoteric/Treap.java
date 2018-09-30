package ru.yaroslavskudarnov.BST.esoteric;

import ru.yaroslavskudarnov.BST.classic.SimpleBinarySearchTree;
import ru.yaroslavskudarnov.BST.core.BinarySearchTree;

import java.util.Random;

/**
 * User: Skudarnov Yaroslav
 * Date: 9/30/2018
 * Time: 10:23 PM
 */
public class Treap<E extends Comparable<? super E>> extends BinarySearchTree<E, Treap<E>.TreapNode> {
    private Random randomizer;

    class TreapNode extends BinarySearchTree<E, TreapNode>.BinarySearchTreeNode {
        private long priority;

        TreapNode(E e) {
            super(e);
            priority = randomizer.nextLong();
        }
    }

    public Treap() {
        randomizer = new Random();
    }

    @Override
    public boolean add(E e) {
        if (isEmpty()) {
            root = initFirstNode(e);
            return true;
        } else {
            return addToSubtree(new TreapNode(e), root);
        }
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
            return addToSubtree(new TreapNode(newNode.payload), root);
        } else {

        }
    }

    @Override
    protected TreapNode initFirstNode(E e) {
        return new TreapNode(e);
    }


}
