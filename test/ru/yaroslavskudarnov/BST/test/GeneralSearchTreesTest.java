package ru.yaroslavskudarnov.BST.test;

import org.junit.Assert;
import org.junit.Test;
import ru.yaroslavskudarnov.BST.main.BinarySearchTree;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Skudarnov Yaroslav
 * Date: 10/8/2017
 * Time: 1:24 PM
 */
public abstract class GeneralSearchTreesTest<T extends BinarySearchTree<Integer, ?>> {
    @Test
    public void smallTestAddContainsAndRemove() {
        T tree = getTree();

        List<Integer> tmpList1 = List.of(1, 12, 3, -4, 5, 0, 6, 17, 8, -9, 10, 0);
        Assert.assertTrue(tree.addAll(tmpList1));
        Assert.assertFalse(tree.add(1));
        Assert.assertFalse(tree.add(3));
        Assert.assertFalse(tree.add(5));
        Assert.assertTrue(tree.add(7));
        Assert.assertFalse(tree.addAll(tmpList1));
        Assert.assertTrue(tree.containsAll(tmpList1));
        Assert.assertFalse(tree.contains(-1)); Assert.assertFalse(tree.contains(2)); Assert.assertFalse(tree.contains(87));
        Assert.assertTrue(tree.contains(12)); Assert.assertTrue(tree.contains(-9)); Assert.assertTrue(tree.contains(7));

        List<Integer> tmpList2 = List.of(1, 3, 5);
        tree.retainAll(tmpList2);
        Assert.assertTrue(tree.containsAll(tmpList2));
        Assert.assertFalse(tree.contains(0)); Assert.assertFalse(tree.contains(2)); Assert.assertFalse(tree.contains(10)); Assert.assertFalse(tree.contains(-9));

        tree.removeAll(tmpList2);
        Assert.assertFalse(tree.contains(1)); Assert.assertFalse(tree.contains(3)); Assert.assertFalse(tree.contains(5));
    }

    protected abstract T getTree();

    private long randomTest(int times, int elems) {
        long timeElapsed = 0;

        for (int i = 0; i < times; ++i) {
            List<Integer> firstList = new ArrayList<>(), secondList = new ArrayList<>();
            for (int j = 0; j < elems; ++j) {
                firstList.add((int) (Math.random() * 2 * elems)); secondList.add((int) (Math.random() * 2 * elems));
            }
            List<Integer> thirdList = new ArrayList<>(firstList);

            T tree = getTree();

            long start = System.currentTimeMillis();
            tree.addAll(firstList); tree.retainAll(secondList);
            long end = System.currentTimeMillis();
            timeElapsed += end - start;

            firstList.retainAll(secondList);
            Assert.assertTrue(firstList.containsAll(tree)); Assert.assertTrue(tree.containsAll(firstList));

            tree = getTree();

            start = System.currentTimeMillis();
            tree.addAll(thirdList); tree.removeAll(secondList);
            end = System.currentTimeMillis();
            timeElapsed += end - start;

            thirdList.removeAll(secondList);
            Assert.assertTrue(thirdList.containsAll(tree)); Assert.assertTrue(tree.containsAll(thirdList));
        }

        return timeElapsed;
    }

    @Test
    public void smallRandomTest() {
        randomTest(10, 10);
    }

    @Test
    public void bigRandomTests() {
        long timeElapsed;
        for (int i = 1; i <= 10; ++i) {
            timeElapsed = randomTest(5, i * 10000);
            System.out.println("all the insertions and deletions in big tests with " + i * 10000 + " elements took " + timeElapsed + " milliseconds!");
        }
    }
}
