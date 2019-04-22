package fr.lmarchau.proto.fees.repository;

import fr.lmarchau.proto.fees.config.RepositoryConfiguration;
import fr.lmarchau.proto.fees.config.RepositoryTestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringRunner.class)
@Import(RepositoryConfiguration.class)
@RestClientTest(IpStackRepositoryTest.class)
public class IpStackRepositoryTest {

    @Autowired
    private IpStackRepository ipStackRepository;
    @Autowired
    private MockRestServiceServer server;


    @Test
    public void findCountryCodeShouldReturnACountryCode() {
        server.expect(MockRestRequestMatchers.requestTo("http://api.ipstack.com/8.8.8.7?access_key=fea29c3a5cebb3ba38927a6bb3b88ac0"))
                .andRespond(withSuccess()
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .body(new ClassPathResource("dataset/ip-stack.json")));

        String result = ipStackRepository.findCountryCodeByIp("8.8.8.7");
        assertThat(result).isEqualTo("US");
    }

    @Test
    public void findCountryCodeWithApiErrorShouldReturnNull() {
        server.expect(MockRestRequestMatchers.requestTo("http://api.ipstack.com/8.8.8.7?access_key=fea29c3a5cebb3ba38927a6bb3b88ac0"))
                .andRespond(withServerError());

        String result = ipStackRepository.findCountryCodeByIp("8.8.8.7");
        assertThat(result).isNull();
    }

    @Test
    public void findCountryCodeForInvalidIpShouldReturnNull() {
        server.expect(MockRestRequestMatchers.requestTo("http://api.ipstack.com/8.8.8.?access_key=fea29c3a5cebb3ba38927a6bb3b88ac0"))
                .andRespond(withSuccess()
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .body(new ClassPathResource("dataset/ip-stack-empty.json")));

        String result = ipStackRepository.findCountryCodeByIp("8.8.8.");
        assertThat(result).isNull();
    }
}
