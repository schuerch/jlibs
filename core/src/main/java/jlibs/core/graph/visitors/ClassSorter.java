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

package jlibs.core.graph.visitors;

import jlibs.core.graph.Filter;
import jlibs.core.graph.Navigator;
import jlibs.core.graph.Sequence;
import jlibs.core.graph.WalkerUtil;
import jlibs.core.graph.sequences.FilteredSequence;
import jlibs.core.graph.sequences.IterableSequence;

import java.util.Collection;
import java.util.List;

/**
 * @author Santhosh Kumar T
 */
public class ClassSorter{
    public static List<Class<?>> sort(final Sequence<Class<?>> classes){
        return WalkerUtil.topologicalSort(classes, new Navigator<Class<?>>(){
            @Override
            public Sequence<Class<?>> children(final Class<?> parent){
                return new FilteredSequence<Class<?>>(classes.copy(), new Filter<Class<?>>(){
                    @Override
                    public boolean select(Class<?> child){
                        return child!=parent && child.isAssignableFrom(parent);
                    }
                });
            }
        });
    }

    public static List<Class<?>> sort(Collection<Class<?>> classes){
        return sort(new IterableSequence<Class<?>>(classes));
    }
}
