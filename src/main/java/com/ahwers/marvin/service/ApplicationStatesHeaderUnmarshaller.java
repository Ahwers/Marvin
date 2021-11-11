package com.ahwers.marvin.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.ahwers.marvin.framework.application.Application;
import com.ahwers.marvin.framework.application.ApplicationState;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class ApplicationStatesHeaderUnmarshaller {

    private Map<String, Application> apps = new HashMap<>();

    public ApplicationStatesHeaderUnmarshaller(Set<Application> appsSet) {
        for (Application app : appsSet) {
            String appName = app.getName();
            this.apps.put(appName, app);
        }
    }

    public Map<String, ApplicationState> unmarshall(String marshalledAppStates) throws JsonMappingException, JsonProcessingException {
        ObjectMapper jsonMapper = new ObjectMapper();
        SimpleModule jsonModule = new SimpleModule();
        jsonModule.addDeserializer(ApplicationState.class, new ApplicationStateDeserializer());
        jsonMapper.registerModule(jsonModule);

        Map<String, ApplicationState> result = jsonMapper.readValue(marshalledAppStates, new TypeReference<Map<String, ApplicationState>>(){});

        return result;
    }

    private class ApplicationStateDeserializer extends StdDeserializer<ApplicationState> {

        protected ApplicationStateDeserializer() {
            this(null);
        }

        protected ApplicationStateDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public ApplicationState deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            JsonNode node = parser.getCodec().readTree(parser);
            ObjectMapper mapper = (ObjectMapper) parser.getCodec();
            
            String appName = node.findValue("applicationName").asText();
            Application app = apps.get(appName);
            Class<? extends ApplicationState> appStateClass = app.getStateClass();

            ApplicationState appState = null;
            if (appStateClass != null) {
                appState = mapper.treeToValue(node, appStateClass);
            }
           
            return appState;
        }

    }

}
