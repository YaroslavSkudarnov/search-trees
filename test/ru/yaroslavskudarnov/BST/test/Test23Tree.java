package ru.yaroslavskudarnov.BST.test;

import ru.yaroslavskudarnov.BST.main.BTree;

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
