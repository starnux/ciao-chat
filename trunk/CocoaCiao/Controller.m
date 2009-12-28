//  Ciao!
//
//  Created by Mike on Sun Dec 01 2002.
//  Copyright (c) 2008 Na-Prod. All rights reserved.
//

#import "Controller.h"
#import "ChatWindowController.h"
#import <sys/socket.h>
#import <netinet/in.h>

@implementation Controller

- (void)awakeFromNib
{
    // Give nameField an initial value
    [nameField setStringValue:NSFullUserName()];
    
    // Set the double-click action of discoveredServicesList
    [discoveredServicesList setTarget:self];
    [discoveredServicesList setDoubleAction:@selector(openNewChatWindowAsChatInitiator:)];
	
	// Set window delegate
	[window setDelegate:self];
}

- (IBAction)toggleServiceActivation:(id)sender
{
    switch ( [sender state] ) {
	case NSOnState:
	    if([self setupService] == NO)
		{
			[sender setSelected:NO];
			return;
		}
	    [self setupBrowser];
            
		[drawer open];
		[drawerButton setEnabled:YES];
	    [nameField setEnabled:NO];
	    break;
	
	case NSOffState:
	    [self stopService];

	    [serviceBrowser stop];
	    [domainBrowser stop];
		
		[discoveredServices removeAllObjects];
	    
		[drawer close];
		[drawerButton setEnabled:NO];
	    [nameField setEnabled:YES];
	    break;
    }
}

- (BOOL)setupService
{
    struct sockaddr_in addr;
    int sockfd;
	int err;
    
    // Create a socket
    sockfd = socket( AF_INET, SOCK_STREAM, 0 );

    // Setup its address structure
    bzero( &addr, sizeof(struct sockaddr_in));
    addr.sin_family = AF_INET;
    addr.sin_addr.s_addr = htonl( INADDR_ANY );	// Bind to any of the system addresses
    addr.sin_port = htons( 0 );			// Let the system choose a port for us

    // Bind it to an address and port
    err = bind( sockfd, (struct sockaddr *)&addr, sizeof(struct sockaddr));

	if(err == -1)
	{
		NSAlert *alert = [[NSAlert alloc] init];
		[alert addButtonWithTitle:@"OK"];
		[alert setMessageText:@"Failed to bind to port"];
		[alert setInformativeText:@"Failed to bind to port"];
		[alert setAlertStyle:NSWarningAlertStyle];
		//[alert runModal];
		[alert beginSheetModalForWindow:window modalDelegate:self didEndSelector:nil contextInfo:nil];
		return NO;
	}

    // Set it listening for connections
    listen( sockfd, 5 );
    
    // Find out the port number so we can pass it to the net service initializer
    int namelen = sizeof(struct sockaddr_in);
    getsockname( sockfd, (struct sockaddr *)&addr, &namelen );

    // Create NSFileHandle to communicate with socket
    listeningSocket = [[NSFileHandle alloc] initWithFileDescriptor:sockfd closeOnDealloc:YES];

    // Register for NSFileHandle socket-related Notifications
    NSNotificationCenter *nc = [NSNotificationCenter defaultCenter];
    [nc addObserver:self 
	selector:@selector(openNewChatWindowAsMessageReceiver:) 
	name:NSFileHandleConnectionAcceptedNotification 
	object:nil];

    // Accept connections in background and notify
    [listeningSocket acceptConnectionInBackgroundAndNotify];

    // Configure and publish the Rendezvous service
    service = [[NSNetService alloc] initWithDomain:@""
				    type:@"_ciao._tcp."
				    name:[nameField stringValue]
				    port:ntohs(addr.sin_port)];
    [service setDelegate:self];
    [service publish];
	
	return YES;
}

- (void)setupBrowser
{
    if ( !serviceBrowser ) {
	serviceBrowser = [[NSNetServiceBrowser alloc] init];
	[serviceBrowser setDelegate:self];
    }
    
    if ( !domainBrowser ) {
	domainBrowser = [[NSNetServiceBrowser alloc] init];
	[domainBrowser setDelegate:self];
    }
    
    if ( !discoveredServices )
	discoveredServices = [[NSMutableArray alloc] init];
	
    [domainBrowser searchForAllDomains];
    [serviceBrowser searchForServicesOfType:@"_ciao._tcp." inDomain:@""];
}

- (void)stopService
{
    [service stop];
    [service release];
    [[NSNotificationCenter defaultCenter] removeObserver:self];

    [listeningSocket closeFile];
    [listeningSocket release];
}

- (BOOL)windowShouldClose:(id)window
{
	NSLog(@"Attempt to close window");
	return NO; 
}
@end

@implementation Controller (ChatFunctionality)
/*
 * This method could also be called openNewChatWindowAsClient
 */
- (void)openNewChatWindowAsChatInitiator:(id)sender
{
    // Obtain remote service based on selected name in list
    NSNetService *remoteService = [discoveredServices objectAtIndex:[sender selectedRow]];

    // Get the socket address structure for the remote service
    NSData *address = [[remoteService addresses] objectAtIndex:0];

    // Create a socket that will be used to connect to the other chat client.
    int s = socket( AF_INET, SOCK_STREAM, 0 );
    int err = connect( s, [address bytes], [address length] );

	if(err == -1)
	{
		NSAlert *alert = [[NSAlert alloc] init];
		[alert addButtonWithTitle:@"OK"];
		[alert setMessageText:@"Failed to connect to host"];
		[alert setInformativeText:@"Failed to connect to host"];
		[alert setAlertStyle:NSWarningAlertStyle];
		//[alert runModal];
		[alert beginSheetModalForWindow:window modalDelegate:self didEndSelector:nil contextInfo:nil];
		[discoveredServices removeObjectAtIndex:[sender selectedRow]];
		return;
	}

    // Create a file handle for the socket used to connect to the other chat client.
    NSFileHandle *remoteFH;
    remoteFH = [[[NSFileHandle alloc] initWithFileDescriptor:s closeOnDealloc:YES] autorelease];
    [remoteFH autorelease];
    
    // Open a window with a connection to the remote client.
    ChatWindowController *chatWC;
    chatWC = [[ChatWindowController alloc] initWithConnection:remoteFH
                                           myName:[nameField stringValue]];
    [chatWC showWindow:nil];
}

/*
 * This method could also be called openNewChatWindowAsServer
 */
- (void)openNewChatWindowAsMessageReceiver:(NSNotification *)notification
{
    NSFileHandle *remoteFH = [[notification userInfo] 
					objectForKey:NSFileHandleNotificationFileHandleItem];
					
	// Accept connections in background and notify
    [listeningSocket acceptConnectionInBackgroundAndNotify];
    
    ChatWindowController *chatWC;
    chatWC = [[ChatWindowController alloc] initWithConnection:remoteFH
                                            myName:[nameField stringValue]];
}

@end

@implementation Controller (NSNetServiceDelegation)
// Publication Specific
- (void)netService:(NSNetService *)sender didNotPublish:(NSDictionary *)errorDict
{
    NSLog( @"Could not publish the service %@. Error dictionary follows...", [sender name] );
    NSLog( [errorDict description] );
}

- (void)netServiceWillPublish:(NSNetService *)sender
{
    NSLog( @"Publishing service %@", [sender name] );
}

- (void)netServiceDidStop:(NSNetService *)sender
{
    NSLog( @"Stopping service %@", [sender name] );
}

// Resolution Specific
- (void)netService:(NSNetService *)sender didNotResolve:(NSDictionary *)errorDict
{
    NSLog( @"There was an error while attempting to resolve address for %@",
			[sender name] );
}

- (void)netServiceDidResolveAddress:(NSNetService *)sender
{
    NSLog( @"Successfully resolved address for %@.", [sender name] );
}

- (void)netServiceWillResolve:(NSNetService *)sender
{
    NSLog( @"Attempting to resolve address for %@...", [sender name] );
}

@end

@implementation Controller (NSNetServiceBrowserDelegation)
- (void)netServiceBrowser:(NSNetServiceBrowser *)aNetServiceBrowser 
	    didFindService:(NSNetService *)aNetService 
	    moreComing:(BOOL)moreComing
{
    NSLog( @"Found service named %@.", [aNetService name] );

    [discoveredServices addObject:aNetService];
    
    [aNetService setDelegate:self];
    [aNetService resolve];
    
    if ( !moreComing )
	[discoveredServicesList reloadData];
}

- (void)netServiceBrowser:(NSNetServiceBrowser *)aNetServiceBrowser 
	    didRemoveService:(NSNetService *)aNetService 
	    moreComing:(BOOL)moreComing
{
	NSLog( @"Removed service named %@.", [aNetService name] );

    [discoveredServices removeObject:aNetService];
    
    if ( !moreComing )
	[discoveredServicesList reloadData];
}

- (void)netServiceBrowserDidStopSearch:(NSNetServiceBrowser *)aNetServiceBrowser
{
    if ( aNetServiceBrowser == serviceBrowser ) {
	[discoveredServices removeAllObjects];
	[discoveredServicesList reloadData];
    }    
}

- (void)netServiceBrowser:(NSNetServiceBrowser *)aNetServiceBrowser 
	    didNotSearch:(NSDictionary *)errorDict
{
    NSLog(@"There was an error in searching. Error Dictionary follows...");
    NSLog( [errorDict description] );
}

- (void)netServiceBrowserWillSearch:(NSNetServiceBrowser *)aNetServiceBrowser
{
    if ( aNetServiceBrowser == domainBrowser ) 
	NSLog(@"We're about to start searching for domains..." );
    else
	NSLog(@"We're about to start searching for services..." );
}

- (void)netServiceBrowser:(NSNetServiceBrowser *)aNetServiceBrowser 
	    didFindDomain:(NSString *)domainString 
	    moreComing:(BOOL)moreComing
{
    NSLog( @"Found domain %@.", domainString );
}

- (void)netServiceBrowser:(NSNetServiceBrowser *)aNetServiceBrowser 
	    didRemoveDomain:(NSString *)domainString 
	    moreComing:(BOOL)moreComing
{
    NSLog( @"Removed domain %@.", domainString );
}

@end

@implementation Controller (ContactListDataSource)
- (int)numberOfRowsInTableView:(NSTableView *)aTableView
{
    return [discoveredServices count];
}

- (id)tableView:(NSTableView *)aTableView 
	    objectValueForTableColumn:(NSTableColumn *)aTableColumn 
	    row:(int)rowIndex
{
    return [[discoveredServices objectAtIndex:rowIndex] name];
}
@end