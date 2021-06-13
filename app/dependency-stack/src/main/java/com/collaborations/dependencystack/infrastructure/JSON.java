package com.collaborations.dependencystack.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JSON {
    public static ObjectNode create() {
       return new ObjectMapper().createObjectNode();
    }
}
