package com.kantboot.util.http.callback;

import lombok.Data;

@Data
public abstract class HttpResponseStreamCallback {

    public abstract void run(String streamRead);

    public abstract void finish(String content);

}
