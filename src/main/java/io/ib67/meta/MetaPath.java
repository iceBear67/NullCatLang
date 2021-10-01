package io.ib67.meta;

import io.ib67.parser.meta.CatMetadata;

public interface MetaPath {
    CatMetadata findClass(String clazz);
}
