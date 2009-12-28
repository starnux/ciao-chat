//
//  ChatWindowController.m
//  Ciao!
//
//  Created by Mike on Sun Dec 01 2002.
//  Copyright (c) 2008 Na-Prod. All rights reserved.
//

#import "ChatWindowController.h"

@implementation ChatWindowController

- (id)initWithConnection:(NSFileHandle *)aFileHandle myName:(NSString *)me 
{
    self = [super initWithWindowNibName:@"ChatWindow"];
    
    if ( self ) {
	fileHandle = [aFileHandle retain];
	fileEOFHandle = [[NSFileHandle alloc] initWithFileDescriptor:[fileHandle fileDescriptor] closeOnDealloc:YES];
        myName = [me copy];
	
	NSNotificationCenter *nc = [NSNotificationCenter defaultCenter];
	[nc addObserver:self 
	       selector:@selector(receiveMessage:)
	           name:NSFileHandleReadCompletionNotification
		 object:fileHandle];
	[nc addObserver:self 
	       selector:@selector(socketClosed:)
	           name:NSFileHandleReadToEndOfFileCompletionNotification
		 object:fileEOFHandle];

	[fileHandle readInBackgroundAndNotify];
	[fileEOFHandle readToEndOfFileInBackgroundAndNotify];
	
    }
    return self;
}

- (void)dealloc
{
	NSLog( @"Dealloc");
    [[NSNotificationCenter defaultCenter] removeObserver:self];
    [fileHandle closeFile];
    [fileHandle release];
    [myName release];
    
    [super dealloc];
}

- (void)postMessage:(NSString *)message fromPerson:(NSString *)person
{
    NSString *str = [NSString stringWithFormat:@"%@: %@", person, message];
    NSAttributedString *aStr = [[NSAttributedString alloc] initWithString:str];
    
    [[textView textStorage] appendAttributedString:aStr];
    [aStr release];
}

- (IBAction)sendMessage:(id)sender
{
    NSString *message = [NSString stringWithFormat:@"_ciao_:%@:%@\n", myName, [sender stringValue]];
    NSData *messageData = [NSData dataWithBytes:[message UTF8String]
				         length:[message length]];
	
	@try {
    	[fileHandle writeData:messageData];
	}
	@catch(NSException * e)
	{
		NSLog( @"Writedata Exception");
		[self socketClosed:nil];
		return;
	}
	
    [self postMessage:[NSString stringWithFormat:@"%@\n", [sender stringValue]] fromPerson:@"Me"];
    [sender setStringValue:@""];
}

- (void)socketClosed:(NSNotification *)notification
{
	[[NSNotificationCenter defaultCenter] removeObserver:self];
	[fileEOFHandle closeFile];
	[fileHandle closeFile];
	[fileEOFHandle release];
	[fileHandle release];
	
	NSAlert *alert = [[NSAlert alloc] init];
	[alert addButtonWithTitle:@"OK"];
	[alert setMessageText:@"Connection was closed by peer"];
	[alert setInformativeText:@"Connection was closed by peer"];
	[alert setAlertStyle:NSWarningAlertStyle];
	[alert beginSheetModalForWindow:[super window] modalDelegate:self didEndSelector:nil contextInfo:nil];
	
	NSAttributedString *aStr = [[NSAttributedString alloc] initWithString:@"Connection was closed by peer"];
	[[textView textStorage] appendAttributedString:aStr];
	[textBox setEnabled:NO];
}

- (void)receiveMessage:(NSNotification *)notification
{
	NSNumber *error = [[notification userInfo] objectForKey:@"NSFileHandleError"];
	
	if(error != nil)
	{
		NSLog(@"FileHandle Error");
		return;
	}

	NSData *messageData = [[notification userInfo] objectForKey:NSFileHandleNotificationDataItem];
	
	if ( [messageData length] == 0 ) {
		NSLog(@"String empty" );
        [fileHandle readInBackgroundAndNotify];
        return;
    }
    
    NSString *message = [NSString stringWithUTF8String:[messageData bytes]];
    NSArray *msgComponents = [message componentsSeparatedByString:@":"];
    
    if ( [msgComponents count] != 3 ) {
        [fileHandle readInBackgroundAndNotify];
		NSLog(@"String not 3" );
        return;
    }
    
    if ( ![[msgComponents objectAtIndex:0] isEqualToString:@"_ciao_"] ) {
        [fileHandle readInBackgroundAndNotify];
		NSLog(@"String not equal" );
        return;
    }

    if ( ![[self window] isVisible] )
        [self showWindow:nil];

    [self postMessage:[msgComponents objectAtIndex:2] 
           fromPerson:[msgComponents objectAtIndex:1]];
    
    [fileHandle readInBackgroundAndNotify];
}

- (void)windowWillClose:(NSNotification *)notification
{
	NSLog( @"Window will close");
    [self release];
}

@end
