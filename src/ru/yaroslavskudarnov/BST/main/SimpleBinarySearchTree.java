package ru.yaroslavskudarnov.BST.main;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * User: Skudarnov Yaroslav
 * Date: 10/2/2017
 * Time: 12:34 PM
 */
public class SimpleBinarySearchTree<E extends Comparable<? super E>> extends BinarySearchTree<E, SimpleBinarySearchTree.SimpleBinarySearchTreeNode> {
    class SimpleBinarySearchTreeNode extends BinarySearchTree<E, SimpleBinarySearchTreeNode>.BinarySearchTreeNode {
        SimpleBinarySearchTreeNode(E payload) {
            super(payload);
        }

        SimpleBinarySearchTreeNode(SimpleBinarySearchTreeNode node) {
            super(node);
        }
    }

    protected SimpleBinarySearchTreeNode root;

    public SimpleBinarySearchTree() {}

    public SimpleBinarySearchTree(Collection<E> collection) {
        super(collection);
    }

    @Override
    public boolean add(E e) {
        if (isEmpty()) {
            root = new SimpleBinarySearchTreeNode(e);
            return true;
        } else {
            return addToSubtree(e, root);
        }
    }

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
    public boolean contains(Object o) {
        if ((o == null) || (root == null)) {
            return false;
        } else {
            @SuppressWarnings("unchecked")
            E e = (E) o;

            return subtreeContains(e, root);
        }
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<>() {
            private SimpleBinarySearchTreeNode currentNode, nextNode = root.leftmostDescendant();

            @Override
            public boolean hasNext() {
                if (nextNode == null) {
                    if (currentNode == null) {
                        return false;
                    } else {
                        nextNode = currentNode.getNext();
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
                    nextNode = currentNode.getNext();
                    removeFromSubtree(currentNode.payload, root, null);
                    if (root.payload == null) {
                        root = null;
                    }
                    currentNode = null;
                }
            }
        };
    }

    private boolean addToSubtree(E e, BinarySearchTreeNode node) {
        int compare = node.compareTo(e);

        if (compare == 0) {
            return false;
        } else if (compare < 0) {
            if (node.right == null) {
                node.right = new SimpleBinarySearchTreeNode(e);
                return true;
            } else {
                return addToSubtree(e, node.right);
            }
        } else {
            if (node.left == null) {
                node.left = new SimpleBinarySearchTreeNode(e);
                return true;
            } else {
                return addToSubtree(e, node.left);
            }
        }
    }

    private boolean removeFromSubtree(E e, SimpleBinarySearchTreeNode node, SimpleBinarySearchTreeNode parent) {
        int compare = node.compareTo(e);

        if (compare == 0) {
            SimpleBinarySearchTreeNode replacement;

            if (node.left == null) {
                if (node.right == null) {
                    replacement = null;
                } else {
                    replacement = node.right;
                }
            } else {
                if (node.right == null) {
                    replacement = node.left;
                } else {
                    SimpleBinarySearchTreeNode next = node.getNext();

                    if (next == null) {
                        replacement = node.left;
                    } else {
                        replacement = next;
                        SimpleBinarySearchTree.this.remove(replacement.payload);
                        replacement = new SimpleBinarySearchTreeNode(node);
                    }
                }
            }

            if (parent == null) {
                if (replacement == null) {
                    node.payload = null;
                } else {
                    node.replaceContent(replacement);
                }
            } else {
                if ((parent.left != null) && (parent.left.compareTo(e) == 0)) {
                    parent.left = replacement;
                } else {
                    parent.right = replacement;
                }
            }

            return true;
        } else if (compare < 0) {
            return node.right != null && removeFromSubtree(e, node.right, node);
        } else {
            return node.left != null && removeFromSubtree(e, node.left, node);
        }
    }

    private boolean subtreeContains(E e, BinarySearchTreeNode node) {
        int compare = node.compareTo(e);

        if (compare == 0) {
            return true;
        } else if (compare < 0) {
            return node.right != null && subtreeContains(e, node.right);
        } else {
            return node.left != null && subtreeContains(e, node.left);
        }
    }

    protected int subtreeHeight(BinarySearchTreeNode node) {
        if (node == null) {
            return 0;
        }

        return Math.max(subtreeHeight(node.left), subtreeHeight(node.right)) + 1;
    }
}
