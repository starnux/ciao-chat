//
//  ChatWindowController.h
//  Ciao!
//
//  Created by Mike on Sun Dec 01 2002.
//  Copyright (c) 2008 Na-Prod. All rights reserved.
//

#import <AppKit/AppKit.h>

@interface ChatWindowController : NSWindowController {
    IBOutlet NSTextView *textView;
	IBOutlet id textBox;
    NSFileHandle *fileHandle;
    NSFileHandle *fileEOFHandle;
    NSString *myName;
}
- (id)initWithConnection:(NSFileHandle *)aFileHandle myName:(NSString *)me;
- (IBAction)sendMessage:(id)sender;
- (void)receiveMessage:(NSNotification *)notification;
- (void)socketClosed:(NSNotification *)notification;
- (void)postMessage:(NSString *)message fromPerson:(NSString *)person;
- (void)windowWillClose:(NSNotification *)notification;
@end
