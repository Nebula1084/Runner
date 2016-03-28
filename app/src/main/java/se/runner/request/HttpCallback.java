package se.runner.request;

public interface HttpCallback {
    String baseUrl = "http://10.214.0.195:9000";
    void onPost(String get);
}