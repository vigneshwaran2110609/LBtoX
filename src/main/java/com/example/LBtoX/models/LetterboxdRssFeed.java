package com.example.LBtoX.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.List;

@JacksonXmlRootElement(localName = "rss")
@JsonIgnoreProperties(ignoreUnknown = true)
public class LetterboxdRssFeed {

    @JacksonXmlProperty(localName = "channel")
    private Channel channel;

    public LetterboxdRssFeed() {}

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public List<LetterboxdRssEntry> getItems() {
        return channel != null ? channel.getItems() : null;
    }

    public int getItemCount() {
        List<LetterboxdRssEntry> items = getItems();
        return items != null ? items.size() : 0;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Channel {
        
        private String title;
        private String link;
        private String description;

        @JacksonXmlElementWrapper(useWrapping = false)
        @JacksonXmlProperty(localName = "item")
        private List<LetterboxdRssEntry> items;

        public Channel() {}

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public List<LetterboxdRssEntry> getItems() {
            return items;
        }

        public void setItems(List<LetterboxdRssEntry> items) {
            this.items = items;
        }
    }

    @Override
    public String toString() {
        return "LetterboxdRssFeed{" +
                "title='" + (channel != null ? channel.getTitle() : "null") + '\'' +
                ", itemCount=" + getItemCount() +
                '}';
    }
}