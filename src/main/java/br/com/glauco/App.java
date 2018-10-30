package br.com.glauco;

import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.Query;
import org.camunda.bpm.model.bpmn.instance.*;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.camunda.bpm.model.xml.type.ModelElementType;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;


public class App {


    public static void main(String[] args) {

        if(args.length < 2){
            System.out.println("Please provide 2 parameters to serve as entry and exit points");

        }else {

            String startPoint = args[0];
            String endPoint = args[1];
            String URL = "http://localhost:8080/engine-rest/process-definition/key/invoice/xml";

            //Acessing the API and parsing the data
            RestTemplate restTemplate = new RestTemplate();

            try {
                JsonObject jsonObject = restTemplate.getForObject(URL, JsonObject.class);
                InputStream inputStream = new ByteArrayInputStream(jsonObject.getBpmn20Xml().getBytes(StandardCharsets.UTF_8));
                BpmnModelInstance bpmnModelInstance = Bpmn.readModelFromStream(inputStream);


                StartEvent start = (StartEvent) bpmnModelInstance.getModelElementById("StartEvent_1");
                UserTask task = bpmnModelInstance.getModelElementById(startPoint);
                ModelElementType taskType = bpmnModelInstance.getModel().getType(Task.class);
                Collection<ModelElementInstance> taskInstances = bpmnModelInstance.getModelElementsByType(taskType);

                Query flowNodes = start.getSucceedingNodes();

                System.out.println("The path from " + startPoint + " to " + endPoint + " is:");
                System.out.println("[ " + "RESULTADO AQUI " + " ]");

                //getFlowingFlowNodes(startPoint);

            }catch (Exception e){
                System.out.println("The resource: "+ URL + " is unavailable");
            }
        }


    }

    public static Collection<FlowNode> getFlowingFlowNodes(FlowNode node) {
        Collection<FlowNode> followingFlowNodes = new ArrayList<FlowNode>();
        for (SequenceFlow sequenceFlow : node.getOutgoing()) {
            followingFlowNodes.add(sequenceFlow.getTarget());
        }
        return followingFlowNodes;
    }
}
