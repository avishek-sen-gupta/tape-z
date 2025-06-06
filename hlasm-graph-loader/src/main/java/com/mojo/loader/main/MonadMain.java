package com.mojo.loader.main;

import com.google.common.collect.ImmutableList;
import com.mojo.algorithms.list.CarCdr;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class MonadMain {

    public static void main(String[] args) {
        Stream<Integer> x = Stream.of(1, 2, 3);
        List<Integer> flattened = x.flatMap(e -> Stream.of(e * 2, e * 3)).toList();

        Stream<Stream<Integer>> y = Stream.of(Stream.of(2, 3), Stream.of(4, 6), Stream.of(6, 9));
        List<Integer> flattened2 = y.flatMap(e -> e).toList();

        // Left Identity Rule: Flatmapping a Monoid is the same as applying the function directly to the unwrapped value
        Function<Integer, Stream<Integer>> f = e -> Stream.of(e + 1);
        boolean equals = Stream.of(1).flatMap(f).toList().equals(f.apply(1).toList());

        // Right identity: Flatmapping a monoid with just a lifting function leaves the monoid unchanged
        boolean equals1 = Stream.of(1).flatMap(e -> Stream.of(1)).toList().equals(Stream.of(1).toList());

        // Associative Law
        Function<Integer, Stream<Integer>> i = e -> Stream.of(e + 1);
        Function<Integer, Stream<Integer>> j = e -> Stream.of(e + 2);
        List<Integer> lhs = Stream.of(1).flatMap(i).flatMap(j).toList();
        List<Integer> rhs = Stream.of(1).flatMap(e -> f.apply(e).flatMap(j)).toList();
        System.out.println(lhs.equals(rhs));
        System.out.println(equals);
        System.out.println(equals1);
        System.out.println(flattened);
        System.out.println(flattened2);
        System.out.println("COMPLETE!");

        List<Integer> original = ImmutableList.of(1, 2, 3);
        List<Integer> reversed = reverseExplicit(original, a -> a);
        System.out.println(reversed);
    }

    private static List<Integer> reverseExplicit(List<Integer> current, Function<List<Integer>, List<Integer>> x) {
        if (current.isEmpty()) return x.apply(ImmutableList.of());
        return reverseExplicit(CarCdr.tail(current),
                reversed -> Stream.concat(Stream.of(CarCdr.head(current).get()), x.apply(reversed).stream()).toList());
    }
}
