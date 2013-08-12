package com.lazyloris.model;

import java.io.Serializable;

public interface Entity<T, ID extends Serializable> {

    ID getId();

    void setId(ID id);

    boolean equals(Object o);
}
