package io.shabab477.github.model2cobol.processor;

import java.util.concurrent.atomic.AtomicInteger;

public class CobolBuilder {

    public static CobolBuilderChain with(Object object) {
        return new CobolBuilderChain(object);
    }

    public static class CobolBuilderChain {
        private int startingLevel = 1;
        private Object object;
        private int maxDepth = 5;

        private CobolBuilderChain(Object object) {
            this.object = object;
        }

        public void withStartingLevel(int startingLevel) {
            this.startingLevel = startingLevel;
        }

        public void withMaxDepth(int maxDepth) {
            this.maxDepth = maxDepth;
        }

        public String build() {
            return ModelProcessor.processModel(object, startingLevel, maxDepth, new AtomicInteger());
        }
    }
}
