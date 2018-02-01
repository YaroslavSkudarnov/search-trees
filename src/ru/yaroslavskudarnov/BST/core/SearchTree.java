package ru.yaroslavskudarnov.BST.core;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;

public abstract class SearchTree<E extends Comparable<? super E>> extends AbstractCollection<E> {
    protected SearchTree() {}

    public SearchTree(Collection<E> collection) {
        this.addAll(collection);
    }

    @Override
    abstract public Iterator<E> iterator();

    @Override
    abstract public int size();
    
    @Override
    abstract public boolean remove(Object o);

    @Override
    abstract public boolean add(E e);

    @Override
    abstract public boolean contains(Object o);
}
