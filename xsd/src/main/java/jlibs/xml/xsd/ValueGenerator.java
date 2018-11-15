/**
 * Copyright 2015 Santhosh Kumar Tekuri
 *
 * The JLibs authors license this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package jlibs.xml.xsd;

import java.util.List;

/**
 * An interchangeable value generator, according to the Strategy Pattern.
 * 
 * @author rho
 *
 */
public interface ValueGenerator {

  double value(double min, double max);

  float value(float min, float max);

  long value(long min, long max);

  int value(int min, int max);

  short value(short min, short max);

  byte value(byte min, byte max);

  boolean value(Boolean bool);
  
  boolean value(Class<Boolean> clazz);

  void process(List<?> list);

}
