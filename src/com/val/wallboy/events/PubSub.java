package com.val.wallboy.events;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

//TODO ADD JAVA DOC
public class PubSub {
    private static HashMap<String, Set<Long>> registrar;
    private static HashMap<Long, Event> events;
    private static long idSize = 0;
    private static String _TAG = PubSub.class.getName().replace(".", "/");

    public static void resetRegistrar() {
        registrar = new HashMap<>();
        events = new HashMap<>();
        idSize = 0;
    }

    public static void sendMessage(String topic, Object... args) {
        if (registrar.containsKey(topic)) {
            for (Long curID : registrar.get(topic)) {
                RunnableObjects curEvent = events.get(curID).action;
                if (curEvent != null)
                    try {
                        curEvent.run(args);
                    } catch (ClassCastException e) {
                        System.out.println("PubSub: Unable to cast Arguments!");
                        e.printStackTrace();
                    }
            }
        }
    }

    public static Event subscribe(String topic, RunnableObjects event) {
        if (!registrar.containsKey(topic)) {
            return addSubscriber(new HashSet<Long>(), idSize++, event, topic);
        } else {
            return addSubscriber(registrar.get(topic), idSize++, event, topic);
        }
    }

    public static void unsubscribe(long id, String topic) {
        registrar.get(topic).remove(id);
        events.remove(id);
    }

    private static Event addSubscriber(Set<Long> subscribers, Long id, RunnableObjects newSub, String topic) {
        subscribers.add(id);
        Event newOne = new Event(id, newSub);
        registrar.put(topic, subscribers);
        events.put(id, newOne);
        return newOne;
    }
}