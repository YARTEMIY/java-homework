package edu.phystech.hw2;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

class ToUpperCaseOperator implements UnaryOperator<String> {
    @Override
    public String apply(String s) {
        return s.toUpperCase();
    }
}

// Возвращает модуль максимума из двух модулей чисел
class AbsMaxOperator implements BinaryOperator<Integer> {

    @Override
    public Integer apply(Integer integer, Integer integer2) {
        return Math.max(Math.abs(integer), Math.abs(integer2));
    }
}

class StringLengthMoreThan5 implements Predicate<String> {

    @Override
    public boolean test(String s) {
        return s.length() > 5;
    }
}

// Проверяет, является ли число квадратом
class IsNumberASquareOfAnotherNumber implements Predicate<Integer> {

    @Override
    public boolean test(Integer integer) {
        double sqrt = Math.sqrt(integer);
        return sqrt * sqrt == integer;
    }
}

// Возвращает четные числа, начиная с from включительно, если в from нечетное
// число, то начиная с первого четного с from
class EvenNumberSupplier implements Supplier<Integer> {

    private int number;

    public EvenNumberSupplier(int from) {
        if (from % 2 == 0) {
            this.number = from;
        } else {
            this.number = from + 1;
        }
    }

    @Override
    public Integer get() {
        int answer = this.number;
        this.number += 2;
        return answer;
    }
}

public class FunctionalInterfacesTest {

    @Test
    public void unaryOperatorTest() {
        var result = new ArrayList<>(List.of("abC", "edf"));
        result.replaceAll(new ToUpperCaseOperator());
        Assertions.assertEquals(List.of("ABC", "EDF"), result);
    }

    @Test
    public void binaryOperatorTest() {
        var result = Stream.of(2, 3, 1, -10).reduce(4, new AbsMaxOperator());
        Assertions.assertEquals(10, result);
    }

    @Test
    public void predicateTest() {
        Assertions.assertEquals(
                Stream.of("a", "bb", "ccc", "1234567", "aaaaaaaaa").filter(new StringLengthMoreThan5()).toList(),
                List.of("1234567", "aaaaaaaaa"));

        Assertions.assertEquals(
                Stream.of(1, 4, 5, 10, 16, 25).filter(new IsNumberASquareOfAnotherNumber()).toList(),
                List.of(1, 4, 16, 25));
    }

    @Test
    public void supplierTest() {
        var evenNumberSupplier = new EvenNumberSupplier(0);
        Stream.of(0, 2, 4, 6, 8, 10).forEach(number -> Assertions.assertEquals(number, evenNumberSupplier.get()));

        var anotherSupplier = new EvenNumberSupplier(11);
        Stream.of(12, 14, 16, 18, 20, 22).forEach(number -> Assertions.assertEquals(number, anotherSupplier.get()));
    }

}