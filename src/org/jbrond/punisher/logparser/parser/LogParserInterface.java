/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jbrond.punisher.logparser.parser;

import org.jbrond.punisher.logparser.LogObject;

public interface LogParserInterface {
  public LogObject match(String line);
}
