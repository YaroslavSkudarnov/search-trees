package ru.yaroslavskudarnov.BST.test.esoteric;

import ru.yaroslavskudarnov.BST.esoteric.Treap;
import ru.yaroslavskudarnov.BST.test.GeneralSearchTreesTest;

public class TestTreap extends GeneralSearchTreesTest<Treap<Integer>> {
    @Override
    protected Treap<Integer> getTree() {
        return new Treap<>();
    }
}
