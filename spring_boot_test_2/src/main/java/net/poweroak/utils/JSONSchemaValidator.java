package net.poweroak.utils;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;

public class JSONSchemaValidator {
	private static final Logger logger=LoggerFactory.getLogger(JSONSchemaValidator.class);
	
	private final static JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
	
	public static boolean validatorSchema(String mainSchema, JsonNode instanceNode)  {
        JsonNode mainNode;
        JsonSchema schema;
        ProcessingReport processingReport = null;
		try {
			mainNode = JsonLoader.fromString(mainSchema);
			schema = factory.getJsonSchema(mainNode);
			processingReport = schema.validate(instanceNode);
		} catch (IOException | ProcessingException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			logger.error("-----------json解析异常--------------------------------");
			return false;
		}
       
        
//        logger.debug(processingReport.toString());

        return processingReport.isSuccess();
    }
}
