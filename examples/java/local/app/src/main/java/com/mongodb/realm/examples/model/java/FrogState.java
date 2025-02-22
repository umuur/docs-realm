package com.mongodb.realm.examples.model.java;
// :code-block-start: complete
public enum FrogState {
    TADPOLE("Tadpole"),
    FROG("Frog"),
    OLD_FROG("Old Frog");

    private String state;
    FrogState(String state) {
        this.state = state;
    }
    public String getState() {
        return state;
    }
}
// :code-block-end: