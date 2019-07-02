package com.arondor.fast2p8.samples.task;

import com.arondor.common.management.mbean.annotation.Description;
import com.arondor.fast2p8.model.punnet.Document;
import com.arondor.fast2p8.model.punnet.Punnet;
import com.arondor.fast2p8.model.task.TaskException;
import com.arondor.fast2p8.task.common.BasicTask;

public class SimpleDataRenamer extends BasicTask
{
    @Description("Key of data to rename from")
    private String srcData;

    @Description("Key of data to rename to")
    private String targetData;

    public boolean runTask(Punnet punnet) throws TaskException
    {
        if (punnet.getDocumentList() == null)
        {
            throw new TaskException("No document found in the punnet");
        }

        for (Document document : punnet.getDocumentList())
        {
            String value = document.getDataSet().getDataValue(srcData);

            if (value != null && document.getDataSet().removeData(srcData))
            {
                document.getDataSet().addData(targetData, "String", value);
            }
        }

        return false;
    }

    public String getSrcData()
    {
        return srcData;
    }

    public void setSrcData(String srcData)
    {
        this.srcData = srcData;
    }

    public String getTargetData()
    {
        return targetData;
    }

    public void setTargetData(String targetdata)
    {
        this.targetData = targetdata;
    }

}
