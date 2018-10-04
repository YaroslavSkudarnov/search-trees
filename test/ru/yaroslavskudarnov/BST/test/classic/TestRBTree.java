package ru.yaroslavskudarnov.BST.test.classic;

import ru.yaroslavskudarnov.BST.classic.RBTree;
import ru.yaroslavskudarnov.BST.test.GeneralSearchTreesTest;

public class TestRBTree extends GeneralSearchTreesTest<RBTree<Integer>> {
    @Override
    protected RBTree<Integer> getTree() {
        return new RBTree<>();
    }
}
