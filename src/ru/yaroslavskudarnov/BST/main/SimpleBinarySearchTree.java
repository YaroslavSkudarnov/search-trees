package ru.yaroslavskudarnov.BST.main;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * User: Skudarnov Yaroslav
 * Date: 10/2/2017
 * Time: 12:34 PM
 */
public class SimpleBinarySearchTree<E extends Comparable<? super E>> extends BinarySearchTree<E> {
    public SimpleBinarySearchTree() {}

    public SimpleBinarySearchTree(Collection<E> collection) {
        super(collection);
    }
    
    @Override
    public boolean add(E e) {
        if (isEmpty()) {
            root = new BinarySearchTreeNode(e);
            return true;
        } else {
            return root.addToSubtree(e);
        }
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) {
            return false;
        } else {
            @SuppressWarnings("unchecked")
                E e = (E) o;

            return root.removeFromSubtree(e, null);
        }
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<>() {
            private BinarySearchTreeNode currentNode, nextNode = root.leftmostDescendant();

            @Override
            public boolean hasNext() {
                if (nextNode == null) {
                    if (currentNode == null) {
                        return false;
                    } else {
                        nextNode = currentNode.getNext(root);
                    }
                }

                return nextNode != null;
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                } else {
                    currentNode = nextNode;
                    nextNode = null;
                }

                return currentNode.payload;
            }

            @Override
            public void remove() {
                if (currentNode == null) {
                    throw new NoSuchElementException();
                } else {
                    nextNode = currentNode.getNext(root);
                    root.removeFromSubtree(currentNode.payload,  null);
                    if (root.payload == null) {
                        root = null;
                    }
                    currentNode = null;
                }
            }
        };
    }
}
