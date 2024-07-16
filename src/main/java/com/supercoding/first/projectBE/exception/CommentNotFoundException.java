package com.supercoding.first.projectBE.exception;

import javax.naming.AuthenticationException;

public class CommentNotFoundException extends AuthenticationException {

  public CommentNotFoundException(String msg) {
    super(msg);
  }

}
