using System;
using System.Collections.Generic;
using System.Text;
using System.Net;

namespace Ciao_
{
    public class RendezVousClient
    {
        public String name;
        public int port;
        public IPAddress address;

        public RendezVousClient(String name, IPAddress address, int port)
        {
            this.name = name;
            this.port = port;
            this.address = address;
        }

        public override String ToString()
        {
            return name;
        }
    };
}
