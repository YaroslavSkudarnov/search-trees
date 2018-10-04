package ru.yaroslavskudarnov.BST.test.classic;

import ru.yaroslavskudarnov.BST.classic.AVLTree;
import ru.yaroslavskudarnov.BST.test.GeneralSearchTreesTest;

/**
 * User: Skudarnov Yaroslav
 * Date: 2/10/2018
 * Time: 2:46 PM
 */
public class TestAVLTree extends GeneralSearchTreesTest<AVLTree<Integer>> {
    @Override
    protected AVLTree<Integer> getTree() {
        return new AVLTree<>();
    }
}
