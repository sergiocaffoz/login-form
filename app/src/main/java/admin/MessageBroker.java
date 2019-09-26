package admin;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MessageBroker {
    private static final MessageBroker ourInstance = new MessageBroker();
    private static final HashMap<String, ArrayList<Observable>> Observers = new HashMap<>();

    public static MessageBroker getInstance() {
        return ourInstance;
    }

    private MessageBroker() {}

    public void publish(String eventName, Boolean success, JSONObject result)
    {
        if (Observers.containsKey(eventName))
        {
            ArrayList<Observable> obs = Observers.get(eventName);
            if (obs != null && obs.size() > 0)
                for (Observable o: obs) {
                    o.onEvent(eventName, success, result);
                }
        }
    }

    public void observe(String eventName, Observable observer) {
        if (Observers.containsKey(eventName))
        {
            Observers.get(eventName).add(observer);
        }
        else
        {
            ArrayList<Observable> obs = new ArrayList<>();
            obs.add(observer);

            Observers.put(eventName, obs);
        }
    }

    public void deObserve(String eventName, Observable observer)
    {
        if (Observers.containsKey(eventName)) {
            ArrayList obs = Observers.get(eventName);
            obs.remove(observer);
        }
    }
}
