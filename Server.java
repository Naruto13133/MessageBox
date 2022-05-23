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
import java.net.*;
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
public class Server extends JFrame 
{
    
    
ServerSocket server;
Socket socket;
BufferedReader br;
PrintWriter out;  

//Declaring Componet
private JLabel heading =new JLabel ("Server Area");
private JTextArea messageArea=new JTextArea();
private JTextField messageInput =new JTextField();
private Font font=new Font("Roboto",Font.BOLD,20 );
    
 public static void main(String[] args){

   new Server();

}

    
    //Contructor
    public Server()
    {
        createGUI();
        hadelEvents();


        try{
            server =new ServerSocket(8086);
                System.out.println("Server Is ready To Accept Connection "); 
                System.out.println("Waiting");
                socket=server.accept();
                
                //taking Input from Socket
                br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out=new PrintWriter(socket.getOutputStream());
                
                startReading();
                startWriting();
             }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            startReading();
            startWriting();

    }


    //Creating Server GUI
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

    ////alighning Text  
    heading.setHorizontalAlignment(SwingConstants.CENTER);
    heading.setHorizontalTextPosition(SwingConstants.CENTER);
    heading.setVerticalTextPosition(SwingConstants.BOTTOM);
    heading.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
     heading.setBorder(BorderFactory.createBevelBorder(20, Color.GREEN, Color.yellow));

    //Message input Design
    messageInput.setHorizontalAlignment(SwingConstants.CENTER);
    
    

    //Setting the layout
    this.setLayout(new BorderLayout());

    //adding componet to frame
    this.add(heading,BorderLayout.NORTH);
    this.add(messageArea,BorderLayout.CENTER);
    this.add(messageInput,BorderLayout.SOUTH);

}
    //Event handeler 
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


    
//this is use for ReadinInput    
    public void startReading()
    {
        Runnable r1=()->
            {
                System.out.println("Reader Start");

	            try{
                    while(true)
                        {
                            String msg;
                            msg = br.readLine();
                            messageArea.append("Client: "+msg+"\n" );
                            if(msg.equals("Exit"))
                                {
                                    System.out.println("Server Terminate the application!");
                                    JOptionPane.showMessageDialog(this, "Server Is Terminated", "Error!!", 232);
                                    messageInput.setEnabled(false);
                                    socket.close();
                            
                                    break;
                                }
                        System.out.println("Client: "+msg);
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
                        {//Taking input from Client
                            BufferedReader bis;
                            bis = new BufferedReader(new InputStreamReader(System.in));
                            String content=bis.readLine();
                            out.println(content);
                            messageArea.append("Client: "+content+"\n");
                            out.flush();
                                 //taking care of termination        
                                if(content.equals("exit"))
                                    {
                                        socket.close();
                                        break;
                                    }
                                    
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