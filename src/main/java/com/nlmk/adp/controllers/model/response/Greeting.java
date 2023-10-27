package com.nlmk.adp.controllers.model.response;

/**
 * Модель сообщений веб сокета.
 */
public class Greeting {

    private String content;

    /**
     * Greeting.
     */
    public Greeting() {
    }

    /**
     * Greeting.
     *
     * @param content
     *          content
     */
    public Greeting(String content) {
        this.content = content;
    }

    /**
     * getContent.
     *
     * @return String
     */
    public String getContent() {
        return content;
    }

}
