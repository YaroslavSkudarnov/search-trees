package ru.yaroslavskudarnov.BST.main;

/**
 * User: Skudarnov Yaroslav
 * Date: 11/3/2017
 * Time: 1:18 PM
 */
public class AVLBinarySearchTree<E extends Comparable<? super E>> extends SimpleBinarySearchTree<E> {
/*    protected class AVLBinarySearchTreeNode extends SimpleBinarySearchTree.SimpleBinarySearchTreeNode {
        SimpleBinarySearchTree.SimpleBinarySearchTreeNode left, right;

        E payload;

        SimpleBinarySearchTreeNode(E payload) {
            this.payload = payload;
        }
        SimpleBinarySearchTreeNode(SimpleBinarySearchTree.SimpleBinarySearchTreeNode left, SimpleBinarySearchTree.SimpleBinarySearchTreeNode right, E payload) {
            this.left = left;
            this.right = right;
            this.payload = payload;
        }

        SimpleBinarySearchTree.SimpleBinarySearchTreeNode leftmostDescendant() {
            SimpleBinarySearchTree.SimpleBinarySearchTreeNode tmp = this;

            while (tmp.left != null) {
                tmp = tmp.left;
            }

            return tmp;
        }

        protected SimpleBinarySearchTree.SimpleBinarySearchTreeNode getPrevious() {
            SimpleBinarySearchTree.SimpleBinarySearchTreeNode tmp = root;
            SimpleBinarySearchTree.SimpleBinarySearchTreeNode tmpResult = null;

            while (tmp != null) {
                if (tmp.payload.compareTo(payload) < 0) {
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

        protected SimpleBinarySearchTree.SimpleBinarySearchTreeNode getNext() {
            SimpleBinarySearchTree.SimpleBinarySearchTreeNode tmp = root;
            SimpleBinarySearchTree.SimpleBinarySearchTreeNode tmpResult = null;

            while (tmp != null) {
                if (tmp.payload.compareTo(payload) > 0) {
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
                    right = new SimpleBinarySearchTree.SimpleBinarySearchTreeNode(e);
                    return true;
                } else {
                    return right.add(e);
                }
            } else {
                if (left == null) {
                    left = new SimpleBinarySearchTree.SimpleBinarySearchTreeNode(e);
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

        boolean remove(E e, SimpleBinarySearchTree.SimpleBinarySearchTreeNode parent) {
            int compare = payload.compareTo(e);

            if (compare == 0) {
                SimpleBinarySearchTree.SimpleBinarySearchTreeNode replacement;

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
                        SimpleBinarySearchTree.SimpleBinarySearchTreeNode next = getNext();

                        if (next == null) {
                            replacement = left;
                        } else {
                            replacement = next;
                            SimpleBinarySearchTree.this.remove(replacement.payload);
                            replacement = new SimpleBinarySearchTree.SimpleBinarySearchTreeNode(left, right, replacement.payload);
                        }
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

    protected SimpleBinarySearchTree.SimpleBinarySearchTreeNode root;

    public SimpleBinarySearchTree() {}

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
            root = new SimpleBinarySearchTree.SimpleBinarySearchTreeNode(e);
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
                E tmpPayload = null;

                if (root.getNext() != null) {
                    tmpPayload = root.getNext().payload;
                } else if (root.getPrevious() != null) {
                    tmpPayload = root.getPrevious().payload;
                } else {
                    root = null;
                }

                if (root != null) {
                    remove(tmpPayload);
                    root.payload = tmpPayload;
                }

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
            private SimpleBinarySearchTree.SimpleBinarySearchTreeNode currentNode = null;
            private SimpleBinarySearchTree.SimpleBinarySearchTreeNode nextNode = root.leftmostDescendant();

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
                    SimpleBinarySearchTree.this.remove(currentNode.payload);
                    currentNode = null;
                }
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
    }*/

}