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

import java.util.Dictionary;
import java.util.Map;

import org.apache.camel.util.KeyValueHolder;
import org.junit.jupiter.api.Test;
import org.osgi.framework.ServiceReference;

import static org.junit.jupiter.api.Assertions.*;

public class BlueprintAddServiceTest extends CamelBlueprintTestSupport {

    private MyService myService = new MyService();

    @Override
    protected String getBlueprintDescriptor() {
        return "org/apache/camel/test/blueprint/BlueprintAddServiceTest.xml";
    }

    @Override
    protected void addServicesOnStartup(Map<String, KeyValueHolder<Object, Dictionary>> services) {
        services.put("myService", asService(myService, "beer", "Carlsberg"));
    }

    @Test
    public void testAddService() throws Exception {
        getMockEndpoint("mock:result").expectedBodiesReceived("CamelCamel");

        template.sendBody("direct:start", "Camel");

        assertMockEndpointsSatisfied();

        ServiceReference<?> ref = getBundleContext().getServiceReference("myService");
        assertEquals("Carlsberg", ref.getProperty("beer"));
        Object service = getBundleContext().getService(ref);
        assertSame(myService, service);
    }

}
