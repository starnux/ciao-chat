package org.eu.dedale.ciao.utils;

import java.awt.Color;
import java.awt.Panel;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class Console extends Panel implements ComponentListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4464719689311010396L;

	/** Consoles' hashmap */
	private static HashMap<String, Console>hashConsoles;
	
	private final static int CHAR_MAX = 80000;
	
	/** Scroll bar */
	private JScrollPane sc = null;
	
	/** The text area */
	private JTextPane textView = null;
	
	/** Document showed */
	private DefaultStyledDocument dsd;
	
	/** Autoscroll */
	private boolean autoScroll;
	
	/* Writting styles */
	public static SimpleAttributeSet BOLD_BLACK;
	public static SimpleAttributeSet BOLD_RED;
	public static SimpleAttributeSet BOLD_BLUE;
	public static SimpleAttributeSet BOLD_GREEN;
	public static SimpleAttributeSet SIMPLE_RED; 
	public static SimpleAttributeSet SIMPLE_BLUE; 
	public static SimpleAttributeSet SIMPLE_GREEN; 
	public static SimpleAttributeSet SIMPLE_BLACK; 
	
	static
	{
		hashConsoles = new HashMap<String, Console>();
	}
	
	/** Default constructor */
	public Console(String name)
	{
		/* Creation of our components support */
		this.dsd 		= new DefaultStyledDocument();
		this.textView   = new JTextPane(dsd);
		this.sc         = new JScrollPane(this.textView);
		this.autoScroll = true;
		
		stylesInitialisation();
		
		// Scroll zone configuration
		sc.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		sc.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		this.textView.addComponentListener(this);
		
		// textview configuration
		this.textView.setEditable(false);
		
		// layout creation for the textearea
		setLayout(new BoxLayout(this,1));

		// adding text area to scroll area
		add(sc);
		
		hashConsoles.put(name, this);
	}

	/** Initialisation and writting styles adding */
	private void stylesInitialisation()
	{
		BOLD_BLACK  	= new SimpleAttributeSet();
		BOLD_RED  		= new SimpleAttributeSet();
		BOLD_BLUE  		= new SimpleAttributeSet();
		BOLD_GREEN  	= new SimpleAttributeSet();
		SIMPLE_RED 		= new SimpleAttributeSet();
		SIMPLE_BLUE  	= new SimpleAttributeSet();
		SIMPLE_GREEN  	= new SimpleAttributeSet();
		SIMPLE_BLACK  	= new SimpleAttributeSet();
		
		BOLD_BLACK.addAttribute(StyleConstants.Bold, true);
		BOLD_RED.addAttribute(StyleConstants.Bold, true);
		BOLD_BLUE.addAttribute(StyleConstants.Bold, true);
		BOLD_GREEN.addAttribute(StyleConstants.Bold, true);
		SIMPLE_RED.addAttribute(StyleConstants.Foreground, Color.RED);
		SIMPLE_BLUE.addAttribute(StyleConstants.Foreground, Color.BLUE);
		SIMPLE_GREEN.addAttribute(StyleConstants.Foreground, Color.GREEN);
		SIMPLE_BLACK.addAttribute(StyleConstants.Foreground, Color.BLACK); // Default style
		BOLD_RED.addAttribute(StyleConstants.Foreground, Color.RED);
		BOLD_BLUE.addAttribute(StyleConstants.Foreground, Color.BLUE);
		BOLD_GREEN.addAttribute(StyleConstants.Foreground, Color.GREEN);
		BOLD_BLACK.addAttribute(StyleConstants.Foreground, Color.BLACK); // Default style
	}
	
	/** Return console instance */
	public static Console getConsoleInstance(String name)
	{
		return hashConsoles.get(name);
	}

	/** Insert a line 
	 * @param pos Insertion position
	 * @param msg Message to insert
	 * @return current lenght of the buffer */
	public int insertLine(int pos, String msg)
	{
		return insertLine(pos,msg, SIMPLE_BLACK);
	}
	
	/** Insert a line with a writting style */
	public int insertLine(int pos, String msg, AttributeSet style)
	{
		flushBuffer();
		if (pos > dsd.getLength())
			return dsd.getLength();

		try
		{
			if (style != null)
				this.dsd.insertString(pos,msg,style);
			else
				this.dsd.insertString(pos,msg,SIMPLE_BLACK);
		}
		catch (BadLocationException e)
		{
			e.printStackTrace();
		}

		return this.dsd.getLength();
	}
	
	/** Adding a line without writting style */
	public int addLine(String msg)
	{
		return addLine(msg, SIMPLE_BLACK);
	}

	/** Console reset */
	public void reset()
	{
		this.dsd = new DefaultStyledDocument();
		this.textView.setDocument(dsd);
	}
	
	/** Adding a line with a writting style
	 * @param msg Message to add
	 * @param style Writting style to use
	 * @return current lenght of the buffer
	 */
	public int addLine(String msg, AttributeSet style) 
	{
		flushBuffer();
		try
		{
			if (style != null)
				this.dsd.insertString(dsd.getLength(),msg,style);
			else
				this.dsd.insertString(dsd.getLength(),msg,SIMPLE_BLACK);
		}
		catch (BadLocationException e)
		{
			e.printStackTrace();
		}
		
		return this.dsd.getLength();
	}

	/** Adding several lines
	 * If corresponding style is null, use of the default style,
	 * if there is less styles than texts, use of the default style too.
	 * @param msg text to add
	 * @param style style to use 
	 */
	public int addLine(String []msg, AttributeSet []style) 
	{
		for(int i=0;i<msg.length;i++)
		{
			if (i < style.length)
				this.addLine(msg[i], style[i]);
			else
				this.addLine(msg[i]);
		}
		
		return this.dsd.getLength();
	}
	
	/** Flush buffer */
	private void flushBuffer()
	{
		if (this.dsd.getLength() > CHAR_MAX)
		{	
			String s;
			try 
			{
				s = dsd.getText(0, dsd.getLength());
				s = s.substring(dsd.getLength()/2);
				int i = s.length();
				int j = 0;
				
				while(j<i)
				{
					if (s.charAt(j) == '\n')
						 break;
					j++;
				}
				
				if (j < s.length())
					s = s.substring(j);
				
				this.textView.select(j, dsd.getLength()-1);
				this.textView.cut();
				reset();
				this.textView.paste();
				this.dsd.insertString(0, s, null);
			} 
			catch (BadLocationException e) 
			{
				e.printStackTrace();
			}
		}
	}
	
	public void componentResized(ComponentEvent e)
	{
		if (autoScroll)
			this.sc.getVerticalScrollBar().setValue(this.sc.getVerticalScrollBar().getMaximum());
	}
	
	public boolean isAutoScroll() {
		return autoScroll;
	}

	public void setAutoScroll(boolean autoScroll) {
		this.autoScroll = autoScroll;
	}
	
	public void componentHidden(ComponentEvent e){}
	public void componentMoved(ComponentEvent e){}
	public void componentShown(ComponentEvent e){}
}