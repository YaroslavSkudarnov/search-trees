package ru.yaroslavskudarnov.BST.test.esoteric;

import ru.yaroslavskudarnov.BST.esoteric.SplayTree;
import ru.yaroslavskudarnov.BST.test.GeneralSearchTreesTest;

/**
 * User: Skudarnov Yaroslav
 * Date: 11/13/2018
 * Time: 2:20 PM
 */
public class TestSplayTree extends GeneralSearchTreesTest<SplayTree<Integer>> {
    @Override
    protected SplayTree<Integer> getTree() {
        return new SplayTree<>();
    }
}
