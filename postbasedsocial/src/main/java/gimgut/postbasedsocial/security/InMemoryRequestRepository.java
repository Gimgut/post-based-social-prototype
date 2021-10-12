package gimgut.postbasedsocial.security;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class InMemoryRequestRepository implements AuthorizationRequestRepository< OAuth2AuthorizationRequest > {
    private final Log logger = LogFactory.getLog(this.getClass());
    private final Map< String, OAuth2AuthorizationRequest > cache = new HashMap<>();

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request ) {
        logger.info("loadAuthorizationRequest");
        String state = request.getParameter( "state" );
        if ( state != null ) {
            return removeAuthorizationRequest( request );
        }
        return null;
    }

    @Override
    public void saveAuthorizationRequest( OAuth2AuthorizationRequest authorizationRequest,
                                          HttpServletRequest request, HttpServletResponse response ) {
        String state = authorizationRequest.getState();
        logger.info("saveAuthorizationRequest. state: " + state +"\nclient id: " + authorizationRequest.getScopes());
        cache.put( state, authorizationRequest );
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest( HttpServletRequest request ) {
        logger.info("removeAuthorizationRequest");
        String state = request.getParameter( "state" );
        if ( state != null ) {
            return cache.remove( state );
        }

        return null;
    }
}
