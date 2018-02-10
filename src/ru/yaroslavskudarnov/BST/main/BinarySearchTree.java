package ru.yaroslavskudarnov.BST.main;

import ru.yaroslavskudarnov.BST.core.SearchTree;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * User: Skudarnov Yaroslav
 * Date: 12/17/2017
 * Time: 11:41 PM
 */
public abstract class BinarySearchTree<E extends Comparable<? super E>, N extends BinarySearchTree<E, N>.BinarySearchTreeNode> extends SearchTree<E> {
    public class BinarySearchTreeNode {
        protected N left, right;
        protected E payload;

        protected BinarySearchTreeNode(E payload) {
            this.payload = payload;
        }

        protected BinarySearchTreeNode(N node) {
            replaceContent(node);
        }

        protected int compareTo(E e) {
            return payload.compareTo(e);
        }

        protected void replaceContent(N replacement) {
            this.payload = replacement.payload;
            this.left = replacement.left;
            this.right = replacement.right;
        }
    }

    N getPrevious(N node) {
        N tmp = root, tmpResult = null;

        while (tmp != null) {
            if (tmp.payload.compareTo(node.payload) < 0) {
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

    N getNext(N node) {
        N tmp = root, tmpResult = null;

        while (tmp != null) {
            if (tmp.payload.compareTo(node.payload) > 0) {
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

    protected N leftmostDescendant(N node) {
        @SuppressWarnings("unchecked")
        N tmp = node;

        if (node == null) {
            return null;
        }

        while (tmp.left != null) {
            tmp = tmp.left;
        }

        return tmp;
    }

    protected N root;

    protected BinarySearchTree() {}

    protected BinarySearchTree(Collection<E> collection) {
        super(collection);
    }

    protected int subtreeSize(N node) {
        int size = 1;

        size += node.left == null ? 0 : subtreeSize(node.left);
        size += node.right == null ? 0 : subtreeSize(node.right);

        return size;
    }

    @Override
    public boolean contains(Object o) {
        if ((o == null) || (root == null)) {
            return false;
        } else {
            @SuppressWarnings("unchecked")
            E e = (E) o;

            return subtreeContains(e, root);
        }
    }

    private boolean subtreeContains(E e, N node) {
        int compare = node.compareTo(e);

        if (compare == 0) {
            return true;
        } else if (compare < 0) {
            return node.right != null && subtreeContains(e, node.right);
        } else {
            return node.left != null && subtreeContains(e, node.left);
        }
    }

    @Override
    public int size() {
        if (isEmpty()) {
            return 0;
        } else {
            return subtreeSize(root);
        }
    }

    public boolean isEmpty() {
        return root == null;
    }

    @Override
    public boolean add(E e) {
        if (isEmpty()) {
            root = initNode(e);
            return true;
        } else {
            return addToSubtree(e, root, null);
        }
    }

    protected abstract boolean addToSubtree(E e, N subtree, N parent);

    protected abstract N initNode(E e);

    @Override
    public boolean remove(Object o) {
        if (o == null) {
            return false;
        } else {
            @SuppressWarnings("unchecked")
            E e = (E) o;

            return removeFromSubtree(e, root, null);
        }
    }

    protected abstract boolean removeFromSubtree(E e, N subtree, N parent);

    @Override
    public Iterator<E> iterator() {
        return new Iterator<>() {
            private N currentNode, nextNode = leftmostDescendant(root);

            @Override
            public boolean hasNext() {
                if (nextNode == null) {
                    if (currentNode == null) {
                        return false;
                    } else {
                        nextNode = getNext(currentNode);
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
                    nextNode = getNext(currentNode);
                    removeFromSubtree(currentNode.payload, root, null);
                    if (root.payload == null) {
                        root = null;
                    }
                    currentNode = null;
                }
            }
        };
    }
}
