package ru.yaroslavskudarnov.BST.main;

import ru.yaroslavskudarnov.BST.core.SearchTree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * User: Skudarnov Yaroslav
 * Date: 9/12/2018
 * Time: 3:42 PM
 */
public class BTree<E extends Comparable<? super E>> extends SearchTree<E> {
    final private int MINIMUM_NUMBER_OF_KEYS_IN_A_NODE;
    final private int NUMBER_OF_KEYS_TO_START_BINSEARCH = 5; //TODO: benchmark and set optimal value here
    private BTreeNode root;

    public BTree(int d) {
        this.MINIMUM_NUMBER_OF_KEYS_IN_A_NODE = d;
    }

    private class BTreeNode {
        private BTreeNode parent;
        private List<BTreeNode> children;
        private List<E> keys;

        private BTreeNode() {
            this.keys = new ArrayList<>();
            this.children = new ArrayList<>();
        }

        private BTreeNode(E payload) {
            this();
            this.keys.add(payload);
        }

        private BTreeNode(BTreeNode parent, List<BTreeNode> firstChildren, List<E> firstKeys) {
            this.parent = parent;
            this.children = firstChildren;
            this.keys = firstKeys;
        }

        private void replaceContent(BTreeNode replacement) {
            this.children = new ArrayList<>(replacement.children);
            this.keys = new ArrayList<>(replacement.keys);
        }
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public int size() {
        return subtreeSize(root);
    }

    private int subtreeSize(BTreeNode node) {
        if (node == null) {
            return 0;
        }

        return node.children.size() + node.children.stream().map(this::subtreeSize).mapToInt(Integer::intValue).sum();
    }

    public boolean isEmpty() {
        return root == null;
    }

    @Override
    public boolean remove(Object o) {
        return false;
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

    private boolean addToSubtree(E e, BTreeNode node) {
        int index = getAppropriateIndex(e, node);

        //check ranges?

        if ((index < node.keys.size()) && (node.keys.get(index).compareTo(e) == 0)) {
            return false;
        } else {
            if (node.children.isEmpty()) {
                node.keys.add(index, e);
                rebalanceAfterInsertion(node);
                return true;
            } else {
                return addToSubtree(e, node.children.get(index));
            }
        }
    }

    private void rebalanceAfterInsertion(BTreeNode node) {
        if (node.keys.size() > 2 * MINIMUM_NUMBER_OF_KEYS_IN_A_NODE) {
            int median = node.keys.size() / 2;
            E middleKey = node.keys.get(median);

            //create two new nodes with the same parent, splitted keys/children

            List<E> firstKeys = new ArrayList<>(), secondKeys = new ArrayList<>();
            for (int i = 0; i < median; ++i) {
                firstKeys.add(node.keys.get(i));
            }

            for (int i = median + 1; i < node.keys.size(); ++i) {
                secondKeys.add(node.keys.get(i));
            }

            List<BTreeNode> firstChildren = new ArrayList<>(), secondChildren = new ArrayList<>();
            if (!node.children.isEmpty()) { //TODO: recheck this maybe?
                for (int i = 0; i < median; ++i) {
                    firstChildren.add(node.children.get(i));
                }

                for (int i = median + 1; i < node.children.size(); ++i) {
                    secondChildren.add(node.children.get(i));
                }
            }

            if (node.parent != null) {
                BTreeNode firstHalf = new BTreeNode(node.parent, firstChildren, firstKeys), secondHalf = new BTreeNode(node.parent, secondChildren, secondKeys);
                int indexOfCurrentNodeInParent = getAppropriateIndex(middleKey, node.parent);
                node.parent.children.remove(indexOfCurrentNodeInParent);
                node.parent.children.add(indexOfCurrentNodeInParent, secondHalf);
                node.parent.children.add(indexOfCurrentNodeInParent, firstHalf);
                node.parent.keys.add(indexOfCurrentNodeInParent, middleKey);
                rebalanceAfterInsertion(node.parent);
            } else {
                root = new BTreeNode(middleKey);
                BTreeNode firstHalf = new BTreeNode(root, firstChildren, firstKeys), secondHalf = new BTreeNode(root, secondChildren, secondKeys);
                root.children.add(firstHalf);
                root.children.add(secondHalf);
            }
        }
    }

    private BTreeNode initFirstNode(E e) {
        return new BTreeNode(e);
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

    private boolean subtreeContains(E e, BTreeNode node) {
        if (node == null) {
            return false;
        }

        int index = getAppropriateIndex(e, node);

        if (node.keys.get(index).compareTo(e) == 0) {
            return true;
        } else {
            return subtreeContains(e, node.children.get(index));
        }
    }

    private int getAppropriateIndex(E e, BTreeNode node) {
        if (node.keys.size() < NUMBER_OF_KEYS_TO_START_BINSEARCH) {
            for (int i = 0; i < node.keys.size(); ++i) {
                int compare = node.keys.get(i).compareTo(e);

                if (compare >= 0) {
                    return i;
                }
            }

            return node.keys.size();
        } else {
            int l = 0, r = node.keys.size(); //TODO: test this

            while (l < r) {
                int m = (l + r) / 2;

                int compare = node.keys.get(m).compareTo(e);

                if (compare == 0) {
                    return m;
                } else if (compare > 0) {
                    r = m + 1;
                } else {
                    l = m + 1;
                }
            }

            return l;
        }
    }
}
