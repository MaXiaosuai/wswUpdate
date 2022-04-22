#import "wswUpdate.h"


@implementation wswUpdate

- (void)wswUpdate:(CDVInvokedUrlCommand *)command
{
    NSDictionary *params = [command.arguments objectAtIndex:0];
       if (![params objectForKey:@"url"]) {
           [self failWithCallbackID:command.callbackId withMessage:@"参数格式错误"];
           return ;
       }
    [[UIApplication sharedApplication] openURL:[NSURL URLWithString:params[@"url"]] options:@{} completionHandler:nil];
}


- (void)successWithCallbackID:(NSString *)callbackID withMessage:(NSString *)message
{
    CDVPluginResult *commandResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:message];
    [self.commandDelegate sendPluginResult:commandResult callbackId:callbackID];
}



- (void)failWithCallbackID:(NSString *)callbackID withMessage:(NSString *)message
{
    CDVPluginResult *commandResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:message];
    [self.commandDelegate sendPluginResult:commandResult callbackId:callbackID];
}


@end
