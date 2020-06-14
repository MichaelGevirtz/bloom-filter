package collection;

import org.junit.Before;
import org.junit.Test;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.stream.IntStream;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

public class InMemoryBloomFilterTest {

    private static final int CAPACITY = 100;
    private BloomFilter bloomFilter;
    private String EXISTING_IP = "0.0.0.255";

    @Before
    public void setUp() throws NoSuchAlgorithmException {
        bloomFilter = new InMemoryBloomFilter(CAPACITY, MessageDigest.getInstance("SHA-256"), 2);
        Set<String> ips = generateIps();
        bloomFilter.addAll(ips);

    }

    @Test
    public void element_does_not_exist() {

        assertFalse(bloomFilter.mightContains("not exist"));
    }

    @Test
    public void element_does_exist() {

        assertTrue(bloomFilter.mightContains(EXISTING_IP));
    }

    private Set<String> generateIps() {

        Set<String> result = new HashSet<>();
        Random r = new Random();

        IntStream.range(0, 20).forEach(
                num -> {
                    StringBuffer sb = new StringBuffer();
                    String ip = sb.append(r.nextInt(256)).append(".").append(r.nextInt(256)).append(".").append(r.nextInt(256)).append(".").append(
                            r.nextInt(256)).toString();
                    result.add(ip);
                }
        );
        result.add(EXISTING_IP);

        return result;
    }
}
