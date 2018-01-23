package com.example.administrator.wbook;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

/**
 * Created by sws28 on 2018-01-22.
 */

public interface Strategy {
    String urlStrategy();
    void uiStrategy(final JSONObject jsonobject) throws JSONException;
}
