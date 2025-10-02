package edu.univ.scientometrics.service;

import edu.univ.scientometrics.model.ApiResponseRecord;
import java.util.Map;

public interface ApiClient {
    ApiResponseRecord get(Map<String, String> parameters);
}