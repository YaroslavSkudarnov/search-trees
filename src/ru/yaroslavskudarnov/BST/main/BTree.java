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

        private BTreeNode(E payload) {
            this.keys = new ArrayList<>();
            keys.add(payload);
        }

        private BTreeNode(BTreeNode node) {
            replaceContent(node);
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
        return false;
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
