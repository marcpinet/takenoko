package takenoko.utils;

import java.util.Collection;
import java.util.Optional;
import java.util.Random;

public class Utils {
    private Utils() {
        throw new IllegalStateException("Utility class");
    }

    public static <T> Optional<T> randomPick(Collection<T> collection, Random random) {
        if (collection.isEmpty()) {
            return Optional.empty();
        }
        return collection.stream().skip(random.nextInt(collection.size())).findFirst();
    }
}
