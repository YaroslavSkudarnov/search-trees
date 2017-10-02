package ru.yaroslavskudarnov.BST.main;

import java.util.Collection;
import java.util.Iterator;

/**
 * User: Skudarnov Yaroslav
 * Date: 10/2/2017
 * Time: 12:34 PM
 */
public class SimpleBinarySearchTree<E extends Comparable> extends AbstractBinarySearchTree<E> {
    class Node {
        Node left, right;

        E payload;

        Node(Node left, Node right, E payload) {
            this.left = left;
            this.right = right;
            this.payload = payload;
        }
    }

    Node root;

    public SimpleBinarySearchTree(Collection<E> collection) {
        this.addAll(collection);
    }

    @Override
    public boolean contains(Object o) {
        return super.contains(o);
    }

    @Override
    public boolean add(E e) {
        return super.add(e);
    }

    @Override
    public boolean remove(Object o) {
        return super.remove(o);
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }
}
