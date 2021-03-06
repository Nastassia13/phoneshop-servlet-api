package com.es.phoneshop.exception;

public class NotFoundException extends RuntimeException {
    private Long id;
    private String className;

    public NotFoundException(Long id, String className) {
        this.id = id;
        this.className = className;
    }

    public Long getId() {
        return id;
    }

    public String getClassName() {
        return className;
    }
}
