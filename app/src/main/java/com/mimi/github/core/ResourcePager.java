package com.mimi.github.core;

import org.eclipse.egit.github.core.client.PageIterator;

/**
 * Created by zwb on 15-10-20.
 */
public abstract class ResourcePager<E> {

    protected E register(final E resource) {
        return resource;
    }

    protected abstract Object getId(E resource);

    public abstract PageIterator<E> createIterator(final int page,
                                                   final int size);
}
