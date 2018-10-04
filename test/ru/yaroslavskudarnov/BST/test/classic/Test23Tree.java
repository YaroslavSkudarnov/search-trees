package ru.yaroslavskudarnov.BST.test.classic;

import ru.yaroslavskudarnov.BST.classic.BTree;
import ru.yaroslavskudarnov.BST.test.GeneralSearchTreesTest;

/**
 * User: Skudarnov Yaroslav
 * Date: 9/13/2018
 * Time: 11:45 AM
 */
public class Test23Tree extends GeneralSearchTreesTest<BTree<Integer>> {
    @Override
    protected BTree<Integer> getTree() {
        return new BTree<>(1);
    }
}
