package cz.judas.jan.haml.parser.tokens;

import cz.judas.jan.haml.parser.InputString;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

@SuppressWarnings("UtilityClass")
public class TokenCache {
    private static final Map<Caller, Set<ProxyToken<?>>> UNFINISHED_TOKENS = new HashMap<>();

    public static synchronized <T> Token<T> rule(Supplier<Token<T>> tokenSupplier) {
        Caller caller = getCaller();
        Set<ProxyToken<?>> tokens = UNFINISHED_TOKENS.get(caller);
        if(tokens == null) {
            tokens = new HashSet<>();
            UNFINISHED_TOKENS.put(caller, tokens);
            Token<T> token = tokenSupplier.get();
            for (ProxyToken<?> proxyToken : tokens) {
                proxyToken.setRealToken(token);
            }
            UNFINISHED_TOKENS.remove(caller);
            return token;
        } else {
            ProxyToken<T> token = new ProxyToken<>();
            tokens.add(token);
            return token;
        }
    }

    private static Caller getCaller() {
        Throwable throwable = new Throwable();
        StackTraceElement[] stackTrace = throwable.getStackTrace();
        StackTraceElement stackTraceElement = stackTrace[2];

        return new Caller(stackTraceElement.getClassName(), stackTraceElement.getMethodName());
    }

    private static class Caller {
        private final String className;
        private final String methodName;

        private Caller(String className, String methodName) {
            this.className = className;
            this.methodName = methodName;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Caller caller = (Caller) o;

            return className.equals(caller.className)
                    && methodName.equals(caller.methodName);
        }

        @Override
        public int hashCode() {
            int result = className.hashCode();
            result = 31 * result + methodName.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return className + "." + methodName;
        }
    }

    private static class ProxyToken<T> implements Token<T> {
        private Token<T> realToken;

        @SuppressWarnings("unchecked")
        private void setRealToken(Token<?> realToken) {
            this.realToken = (Token<T>)realToken;
        }

        @Override
        public boolean tryEat(InputString line, T parsingResult) {
            return realToken.tryEat(line, parsingResult);
        }
    }
}
