package com.lukka.notifybackend.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
@Getter
public class EmptyRepositoryException extends RuntimeException{

    private final String repositoryName;
    private final String operation;

    public EmptyRepositoryException(String operation, String repositoryName) {
        super(String.format("%s operation failed because %s repository is empty.", operation, repositoryName));
        this.repositoryName = repositoryName;
        this.operation = operation;
    }
}
