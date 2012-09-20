package org.nsu.images.controllers;

/**
 * Created with IntelliJ IDEA.
 * User: max
 * Date: 9/18/12
 * Time: 5:03 PM
 * To change this template use File | Settings | File Templates.
 */
public enum ServerBehaviour {
  RESPONSE_OK,
  RESPONSE_503,
  RESPONSE_CLOSE_CONNECTION,
  RESPONSE_WAIT_TIMEOUT,
  LAST_VALUE,
}
