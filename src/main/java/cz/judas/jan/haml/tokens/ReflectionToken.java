package cz.judas.jan.haml.tokens;

import cz.judas.jan.haml.ParseException;

import java.lang.reflect.Field;

public class ReflectionToken<T> implements Token<T> {
    private final Field field;
    private volatile Token<T> token = null;

    @SuppressWarnings("unchecked")
    public ReflectionToken(Field field) {
        this.field = field;
    }

    @SuppressWarnings("unchecked")
    @Override
    public int tryEat(String line, int position, T parsingResult) throws ParseException {
        try {
            if(token == null) {
                token = ((Token<T>)field.get(null));
            }
            return token.tryEat(line, position, parsingResult);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Could not access field " + field.getName() + " of " + field.getDeclaringClass().getName());
        }
    }

    public static <T> Token<T> reference(String fieldName) {
        try {
            Throwable throwable = new Throwable();
            String callerClassName = throwable.getStackTrace()[1].getClassName();
            Class<?> callerClass = Class.forName(callerClassName);
            Field field = callerClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            return new ReflectionToken<>(field);
        } catch (ClassNotFoundException | NoSuchFieldException e) {
            throw new RuntimeException("Could not access field " + fieldName);
        }
    }
}
