using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Drawing;
using System.Data;
using System.Text;
using System.Windows.Forms;
using System.Net.Sockets;

namespace Ciao_
{
    public partial class ChatControl : UserControl
    {
        Socket client;
        private byte[] data = new byte[1024];
        private int size = 1024;

        public delegate void delegateClientMessage(String text);
        public delegate void delegateClientClosed(String text);
        delegateClientMessage onClientMessage;
        delegateClientClosed onClientClosed;
        
        public ChatControl(Socket client)
        {
            onClientMessage = new delegateClientMessage(doClientMessage);
            onClientClosed = new delegateClientClosed(doClientClosed);
            this.client = client;
            InitializeComponent();
            client.BeginReceive(data, 0, size, SocketFlags.None,
                    new AsyncCallback(ReceiveData), client);
        }

        void ReceiveData(IAsyncResult iar)
        {
            Socket theclient = (Socket)iar.AsyncState;
            int recv = theclient.EndReceive(iar);
            if (recv == 0)
            {
                theclient.Close();
                this.Invoke(onClientClosed, "Socket closed by peer.");
                return;
            }
            string receivedData = Encoding.UTF8.GetString(data, 0, recv);
            this.Invoke(onClientMessage, receivedData);
            client.BeginReceive(data, 0, size, SocketFlags.None,
                    new AsyncCallback(ReceiveData), client);
        }

        void doClientMessage(String text)
        {
            chatText.Text += text + "\r\n";
        }

        void doClientClosed(String text)
        {
            chatText.Text += text + "\r\n"; 
            textSend.Enabled = false;
        }

        private void textSend_KeyPress(object sender, KeyPressEventArgs e)
        {
            if (e.KeyChar == '\n')
            {
                chatText.Text += "Me : " + textSend.Text + "\r\n";
                byte[] message = Encoding.UTF8.GetBytes("_ciao_:Pwet:"+textSend.Text);
                client.BeginSend(message, 0, message.Length, SocketFlags.None,
                            new AsyncCallback(SendData), client);
                textSend.Text = "";
            }
        }

        void SendData(IAsyncResult iar)
        {
            Socket client = (Socket)iar.AsyncState;
            int sent = client.EndSend(iar);
        }

        private void buttonSend_Click(object sender, EventArgs e)
        {
            chatText.Text += "Me : " + textSend.Text + "\r\n";
            byte[] message = Encoding.UTF8.GetBytes("_ciao_:Pwet:" + textSend.Text);
            client.Send(message);
            //client.BeginSend(message, 0, message.Length, SocketFlags.None,
            //            new AsyncCallback(SendData), client);
            textSend.Text = "";
        }
    }
}
