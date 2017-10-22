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

            size += left == null ? 0 : left.size();
            size += right == null ? 0 : right.size();

            return size;
        }

        boolean add(E e) {
            int compare = payload.compareTo(e);

            if (compare == 0) {
                return false;
            } else if (compare < 0) {
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

        boolean contains(E e) {
            int compare = payload.compareTo(e);

            if (compare == 0) {
                return true;
            } else if (compare < 0) {
                return right != null && right.contains(e);
            } else {
                return left != null && left.contains(e);
            }
        }

        boolean remove(E e, Node parent) {
            int compare = payload.compareTo(e);

            if (compare == 0) {
                Node replacement;

                if (left == null) {
                    if (right == null) {
                        replacement = null;
                    } else {
                        replacement = right;
                    }
                } else {
                    if (right == null) {
                        replacement = left;
                    } else {
                        replacement = parent.leftmostDescendant();
                        SimpleBinarySearchTree.this.remove(replacement.payload);
                        replacement.left = left; replacement.right = right;
                    }
                }

                if ((parent.left != null) && (parent.left.payload.compareTo(e) == 0)) {
                    parent.left = replacement;
                } else {
                    parent.right = replacement;
                }

                return true;
            } else if (compare < 0) {
                return right != null && right.remove(e, this);
            } else {
                return left != null && left.remove(e, this);
            }
        }
    }

    private Node root;

    public SimpleBinarySearchTree(Collection<E> collection) {
        super(collection);
    }

    @Override
    public boolean contains(Object o) {
        if ((o == null) || (root == null)) {
            return false;
        } else {
            @SuppressWarnings("unchecked")
                E e = (E) o;

            return root.contains(e);
        }
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
        if ((o == null) || (root == null)) {
            return false;
        } else {
            @SuppressWarnings("unchecked")
                E e = (E) o;

            int compare = root.payload.compareTo(e);

            if (compare == 0) {
                root = null;
                return true;
            } else if (compare < 0) {
                return root.right != null && root.right.remove(e, root);
            } else {
                return root.left != null && root.left.remove(e, root);
            }
        }
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<>() {
            private Node currentNode = null;
            private Node nextNode = root.leftmostDescendant();

            private Node getPrevious() {
                Node tmp = root;
                Node tmpResult = null;

                while (tmp != null) {
                    if (tmp.payload.compareTo(currentNode.payload) < 0) {
                        tmpResult = tmp;

                        tmp = tmp.right;
                    } else {
                        if (tmp.left != null) {
                            tmp = tmp.left;
                        } else {
                            return tmpResult;
                        }
                    }
                }

                return tmpResult;
            }

            private Node getNext() {
                Node tmp = root;
                Node tmpResult = null;

                while (tmp != null) {
                    if (tmp.payload.compareTo(currentNode.payload) > 0) {
                        tmpResult = tmp;

                        tmp = tmp.left;
                    } else {
                        if (tmp.right != null) {
                            tmp = tmp.right;
                        } else {
                            return tmpResult;
                        }
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

            @Override
            public void remove() {
                nextNode = getNext();
                Node previousNode = getPrevious();
                SimpleBinarySearchTree.this.remove(currentNode.payload);
                currentNode = previousNode;
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
