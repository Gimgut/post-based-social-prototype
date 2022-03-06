import { AuthenticationService } from "./shared/services/auth/authentication.service";

export function appInitializer(authenticationService: AuthenticationService) {
    console.log('startup authentication start');
    const storageRefreshToken = localStorage.getItem(authenticationService.getRtStorageKey());
    console.log('storageRefreshToken = ' + storageRefreshToken);
    if (!storageRefreshToken) {
      return () => new Promise(resolve => {resolve.call(null,null)});
    }
    
    return () => new Promise(resolve => {
        authenticationService.callRefreshToken(storageRefreshToken)
          .subscribe()
          .add(resolve);
    });
}