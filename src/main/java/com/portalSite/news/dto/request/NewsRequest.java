package com.portalSite.news.dto.request;

public record NewsRequest(
    Long categoryId,
    String newsTitle,
    String description) {

}
