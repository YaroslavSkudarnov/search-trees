package ru.yaroslavskudarnov.BST.main;

import java.util.AbstractCollection;
import java.util.Collection;

public abstract class BinarySearchTree<E extends Comparable<? super E>> extends AbstractCollection<E> {
    BinarySearchTree() {}

    BinarySearchTree(Collection<E> collection) {
        this.addAll(collection);
    }
}
