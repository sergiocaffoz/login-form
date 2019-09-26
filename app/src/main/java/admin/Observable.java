package admin;

import org.json.JSONObject;

public interface Observable {
    void onEvent(String event, Boolean success, JSONObject object);
}
