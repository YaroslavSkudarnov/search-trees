package ru.yaroslavskudarnov.BST.test.classic;

import ru.yaroslavskudarnov.BST.classic.RBTree;

public class TestRBTree extends GeneralSearchTreesTest<RBTree<Integer>> {
    @Override
    protected RBTree<Integer> getTree() {
        return new RBTree<>();
    }
}
