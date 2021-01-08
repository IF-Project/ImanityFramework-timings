package org.imanity.framework.timings;


class EmptyTiming extends MCTiming {
    EmptyTiming() {
        super();
    }

    @Override
    public final MCTiming startTiming() {
        return this;
    }

    @Override
    public final void stopTiming() {

    }
}
