package gimgut.postbasedsocial.api.subscription;

import gimgut.postbasedsocial.api.user.UserInfo;
import gimgut.postbasedsocial.api.user.UserInfoMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/api/v1/subscription")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;
    private final UserInfoMapper userInfoMapper;

    public SubscriptionController(SubscriptionService subscriptionService, UserInfoMapper userInfoMapper) {
        this.subscriptionService = subscriptionService;
        this.userInfoMapper = userInfoMapper;
    }

    @GetMapping("")
    public ResponseEntity getSubscriptions(Authentication authentication) {
        Long idSubscriber = Long.valueOf(authentication.getName());
        List<UserInfo> subscriptions = subscriptionService.getSubscriptionsUsersInfo(idSubscriber);
        return new ResponseEntity(userInfoMapper.toListDto(subscriptions), HttpStatus.OK);
    }

    @PostMapping("subscribe/{idPublisher}")
    public ResponseEntity subscribe(@PathVariable @NotNull Long idPublisher, Authentication authentication) {
        Long idSubscriber = Long.valueOf(authentication.getName());
        this.subscriptionService.subscribe(idSubscriber, idPublisher);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("unsubscribe/{idPublisher}")
    public ResponseEntity unsubscribe(@PathVariable @NotNull Long idPublisher, Authentication authentication) {
        Long idSubscriber = Long.valueOf(authentication.getName());
        this.subscriptionService.unsubscribe(idSubscriber, idPublisher);
        return new ResponseEntity(HttpStatus.OK);
    }
}