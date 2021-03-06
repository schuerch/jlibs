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

package jlibs.wamp4j.msg;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jlibs.wamp4j.error.InvalidMessageException;

import static jlibs.wamp4j.Util.nonNull;

/**
 * When a Caller wishes to call a remote procedure, it sends a CALL message to a Dealer
 *
 * @author Santhosh Kumar Tekuri
 */
public class CallMessage extends RequestMessage{
    public static final int ID = 48;

    /**
     * a random, ephemeral ID chosen by the Caller and used to correlate the Dealer's response with the request
     */
    public final long requestID;

    /**
     * a dictionary that allows to provide additional call request details in an extensible way
     */
    public final ObjectNode options;

    /**
     * the URI of the procedure to be called
     */
    public final String procedure;

    /**
     * a list of positional call arguments (each of arbitrary type). The list may be of zero length
     */
    public final ArrayNode arguments;

    /**
     * a dictionary of keyword call arguments (each of arbitrary type). The dictionary may be empty
     */
    public final ObjectNode argumentsKw;


    public CallMessage(long requestID, ObjectNode options, String procedure, ArrayNode arguments, ObjectNode argumentsKw){
        this.requestID = requestID;
        this.options = options;
        this.procedure = nonNull(procedure, "null procedure");
        this.arguments = arguments;
        this.argumentsKw = argumentsKw;
    }

    public CallMessage(long requestID, ObjectNode options, String procedure, ArrayNode arguments){
        this(requestID, options, procedure, arguments, null);
    }

    public CallMessage(long requestID, ObjectNode options, String procedure){
        this(requestID, options, procedure, null, null);
    }


    @Override
    public int getID(){
        return ID;
    }

    @Override
    public long getRequestID(){
        return requestID;
    }

    @Override
    public void toArrayNode(ArrayNode array){
        array.add(idNodes[ID]);
        array.add(requestID);
        array.add(objectNode(options));
        array.add(procedure);
        if(arguments!=null)
            array.add(arguments);
        if(argumentsKw!=null)
            array.add(argumentsKw);
    }

    static final Decoder decoder = new Decoder(){
        @Override
        public WAMPMessage decode(ArrayNode array) throws InvalidMessageException{
            if(array.size()<4 || array.size()>6)
                throw new InvalidMessageException();

            assert id(array)==ID;
            long requestID = longValue(array, 1);
            ObjectNode options = objectValue(array, 2);
            String topic = textValue(array, 3);
            ArrayNode arguments = array.size()>=5 ? arrayValue(array, 4) : null;
            ObjectNode argumentsKw = array.size()==6 ? objectValue(array, 5) : null;

            return new CallMessage(requestID, options, topic, arguments, argumentsKw);
        }
    };
}
