package com.blogspot.vsvydenko.reutersreader.entity;

/**
 * Created by vsvydenko on 07.10.14.
 */

public class FeedItem {

    private String title;
    private String link;
    private String description;
    private String pubDate;

    public FeedItem(String title, String link, String description, String pubDate) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.pubDate = pubDate;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }

    public String getPubDate() {
        return pubDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FeedItem feedItem = (FeedItem) o;

        if (!link.equals(feedItem.link)) {
            return false;
        }
        if (!pubDate.equals(feedItem.pubDate)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = link.hashCode();
        result = 31 * result + pubDate.hashCode();
        return result;
    }
}
