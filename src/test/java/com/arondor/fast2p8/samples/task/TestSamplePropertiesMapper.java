package com.arondor.fast2p8.samples.task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.arondor.common.io.scan.DirectoryScanner;
import com.arondor.fast2p8.model.dummy.DummyManager;
import com.arondor.fast2p8.model.factory.PunnetFactory;
import com.arondor.fast2p8.model.manager.Manager;
import com.arondor.fast2p8.model.punnet.ContentContainer;
import com.arondor.fast2p8.model.punnet.Document;
import com.arondor.fast2p8.model.punnet.Punnet;
import com.arondor.fast2p8.model.punnet.id.DocumentIdFactory;
import com.arondor.fast2p8.model.task.TaskException;
import com.arondor.fast2p8.punnetlist.csv.CSVPunnetList;
import com.arondor.fast2p8.xstream.factory.XStreamPunnetFactory;

public class TestSamplePropertiesMapper
{
    private Manager manager;

    private PunnetFactory punnetFactory;

    private SamplePropertiesMapper samplePropertiesMapperTask;

    @Before
    public void init()
    {
        DummyManager dummyManager = new DummyManager();
        punnetFactory = new XStreamPunnetFactory();
        dummyManager.setPunnetFactory(punnetFactory);
        this.manager = dummyManager;

        samplePropertiesMapperTask = new SamplePropertiesMapper();
        samplePropertiesMapperTask.setManager(manager);
    }

    @Test
    public void test_mapProperties_createFolderPath() throws TaskException
    {
        Punnet punnet = punnetFactory.createEmptyPunnet();
        Document document = punnet.addDocument(DocumentIdFactory.createDocumentId("DocumentId1"));
        document.getDataSet().addData("Sponsor", "String", "Curie");
        document.getDataSet().addData("Study", "String", "Study123");
        document.getDataSet().addData("Study_Country", "String", "IN");
        document.getDataSet().addData("Study_Site", "String", "Site01");

        samplePropertiesMapperTask.runTask(punnet);

        Assert.assertEquals(1, punnet.getDocumentList().size());

        Assert.assertEquals("Curie",
                punnet.getDocumentList().get(0).getDataSet().getDataValue("someaspect:SponsorName"));
        Assert.assertEquals("Site01",
                punnet.getDocumentList().get(0).getDataSet().getDataValue("someaspect:StudySite"));
        Assert.assertEquals(1,
                punnet.getDocumentList().get(0).getFolderReferenceSet().getFinalFolderReferences().size());
        Assert.assertEquals("/Curie/Study123/IN/Site01",
                punnet.getDocumentList().get(0).getFolderReferenceSet().getFinalFolderReferences().get(0).getPath());
    }

    @Test
    public void test_fromCsv_mapProperties_createFolderPath() throws TaskException, IOException
    {
        CSVPunnetList csvPunnetList = new CSVPunnetList();
        csvPunnetList.setManager(manager);
        DirectoryScanner directoryScanner = new DirectoryScanner();
        List<String> filters = new ArrayList<String>();
        filters.add("src/test/resources/sample1.csv");
        directoryScanner.setFilters(filters);
        csvPunnetList.setFileScanner(directoryScanner);

        List<Punnet> result = new ArrayList<Punnet>();
        for (Punnet p : csvPunnetList)
        {
            result.add(p);
        }

        Assert.assertEquals(1, result.size());
        Punnet punnet = result.get(0);

        samplePropertiesMapperTask.runTask(punnet);

        Assert.assertEquals(1, punnet.getDocumentList().size());
        ContentContainer content = punnet.getDocumentList().get(0).getContentSet().getContent().get(0);
        String url = manager.getPunnetContentFactory().getContentAsUrl(content);
        Assert.assertEquals("DocumentId1", url);
        Assert.assertEquals("Curie",
                punnet.getDocumentList().get(0).getDataSet().getDataValue("someaspect:SponsorName"));
        Assert.assertEquals("Site01",
                punnet.getDocumentList().get(0).getDataSet().getDataValue("someaspect:StudySite"));
        Assert.assertEquals(1,
                punnet.getDocumentList().get(0).getFolderReferenceSet().getFinalFolderReferences().size());
        Assert.assertEquals("/Curie/Study123/IN/Site01",
                punnet.getDocumentList().get(0).getFolderReferenceSet().getFinalFolderReferences().get(0).getPath());
    }
}
