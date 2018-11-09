package io.shabab477.github.model2cobol.processor;

public class CobolBuilder {

    public static CobolBuilderChain with(Object object) {
        return new CobolBuilderChain(object);
    }

    private static class CobolBuilderChain {
        private int startingLevel = 1;
        private Object object;
        private int maxDepth = 5;

        CobolBuilderChain(Object object) {
            this.object = object;
        }

        public void withStartingLevel(int startingLevel) {
            this.startingLevel = startingLevel;
        }

        public void withMaxDepth(int maxDepth) {
            this.maxDepth = maxDepth;
        }

        public String build() {
            return ModelProcessor.processModel(object, startingLevel, maxDepth);
        }
    }
}
