package com.accepted.matches.mappers;

public interface Mapper<A, B> {
    B mapTo(A a);

    A mapFrom(B b);
}