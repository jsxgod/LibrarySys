package util;
import java.security.SecureRandom;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

public class SaltGenerator {

    public String nextString() {
        for (int idx = 0; idx < buf.length; ++idx)
            buf[idx] = symbols[random.nextInt(symbols.length)];
        return new String(buf);
    }

    private final Random random;

    private final char[] symbols;

    private final char[] buf;

    public SaltGenerator(int length) {
        random = new SecureRandom();
        if (length < 1) throw new IllegalArgumentException();
        this.symbols = ("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuwxyz0123456789").toCharArray();
        this.buf = new char[length];
    }
}