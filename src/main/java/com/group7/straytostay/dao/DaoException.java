package com.group7.straytostay.dao;

/**
 * Wraps SQLException so the layers above the DAO do not depend on JDBC.
 */
public class DaoException extends RuntimeException {

    public DaoException(String message, Throwable cause) {
        super(message, cause);
    }
}
