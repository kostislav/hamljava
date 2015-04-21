package cz.judas.jan.haml.parser.tokens;

import cz.judas.jan.haml.parser.Grammar;
import cz.judas.jan.haml.parser.InputString;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

@SuppressWarnings("UtilityClass")
public class TokenCache {
    private static final Map<Caller, SameTokens> UNFINISHED_TOKENS = new HashMap<>();

    @SuppressWarnings("StaticNonFinalField")
    private static boolean BUILDING = false;

    public static synchronized <T> Token<T> build(Grammar<T> grammar) {
        try {
            BUILDING = true;
            Token<T> mainToken = grammar.buildRules();
            for (SameTokens proxyTokens : UNFINISHED_TOKENS.values()) {
                proxyTokens.initializeTokens();
            }
            return mainToken;
        } finally {
            UNFINISHED_TOKENS.clear();
            BUILDING = false;
        }
    }

    public static synchronized <T> Token<T> rule(Supplier<Token<T>> tokenSupplier) {
        Caller caller = getCaller();
        SameTokens tokens = UNFINISHED_TOKENS.get(caller);
        if (tokens == null) {
            tokens = new SameTokens();
            UNFINISHED_TOKENS.put(caller, tokens);
            Token<T> token = tokenSupplier.get();
            tokens.setRealToken(token);
            if(!BUILDING) {
                tokens.initializeTokens();
                UNFINISHED_TOKENS.remove(caller);
            }
            return token;
        } else {
            ProxyToken<T, Object> token = new ProxyToken<>();
            tokens.addProxyToken(token);
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

    private static class SameTokens {
        private final Set<ProxyToken<?, ?>> proxyTokens = new HashSet<>();
        private Token<?> realToken;

        public void addProxyToken(ProxyToken<?, ?> proxyToken) {
            proxyTokens.add(proxyToken);
        }

        public void setRealToken(Token<?> realToken) {
            this.realToken = realToken;
        }

        public void initializeTokens() {
            for (ProxyToken<?, ?> proxyToken : proxyTokens) {
                proxyToken.setRealToken(realToken);
            }
        }
    }

    private static class ProxyToken<C, T> implements TypedToken<C, T> {
        private Token<C> realToken;

        @SuppressWarnings("unchecked")
        private void setRealToken(Token<?> realToken) {
            this.realToken = (Token<C>) realToken;
        }

        @Override
        public boolean tryEat(InputString line, C parsingResult) {
            return realToken.tryEat(line, parsingResult);
        }
    }
}
