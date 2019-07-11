package com.arondor.fast2p8.samples.task;

import com.arondor.fast2p8.model.punnet.Document;
import com.arondor.fast2p8.model.punnet.Folder;
import com.arondor.fast2p8.model.punnet.Punnet;
import com.arondor.fast2p8.model.task.TaskException;
import com.arondor.fast2p8.task.common.BasicTask;

/**
 * Simple java sample to manipulate document data and folder association.
 *
 */
public class SamplePropertiesMapper extends BasicTask
{

    public boolean runTask(Punnet punnet) throws TaskException
    {
        for (Document doc : punnet.getDocumentList())
        {
            /*
             * Get existing values from the document
             */
            String sponsor = doc.getDataSet().getDataValue("Sponsor");
            String study = doc.getDataSet().getDataValue("Study");
            String studyCountry = doc.getDataSet().getDataValue("Study_Country");
            String studySite = doc.getDataSet().getDataValue("Study_Site");

            /*
             * Create new properties from provided values
             */
            doc.getDataSet().addData("someaspect:SponsorName", "String", sponsor);
            doc.getDataSet().addData("someaspect:StudySite", "String", studySite);

            /*
             * Create the folder path to file the document to
             */
            Folder newFolder = doc.getFolderReferenceSet().addFolderReference();
            newFolder.setPath("/" + sponsor + "/" + study + "/" + studyCountry + "/" + studySite);
        }
        return true;
    }

}
