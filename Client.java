    /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
//package com.chat.chatapp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.TrayIcon;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 *
 * @author ATul
 */
public class Client extends JFrame
{
    Socket  socket;
    BufferedReader br;
    PrintWriter out;  
    
   //Declaring Component 
    private JLabel heading=new JLabel ("Client Area");
    private JTextArea messageArea=new JTextArea();
    private JTextField messageInput=new JTextField();
    private Font font=new Font("Roboto",Font.PLAIN,20);
    
    
public static void main(String[] args)
{
    new Client();
}

public Client()
{
    try
        {
            createGUI();
            hadelEvents();
        
            System.out.println("Sending request to Server");
            socket=new Socket("127.0.0.1",8086);                System.out.println("Connection Done"); 
            br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out=new PrintWriter(socket.getOutputStream());

            startReading();
            startWriting();
        }
    catch(Exception e)
        {
            e.printStackTrace();
        }
    
}
    
    
    //this method will hande Events
private void hadelEvents()
{
        
        //using KeyLister interface for Sending data from pc
    messageInput.addKeyListener(new KeyListener() {
     @Override
    public void keyTyped(KeyEvent e) 
        {
 
        }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) 
        {
//                
        //System.out.println("Key Released "+e.getKeyCode());
            if(e.getKeyCode()==10)
                {
                //  System.out.println("You have Enter Button");
                    String ContentToSend=messageInput.getText();
                    out.println(ContentToSend);
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();
                    messageArea.append("Me: "+ContentToSend+"\n");
                    
                }
            }
    }
                                    );
}
    
    
    
    //this will create GUI
private void createGUI()
    {
       this.setTitle("Client Apllication");
       this.setSize(500,500);
       this.setLocationRelativeTo(null);
       this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       this.setVisible(true);
       
       //Stting the Font
       heading.setFont(font);
       messageArea.setFont(font);
       messageInput.setFont(font);
       
       //setting icon 
       

//alighning Text field        
//      heading.setIcon(new ImageIcon("logo.jpg"));
       heading.setHorizontalAlignment(SwingConstants.CENTER);
       heading.setHorizontalTextPosition(SwingConstants.CENTER);
       heading.setVerticalTextPosition(SwingConstants.BOTTOM);
       heading.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        heading.setBorder(BorderFactory.createBevelBorder(20, Color.GREEN, Color.yellow));
       

//message input Design
    messageInput.setHorizontalAlignment(SwingConstants.CENTER);

//create
//Setting the layout
       this.setLayout(new BorderLayout());
       
       //adding component to Frame
       this.add(heading,BorderLayout.NORTH);
       this.add(messageArea,BorderLayout.CENTER);
       this.add(messageInput,BorderLayout.SOUTH);
}
    
public void startReading()
{
    Runnable r1=()->
        {
            System.out.println("Reader Start");
                try {
                        while(true)
                            {
                                String msg;
                       
                                msg = br.readLine();
                                if(msg.equals("Exit"))
                                        {
                                            System.out.println("Server Terminate the application!");
                                            JOptionPane.showMessageDialog(this, "Server Is Terminated", "Error!!", 232);
                                            messageInput.setEnabled(false);
                                            socket.close();
                                    
                                            break;
                                        }
                            messageArea.append("Server: "+msg+"\n");
                                
                        
//                        System.out.println("Server: "+msg);
                    
                            }
                    }
                    catch (Exception ex) 
                        {
                            ex.printStackTrace();
                        }
        };
        new Thread(r1).start();
}


//this send Data From 
 public void startWriting()
    {
        Runnable r2=()->
            {
                System.out.println("Writing Start");
                    try
                        {
                            while(true)
                                {
                                BufferedReader bis;
                                bis = new BufferedReader(new InputStreamReader(System.in));
                                String content =bis.readLine();
                                out.println(content);
                                out.flush();
//                              bis.close();
                                }
                        }
                    catch(Exception e)
                         {
                             e.printStackTrace();
                         }   
            };
                new Thread(r2).start();
    }
 }
