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

import java.util.Collections;
import java.util.List;

public class RandomValueGenerator implements ValueGenerator {

  public double value(double min, double max) {
    // see http://stackoverflow.com/a/9724775
    return Math.random() < 0.5 ? ((1 - Math.random()) * (max - min) + min) : (Math.random() * (max - min) + min);
  }

  public float value(float min, float max) {
    // see http://stackoverflow.com/a/9724775
    return Math.random() < 0.5 ? (float) ((1 - Math.random()) * (max - min) + min)
        : (float) (Math.random() * (max - min) + min);

  }

  public long value(long min, long max) {
    return Math.round(min + Math.random() * (max - min));
  }

  public int value(int min, int max) {
    return (int) Math.round(min + Math.random() * (max - min));
  }

  public short value(short min, short max) {
    return (short) Math.round(min + Math.random() * (max - min));
  }

  public byte value(byte min, byte max) {
    return (byte) Math.round(min + Math.random() * (max - min));
  }

  public boolean value(Class<Boolean> clazz) {
    return Math.random() < 0.5d;
  }

  public boolean value(Boolean bool) {
    if (Boolean.TRUE.equals(bool))
      return true;
    else if (Boolean.FALSE.equals(bool))
      return false;
    else // random either 0 or 1
      return value(Boolean.class);
  }

  @Override
  public void process(List<?> list) {
    Collections.shuffle(list);
  }


}
