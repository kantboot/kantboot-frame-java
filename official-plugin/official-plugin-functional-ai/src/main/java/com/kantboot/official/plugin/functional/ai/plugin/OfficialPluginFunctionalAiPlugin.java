package com.kantboot.official.plugin.functional.ai.plugin;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.kantboot.ai.chat.domain.entity.AiChatModel;
import com.kantboot.ai.chat.domain.vo.AiChatMessageAllVO;
import com.kantboot.ai.chat.method.AiChatMethod;
import com.kantboot.ai.chat.slot.AiChatSlot;
import okhttp3.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Configuration
public class OfficialPluginFunctionalAiPlugin {

    private static final MediaType JSON_MEDIA_TYPE = MediaType.get("application/json; charset=utf-8");

    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.MINUTES)
            .build();

    @Bean
    public AiChatSlot aiChatSlot() {
        return new AiChatSlot() {
            @Override
            public void sendMessageHasStream(
                    List<AiChatMessageAllVO> messages,
                    AiChatModel model,
                    AiChatMethod method
            ) {
                doStream(messages, model, method);
            }
        };
    }

    private void doStream(List<AiChatMessageAllVO> messages, AiChatModel model, AiChatMethod method) {
        List<Map<String, String>> msgList = new ArrayList<>();
        for (AiChatMessageAllVO msg : messages) {
            msgList.add(Map.of("role", msg.getRole(), "content", msg.getContent()));
        }

        JSONObject body = new JSONObject();
        body.put("model", model.getModelCode());
        body.put("messages", msgList);
        body.put("stream", true);
        if (model.getTemperature() != null) {
            body.put("temperature", model.getTemperature());
        }
        if (model.getMaxTokens() != null) {
            body.put("max_tokens", model.getMaxTokens());
        }

        String url = model.getApiUrl();
        if (!url.endsWith("/")) {
            url = url + "/";
        }
        url = url + "v1/chat/completions";

        Request.Builder requestBuilder = new Request.Builder()
                .url(url)
                .post(RequestBody.create(body.toJSONString(), JSON_MEDIA_TYPE));

        Map<String, String> headers = model.getRequestHeaders();
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                requestBuilder.addHeader(entry.getKey(), entry.getValue());
            }
        }

        Request request = requestBuilder.build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException("AI request failed: " + response.code() + " " + response.message());
            }

            ResponseBody responseBody = response.body();
            if (responseBody == null) {
                throw new RuntimeException("AI response body is null");
            }

            StringBuilder contentBuilder = new StringBuilder();

            try (InputStream is = responseBody.byteStream();
                 BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {

                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.isEmpty() || !line.startsWith("data:")) {
                        continue;
                    }

                    String data = line.substring("data:".length()).strip();

                    if ("[DONE]".equals(data)) {
                        method.run("", contentBuilder.toString(), true);
                        method.finish(contentBuilder.toString());
                        return;
                    }

                    JSONObject json;
                    try {
                        json = JSON.parseObject(data);
                    } catch (Exception e) {
                        continue;
                    }

                    if (json == null) {
                        continue;
                    }

                    var choices = json.getJSONArray("choices");
                    if (choices == null || choices.isEmpty()) {
                        continue;
                    }

                    JSONObject delta = choices.getJSONObject(0).getJSONObject("delta");
                    if (delta == null) {
                        continue;
                    }

                    String text = delta.getString("content");
                    if (text == null) {
                        text = "";
                    }

                    contentBuilder.append(text);

                    String finishReason = choices.getJSONObject(0).getString("finish_reason");
                    boolean done = "stop".equals(finishReason) || "length".equals(finishReason);

                    method.run(text, contentBuilder.toString(), done);

                    if (done) {
                        method.finish(contentBuilder.toString());
                        return;
                    }
                }
            }

            method.finish(contentBuilder.toString());

        } catch (IOException e) {
            throw new RuntimeException("AI stream request error: " + e.getMessage(), e);
        }
    }
}
