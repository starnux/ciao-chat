using System;
using System.IO;
using System.Net;
using System.Net.Sockets;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Text;
using System.Windows.Forms;
using Mono.Zeroconf;

namespace Ciao_
{
    public partial class MainForm : Form
    {
        FormChat chat;

        // Bonjour
        public delegate void delegateServiceRemoved(ServiceBrowseEventArgs args);
        public delegate void delegateServiceResolved(object args);
        public delegate void delegateClientConnexion(Socket client);

        delegateServiceRemoved onServiceRemoved;
        delegateServiceResolved onServiceResolved;
        delegateClientConnexion onClientConnexion;

        ServiceBrowser browser = new ServiceBrowser("_ciao._tcp");
        RegisterService service = new RegisterService();

        // Server
        private Socket server;

        public MainForm()
        {
            // Delegates
            onServiceRemoved = new delegateServiceRemoved(doServiceRemoved);
            onServiceResolved = new delegateServiceResolved(doServiceResolved);
            onClientConnexion = new delegateClientConnexion(doClientConnexion);

            browser.ServiceAdded += browser_ServiceAdded;//new ServiceBrowseEventHandler(browser_ServiceAdded);
            browser.ServiceRemoved += browser_ServiceRemoved;//new ServiceBrowseEventHandler(browser_ServiceRemoved);

            InitializeComponent();

            // Chat Window
            chat = new FormChat();

            // Bonjour Service Setup
            service.Name = "WinCiao";
            service.Port = 52345;
            service.RegType = "_ciao._tcp";
            service.ReplyDomain = "local.";

            // Socket Server Start
            service.Port = (ushort)StartServer();
            
            // Start Bonjour
            browser.StartAsync();
            service.RegisterAsync();

            chat.Show();
        }

        void userList_DoubleClick(object sender, EventArgs e)
        {
            RendezVousClient client = (RendezVousClient)userList.SelectedItem;
            Socket sock = new Socket(client.address.AddressFamily, SocketType.Stream, ProtocolType.Tcp);
            sock.Connect(client.address, client.port);
            onClientConnexion(sock);
        }

        int StartServer()
        {
            server = new Socket(AddressFamily.InterNetwork,
                      SocketType.Stream, ProtocolType.Tcp);
            IPEndPoint iep = new IPEndPoint(IPAddress.Any, 0);
            server.Bind(iep);
            server.Listen(5);
            server.BeginAccept(new AsyncCallback(AcceptConn), server);
            return ((IPEndPoint)server.LocalEndPoint).Port;
        }

        void AcceptConn(IAsyncResult iar)
        {
            Socket origserver = (Socket)iar.AsyncState;
            Socket client = origserver.EndAccept(iar);
            /*conStatus.Text = "Connected to: " + client.RemoteEndPoint.ToString();
            string stringData = "Welcome to my server";
            byte[] message1 = Encoding.ASCII.GetBytes(stringData);
            client.BeginSend(message1, 0, message1.Length, SocketFlags.None,
                        new AsyncCallback(SendData), client);*/
            onClientConnexion(client);
        }

        void doClientConnexion(Socket client)
        {
            chat.NewClient(client);
        }

        void browser_ServiceRemoved(object o, ServiceBrowseEventArgs args)
        {
            this.Invoke(onServiceRemoved, args);
        }

        void doServiceRemoved(ServiceBrowseEventArgs args)
        {
            logBox.Text += "\r\n" + "Service Removed :" +
                    args.Service.FullName + "," +
                    args.Service.Name + "," +
                    args.Service.Port + "," +
                    args.Service.RegType + "," +
                    args.Service.TxtRecord;
        }

        void doServiceResolved(object o)
        {
            BrowseService rservice = o as BrowseService;
            logBox.Text += "\r\n" + "Service Resolved : " +
                    rservice.Name + " Port:" +
                    (ushort)rservice.Port + " @" + rservice.HostEntry.AddressList[0].ToString();
            
            userList.Items.Add(new RendezVousClient(rservice.Name, rservice.HostEntry.AddressList[0], rservice.Port));
        }

        void browser_ServiceAdded(object o, ServiceBrowseEventArgs args)
        {
            //this.Invoke(onServiceAdded, args);
            args.Service.Resolved += Service_Resolved;//new EventHandler(Service_Resolved);
            args.Service.Resolve();
        }

        void Service_Resolved(object sender, EventArgs e)
        {
            this.Invoke(onServiceResolved, sender);
        }

        private void button1_Click(object sender, EventArgs e)
        {
            RendezVousClient client = (RendezVousClient)userList.SelectedItem;
            Socket sock = new Socket(client.address.AddressFamily, SocketType.Stream, ProtocolType.Tcp);
            sock.Connect(client.address, client.port);
            onClientConnexion(sock);
        }
    }
}