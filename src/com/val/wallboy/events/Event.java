package com.val.wallboy.events;

//TODO ADD JAVA DOC
public class Event {
    public RunnableObjects action;
    long id;

    public Event(Long id, RunnableObjects newSub) {
        this.id = id;
        this.action = newSub;
    }

}
