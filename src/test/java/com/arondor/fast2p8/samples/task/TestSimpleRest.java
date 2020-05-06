package com.arondor.fast2p8.samples.task;

import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.junit.Test;

public class TestSimpleRest
{
    private static final Logger LOG = Logger.getLogger(TestSimpleRest.class);

    @Test
    public void testRestCall()
    {

        Client rsClient = ClientBuilder.newBuilder().property("connection.timeout", 30).newClient();

        WebTarget target = rsClient.target("https://arender.io/ARender/openExternalDocument.jsp?url={url}")
                .resolveTemplate("url", "https://arender.io/docs/ARender-doc-demo.pdf");

        String response = target.request().get(String.class);

        LOG.info("Result : " + response);

        WebTarget otherTarget = rsClient.target("https://arender.io/ARender/getDocumentLayout.jsp?uuid={uuid}")
                .resolveTemplate("uuid", response);

        JsonObject object = otherTarget.request(MediaType.APPLICATION_JSON_TYPE).get(JsonObject.class);
    }

}
