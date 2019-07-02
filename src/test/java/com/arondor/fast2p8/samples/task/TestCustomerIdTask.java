package com.arondor.fast2p8.samples.task;

import org.junit.Before;
import org.junit.Test;

import com.arondor.fast2p8.model.dummy.DummyManager;
import com.arondor.fast2p8.model.factory.PunnetFactory;
import com.arondor.fast2p8.model.manager.Manager;
import com.arondor.fast2p8.model.punnet.Document;
import com.arondor.fast2p8.model.punnet.Punnet;
import com.arondor.fast2p8.model.punnet.id.DocumentIdFactory;
import com.arondor.fast2p8.model.task.TaskException;
import com.arondor.fast2p8.xstream.factory.XStreamPunnetFactory;

import junit.framework.Assert;

public class TestCustomerIdTask
{
    private Manager manager;

    private PunnetFactory punnetFactory;

    private CustomerIdTask customerIdTask;

    @Before
    public void init()
    {
        DummyManager dummyManager = new DummyManager();
        punnetFactory = new XStreamPunnetFactory();
        dummyManager.setPunnetFactory(punnetFactory);
        this.manager = dummyManager;

        customerIdTask = new CustomerIdTask();
        customerIdTask.setManager(manager);
    }

    @Test
    public void testCustomer_shallValidate() throws TaskException
    {
        Punnet punnet = punnetFactory.createEmptyPunnet();
        Document document = punnet.addDocument(DocumentIdFactory.createDocumentId("DocumentId1"));
        document.getDataSet().addData("CUSTOMER_ID", "String", "0042785412");

        customerIdTask.runTask(punnet);

        Assert.assertEquals(1, punnet.getDocumentList().size());

        Assert.assertEquals("C-0042785412", punnet.getDocumentList().get(0).getDataSet().getDataValue("customerId"));
    }

    @Test(expected = TaskException.class)
    public void testCustomer_shallNotValidate() throws TaskException
    {
        Punnet punnet = punnetFactory.createEmptyPunnet();
        Document document = punnet.addDocument(DocumentIdFactory.createDocumentId("DocumentId1"));
        document.getDataSet().addData("CUSTOMER_ID", "String", "InvalidId");

        customerIdTask.runTask(punnet);
    }

    @Test(expected = TaskException.class)
    public void testCustomer_noCustomerId() throws TaskException
    {
        Punnet punnet = punnetFactory.createEmptyPunnet();
        Document document = punnet.addDocument(DocumentIdFactory.createDocumentId("DocumentId1"));
        document.getDataSet().addData("CUSTOMER_NAME", "String", "michel");

        customerIdTask.runTask(punnet);
    }

    @Test(expected = TaskException.class)
    public void testCustomer_noDocument() throws TaskException
    {
        Punnet punnet = punnetFactory.createEmptyPunnet();

        customerIdTask.runTask(punnet);
    }
}
