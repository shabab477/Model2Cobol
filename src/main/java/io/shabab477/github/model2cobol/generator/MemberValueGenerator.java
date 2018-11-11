package io.shabab477.github.model2cobol.generator;

import io.shabab477.github.model2cobol.processor.LineBuilder;

/**
 * @author shabab
 * @since 11/6/18
 */
public interface MemberValueGenerator {

    public boolean supports(Class<?> clazz);

    public void generateCode(LineBuilder lineBuilder, Object object);
}
