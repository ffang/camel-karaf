/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.test.blueprint;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.junit.jupiter.api.Test;

import static org.apache.camel.test.junit5.TestSupport.assertIsInstanceOf;
import static org.junit.jupiter.api.Assertions.*;

public class DefaultErrorHandlerOnPrepareTestTest extends CamelBlueprintTestSupport {

    @Override
    protected String getBlueprintDescriptor() {
        return "org/apache/camel/test/blueprint/DefaultErrorHandlerOnPrepareTestTest.xml";
    }

    @Test
    public void testDefaultErrorHandlerOnPrepare() throws Exception {
        Exchange out = template.request("direct:start", new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                exchange.getIn().setBody("Hello World");
            }
        });
        assertNotNull(out);
        assertTrue(out.isFailed(), "Should be failed");
        assertIsInstanceOf(IllegalArgumentException.class, out.getException());
        assertEquals("Forced", out.getIn().getHeader("FailedBecause"));
    }

    public static class MyPrepareProcessor implements Processor {

        @Override
        public void process(Exchange exchange) throws Exception {
            Exception cause = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
            exchange.getIn().setHeader("FailedBecause", cause.getMessage());
        }
    }

}
