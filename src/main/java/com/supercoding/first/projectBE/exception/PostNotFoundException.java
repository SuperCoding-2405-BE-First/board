package com.supercoding.first.projectBE.exception;

import javax.naming.AuthenticationException;

public class PostNotFoundException extends AuthenticationException {

  public PostNotFoundException(String msg) {
    super(msg);
  }

}
