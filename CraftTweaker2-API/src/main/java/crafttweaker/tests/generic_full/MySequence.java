package crafttweaker.tests.generic_full;

import stanhebben.zenscript.annotations.*;

import java.util.*;
import java.util.stream.*;

public class MySequence<T> {
    
    private final List<T> content;
    
    public MySequence(T[] content) {
        this.content = Arrays.asList(content);
    }
    
    public MySequence(List<T> content) {
        this.content = content;
    }
    
    public <U> MySequence<U> map(Functions.Mapper<T, U> mapper) {
        return new MySequence<>(content.stream().map(mapper::map).collect(Collectors.toList()));
    }
    
    public MySequence<T> filter(Functions.Predicate<T> predicate) {
        return new MySequence<T>(content.stream().filter(predicate::test).collect(Collectors.toList()));
    }
    
    public void forEach(Functions.Consumer<T> consumer) {
        content.forEach(consumer::accept);
    }
}
