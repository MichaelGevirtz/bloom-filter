package collection;

import com.sangupta.murmur.Murmur3;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class InMemoryBloomFilter<T> implements BloomFilter<T>, Serializable {

    private BitSet elementData;
    private MessageDigest digestFunction;
    private Integer numberOfHashes = 2;
    private final int capacity;

    public InMemoryBloomFilter(int capacity, MessageDigest digestFunction, int numberOfHashes) {
        this.elementData = new BitSet(capacity);
        this.capacity = capacity;
        this.digestFunction = digestFunction;
        this.numberOfHashes = numberOfHashes;
    }

    @Override
    public boolean isEmpty() {
        return elementData.size() > 0 ? Boolean.FALSE : Boolean.TRUE;
    }

    @Override
    public int size() {
        return elementData.size();
    }

    @Override
    public int length() {
        return elementData.length();
    }

    @Override
    public boolean mightContains(T element) {

        List<Long> indexes = calculateIndex(element.toString().getBytes());

        for (Long index : indexes) {

            if ( ! elementData.get(index.intValue()) ) {
                return false;
            }
        }

        return true;
    }

    @Override
    public void add(T element) {
        List<Long> indexes = calculateIndex(element.toString().getBytes());

        indexes.forEach(num -> elementData.set(num.intValue()));
    }

    @Override
    public int addAll(Set<T> ips) {

        ips.forEach(ip -> {
            add(ip);
        });
        return length();
    }

    private List<Long> calculateIndex(byte[] input) {

        long[] hashes = Murmur3.hash_x64_128(input, input.length, 0L); // 128-bit hash
        List<Long> hashList = Arrays.stream(hashes).boxed().collect(Collectors.toList());

        return hashList.stream()
                .map(hash -> Math.abs(hash) % capacity)
                .collect(Collectors.toList());

    }

    private static int byteArrayToLeInt(byte[] b) {
        final ByteBuffer bb = ByteBuffer.wrap(b);
        return bb.getInt();
    }


}
