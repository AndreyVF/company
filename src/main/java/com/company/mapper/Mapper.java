package com.company.mapper;

/**
 * Generic interface to map entity on dto and another way around
 */
public interface Mapper <F, T> {

    T from(F from);

}
