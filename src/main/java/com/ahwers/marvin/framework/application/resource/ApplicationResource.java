package com.ahwers.marvin.framework.application.resource;

import com.ahwers.marvin.framework.application.resource.enums.ResourceRepresentationType;

public class ApplicationResource {

    private ResourceRepresentationType type;
    private String content;
    private String message;

    public ApplicationResource(ResourceRepresentationType type, String content, String message) {
        if (type == null) {
            throw new IllegalArgumentException();
        }
        else if (content == null) {
            throw new IllegalArgumentException();
        }

        this.type = type;
        this.content = content;
        this.message = message;
    }

    public ApplicationResource(ResourceRepresentationType type, String content) {
        if (type == null) {
            throw new IllegalArgumentException();
        }
        else if (content == null) {
            throw new IllegalArgumentException();
        }

        this.type = type;
        this.content = content;
    }

    public ResourceRepresentationType getType() {
        return this.type;
    }

    public String getContent() {
        return this.content;
    }

    public String getMessage() {
        return this.message;
    }

}