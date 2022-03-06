package gimgut.postbasedsocial.security.oauth2;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryRequestRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {
    private final Logger log = LogManager.getLogger(this.getClass());
    // TODO: Think about a method to remove outdated requests. Or replace InMemoryRepository completely.
    private final Map<String, OAuth2AuthorizationRequest> requests = new ConcurrentHashMap<>();

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        String state = request.getParameter("state");
        if (state != null) {
            return removeAuthorizationRequest(request);
        }
        return null;
    }

    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {
        String state = authorizationRequest.getState();
        requests.put(state, authorizationRequest);
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request) {
        String state = request.getParameter("state");
        if (state != null) {
            return requests.remove(state);
        }
        return null;
    }
}
