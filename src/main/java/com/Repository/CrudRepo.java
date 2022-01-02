
package com.Repository;

import java.util.ArrayList;

public interface CrudRepo<E> {
    E findOne(int var1);

    ArrayList<E> findAll();

    E save(E var1);

    E delete(int var1);

    E update(E var1);
}