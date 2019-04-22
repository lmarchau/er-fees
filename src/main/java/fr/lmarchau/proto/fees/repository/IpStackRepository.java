package fr.lmarchau.proto.fees.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
public class IpStackRepository {

    private static final String PARAMS = "/{ip_address}?access_key={access_key}";

    private RestTemplate restTemplate;
    private String apiUri;
    private String apiKey;

    public IpStackRepository(RestTemplateBuilder restTemplateBuilder, String apiUri, String apiKey) {
        this.restTemplate = restTemplateBuilder.build();
        this.apiUri = apiUri + PARAMS;
        this.apiKey = apiKey;
    }

    public String findCountryCodeByIp(String ip) {
        log.info("GeoLoc ip {}", ip);
        Map<String, String> params = new HashMap<>();
        params.put("ip_address", ip);
        params.put("access_key", apiKey);
        ResponseEntity<LinkedHashMap> response = null;
        try {
            response = restTemplate.getForEntity(apiUri, LinkedHashMap.class, params);
        } catch(RestClientException e) {
            // FIXME error process
            log.error("Ip Stack API error", e);
            return null;
        }
        if (response.getStatusCode().is2xxSuccessful()) {
            return (String) response.getBody().get("country_code");
        }
        return null;
    }

}
