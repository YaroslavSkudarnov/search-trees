package ru.yaroslavskudarnov.BST.main;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * User: Skudarnov Yaroslav
 * Date: 10/2/2017
 * Time: 12:34 PM
 */
public class SimpleBinarySearchTree<E extends Comparable<? super E>> extends AbstractBinarySearchTree<E> {
    class Node {
        Node left, right;

        E payload;

        Node(E payload) {
            this.payload = payload;
        }

        Node leftmostDescendant() {
            Node tmp = this;

            while (tmp.left != null) {
                tmp = tmp.left;
            }

            return tmp;
        }

        int size() {
            int size = 1;

            size = left == null ? size : size + left.size();
            size = right == null ? size : size + right.size();

            return size;
        }

        boolean add(E e) {
            int compare = payload.compareTo(e);

            if (compare == 0) {
                return false;
            } else {
                if (compare < 0) {
                    if (right == null) {
                        right = new Node(e);
                        return true;
                    } else {
                        return right.add(e);
                    }
                } else {
                    if (left == null) {
                        left = new Node(e);
                        return true;
                    } else {
                        return left.add(e);
                    }
                }
            }
        }
    }

    private Node root;

    public SimpleBinarySearchTree(Collection<E> collection) {
        this.addAll(collection);
    }

    @Override
    public boolean contains(Object o) {
        return super.contains(o);
    }

    @Override
    public boolean add(E e) {
        if (root == null) {
            root = new Node(e);
            return true;
        } else {
            return root.add(e);
        }
    }

    @Override
    public boolean remove(Object o) {
        return super.remove(o);
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<>() {
            private Node currentNode = root.leftmostDescendant();
            private Node nextNode = null;

            private Node getNext() {
                Node tmp = root;
                Node tmpResult = null;

                while (tmp != null) {
                    if (tmp.payload.compareTo(currentNode.payload) > 0) {
                        tmpResult = tmp;

                        tmp = tmp.left;
                    } else {
                        tmpResult = tmp.right;
                    }
                }

                return tmpResult;
            }

            @Override
            public boolean hasNext() {
                if (nextNode == null) {
                    nextNode = getNext();
                }

                return nextNode != null;
            }

            @Override
            public E next() {
                if (nextNode == null) {
                    nextNode = getNext();
                }

                if (nextNode == null) {
                    throw new NoSuchElementException();
                } else {
                    currentNode = nextNode;
                    nextNode = null;
                }

                return currentNode.payload;
            }
        };
    }

    @Override
    public int size() {
        if (root == null) {
            return 0;
        } else{
            return root.size();
        }
    }
}
