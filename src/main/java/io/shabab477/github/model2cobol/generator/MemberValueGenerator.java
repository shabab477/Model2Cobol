package io.shabab477.github.model2cobol.generator;

/**
 * @author shabab
 * @since 11/6/18
 */
public interface MemberValueGenerator {

    public boolean supports(Object object);

    public String generateCode();
}
