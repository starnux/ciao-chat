package org.eu.dedale.ciao.utils;

import javax.swing.*;

import java.awt.Container;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;

/** Factory de composants SWING, permettant de crééer des composants avec un maximum de param�tres
 * @author <b>Goutaland Nicolas</b> <br> Ecole Polytechnique de l'Université de Nice/Sophia Antipolis <br> Département Sciences Informatiques
 * @version 1.0 */
public class ComponentFactory 
{
	/** Cr�ation d'un label 
	 * @param msg Le message du label ou <b>null</b> s'il n'en a pas
	 * @param posX La position X du label sur son p�re, <b>positive ou nulle</b>
	 * @param posY La position Y du label sur son p�re, <b>positive ou nulle</b>
	 * @param width La largeur du label, <b>positive ou nulle</b>
	 * @param height La hauteur du label, <b>positive ou nulle</b>
	 * @param parent Le p�re du label, ou <b>null</b> s'il n'en a pas
	 * @return un label configur� selon les param�tres en entr�e
	 */
	public static JLabel makeLabelString(String msg, int posX, int posY, int width, int height, Container parent)
	{
		// Création du label
		JLabel lab = new JLabel(msg);
		
		// Configuration du label
		lab.setBounds(posX,posY,width,height);
		
		// Si le label a un père, on lui ajoute
		if (parent != null)
			parent.add(lab);
		
		// On renvoie le Label crée
		return lab;
	}
	
	/** Cr�ation d'un bouton 
	 * @param msg Le message du bouton ou <b>null</b> s'il n'en a pas
	 * @param posX La position X du bouton sur son père, <b>positive ou nulle</b>
	 * @param posY La position Y du bouton sur son père, <b>positive ou nulle</b>
	 * @param width La largeur du bouton, <b>positive ou nulle</b>
	 * @param height La hauteur du bouton, <b>positive ou nulle</b>
	 * @param parent Le p�re du bouton, ou <b>null</b> s'il n'en a pas
	 * @param lst L'écouteur d'évènements de l'objet, ou <b>null</b> s'il n'en a pas
	 * @return un bouton configur� selon les paramètres en entrée
	 */
	public static JButton makeButtonString(String msg, int posX, int posY, int width, int height, Container parent, ActionListener lst)
	{
		// Cr�ation du label
		JButton bt = new JButton(msg);
		
		// Configuration du label
		bt.setBounds(posX,posY,width,height);
		
		// Ajout de l'écouteur d'évènements
		if(lst != null)
			bt.addActionListener(lst);
		
		// Si le label a un père, on lui ajoute
		if (parent != null)
			parent.add(bt);
		
		// On renvoie le Bouton crée
		return bt;
	}
	
	/** Création d'un bouton 
	 * @param nomImg Le path de l'image du bouton
	 * @param posX La position X du bouton sur son père, <b>positive ou nulle</b>
	 * @param posY La position Y du bouton sur son père, <b>positive ou nulle</b>
	 * @param width La largeur du bouton, <b>positive ou nulle</b>
	 * @param height La hauteur du bouton, <b>positive ou nulle</b>
	 * @param parent Le p�re du bouton, ou <b>null</b> s'il n'en a pas
	 * @param lst L'écouteur d'évènements de l'objet, ou <b>null</b> s'il n'en a pas
	 * @return un bouton configuré selon les param�tres en entrée
	 */
	public static JButton makeButtonImage(String nomImg, int posX, int posY, int width, int height, Container parent, ActionListener lst)
	{
		// On renvoie le Bouton crée
		return makeButtonImage(new ImageIcon(nomImg),posX,posY,width,height,parent,lst);
	}

	/** Création d'un bouton 
	 * @param img L'image que l'on souaite affecter au bouton
	 * @param posX La position X du bouton sur son père, <b>positive ou nulle</b>
	 * @param posY La position Y du bouton sur son père, <b>positive ou nulle</b>
	 * @param width La largeur du bouton, <b>positive ou nulle</b>
	 * @param height La hauteur du bouton, <b>positive ou nulle</b>
	 * @param parent Le p�re du bouton, ou <b>null</b> s'il n'en a pas
	 * @param lst L'écouteur d'évènements de l'objet, ou <b>null</b> s'il n'en a pas
	 * @return un bouton configuré selon les paramètres en entrée
	 */
	public static JButton makeButtonImage(ImageIcon img, int posX, int posY, int width, int height, Container parent, ActionListener lst)
	{
		// Création du label
		JButton bt = new JButton();
		
		// Assignation de l'image
		bt.setIcon(img);

		// Configuration du label
		bt.setBounds(posX,posY,width,height);
		
		// Ajout de l'écouteur d'évènements
		if(lst != null)
			bt.addActionListener(lst);
		
		// Si le label a un père, on lui ajoute
		if (parent != null)
			parent.add(bt);
		
		// On renvoie le Bouton crée
		return bt;
	}
	
	/** Création d'une zone de texte
	 * @param msg Le message par défaut ou <b>null</b> si elle n'en a pas
	 * @param posX La position X de l'entry sur son p�re, <b>positive ou nulle</b>
	 * @param posY La position Y de l'entry sur son p�re, <b>positive ou nulle</b>
	 * @param width La largeur de l'entry, <b>positive ou nulle</b>
	 * @param height La hauteur de l'entry, <b>positive ou nulle</b>
	 * @param parent Le p�re de l'entry, ou <b>null</b> si elle n'en a pas
	 * @param lst L'écouteur d'évènements de l'objet, ou <b>null</b> s'il n'en a pas
	 * @return une entry configur�e selon les paramètres en entrée
	 */
	public static JTextField makeEntry(String msg, int posX, int posY, int width, int height, Container parent, KeyListener lst)
	{
		// Création
		JTextField ent = new JTextField(msg);
		
		// Configuration
		ent.setBounds(posX,posY,width,height);
		
		// Ajout de l'écouteur d'évènements
		if(lst != null)
			ent.addKeyListener(lst);
		
		// Si l'objet a un père, on lui ajoute
		if (parent != null)
			parent.add(ent);
		
		// On renvoie l'entry créée
		return ent;
	}
	
	/** Création d'une zone de texte avec liste déroulante
	 * @param msg Le message par défaut ou <b>null</b> si elle n'en a pas
	 * @param posX La position X de l'entry sur son p�re, <b>positive ou nulle</b>
	 * @param posY La position Y de l'entry sur son p�re, <b>positive ou nulle</b>
	 * @param width La largeur de l'entry, <b>positive ou nulle</b>
	 * @param height La hauteur de l'entry, <b>positive ou nulle</b>
	 * @param parent Le p�re de l'entry, ou <b>null</b> si elle n'en a pas
	 * @param lst L'écouteur d'évènements de l'objet, ou <b>null</b> s'il n'en a pas
	 * @return une entry configur�e selon les paramètres en entrée
	 */
	public static JComboBox makeListEntry(String msg, int posX, int posY, int width, int height, Container parent, KeyListener lst)
	{
		// Création
		JComboBox ent = new JComboBox();
		
		//Ajout du premier texte
		ent.addItem(msg);
	
		//Rends éditable
		ent.setEditable(true);
		
		// Configuration
		ent.setBounds(posX,posY,width,height);
		
		// Ajout de l'écouteur d'évènements
		if(lst != null)
			ent.addKeyListener(lst);
		
		// Si l'objet a un père, on lui ajoute
		if (parent != null)
			parent.add(ent);
		
		// On renvoie l'entry créée
		return ent;
	}
	
	/** Création d'une checkBox
	 * @param msg Le message par défaut <b>null</b> si elle n'en a pas
	 * @param posX La position X de la checkBox sur son père, <b>positive ou nulle</b>
	 * @param posY La position Y de la checkBox sur son père, <b>positive ou nulle</b>
	 * @param width La largeur de la checkBox <b>positive ou nulle</b>
	 * @param height La hauteur de la checkBox, <b>positive ou nulle</b>
	 * @param parent Le p�re de la checkBox, ou <b>null</b> si elle n'en a pas
	 * @param lst L'écouteur d'évènements de l'objet, ou <b>null</b> s'il n'en a pas
	 * @return une JCheckBox configurée selon les paramètres en entrée
	 */
	public static JCheckBox makeCheckBox(String msg, int posX, int posY, int width, int height, Container parent, ActionListener lst)
	{
		// Création
		JCheckBox chk = new JCheckBox(msg);
		
		// Configuration
		chk.setBounds(posX,posY,width,height);
		
		// Ajout de l'écouteur d'évènements
		if(lst != null)
			chk.addActionListener(lst);
		
		// Si l'objet à un père, on lui ajoute
		if (parent != null)
			parent.add(chk);
		
		// On renvoie l'objet créé
		return chk;
	}
	
	/** Création d'une checkBox avec des images
	 * @param unchecked Image de la checkbox non sélectionnée
	 * @param checked Image de la checkbox sélectionnée
	 * @param posX La position X de la checkBox sur son père, <b>positive ou nulle</b>
	 * @param posY La position Y de la checkBox sur son père, <b>positive ou nulle</b>
	 * @param width La largeur de la checkBox <b>positive ou nulle</b>
	 * @param height La hauteur de la checkBox, <b>positive ou nulle</b>
	 * @param parent Le p�re de la checkBox, ou <b>null</b> si elle n'en a pas
	 * @param lst L'écouteur d'évènements de l'objet, ou <b>null</b> s'il n'en a pas
	 * @return une JCheckBox configurée selon les paramètres en entrée
	 */
	public static JCheckBox makeCheckBoxImage(Icon unchecked, Icon checked, int posX, int posY, int width, int height, Container parent, ActionListener lst)
	{
		// Création
		JCheckBox chk = new JCheckBox(unchecked);
		chk.setSelectedIcon(checked);
		
		// Configuration
		chk.setBounds(posX,posY,width,height);
		
		// Ajout de l'écouteur d'évènements
		if(lst != null)
			chk.addActionListener(lst);
		
		// Si l'objet à un père, on lui ajoute
		if (parent != null)
			parent.add(chk);
		
		// On renvoie l'objet créé
		return chk;
	}
}

