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
            this.parent = replacement.parent;
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
        if (o == null) {
            return false;
        } else {
            @SuppressWarnings("unchecked")
            E e = (E) o;

            return root != null && removeFromSubtree(e, root);
        }
    }

    private boolean removeFromSubtree(E e, BTreeNode node) {
        int index = getAppropriateIndex(e, node);

        if ((index < node.keys.size()) && (node.keys.get(index).compareTo(e) == 0)) {
            if (node.children.isEmpty()) {
                node.keys.remove(index);
                rebalanceAfterRemoval(node, e);
            } else {
                BTreeNode leafWithReplacement = leftmostDescendant(node.children.get(index + 1));
                E replacement = leafWithReplacement.keys.get(0);
                node.keys.set(index, replacement);
                leafWithReplacement.keys.remove(0);
                rebalanceAfterRemoval(leafWithReplacement, replacement); //TODO: replacement now is in the parent of that leaf. how do we handle this? test it.
            }

            return true;
        } else {
            return !node.children.isEmpty() && removeFromSubtree(e, node.children.get(index));
        }
    }

    private void rebalanceAfterRemoval(BTreeNode node, E removedElement) {
        BTreeNode parent = node.parent;

        if (parent == null) {
            if (node.keys.isEmpty()) {
                if (node.children.isEmpty()) {
                    root = null;
                } else {
                    assert root.children.size() < 2;
                    root = root.children.get(0);
                    root.parent = null;
                }
            }

            return;
        }

        if (node.keys.size() < MINIMUM_NUMBER_OF_KEYS_IN_A_NODE) {
            int indexInParent = getAppropriateIndex(removedElement, parent);
            E exSeparator = parent.keys.get(indexInParent);

            if ((indexInParent + 1 < parent.children.size()) && (parent.children.get(indexInParent + 1).keys.size() > MINIMUM_NUMBER_OF_KEYS_IN_A_NODE)) {
                BTreeNode rightSibling = parent.children.get(indexInParent + 1);

                node.keys.add(exSeparator);
                parent.keys.set(indexInParent, rightSibling.keys.get(0));
                rightSibling.keys.remove(0);

                node.children.add(rightSibling.children.get(0));
                rightSibling.children.remove(0);

                return;
            }

            if ((indexInParent > 0) && (parent.children.get(indexInParent - 1).keys.size() > MINIMUM_NUMBER_OF_KEYS_IN_A_NODE)) {
                BTreeNode leftSibling = parent.children.get(indexInParent - 1);

                node.keys.add(exSeparator);
                parent.keys.set(indexInParent, leftSibling.keys.get(leftSibling.keys.size() - 1));
                leftSibling.keys.remove(leftSibling.keys.size() - 1);

                node.children.add(leftSibling.children.get(leftSibling.children.size() - 1));
                leftSibling.children.remove(leftSibling.children.size() - 1);

                return;
            }

            BTreeNode leftNode, rightNode;

            if (indexInParent + 1 < parent.children.size()) { //we have right sibling
                leftNode = node; rightNode = parent.children.get(indexInParent + 1);
            } else if (indexInParent > 0) { //we have left sibling
                leftNode = parent.children.get(indexInParent - 1); rightNode = node;
            } else {
                leftNode = node; rightNode = node;
                assert false;
            }

            leftNode.keys.add(exSeparator);
            leftNode.keys.addAll(rightNode.keys);
            leftNode.children.addAll(rightNode.children);
            parent.children.remove(indexInParent + 1);
            parent.keys.remove(indexInParent);
            rebalanceAfterRemoval(node, exSeparator);
        }
    }

    private BTreeNode leftmostDescendant(BTreeNode node) {
        if (node == null) {
            return null;
        }

        while (!node.children.isEmpty()) {
            node = node.children.get(0);
        }

        return node;
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

            List<E> firstKeys = new ArrayList<>(), secondKeys = new ArrayList<>();
            for (int i = 0; i < median; ++i) {
                firstKeys.add(node.keys.get(i));
            }

            for (int i = median + 1; i < node.keys.size(); ++i) {
                secondKeys.add(node.keys.get(i));
            }

            List<BTreeNode> firstChildren = new ArrayList<>(), secondChildren = new ArrayList<>();
            if (!node.children.isEmpty()) { //TODO: recheck this maybe?
                for (int i = 0; i < node.children.size() / 2; ++i) {
                    firstChildren.add(node.children.get(i));
                }

                for (int i = node.children.size() / 2; i < node.children.size(); ++i) {
                    secondChildren.add(node.children.get(i));
                }
            }

            BTreeNode newChildrenParent = node.parent == null ? root : node.parent;
            BTreeNode firstHalf = new BTreeNode(newChildrenParent, firstChildren, firstKeys), secondHalf = new BTreeNode(newChildrenParent, secondChildren, secondKeys);
            for (BTreeNode childNode : firstHalf.children) {
                childNode.parent = firstHalf;
            }
            for (BTreeNode childNode : secondHalf.children) {
                childNode.parent = secondHalf;
            }


            if (node.parent != null) {
                int indexOfCurrentNodeInParent = getAppropriateIndex(middleKey, node.parent);

                node.parent.children.remove(indexOfCurrentNodeInParent);
                node.parent.children.add(indexOfCurrentNodeInParent, secondHalf);
                node.parent.children.add(indexOfCurrentNodeInParent, firstHalf);
                node.parent.keys.add(indexOfCurrentNodeInParent, middleKey);

                rebalanceAfterInsertion(node.parent);
            } else {
                BTreeNode newRoot = new BTreeNode(middleKey);
                newRoot.children.add(firstHalf);
                newRoot.children.add(secondHalf);
                root.replaceContent(newRoot);
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
            int l = 0, r = node.keys.size() - 1;

            while (l < r) {
                int m = (l + r) / 2;

                int compare = node.keys.get(m).compareTo(e);

                if (compare == 0) {
                    return m;
                } else if (compare > 0) {
                    r = m - 1;
                } else {
                    l = m + 1;
                }
            }

            if (node.keys.get(l).compareTo(e) < 0) {
                return l + 1;
            } else {
                return l;
            }
        }
    }
}
