package com.angelmaker.japaneseflashcards.supportFiles;

import java.io.Serializable;
import java.util.Observable;

public class ObservableBoolean extends Observable implements Serializable {

    private Boolean state;

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean newState){
        this.state = newState;
        this.setChanged();
        this.notifyObservers(state);
    }
}
