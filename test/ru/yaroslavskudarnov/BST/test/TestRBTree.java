package ru.yaroslavskudarnov.BST.test;

import ru.yaroslavskudarnov.BST.main.RBTree;

public class TestRBTree extends GeneralSearchTreesTest<RBTree<Integer>> {
    @Override
    protected RBTree<Integer> getTree() {
        return new RBTree<>();
    }
}
