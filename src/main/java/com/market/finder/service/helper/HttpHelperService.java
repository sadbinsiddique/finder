package com.market.finder.service.helper;

public interface HttpHelperService {
    String get(String url);
    <T> T getForObject(String url, Class<T> responseType);
}
