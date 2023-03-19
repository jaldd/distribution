package org.shaotang.sequence.generator;

import org.shaotang.sequence.ISequence;
import org.shaotang.sequence.local.DefaultSequence;

public class IDGenerator {

    private ISequence sequence;


    public IDGenerator(ISequence sequence) {
        this.sequence = sequence;
    }


    public IDGenerator() {
        this.sequence = new DefaultSequence();
    }

}
