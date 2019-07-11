package com.arondor.fast2p8.samples.task;

import org.junit.Assert;
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

public class TestSimpleDataRenamer
{

    private Manager manager;

    private PunnetFactory punnetFactory;

    private SimpleDataRenamer simpleDataRenamer;

    @Before
    public void init()
    {
        DummyManager dummyManager = new DummyManager();
        punnetFactory = new XStreamPunnetFactory();
        dummyManager.setPunnetFactory(punnetFactory);
        this.manager = dummyManager;

        simpleDataRenamer = new SimpleDataRenamer();
    }

    @Test
    public void testSimpleDataRenamer_shallValidate() throws TaskException
    {
        Punnet punnet = punnetFactory.createEmptyPunnet();
        Document document = punnet.addDocument(DocumentIdFactory.createDocumentId("DocumentId1"));
        document.getDataSet().addData("CUSTOMER_NAME", "String", "Alfred");
        Assert.assertEquals("Alfred", punnet.getDocumentList().get(0).getDataSet().getDataValue("CUSTOMER_NAME"));

        simpleDataRenamer.setSrcData("CUSTOMER_NAME");
        simpleDataRenamer.setTargetData("customerName");
        simpleDataRenamer.runTask(punnet);

        Assert.assertEquals("Alfred", punnet.getDocumentList().get(0).getDataSet().getDataValue("customerName"));
        Assert.assertNull(punnet.getDocumentList().get(0).getDataSet().getDataValue("CUSTOMER_NAME"));
    }

    @Test(expected = TaskException.class)
    public void testSimpleDataRenamer_noDocument() throws TaskException
    {
        Punnet punnet = punnetFactory.createEmptyPunnet();

        simpleDataRenamer.runTask(punnet);
    }

}
