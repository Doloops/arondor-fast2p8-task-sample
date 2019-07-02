package com.arondor.fast2p8.samples.task;

import java.util.regex.Pattern;

import com.arondor.fast2p8.model.punnet.Document;
import com.arondor.fast2p8.model.punnet.Punnet;
import com.arondor.fast2p8.model.task.TaskException;
import com.arondor.fast2p8.task.common.BasicTask;

public class CustomerIdTask extends BasicTask
{

    public boolean runTask(Punnet punnet) throws TaskException
    {

        if (punnet.getDocumentList() == null)
        {
            throw new TaskException("No document found in the punnet");
        }

        for (Document document : punnet.getDocumentList())
        {
            String customerId = document.getDataSet().getDataValue("CUSTOMER_ID");
            if (customerId == null)
            {
                throw new TaskException("No customer Id property found");
            }
            if (!Pattern.compile("\\d{10}").matcher(customerId).matches())
            {
                throw new TaskException("Invalid customer id: " + customerId);
            }
            String newCustomerId = "C-" + customerId;
            document.getDataSet().addData("customerId", "String", newCustomerId);
        }

        return true;
    }

}
