package com.kerbino.bcpredict.services.DataManipulation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@NoArgsConstructor
public class JsonManipulation {
    public static List<JsonNode> filterCoinJsonByTimestamp(List<JsonNode> items, long start, long end) throws IOException {
        /*
        ObjectMapper mapper = new ObjectMapper();

        ArrayNode json = mapper.createArrayNode();

        for (JsonNode node : item) {
            json.add(node);
        }

        try {
            JsonNode root = mapper.readTree(json.binaryValue());
            ObjectNode filteredData = mapper.createObjectNode();

            String[] keys = {"prices", "market_caps", "total_volumes"};

            for (String key : keys) {
                ArrayNode originalArray = (ArrayNode) root.get(key);
                ArrayNode filteredArray = mapper.createArrayNode();

                for (JsonNode element : originalArray) {
                    long timestamp = element.get(0).asLong();
                    if (timestamp >= start && timestamp <= end){
                        filteredArray.add(element);
                    }
                }

                filteredData.set(key, filteredArray);
            }
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(filteredData);
        } catch (IOException e){
            e.printStackTrace();
        }
        return "";

         */

        ObjectMapper mapper = new ObjectMapper();
        List<JsonNode> filteredList = new ArrayList<>();

        String[] keys = {"prices", "market_caps", "total_volumes"};

        for (JsonNode node : items) {
            ObjectNode filteredItem = mapper.createObjectNode();
            for (String key : keys) {
                JsonNode arrayNode = node.get(key);
                ArrayNode filteredArray = mapper.createArrayNode();

                if (arrayNode != null && arrayNode.isArray()) {
                    for (JsonNode element : arrayNode) {
                        long timestamp = element.get(0).asLong();
                        if (timestamp >= start && timestamp <= end) {
                            filteredArray.add(element);
                        }
                    }
                }

                filteredItem.set(key, filteredArray);
            }

            filteredList.add(filteredItem);
        }

        return filteredList;
    }
}
