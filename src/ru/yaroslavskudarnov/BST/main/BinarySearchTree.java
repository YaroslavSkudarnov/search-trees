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
        protected N left, right, parent;
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
        N firstNodeInRightSubtree = leftmostDescendant(node.right);

        if (firstNodeInRightSubtree != null) {
            return firstNodeInRightSubtree;
        } else {
            while ((node.parent != null) && (node.parent.left != node)) {
                node = node.parent;
            }

            return node.parent;
        }
    }

    protected N leftmostDescendant(N node) {
        if (node == null) {
            return null;
        }

        while (node.left != null) {
            node = node.left;
        }

        return node;
    }

    protected N findParent(E e) {
        N parent, node;

        parent = null;
        node = root;

        while (node != null) {
            if (node.compareTo(e) == 0) {
                return parent;
            }

            parent = node;
            if (node.compareTo(e) > 0) {
                node = node.left;
            } else {
                node = node.right;
            }
        }

        return parent;
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
            root = initFirstNode(e);
            return true;
        } else {
            return addToSubtree(e, root);
        }
    }

    protected void updateLinks(E e, N node, N parent, N replacement) {
        checkNullAndSetParent(replacement, parent);

        if (parent == null) {
            if (replacement == null) {
                node.payload = null;
            } else {
                node.replaceContent(replacement);
                checkNullAndSetParent(node.left, node);
                checkNullAndSetParent(node.right, node);
            }
        } else {
            if ((parent.left != null) && (parent.left.compareTo(e) == 0)) {
                parent.left = replacement;
            } else {
                parent.right = replacement;
            }

            if (replacement != null) {
                checkNullAndSetParent(replacement.left, replacement);
                checkNullAndSetParent(replacement.right, replacement);
            }
        }
    }

    private void pullNewRootUpAndUpdateLinksToParents(N node, N parent, N newSubRoot) {
        if (parent == null) {
            root = newSubRoot;
        } else {
            if ((parent.left != null) && (parent.left.compareTo(node.payload) == 0)) {
                parent.left = newSubRoot;
            } else {
                parent.right = newSubRoot;
            }
        }

        newSubRoot.parent = parent;
        node.parent = newSubRoot;
    }

    protected void minorLeftRotationCommon(N node, N parent) {
        N newSubRoot = node.right;

        pullNewRootUpAndUpdateLinksToParents(node, parent, newSubRoot);

        node.right = newSubRoot.left;
        if (node.right != null) {
            node.right.parent = node;
        }
        newSubRoot.left = node;
    }

    protected void minorRightRotationCommon(N node, N parent) {
        N newSubRoot = node.left;

        pullNewRootUpAndUpdateLinksToParents(node, parent, newSubRoot);

        node.left = newSubRoot.right;
        if (node.left != null) {
            node.left.parent = node;
        }
        newSubRoot.right = node;
    }

    protected abstract boolean addToSubtree(E e, N node);

    protected abstract N initFirstNode(E e);

    @Override
    public boolean remove(Object o) {
        if (o == null) {
            return false;
        } else {
            @SuppressWarnings("unchecked")
            E e = (E) o;

            return root != null && removeFromSubtree(e, root);
        }
    }

    protected abstract boolean removeFromSubtree(E e, N node);

    protected void checkNullAndSetParent(N node, N parent) {
        if (node != null) {
            node.parent = parent;
        }
    }

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
                    BinarySearchTree.this.remove(currentNode.payload);
                    if (root.payload == null) {
                        root = null;
                    }
                    currentNode = null;
                }
            }
        };
    }
}
