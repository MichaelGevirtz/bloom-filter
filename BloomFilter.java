package collection;

import java.util.Set;

public interface BloomFilter<T>  {

    boolean isEmpty();

    int size();

    int length();

    boolean mightContains(T element);

    void add(T element);


    int addAll(Set<T> ips);
}
