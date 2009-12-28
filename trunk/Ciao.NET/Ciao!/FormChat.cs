using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Text;
using System.Windows.Forms;
using System.Net.Sockets;

namespace Ciao_
{
    public partial class FormChat : Form
    {
        public delegate void delegateClientConnexion(Socket client);
        delegateClientConnexion onClientConnexion;

        public FormChat()
        {
            InitializeComponent(); 
            
            onClientConnexion = new delegateClientConnexion(doClientConnexion);
        }

        internal void NewClient(System.Net.Sockets.Socket client)
        {
            this.Invoke(onClientConnexion, client);
        }

        void doClientConnexion(Socket client)
        {
            TabPage page = new TabPage("Chat");
            tabControl1.Controls.Add(page);
            ChatControl ctrl = new ChatControl(client);
            page.Controls.Add(ctrl);
            ctrl.Dock = DockStyle.Fill;
        }
    }
}