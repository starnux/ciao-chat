//  Ciao!
//
//  Created by Mike on Sun Dec 01 2002.
//  Copyright (c) 2008 Na-Prod. All rights reserved.
//

#import <Cocoa/Cocoa.h>

@interface Controller : NSObject
{
    NSNetService *service;
    NSNetServiceBrowser *serviceBrowser;
    NSNetServiceBrowser *domainBrowser;
    NSMutableArray *discoveredServices;

    NSFileHandle *listeningSocket;
    
	IBOutlet id window;
    IBOutlet id discoveredServicesList;
    IBOutlet id nameField;
	IBOutlet NSButton *drawerButton;
	IBOutlet NSDrawer *drawer;
}
- (IBAction)toggleServiceActivation:(id)sender;
- (void)setupBrowser;
- (BOOL)setupService;
- (void)stopService;
- (BOOL)windowShouldClose:(id)window;
@end

@interface Controller (ChatFunctionality)
- (void)openNewChatWindowAsChatInitiator:(id)sender;
- (void)openNewChatWindowAsMessageReceiver:(NSNotification *)notification;
@end

@interface Controller (NSNetServiceDelegation)
// Publication Specific
- (void)netService:(NSNetService *)sender didNotPublish:(NSDictionary *)errorDict;
- (void)netServiceWillPublish:(NSNetService *)sender;
- (void)netServiceDidStop:(NSNetService *)sender;

// Resolution Specific
- (void)netService:(NSNetService *)sender didNotResolve:(NSDictionary *)errorDict;
- (void)netServiceDidResolveAddress:(NSNetService *)sender;
- (void)netServiceWillResolve:(NSNetService *)sender;
@end

@interface Controller (NSNetServiceBrowserDelegation)
- (void)netServiceBrowser:(NSNetServiceBrowser *)aNetServiceBrowser 
	    didFindService:(NSNetService *)aNetService moreComing:(BOOL)moreComing;
- (void)netServiceBrowser:(NSNetServiceBrowser *)aNetServiceBrowser 
	    didNotSearch:(NSDictionary *)errorDict;
- (void)netServiceBrowser:(NSNetServiceBrowser *)aNetServiceBrowser 
	    didRemoveService:(NSNetService *)aNetService moreComing:(BOOL)moreComing;
- (void)netServiceBrowser:(NSNetServiceBrowser *)aNetServiceBrowser 
	    didFindDomain:(NSString *)domainString 
	    moreComing:(BOOL)moreComing;
- (void)netServiceBrowser:(NSNetServiceBrowser *)aNetServiceBrowser 
	    didRemoveDomain:(NSString *)domainString 
	    moreComing:(BOOL)moreComing;
- (void)netServiceBrowserDidStopSearch:(NSNetServiceBrowser *)aNetServiceBrowser;
- (void)netServiceBrowserWillSearch:(NSNetServiceBrowser *)aNetServiceBrowser;
@end

@interface Controller (ContactListDataSource)
- (int)numberOfRowsInTableView:(NSTableView *)aTableView;
- (id)tableView:(NSTableView *)aTableView 
	    objectValueForTableColumn:(NSTableColumn *)aTableColumn row:(int)rowIndex;
@end