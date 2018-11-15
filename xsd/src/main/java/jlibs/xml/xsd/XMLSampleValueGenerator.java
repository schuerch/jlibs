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

import jlibs.core.graph.Path;
import jlibs.core.util.RandomUtil;
import jlibs.xml.sax.SAXUtil;
import org.apache.xerces.xs.*;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.namespace.QName;
import javax.xml.parsers.ParserConfigurationException;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generates sample values from given XML
 *
 * @author Santhosh Kumar T
 */
public class XMLSampleValueGenerator implements XSInstance.SampleValueGenerator{
    private Map<XSElementDeclaration, List<String>> elementValues = new HashMap<XSElementDeclaration, List<String>>();
    private Map<XSAttributeDeclaration, List<String>> attributeValues = new HashMap<XSAttributeDeclaration, List<String>>();

    public XMLSampleValueGenerator(final XSModel schema, InputSource sampleInput) throws SAXException, ParserConfigurationException, IOException{
        DefaultHandler handler = new DefaultHandler(){
            private List<QName> xpath = new ArrayList<QName>();
            private CharArrayWriter contents = new CharArrayWriter();

            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException{
                xpath.add(new QName(uri, localName));
                for(int i=0; i<attributes.getLength(); i++){
                    QName qname = new QName(attributes.getURI(i), attributes.getLocalName(i));
                    xpath.add(qname);
                    XSAttributeDeclaration attr = XSUtil.findAttributeDeclaration(schema, xpath);
                    if(attr!=null){
                        List<String> values = attributeValues.get(attr);
                        if(values==null)
                            attributeValues.put(attr, values=new ArrayList<String>());
                        values.add(attributes.getValue(i));
                    }
                    xpath.remove(xpath.size()-1);
                }
                contents.reset();
            }


            @Override
            public void characters(char[] ch, int start, int length) throws SAXException{
                contents.write(ch, start, length);
            }

            @Override
            public void endElement(String uri, String localName, String qName) throws SAXException{
                if(contents.size()>0){
                    XSElementDeclaration elem = XSUtil.findElementDeclaration(schema, xpath);
                    if(elem!=null){
                        boolean simpleType = false;
                        if(elem.getTypeDefinition().getTypeCategory()==XSTypeDefinition.SIMPLE_TYPE)
                            simpleType = true;
                        else{
                            XSComplexTypeDefinition complexType = (XSComplexTypeDefinition)elem.getTypeDefinition();
                            if(complexType.getContentType()==XSComplexTypeDefinition.CONTENTTYPE_SIMPLE)
                                simpleType = true;
                        }
                        if(simpleType){
                            List<String> values = elementValues.get(elem);
                            if(values==null)
                                elementValues.put(elem, values=new ArrayList<String>());
                            values.add(contents.toString());
                        }
                    }
                }
                xpath.remove(xpath.size()-1);
                contents.reset();
            }
        };

        SAXUtil.newSAXParser(true, false, false).parse(sampleInput, handler);
    }

    @Override
    public String generateSampleValue(XSElementDeclaration element, XSSimpleTypeDefinition simpleType, Path path){
        List<String> values = elementValues.get(element);
        return values==null ? null : values.get(RandomUtil.random(0, values.size()-1));
    }

    @Override
    public String generateSampleValue(XSAttributeDeclaration attribute, XSSimpleTypeDefinition simpleType, Path path){
        List<String> values = attributeValues.get(attribute);
        return values==null ? null : values.get(RandomUtil.random(0, values.size()-1));
    }
}
