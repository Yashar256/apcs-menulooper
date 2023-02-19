package io.github.yashar256.looper;

import static io.github.yashar256.looper.ScannerTestCase.createNewTest;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

public class MenuLooperTest {
    @Test
    public void testCustomScannerMethods() {
        createNewTest("foo", "foo", s -> s.next("Next word"));
        createNewTest("0", new BigInteger("0"), s -> s.nextBigInteger("Big Integer"));
        createNewTest("0.02", new BigDecimal("0.02"), s -> s.nextBigDecimal("Big Decimal"));
        createNewTest("110", new BigInteger("10"), s -> s.nextBigInteger("Big Decimal, radix 2", 2));
        createNewTest("t", true, s -> s.nextBoolean("Boolean, t"));
        createNewTest("T", true, s -> s.nextBoolean("Boolean, T"));
        createNewTest("f", false, s -> s.nextBoolean("Boolean, f"));
        createNewTest("F", false, s -> s.nextBoolean("Boolean, F"));
        createNewTest("f", true, s -> s.nextBoolean("Next boolean, with 1 wrong input"));
        createNewTest("4", (byte) 4, s -> s.nextByte("Byte"));
        createNewTest("11", (byte) 0b11, s -> s.nextByte("Byte radix 2", 2));
        createNewTest("0.50", 0.50d, s -> s.nextDouble("Double"));
        createNewTest("0.50", 0.50f, s -> s.nextFloat("Float"));
        createNewTest("0.50", 0.50f, s -> s.nextDouble("Float"));
        createNewTest("49", 49, s -> s.nextInt("Int"));
        createNewTest("11101", 0b11101, s -> s.nextInt("Int radix 2", 2));
        createNewTest("this is a line", "this is a line", s -> s.nextLine("Next line"));
        createNewTest("2023", 2023L, s -> s.nextLong("Long"));
        createNewTest("11101", 0b11101L, s -> s.nextLong("Long radix 2"));
        createNewTest("34", (short) 34, s -> s.nextShort("Short"));
        createNewTest("11101", (short) 0b11101, s -> s.nextShort("Short, radix 2", 2));
    }
}
