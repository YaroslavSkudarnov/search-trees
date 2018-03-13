package ru.yaroslavskudarnov.BST.test;

import org.junit.Assert;
import org.junit.Test;
import ru.yaroslavskudarnov.BST.main.BinarySearchTree;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
            Set<Integer> firstSet = new HashSet<>(), secondSet = new HashSet<>();
            for (int j = 0; j < elems; ++j) {
                firstSet.add((int) (Math.random() * 2 * elems)); secondSet.add((int) (Math.random() * 2 * elems));
            }
            Set<Integer> thirdSet = new HashSet<>(firstSet);

            T tree = getTree();

            long start = System.currentTimeMillis();
            tree.addAll(firstSet); tree.retainAll(secondSet);
            long end = System.currentTimeMillis();
            timeElapsed += end - start;

            firstSet.retainAll(secondSet);
            Assert.assertTrue(firstSet.containsAll(tree)); Assert.assertTrue(tree.containsAll(firstSet));

            tree = getTree();

            start = System.currentTimeMillis();
            tree.addAll(thirdSet); tree.removeAll(secondSet);
            end = System.currentTimeMillis();
            timeElapsed += end - start;

            thirdSet.removeAll(secondSet);
            Assert.assertTrue(thirdSet.containsAll(tree)); Assert.assertTrue(tree.containsAll(thirdSet));
        }

        return timeElapsed;
    }

    @Test
    public void smallRandomTest() {
        for (int i = 1; i <= 20; i++) {
            randomTest(10, 15);
        }
    }

    @Test
    public void bigRandomTests() {
        long timeElapsed;

        for (int i = 1; i <= 10; ++i) {
            randomTest(15, i * 10000); //to warmup the JVM
        }

        for (int i = 1; i <= 20; ++i) {
            timeElapsed = randomTest(5, i * 10000);
            System.out.println("all the insertions and deletions in big tests with " + i * 10000 + " elements took " + timeElapsed + " milliseconds!");
        }
    }
}
